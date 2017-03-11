package com.team60.ournews.module.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.event.ChangeViewPagerPageEvent;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.module.adapter.ThemeSelectRecyclerViewAdapter;
import com.team60.ournews.module.bean.Theme;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.ui.fragment.HomeFragment;
import com.team60.ournews.module.ui.fragment.TypeFragment;
import com.team60.ournews.module.view.MainView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.PushUtil;
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

    private final int PERMISSION_CODE = 100;

    public final String[] titles = {"推荐", "ACG", "游戏", "社会", "娱乐", "科技"};

    private List<Fragment> fragments;

    private SimpleDraweeView mHeaderUserAvatarImg;
    private TextView mHeaderUserNameText;
    private ImageView mHeaderNightModeImg;
    private LinearLayout mSelectThemeLayout;
    private LinearLayout mLogoutLayout;

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

    private AlertDialog mThemeDialog;

    private AlertDialog mThemeHintDialog;
    private AlertDialog mLogoutDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
        checkPermission();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        View mHeaderView = mNavView.getHeaderView(0);
        mHeaderUserAvatarImg = (SimpleDraweeView) mHeaderView.findViewById(R.id.header_user_avatar_img);
        mHeaderUserNameText = (TextView) mHeaderView.findViewById(R.id.header_user_name_text);
        mHeaderNightModeImg = (ImageView) mHeaderView.findViewById(R.id.header_night_mode_img);
        mSelectThemeLayout = (LinearLayout) mHeaderView.findViewById(R.id.header_theme_select_layout);
        mLogoutLayout = (LinearLayout) mHeaderView.findViewById(R.id.header_logout_layout);

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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mDrawerLayout.setFitsSystemWindows(true);
            mDrawerLayout.setClipToPadding(false);
        }

        if (ThemeUtil.isNightMode())
            mHeaderNightModeImg.setImageResource(R.drawable.night_mode);

        initViewPager();

        setUserInfo();
    }


    @Override
    public void setListener() {
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
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                } else {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                }
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
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
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                recreate();
            }
        });

        mSelectThemeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initThemeDialog();
                mThemeDialog.show();
            }
        });

        mLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogoutDialog == null) {
                    mLogoutDialog = ThemeUtil.getThemeDialogBuilder(MainActivity.this).setMessage(getString(R.string.are_you_sure_logout))
                            .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User.breakLogin();
                                    setUserInfo();
                                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                        mDrawerLayout.closeDrawer(GravityCompat.START);
                                    }
                                }
                            })
                            .setNegativeButton(getString(R.string.no), null)
                            .create();
                }
                mLogoutDialog.show();
            }
        });
    }

    private void initViewPager() {
        if (fragments == null) fragments = new ArrayList<>();
        else fragments.clear();

        fragments.add(new HomeFragment());
        for (int i = 1; i < 6; i++) {
            fragments.add(TypeFragment.newInstance(i));
        }

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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            }
        }
    }

    private void setUserInfo() {
        Uri uri;
        String userName;
        String headerUserName;

        if (User.isLogin() && !user.getPhoto().equals("NoImage")) {
            uri = FrescoLoader.getUri(MyUtil.getPhotoUrl(user.getPhoto()));
        } else {
            uri = FrescoLoader.getUri(R.drawable.user_default_avatar);
        }

        if (User.isLogin()) {
            userName = headerUserName = user.getNickName();
            mLogoutLayout.setVisibility(View.VISIBLE);
            MyUtil.sendLog(this, "token : " + user.getToken());
        } else {
            userName = getString(R.string.no_login);
            headerUserName = getString(R.string.click_avatar_to_login);
            mLogoutLayout.setVisibility(View.GONE);
        }

        mUserNameText.setText(userName);
        mHeaderUserNameText.setText(headerUserName);
        FrescoLoader.load(uri)
                .setCircle()
                .setBorder(2, Color.WHITE)
                .into(mUserAvatarImg);
        FrescoLoader.load(uri)
                .setCircle()
                .setBorder(4, Color.WHITE)
                .into(mHeaderUserAvatarImg);
    }

    private void initThemeDialog() {
        if (mThemeDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_theme, null);
            RecyclerView mThemeRecyclerView = (RecyclerView) view.findViewById(R.id.layout_select_item_recycler_view);
            ThemeSelectRecyclerViewAdapter mThemeAdapter = new ThemeSelectRecyclerViewAdapter(this);
            mThemeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mThemeRecyclerView.setHasFixedSize(true);
            mThemeRecyclerView.setAdapter(mThemeAdapter);

            mThemeDialog = ThemeUtil.getThemeDialogBuilder(this)
                    .setView(view)
                    .create();

            mThemeAdapter.setOnItemClickListener(new ThemeSelectRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onClick(final Theme theme, final int position) {
                    if (theme.getThemeId() != ThemeUtil.getStyle()) {
                        if (ThemeUtil.isNightMode()) {
                            if (mThemeHintDialog == null)
                                mThemeHintDialog = ThemeUtil.getThemeDialogBuilder(MainActivity.this)
                                        .setTitle(getString(R.string.hint))
                                        .setMessage(getString(R.string.select_theme_hint))
                                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ThemeUtil.setStyle(position);
                                                ThemeUtil.setNightMode(false);
                                                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                                }
                                                recreate();
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.no), null)
                                        .create();
                            mThemeHintDialog.show();
                        } else {
                            ThemeUtil.setStyle(position);
                            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                            }
                            recreate();
                        }
                    }
                    mThemeDialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.CODE_LOGIN) {
            if (resultCode == LoginActivity.CODE_LOGIN) {
                setUserInfo();
                showSnackBar(getString(R.string.login_success));
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

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onLoginEvent(LoginEvent event) {
        setUserInfo();
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
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PushUtil.initPush(MainActivity.this);
            } else {
                showSnackBar(getString(R.string.no_write_permission));
            }
        }
    }
}
