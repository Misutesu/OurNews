package com.team60.ournews.module.ui.activity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenter.WriteCommentPresenter;
import com.team60.ournews.module.presenter.impl.WriteCommentPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.WriteCommentView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;
import com.team60.ournews.widget.ResizeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WriteCommentActivity extends BaseActivity implements WriteCommentView {

    public static final int CODE_SEND = 107;

    @BindView(R.id.activity_write_comment_user_name_text)
    TextView mUserNameText;
    @BindView(R.id.activity_write_comment_content_text)
    EditText mContentText;
    @BindView(R.id.activity_write_comment_cancel_btn)
    Button mCancelBtn;
    @BindView(R.id.activity_write_comment_send_btn)
    Button mSendBtn;
    @BindView(R.id.activity_write_comment_layout)
    ResizeLayout mCommentLayout;
    @BindView(R.id.activity_write_comment_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_write_comment_content_layout)
    LinearLayout mContentLayout;
    @BindView(R.id.activity_write_comment_progress_bar)
    ProgressBar mProgressBar;

    private WriteCommentPresenter mPresenter;

    private New n;

    private TranslateAnimation inAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            , Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
    private TranslateAnimation outAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
            , Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.activity_write_comment);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
        showInfo();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        n = getIntent().getParcelableExtra(New.class.getName());

        mPresenter = new WriteCommentPresenterImpl(this);

        inAnimation.setDuration(400);
        outAnimation.setDuration(400);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(ThemeUtil.getColor(getTheme(), R.attr.itemBackgroundColor));
    }

    @Override
    public void setListener() {
        mCommentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mCommentLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = UiUtil.getScreenHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                if (heightDiff > 100) {
                    int statusBarHeight = UiUtil.getStatusBarHeight();
                    int realKeyboardHeight = heightDiff - statusBarHeight;
                    mCommentLayout.setPadding(0, 0, 0, realKeyboardHeight);
                } else {
                    mCommentLayout.setPadding(0, 0, 0, 0);
                }
            }
        });

        mCommentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mContentText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    MyUtil.closeKeyBord(mContentText);
                    sendContent();
                    return true;
                }
                return false;
            }
        });

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.closeKeyBord(mContentText);
                sendContent();
            }
        });
    }

    private void showInfo() {
        mUserNameText.setText(user.getNickName());
    }

    private void sendContent() {
        String content = mContentText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showSnackBar(getString(R.string.no_content));
        } else {
            showOrHideLayout(false);
            mPresenter.sendComment(user.getId(), n.getId(), content);
        }
    }

    private void showOrHideLayout(boolean showOrHide) {
        if (showOrHide) {
            if (mContentLayout.getVisibility() == View.GONE) {
                mContentLayout.setVisibility(View.VISIBLE);
                mContentLayout.startAnimation(inAnimation);
                mProgressBar.setVisibility(View.GONE);
            }
        } else {
            if (mContentLayout.getVisibility() == View.VISIBLE) {
                mContentLayout.startAnimation(outAnimation);
                mContentLayout.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void sendCommentEnd() {
        showOrHideLayout(true);
    }

    @Override
    public void sendCommentSuccess() {
        setResult(CODE_SEND);
        finish();
    }

    @Override
    public void sendCommentError(String message) {
        showSnackBar(message);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
