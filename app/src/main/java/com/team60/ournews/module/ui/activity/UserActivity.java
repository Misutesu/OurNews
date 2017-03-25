package com.team60.ournews.module.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.module.bean.OtherUser;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.ui.fragment.CenterFragment;
import com.team60.ournews.module.view.UserView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.activity_user_collection_text)
    TextView mCollectionText;
    @BindView(R.id.activity_user_history_text)
    TextView mHistoryText;
    @BindView(R.id.activity_user_view_pager)
    ViewPager mViewPager;

    private boolean isTitleShow = true;

    private List<Fragment> fragments;

    private OtherUser mOtherUser;

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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mOtherUser = getIntent().getParcelableExtra("otherUser");
        if (mOtherUser != null && User.isLogin() && mOtherUser.getId() == user.getId())
            mOtherUser = null;

        EventBus.getDefault().register(this);

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (fragments == null) fragments = new ArrayList<>();
        else fragments.clear();

        fragments.add(CenterFragment.newInstance(mOtherUser, 0));
        fragments.add(CenterFragment.newInstance(mOtherUser, 1));

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mCollectionText.setTextColor(ThemeUtil.getColor(getTheme(), R.attr.textColor));
                    mHistoryText.setTextColor(ThemeUtil.getColor(getTheme(), R.attr.textColor2));
                } else if (position == 1) {
                    mCollectionText.setTextColor(ThemeUtil.getColor(getTheme(), R.attr.textColor2));
                    mHistoryText.setTextColor(ThemeUtil.getColor(getTheme(), R.attr.textColor));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                    if (!isTitleShow) {
                        isTitleShow = true;
                        ObjectAnimator.ofFloat(mTitleText, "alpha", mTitleText.getAlpha(), 1f).setDuration(200).start();
                    }
                } else {
                    if (isTitleShow) {
                        isTitleShow = false;
                        ObjectAnimator.ofFloat(mTitleText, "alpha", mTitleText.getAlpha(), 0f).setDuration(200).start();
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
                setUserInfo();
                showSnackBar(getString(R.string.save_success));
                EventBus.getDefault().post(new LoginEvent());
            }
    }

    private void setUserInfo() {
        String userName;
        String userNickname;
        String loginName;

        Uri uri;

        if (mOtherUser != null) {
            userName = mOtherUser.getNickName();
            userNickname = mOtherUser.getNickName();
            loginName = "";

            if (!mOtherUser.getPhoto().equals("NoImage")) {
                uri = FrescoLoader.getUri(MyUtil.getPhotoUrl(user.getPhoto()));
            } else {
                uri = FrescoLoader.getUri(R.drawable.user_default_avatar);
            }

            mUserEditBtn.setVisibility(View.GONE);
        } else {
            userName = user.getNickName();
            userNickname = user.getNickName();
            loginName = user.getLoginName();

            if (!user.getPhoto().equals("NoImage")) {
                uri = FrescoLoader.getUri(MyUtil.getPhotoUrl(user.getPhoto()));
            } else {
                uri = FrescoLoader.getUri(R.drawable.user_default_avatar);
            }
        }

        mTitleText.setText(userName);
        mUserNameText.setText(userNickname);
        mUserDescriptionText.setText(loginName);

        FrescoLoader.load(uri)
                .setCircle()
                .setBorder(4, Color.WHITE)
                .into(mUserAvatarImg);
        FrescoLoader.load(uri)
                .resize(128, 128)
                .setDurationTime(1000)
                .setPostprocessor(new BlurPostprocessor(UserActivity.this))
                .into(mBackgroundImg);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick({R.id.activity_user_collection_text, R.id.activity_user_history_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_user_collection_text:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.activity_user_history_text:
                mViewPager.setCurrentItem(1);
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onShowSnackEvent(ShowSnackEvent event) {
        showSnackBar(event.getMessage());
    }
}
