package com.team60.ournews.module.ui.activity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.adapter.CommentActivityRecyclerViewAdapter;
import com.team60.ournews.module.bean.Comment;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.presenter.CommentPresenter;
import com.team60.ournews.module.presenter.impl.CommentPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.CommentVIew;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    Button mRetryBtn;
    @BindView(R.id.activity_comment_no_comment_text)
    TextView mNoCommentText;
    @BindView(R.id.activity_comment_top_view)
    View mTopView;

    private List<Comment> comments;
    private CommentActivityRecyclerViewAdapter mAdapter;

    private CommentPresenter mPresenter;

    private New n;
    private int page = 1;
    private int sort = 1;

    private boolean isLoad = false;
    private boolean hasMore = true;

    private boolean isShow = true;

    private AlertDialog mLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Explode explodeIn = new Explode();
            explodeIn.setDuration(400);
            explodeIn.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    startGetComments();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
            getWindow().setEnterTransition(explodeIn);

            Explode explodeOut = new Explode();
            explodeOut.setDuration(400);
            getWindow().setExitTransition(explodeOut);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new CommentPresenterImpl(this);

        n = getIntent().getParcelableExtra(New.class.getName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mTopView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight()));

        mToolBar.setTitle(getString(R.string.comment));
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (comments == null)
            comments = new ArrayList<>();
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
        });

        mRecyclerView.addOnScrollListener(new MyRecyclerViewOnScroll());

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isLoad;
            }
        });

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
                    if (mLoginDialog == null) {
                        AlertDialog.Builder builder;
                        if (ThemeUtil.isNightMode()) {
                            builder = new AlertDialog.Builder(CommentActivity.this, R.style.NightDialogTheme);
                        } else {
                            builder = new AlertDialog.Builder(CommentActivity.this);
                        }
                        mLoginDialog = builder.setTitle(getString(R.string.hint))
                                .setMessage(getString(R.string.no_login_do_you_login_now))
                                .setPositiveButton(getString(R.string.go_to_login), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(CommentActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), null)
                                .create();
                    }
                    mLoginDialog.show();
                }
            }
        });
    }

    private void startGetComments() {
        isLoad = true;
        if (mProgressBar.getVisibility() == View.GONE)
            mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.getComments(n.getId(), 1, sort);
    }

    private void startLoadMore() {
        isLoad = true;
        mAdapter.setLoadMore(true);
        mPresenter.getComments(n.getId(), page + 1, sort);
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
                if (comments.size() < Constants.COMMENT_EVERY_PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
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
        if (requestCode == WriteCommentActivity.CODE_SEND)
            if (resultCode == WriteCommentActivity.CODE_SEND) {
                showSnackBar(getString(R.string.send_success));
                startGetComments();
            }
    }

    private class MyRecyclerViewOnScroll extends RecyclerView.OnScrollListener {

        private int firstItem;
        private int lastItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int totalItemCount = layoutManager.getItemCount();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                lastItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItem != -1)
                    lastItem = linearLayoutManager.findLastVisibleItemPosition();
            }
            if (!isLoad && hasMore
                    && (lastItem == totalItemCount - 1) && (dx > 0 || dy > 0)) {
                startLoadMore();
            }

            if (dy > 0) {
                showOrHideBottomLayout(false);
            } else if (dy < 0) {
                showOrHideBottomLayout(true);
            }
        }
    }
}
