package com.team60.ournews.module.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.model.User;
import com.team60.ournews.module.presenter.NewPresenter;
import com.team60.ournews.module.presenter.impl.NewPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.NewView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;
import com.team60.ournews.widget.NewTextAndImageView;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewActivity extends BaseActivity implements NewView {

    @BindView(R.id.activity_new_background_img)
    SimpleDraweeView mBackgroundImg;
    @BindView(R.id.activity_new_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_new_app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.activity_new_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_new_title_text)
    TextView mTitleText;
    @BindView(R.id.activity_new_collapsing_tool_bar_layout)
    CollapsingToolbarLayout mCollapsingToolBarLayout;
    @BindView(R.id.activity_new_create_time_text)
    TextView mCreateTimeText;
    @BindView(R.id.activity_new_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.activity_new_retry_btn)
    Button mRetryBtn;
    @BindView(R.id.activity_new_content_view)
    NewTextAndImageView mContentView;
    @BindView(R.id.activity_new_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.activity_new_nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.activity_new_bottom_action_layout)
    LinearLayout mBottomActionLayout;
    @BindView(R.id.activity_new_bottom_layout)
    LinearLayout mBottomLayout;
    @BindView(R.id.activity_new_write_comment_layout)
    LinearLayout mWriteCommentLayout;
    @BindView(R.id.activity_new_comment_number_text)
    TextView mCommentNumberText;
    @BindView(R.id.activity_new_comment_layout)
    LinearLayout mCommentLayout;
    @BindView(R.id.activity_new_content_layout)
    LinearLayout mContentLayout;

    private New n;

    private AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation outAnimation = new AlphaAnimation(1, 0);

    private TranslateAnimation showAnimation;
    private TranslateAnimation hideAnimation;

    private NewPresenter mPresenter;
    private boolean isHide = false;
    private boolean isShow = false;

    private AlertDialog mLoginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ButterKnife.bind(this);
        n = getIntent().getParcelableExtra(New.class.getName());

        if (n == null) {
            showToast(getString(R.string.open_new_error));
            finish();
        } else {
            init(savedInstanceState);
            setListener();
            showNewInfo();
            getNewContent();
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new NewPresenterImpl(this);

        inAnimation.setDuration(200);
        outAnimation.setDuration(200);
        showAnimation = new TranslateAnimation(0, 0, UiUtil.dip2px(48), 0);
        showAnimation.setDuration(300);
        showAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isShow = true;
                mBottomActionLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isShow = false;
                isHide = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hideAnimation = new TranslateAnimation(0, 0, 0, UiUtil.dip2px(48));
        hideAnimation.setDuration(300);
        hideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isHide = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isHide = false;
                isShow = false;
                mBottomActionLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        mCollapsingToolBarLayout.setExpandedTitleColor(Color.WHITE);
        mCollapsingToolBarLayout.setCollapsedTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxSize = UiUtil.dip2px(320) - mToolBar.getHeight();
                if (verticalOffset <= -maxSize / 3 * 2) {
                    if (mTitleText.getVisibility() == View.GONE) {
                        mTitleText.setVisibility(View.VISIBLE);
                        mTitleText.startAnimation(inAnimation);
                    }
                } else {
                    if (mTitleText.getVisibility() == View.VISIBLE) {
                        mTitleText.startAnimation(outAnimation);
                        mTitleText.setVisibility(View.GONE);
                    }
                }
            }
        });

        mBackgroundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkipUtil.startPhotoActivity(NewActivity.this, null, n.getCover());
            }
        });

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                getNewContent();
            }
        });

        mContentView.setOnActionListener(new NewTextAndImageView.OnActionListener() {
            @Override
            public void onPhotoLoadEnd(View view, String photoName) {
                SkipUtil.startPhotoActivity(NewActivity.this, null, photoName);
            }
        });

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    if (mBottomActionLayout.getVisibility() == View.VISIBLE && !isHide) {
                        mBottomLayout.startAnimation(hideAnimation);
                    }
                } else {
                    if (mBottomActionLayout.getVisibility() == View.GONE && !isShow) {
                        mBottomLayout.startAnimation(showAnimation);
                    }
                }
            }
        });

        mWriteCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    Intent intent = new Intent(NewActivity.this, WriteCommentActivity.class);
                    intent.putExtra(New.class.getName(), n);
                    startActivityForResult(intent, WriteCommentActivity.CODE_SEND);
                } else {
                    if (mLoginDialog == null) {
                        AlertDialog.Builder builder;
                        if (ThemeUtil.isNightMode()) {
                            builder = new AlertDialog.Builder(NewActivity.this, R.style.NightDialogTheme);
                        } else {
                            builder = new AlertDialog.Builder(NewActivity.this);
                        }
                        mLoginDialog = builder.setTitle(getString(R.string.hint))
                                .setMessage(getString(R.string.no_login_do_you_login_now))
                                .setPositiveButton(getString(R.string.go_to_login), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(NewActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), null)
                                .create();
                    }
                    mLoginDialog.show();
                }
            }
        });

        mCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mCommentNumberText.getText().toString())) {
                    Intent intent = new Intent(NewActivity.this, CommentActivity.class);
                    intent.putExtra(New.class.getName(), n);
                    startActivity(intent);
                }
            }
        });
    }

    private void showNewInfo() {
        FrescoLoader.load(MyUtil.getPhotoUrl(n.getCover()))
                .into(mBackgroundImg);
        mCollapsingToolBarLayout.setTitle(n.getTitle());
        mTitleText.setText(n.getTitle());
        mCreateTimeText.setText(n.getCreateTime());
    }

    private void getNewContent() {
        if (User.isLogin()) {
            mPresenter.getNewContent(n.getId(), user.getId());
        } else {
            mPresenter.getNewContent(n.getId(), -1);
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getNewContentEnd() {
        mProgressBar.startAnimation(outAnimation);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void getNewContentSuccess(New n) throws JSONException {
        mContentView.setContent(n.getContent());
        mCommentNumberText.setText(String.valueOf(n.getCommentNum()));
        mCommentNumberText.startAnimation(inAnimation);
        this.n.setContent(n.getContent());
        this.n.setCommentNum(n.getCommentNum());
    }

    @Override
    public void getNewContentError(String message) {
        showSnackBar(message);
        mRetryBtn.setVisibility(View.VISIBLE);
        mRetryBtn.startAnimation(inAnimation);
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
                n.setCommentNum(n.getCommentNum() + 1);
                mCommentNumberText.setText(String.valueOf(n.getCommentNum()));
            }
    }
}
