package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.UserView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.UiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

public class UserActivity extends BaseActivity implements UserView {

    public static final int CODE_CHANGE_INFO = 103;

    @BindView(R.id.activity_user_background_img)
    SimpleDraweeView mBackgroundImg;
    @BindView(R.id.activity_user_name_text)
    TextView mUserNameText;
    @BindView(R.id.activity_user_description_text)
    TextView mUserDescriptionText;
    @BindView(R.id.activity_user_avatar_img)
    SimpleDraweeView mUserAvatarImg;
    @BindView(R.id.activity_user_edit_btn)
    Button mUserEditBtn;
    @BindView(R.id.activity_user_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_user_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_user_title_text)
    TextView mTitleText;
    @BindView(R.id.activity_user_app_bar_layout)
    AppBarLayout mAppBarLayout;

    private AlphaAnimation inAnimation = new AlphaAnimation(0, 1);
    private AlphaAnimation outAnimation = new AlphaAnimation(1, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
        setUserInfo();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        inAnimation.setDuration(200);
        outAnimation.setDuration(200);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxSize = UiUtil.dip2px(240) - mToolBar.getHeight();
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

                float scaleSize = (1 + ((float) verticalOffset) / maxSize / 2);
                mUserAvatarImg.setScaleX(scaleSize);
                mUserAvatarImg.setScaleY(scaleSize);

                mUserNameText.setScaleX(scaleSize);
                mUserNameText.setScaleY(scaleSize);

                mUserDescriptionText.setScaleX(scaleSize);
                mUserDescriptionText.setScaleY(scaleSize);

                mUserEditBtn.setScaleX(scaleSize);
                mUserEditBtn.setScaleY(scaleSize);
            }
        });

        mUserAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SkipUtil.startPhotoActivity(UserActivity.this, null, user.getPhoto());
            }
        });

        mUserEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(UserActivity.this, EditUserActivity.class), CODE_CHANGE_INFO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_CHANGE_INFO)
            if (resultCode == CODE_CHANGE_INFO) {
                setResult(CODE_CHANGE_INFO);
                setUserInfo();
                showSnackBar(getString(R.string.save_success));
            }
    }

    private void setUserInfo() {
        mTitleText.setText(user.getNickName());
        mUserNameText.setText(user.getNickName());
        mUserDescriptionText.setText(user.getLoginName());

        Uri uri;

        if (!user.getPhoto().equals("NoImage")) {
            uri = FrescoLoader.getUri(MyUtil.getPhotoUrl(user.getPhoto()));
        } else {
            uri = FrescoLoader.getUri(R.drawable.user_default_avatar);
        }

        FrescoLoader.load(uri)
                .setCircle()
                .setBorder(4, Color.WHITE)
                .into(mUserAvatarImg);
        FrescoLoader.load(uri)
                .resize(128, 128)
                .setPostprocessor(new BlurPostprocessor(UserActivity.this))
                .into(mBackgroundImg);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
