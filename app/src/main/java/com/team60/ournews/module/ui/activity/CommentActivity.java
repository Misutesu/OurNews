package com.team60.ournews.module.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.event.LoginForNewEvent;
import com.team60.ournews.listener.MyObjectAnimatorListener;
import com.team60.ournews.listener.MyRecyclerViewOnScrollListener;
import com.team60.ournews.listener.MyTransitionListener;
import com.team60.ournews.module.adapter.CommentActivityRecyclerViewAdapter;
import com.team60.ournews.module.bean.Comment;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.bean.OtherUser;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.presenter.CommentPresenter;
import com.team60.ournews.module.presenter.impl.CommentPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.CommentVIew;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;
import com.team60.ournews.widget.LikeButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.BrowseEvent;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CommentActivity extends BaseActivity implements CommentVIew {

    @BindView(R.id.activity_comment_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_new_bottom_action_layout)
    LinearLayout mBottomActionLayout;
    @BindView(R.id.activity_new_bottom_layout)
    LinearLayout mBottomLayout;
    @BindView(R.id.activity_comment_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_comment_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_comment_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.activity_comment_retry_btn)
    AppCompatButton mRetryBtn;
    @BindView(R.id.activity_comment_no_comment_text)
    AppCompatTextView mNoCommentText;

    private List<Comment> comments;
    private CommentActivityRecyclerViewAdapter mAdapter;

    private NestedScrollView mScrollView;
//    private RecyclerView mChildRecyclerView;
//    private List<CommentChild> mChildList;
//    private CommentChildActivityRecyclerViewAdapter mChildAdapter;

    private CommentPresenter mPresenter;

    private AlertDialog mLoginDialog;

//    private MyBottomSheetDialog mChildDialog;
//    private BottomSheetBehavior mBehavior;

    private New n;

//    private Comment mCommentTemp;

    private int page = 1;
    private int sort = 1;

    private boolean isLoad = false;
    private boolean hasMore = true;

    private boolean isShow = true;

    private long readTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        EventBus.getDefault().unregister(this);
        init(savedInstanceState);
        setListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explodeIn = new Explode();
            explodeIn.setDuration(400);
            explodeIn.addListener(new MyTransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    startGetComments();
                }
            });
            getWindow().setEnterTransition(explodeIn);

            Explode explodeOut = new Explode();
            explodeOut.setDuration(400);
            getWindow().setExitTransition(explodeOut);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().register(this);
        long time = (System.currentTimeMillis() - readTime) / 1000;
        BrowseEvent browseEvent = new BrowseEvent(String.valueOf(n.getId()), n.getTitle(), "comment", time);
        JAnalyticsInterface.onEvent(this, browseEvent);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        readTime = System.currentTimeMillis();

        mPresenter = new CommentPresenterImpl(this, this);

        n = getIntent().getParcelableExtra(New.class.getName());

        mToolBar.setTitle(getString(R.string.comment));
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (comments == null) comments = new ArrayList<>();
        mAdapter = new CommentActivityRecyclerViewAdapter(this, n, comments);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setOnItemClickListener(new CommentActivityRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onTitleClick() {
                finish();
            }

            @Override
            public void onAvatarClick(OtherUser otherUser) {
                if (User.isLogin()) {
                    Intent intent = new Intent(CommentActivity.this, UserActivity.class);
                    intent.putExtra("otherUser", otherUser);
                    startActivity(intent);
                } else {
                    createLoginDialog();
                    mLoginDialog.setMessage(getString(R.string.only_login_can_see_other_user));
                    mLoginDialog.show();
                }
            }

            @Override
            public void onLayoutClick(Comment comment) {
//                getChildComment(comment);
//                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                mChildDialog.show();
            }

            @Override
            public void onLickBtnClick(final Comment comment, final TextView mLikeNumText, final LikeButton likeButton) {
                if (User.isLogin() && comment.getIsLike() != -1) {
                    addSubscription(RetrofitUtil.newInstance()
                            .lickComment(comment.getId(), user.getId(), user.getToken(), comment.getIsLike())
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableSubscriber<NoDataResult>() {
                                @Override
                                protected void onStart() {
                                    likeButton.startLoad();
                                    request(1);
                                }

                                @Override
                                public void onNext(NoDataResult result) {
                                    if (result.getResult().equals("success")) {
                                        if (comment.getIsLike() == 0) {
                                            comment.setIsLike(1);
                                            comment.setLickNum(comment.getLickNum() + 1);
                                            likeButton.hasLike();
                                        } else {
                                            comment.setIsLike(0);
                                            comment.setLickNum(comment.getLickNum() - 1);
                                            likeButton.noLike();
                                        }
                                        ObjectAnimator outAnim = ObjectAnimator.ofFloat(mLikeNumText, "alpha", mLikeNumText.getAlpha(), 0f);
                                        outAnim.addListener(new MyObjectAnimatorListener() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                String likeNumText = comment.getLickNum() + " 赞";
                                                mLikeNumText.setText(likeNumText);
                                            }
                                        });

                                        outAnim.setDuration(100);
                                        ObjectAnimator inAnim = ObjectAnimator.ofFloat(mLikeNumText, "alpha", mLikeNumText.getAlpha(), 1f);
                                        inAnim.setDuration(100);
                                        AnimatorSet set = new AnimatorSet();
                                        set.play(outAnim).before(inAnim);
                                        set.start();
                                    } else {
                                        showSnackBar(ErrorUtil.getErrorMessage(result.getErrorCode()));
                                    }
                                }

                                @Override
                                public void onError(Throwable t) {
                                    t.printStackTrace();
                                    onComplete();
                                    showSnackBar(getString(R.string.internet_error));
                                }

                                @Override
                                public void onComplete() {
                                    likeButton.endLoad();
                                }
                            }));
                } else {
                    createLoginDialog();
                    mLoginDialog.setMessage(getString(R.string.only_login_can_like_comment));
                    mLoginDialog.show();
                }
            }
        });

        mRecyclerView.addOnScrollListener(new MyRecyclerViewOnScrollListener(new MyRecyclerViewOnScrollListener.OnScrollBottomListener() {
            @Override
            public void onScrollBottom() {
                if (!isLoad && hasMore) {
                    startLoadMore();
                }
            }
        }, new MyRecyclerViewOnScrollListener.OnScrollListener() {
            @Override
            public void onScrollY(int dy) {
                if (dy > 0) {
                    showOrHideBottomLayout(false);
                } else if (dy < 0) {
                    showOrHideBottomLayout(true);
                }
            }
        }));

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRetryBtn.setVisibility(View.GONE);
                startGetComments();
            }
        });

        mBottomActionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    Intent intent = new Intent(CommentActivity.this, WriteCommentActivity.class);
                    intent.putExtra(New.class.getName(), n);
                    startActivityForResult(intent, WriteCommentActivity.CODE_SEND);
                } else {
                    createLoginDialog();
                    mLoginDialog.setMessage(getString(R.string.no_login_do_you_login_now));
                    mLoginDialog.show();
                }
            }
        });
    }

    private void startGetComments() {
        Log.d("TAG", "startGetComments");
        isLoad = true;
        mProgressBar.setVisibility(View.VISIBLE);
        long uid = -1;
        if (User.isLogin()) {
            uid = user.getId();
        }
        mPresenter.getComments(uid, n.getId(), 1, sort);
    }

    private void startLoadMore() {
        isLoad = true;
        mAdapter.setLoadMore(true);
        long uid = -1;
        if (User.isLogin()) {
            uid = user.getId();
        }
        mPresenter.getComments(uid, n.getId(), page + 1, sort);
    }

    private void showOrHideBottomLayout(boolean showOrHide) {
        if (showOrHide) {
            if (!isShow) {
                isShow = true;
                float distance = mBottomLayout.getTranslationY();
                ObjectAnimator.ofFloat(mBottomLayout, "translationY"
                        , distance, 0)
                        .setDuration(200).start();
            }
        } else {
            if (isShow) {
                isShow = false;
                ObjectAnimator.ofFloat(mBottomLayout, "translationY"
                        , mBottomLayout.getTranslationY(), UiUtil.dip2px(48))
                        .setDuration(200).start();
            }
        }
    }

    private void createLoginDialog() {
        if (mLoginDialog == null) {
            mLoginDialog = ThemeUtil.getThemeDialogBuilder(CommentActivity.this)
                    .setTitle(R.string.hint)
                    .setPositiveButton(R.string.go_to_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(CommentActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .create();
        }
    }

//    private void getChildComment(Comment comment) {
//        if (mChildDialog == null) {
//            mChildDialog = new MyBottomSheetDialog(CommentActivity.this);
//            if (ThemeUtil.newInstance().isNightMode()) {
//                mChildDialog = new MyBottomSheetDialog(CommentActivity.this, R.style.NightDialogTheme);
//            } else {
//                mChildDialog = new MyBottomSheetDialog(CommentActivity.this);
//            }
//            View view = LayoutInflater.from(CommentActivity.this)
//                    .inflate(R.layout.layout_comment_child, null);
//            mChildDialog.setContentView(view);
//            mBehavior = BottomSheetBehavior.from((View) view.getParent());
//            mBehavior.setPeekHeight(UiUtil.getScreenHeight() * 4 / 5);
//
//            mScrollView = (NestedScrollView) view.findViewById(R.id.layout_comment_child_scroll_view);
//            mChildRecyclerView = (RecyclerView) view.findViewById(R.id.layout_comment_child_recycler_view);
//            mChildRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            mChildList = new ArrayList<>();
//            mChildAdapter = new CommentChildActivityRecyclerViewAdapter(this, mChildList);
//        }
//        if (mChildList.size() == 0 || (mCommentTemp != null && mCommentTemp.getId() != comment.getId())) {
//            mChildList.clear();
//        }
//        mCommentTemp = comment;
//    }

    @Override
    public void getCommentsEnd() {
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setLoadMore(false);
        isLoad = false;
    }

    @Override
    public void getCommentsSuccess(List<Comment> comments, int page) {
        if (mNoCommentText.getVisibility() == View.VISIBLE)
            mNoCommentText.setVisibility(View.GONE);
        if (page == 1) {
            this.page = 1;
            if (comments.size() == 0) {
                if (this.comments.size() == 0) {
                    mNoCommentText.setVisibility(View.VISIBLE);
                } else {
                    mNoCommentText.setVisibility(View.VISIBLE);
                    this.comments.clear();
                    mAdapter.notifyDataSetChanged();
                    showSnackBar(getString(R.string.this_new_error));
                }
                hasMore = false;
            } else {
                if (this.comments.size() == 0) {
                    this.comments.clear();
                    this.comments.addAll(comments);
                    mAdapter.notifyItemRangeInserted(1, this.comments.size());
                } else {
                    this.comments.clear();
                    this.comments.addAll(comments);
                    mAdapter.notifyDataSetChanged();
                }
                hasMore = comments.size() >= Constants.COMMENT_EVERY_PAGE_SIZE;
            }
        } else {
            this.page++;
            if (comments.size() == 0) {
                showSnackBar(getString(R.string.no_more));
                hasMore = false;
            } else {
                if (comments.size() < Constants.COMMENT_EVERY_PAGE_SIZE) {
                    showSnackBar(getString(R.string.no_more));
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                this.comments.addAll(comments);
                mAdapter.notifyItemRangeInserted(this.comments.size() + 1, this.comments.size() + comments.size());
            }
        }
    }

    @Override
    public void getCommentsError(String message, int page) {
        if (page == 1) {
            mRetryBtn.setVisibility(View.VISIBLE);
        } else {
            showSnackBar(message);
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WriteCommentActivity.CODE_SEND) {
            if (resultCode == WriteCommentActivity.CODE_SEND) {
                showSnackBar(getString(R.string.send_success));
                startGetComments();
            }
        } else if (requestCode == LoginActivity.CODE_LOGIN) {
            if (resultCode == LoginActivity.CODE_LOGIN) {
                comments.clear();
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.VISIBLE);
                startGetComments();
                showSnackBar(getString(R.string.login_success));
                EventBus.getDefault().post(new LoginEvent());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onLoginEvent(LoginForNewEvent event) {
        comments.clear();
        mAdapter.notifyItemRangeRemoved(1, comments.size());
        startGetComments();
    }
}
