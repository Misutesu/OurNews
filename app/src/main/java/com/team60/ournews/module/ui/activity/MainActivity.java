package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.team60.ournews.R;
import com.team60.ournews.event.ChangeViewPagerPageEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.module.model.User;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.ui.fragment.HomeFragment;
import com.team60.ournews.module.ui.fragment.TypeFragment;
import com.team60.ournews.module.view.MainView;
import com.team60.ournews.util.ImageLoader;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    public final String[] titles = {"推荐", "ACG", "游戏", "社会", "娱乐", "科技"};

    private List<Fragment> fragments;

    private SimpleDraweeView mHeaderUserAvatarImg;
    private TextView mHeaderUserNameText;
    private ImageView mHeaderNightModeImg;

    @BindView(R.id.activity_main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_main_top_view)
    View mTopView;
    @BindView(R.id.activity_main_nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_main_tool_bar_user_layout)
    LinearLayout mUserLayout;
    @BindView(R.id.activity_main_user_avatar_img)
    SimpleDraweeView mUserAvatarImg;
    @BindView(R.id.activity_main_user_name_text)
    TextView mUserNameText;
    @BindView(R.id.activity_main_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.activity_main_view_pager)
    ViewPager mViewPager;

    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init() {
        EventBus.getDefault().register(this);

        View mHeaderView = mNavView.getHeaderView(0);
        mHeaderUserAvatarImg = (SimpleDraweeView) mHeaderView.findViewById(R.id.header_user_avatar_img);
        mHeaderUserNameText = (TextView) mHeaderView.findViewById(R.id.header_user_name_text);
        mHeaderNightModeImg = (ImageView) mHeaderView.findViewById(R.id.header_night_mode_img);
        if (mNavView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) mNavView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mDrawerLayout.setFitsSystemWindows(true);
            mDrawerLayout.setClipToPadding(false);
        }

        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mTopView.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight()));

        if (ThemeUtil.isNightMode())
            mHeaderNightModeImg.setImageResource(R.drawable.night_mode);

        setUserInfo();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initViewPager(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        manager.putFragment(outState, HomeFragment.class.getName(), mHomeFragment);
        for (int i = 0; i < 6; i++) {

        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void setListener() {
        mUserLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                User.breakLogin();
                recreate();
                return false;
            }
        });

        mUserLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mHeaderUserAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User.isLogin()) {
                    startActivityForResult(new Intent(MainActivity.this, UserActivity.class), UserActivity.CODE_CHANGE_INFO);
                } else {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        mHeaderNightModeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ThemeUtil.isNightMode()) {
                    ThemeUtil.setNightMode(false);
                } else {
                    ThemeUtil.setNightMode(true);
                }
                recreate();
//                mDrawerLayout.closeDrawer(GravityCompat.START);
//                startActivity(new Intent(MainActivity.this, LoginTempActivity.class));
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    TypeFragment typeFragment = (TypeFragment) fragments.get(position);
                    typeFragment.getNewList();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initViewPager(Bundle savedInstanceState) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        } else {
            fragments.clear();
        }

//        mHomeFragment = new HomeFragment();
//
//        fragments.add(mHomeFragment);
//        for (int i = 1; i < 6; i++) {
//            fragments.add(TypeFragment.newInstance(i));
//        }

        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            mHomeFragment = (HomeFragment) manager.getFragment(savedInstanceState, HomeFragment.class.getName());
            fragments.add(mHomeFragment);
            for (int i = 1; i < 6; i++) {
                fragments.add(manager.getFragment(savedInstanceState, TypeFragment.class.getName() + i));
            }
        } else {
            mHomeFragment = new HomeFragment();

            fragments.add(mHomeFragment);
            for (int i = 1; i < 6; i++) {

                fragments.add(TypeFragment.newInstance(i));
            }
        }

        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setUserInfo() {
        if (User.isLogin() && !user.getPhoto().equals("NoImage")) {
            ImageLoader.with(MainActivity.this)
                    .setCircle()
                    .setBorder(2, Color.WHITE)
                    .setImage(MyUtil.getPhotoUrl(user.getPhoto()))
                    .into(mUserAvatarImg);
            ImageLoader.with(MainActivity.this)
                    .setCircle()
                    .setBorder(4, Color.WHITE)
                    .setImage(MyUtil.getPhotoUrl(user.getPhoto()))
                    .into(mHeaderUserAvatarImg);
        } else {
            ImageLoader.with(MainActivity.this)
                    .setCircle()
                    .setBorder(2, Color.WHITE)
                    .setImage(R.drawable.user_default_avatar)
                    .into(mUserAvatarImg);
            ImageLoader.with(MainActivity.this)
                    .setCircle()
                    .setBorder(4, Color.WHITE)
                    .setImage(R.drawable.user_default_avatar)
                    .into(mHeaderUserAvatarImg);
        }

        String userName;
        String headerUserName;
        if (User.isLogin()) {
            userName = headerUserName = user.getNickName();
        } else {
            userName = getString(R.string.no_login);
            headerUserName = getString(R.string.click_avatar_to_login);
        }
        mUserNameText.setText(userName);
        mHeaderUserNameText.setText(headerUserName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.CODE_LOGIN) {
            if (resultCode == LoginActivity.CODE_LOGIN) {
                setUserInfo();
                showSnackBar(getString(R.string.login_success));
            }
        } else if (requestCode == UserActivity.CODE_CHANGE_INFO) {
            if (resultCode == UserActivity.CODE_CHANGE_INFO) {
                setUserInfo();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (mViewPager.getCurrentItem() != 0) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onShowSnackEvent(ShowSnackEvent event) {
        showSnackBar(event.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onChangePageEvent(ChangeViewPagerPageEvent event) {
        mViewPager.setCurrentItem(event.getPage());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.tool_bar_save_img).setVisible(true);
//        invalidateOptionsMenu();
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tool_bar_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
