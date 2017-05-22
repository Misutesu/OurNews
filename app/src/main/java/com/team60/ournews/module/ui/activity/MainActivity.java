package com.team60.ournews.module.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.event.ChangeStyleEvent;
import com.team60.ournews.event.ChangeViewPagerPageEvent;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.event.ShowSnackEvent;
import com.team60.ournews.module.adapter.ThemeSelectRecyclerViewAdapter;
import com.team60.ournews.module.bean.Theme;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.model.CheckUpdateResult;
import com.team60.ournews.module.presenter.MainPresenter;
import com.team60.ournews.module.presenter.impl.MainPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.ui.fragment.HomeFragment;
import com.team60.ournews.module.ui.fragment.TypeFragment;
import com.team60.ournews.module.view.MainView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.PushUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;
import com.team60.ournews.util.UpdataUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.team60.ournews.common.Constants.SHARED_PREFERENCES_VERSION;

public class MainActivity extends BaseActivity implements MainView {

    private final int NOTIFY_ID = 101;

    private final int NO_ACTION = -1;
    private final int CLICK_AVATAR_ACTION = 1;

    public final String[] titles = {"推荐", "ACG", "游戏", "社会", "娱乐", "科技"};

    private List<Fragment> fragments;

    private RelativeLayout mHeaderTopLayout;
    private SimpleDraweeView mHeaderUserAvatarImg;
    private AppCompatTextView mHeaderUserNameText;
    private ImageView mHeaderNightModeImg;
    private LinearLayout mSelectThemeLayout;
    private LinearLayout mSettingLayout;
    private LinearLayout mLogoutLayout;

    @BindView(R.id.activity_main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_main_top_view)
    View mTopView;
    @BindView(R.id.activity_main_nav_view)
    NavigationView mNavView;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.activity_main_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_main_tool_bar_user_layout)
    LinearLayout mUserLayout;
    @BindView(R.id.activity_main_user_avatar_img)
    SimpleDraweeView mUserAvatarImg;
    @BindView(R.id.activity_main_user_name_text)
    AppCompatTextView mUserNameText;
    @BindView(R.id.activity_main_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.activity_main_view_pager)
    ViewPager mViewPager;

    private MainPresenter mPresenter;

    private ThemeSelectRecyclerViewAdapter mThemeAdapter;

    private AlertDialog mThemeDialog;
    private AlertDialog mThemeHintDialog;
    private AlertDialog mLogoutDialog;

    private long lastOnBackTime;
    private int drawerLayoutCloseAction = NO_ACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init(savedInstanceState);
        setListener();
        setUserInfo();
        showWellCome();
        mPresenter.checkUpdate();
    }

    @Override
    protected void onDestroy() {
        UpdataUtil.newInstance().destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new MainPresenterImpl(this, this);

        View mHeaderView = mNavView.getHeaderView(0);
        mHeaderTopLayout = (RelativeLayout) mHeaderView.findViewById(R.id.header_top_layout);
        mHeaderUserAvatarImg = (SimpleDraweeView) mHeaderView.findViewById(R.id.header_user_avatar_img);
        mHeaderUserNameText = (AppCompatTextView) mHeaderView.findViewById(R.id.header_user_name_text);
        mHeaderNightModeImg = (ImageView) mHeaderView.findViewById(R.id.header_night_mode_img);
        mSelectThemeLayout = (LinearLayout) mHeaderView.findViewById(R.id.header_theme_select_layout);
        mSettingLayout = (LinearLayout) mHeaderView.findViewById(R.id.header_setting_layout);
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

        if (ThemeUtil.newInstance().isNightMode())
            mHeaderNightModeImg.setImageResource(R.drawable.night_mode);

        initViewPager();
    }


    @Override
    public void setListener() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                switch (drawerLayoutCloseAction) {
                    case CLICK_AVATAR_ACTION:
                        if (User.isLogin()) {
                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                        } else {
                            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LoginActivity.CODE_LOGIN);
                        }
                        break;
                }
                drawerLayoutCloseAction = NO_ACTION;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

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
                drawerLayoutCloseAction = CLICK_AVATAR_ACTION;
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        mHeaderNightModeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ThemeUtil.newInstance().isNightMode()) {
                    ThemeUtil.newInstance().setNightMode(false);
                    EventBus.getDefault().post(
                            getChangeColorEvent(getTheme(), MainActivity.this, ThemeUtil.newInstance().getStyle()));
                } else {
                    ThemeUtil.newInstance().setNightMode(true);
                    EventBus.getDefault().post(
                            getChangeColorEvent(getTheme(), MainActivity.this, R.style.NightTheme));
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        mSelectThemeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initThemeDialog();
                mThemeDialog.show();
            }
        });

        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });

        mLogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLogoutDialog == null) {
                    mLogoutDialog = ThemeUtil.getThemeDialogBuilder(MainActivity.this).setMessage(getString(R.string.are_you_sure_logout))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User.breakLogin();
                                    PushUtil.newInstance().logout(MainActivity.this);
                                    setUserInfo();
                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                }
                            })
                            .setNegativeButton(R.string.no, null)
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

    private void showWellCome() {
        SharedPreferences versionSP = MyUtil.getSharedPreferences(this, SHARED_PREFERENCES_VERSION);
        int nowVersionCode = MyUtil.getVersionCode(this);
        int oldVersionCode = versionSP.getInt("versionCode", -2);
        if (nowVersionCode != oldVersionCode) {
            versionSP.edit().putInt("versionCode", nowVersionCode).apply();
            startActivity(new Intent(MainActivity.this, WellComeActivity.class));
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
            mThemeAdapter = new ThemeSelectRecyclerViewAdapter(this);
            mThemeRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            mThemeRecyclerView.setHasFixedSize(true);
            mThemeRecyclerView.setAdapter(mThemeAdapter);

            mThemeDialog = ThemeUtil.getThemeDialogBuilder(this)
                    .setView(view)
                    .create();

            mThemeAdapter.setOnItemClickListener(new ThemeSelectRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onClick(final Theme theme, final int position) {
                    if (theme.getThemeId() != ThemeUtil.newInstance().getStyle()) {
                        if (ThemeUtil.newInstance().isNightMode()) {
                            if (mThemeHintDialog == null)
                                mThemeHintDialog = ThemeUtil.getThemeDialogBuilder(MainActivity.this)
                                        .setTitle(getString(R.string.hint))
                                        .setMessage(getString(R.string.select_theme_hint))
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ThemeUtil.newInstance().setStyle(position);
                                                ThemeUtil.newInstance().setNightMode(false);
                                                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                                    mDrawerLayout.closeDrawer(GravityCompat.START);
                                                }
                                                EventBus.getDefault().post(
                                                        getChangeColorEvent(getTheme(), MainActivity.this, ThemeUtil.newInstance().getStyle()));
                                            }
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .create();
                            mThemeHintDialog.show();
                        } else {
                            ThemeUtil.newInstance().setStyle(position);
                            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                                mDrawerLayout.closeDrawer(GravityCompat.START);
                            }
                            EventBus.getDefault().post(
                                    getChangeColorEvent(getTheme(), MainActivity.this, ThemeUtil.newInstance().getStyle()));
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
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastOnBackTime >= 2 * 1000) {
                lastOnBackTime = nowTime;
                showSnackBar(getString(R.string.click_again_exit));
            } else {
                super.onBackPressed();
            }
//            Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
//            launcherIntent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(launcherIntent);
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

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
    public void onChangeStyle(ChangeStyleEvent event) {
        ThemeUtil.changeColor(event.getColorPrimary(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                mTopView.setBackgroundColor(color);
                mAppBar.setBackgroundColor(color);
                mHeaderTopLayout.setBackgroundColor(color);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setNavigationBarColor(color);
                }
            }
        });

        ThemeUtil.changeColor(event.getColorText(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                ((ImageView) mSelectThemeLayout.findViewById(R.id.header_theme_img)).setColorFilter(color);
                ((ImageView) mLogoutLayout.findViewById(R.id.header_logout_img)).setColorFilter(color);
            }
        });

        ThemeUtil.changeColor(event.getColorText1(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                ((TextView) mSelectThemeLayout.findViewById(R.id.header_theme_text)).setTextColor(color);
                ((TextView) mLogoutLayout.findViewById(R.id.header_logout_text)).setTextColor(color);
            }
        });

        ThemeUtil.changeColor(event.getColorBackground(), new ThemeUtil.OnColorChangeListener() {
            @Override
            public void onColorChange(int color) {
                ((View) mTopView.getParent()).setBackgroundColor(color);
                mNavView.setBackgroundColor(color);
            }
        });

        if (mThemeAdapter != null) mThemeAdapter.notifyDataSetChanged();
    }

    private ChangeStyleEvent getChangeColorEvent(Resources.Theme theme, Activity activity, int style) {
        int startColorPrimary = ThemeUtil.getColor(theme, R.attr.colorPrimary);
        int startColorIcon = ThemeUtil.getColor(theme, R.attr.iconColor);
        int startColorText = ThemeUtil.getColor(theme, R.attr.textColor);
        int startColorText1 = ThemeUtil.getColor(theme, R.attr.textColor1);
        int startColorText3 = ThemeUtil.getColor(theme, R.attr.textColor3);
        int startColorBackground = ThemeUtil.getColor(theme, R.attr.windowsBackgroundColor);
        int startColorItemBackground = ThemeUtil.getColor(theme, R.attr.itemBackgroundColor);
        activity.setTheme(style);
        int endColorPrimary = ThemeUtil.getColor(theme, R.attr.colorPrimary);
        int endColorIcon = ThemeUtil.getColor(theme, R.attr.iconColor);
        int endColorText = ThemeUtil.getColor(theme, R.attr.textColor);
        int endColorText1 = ThemeUtil.getColor(theme, R.attr.textColor1);
        int endColorText3 = ThemeUtil.getColor(theme, R.attr.textColor3);
        int endColorBackground = ThemeUtil.getColor(theme, R.attr.windowsBackgroundColor);
        int endColorItemBackground = ThemeUtil.getColor(theme, R.attr.itemBackgroundColor);

        int[] colorPrimary = {startColorPrimary, endColorPrimary};
        int[] colorBackground = {startColorBackground, endColorBackground};
        int[] colorItemBackground = {startColorItemBackground, endColorItemBackground};
        int[] colorIcon = {startColorIcon, endColorIcon};
        int[] colorText = {startColorText, endColorText};
        int[] colorText1 = {startColorText1, endColorText1};
        int[] colorText3 = {startColorText3, endColorText3};
        return new ChangeStyleEvent(colorPrimary, colorBackground,
                colorItemBackground, colorIcon, colorText, colorText1, colorText3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

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
    public void hasNewVersion(final CheckUpdateResult result) {
        UpdataUtil.newInstance().showUpdataDialog(this, result);
    }

//    private void checkPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED) {
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        PERMISSION_CODE);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                showSnackBar(getString(R.string.no_write_permission));
//            }
//        }
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.tool_bar_save_img).setVisible(true);
//        invalidateOptionsMenu();
//        return super.onPrepareOptionsMenu(menu);
//    }
}
