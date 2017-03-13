package com.team60.ournews.module.ui.activity.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.CheckLoginResult;
import com.team60.ournews.module.ui.activity.FirstActivity;
import com.team60.ournews.module.ui.activity.LoginActivity;
import com.team60.ournews.module.view.base.BaseView;
import com.team60.ournews.util.PushUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    public User user = User.newInstance();

    private CompositeDisposable mDisposable;

    private AlertDialog mTokenErrorDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyApplication.addActivity(this);
        super.onCreate(savedInstanceState);

        UiUtil.initialize(this);

        if (!getClass().getName().equals(FirstActivity.class.getName())) {
            checkLogin();
            if (ThemeUtil.isNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                setTheme(R.style.NightTheme);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                int styleId = ThemeUtil.getStyle();
                if (styleId != -1)
                    setTheme(styleId);
            }
        }
        hideStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null)
            mDisposable.clear();
    }

    public void addSubscription(@NonNull Disposable dis) {
        if (mDisposable == null)
            mDisposable = new CompositeDisposable();
        mDisposable.add(dis);
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(ThemeUtil.getColor(getTheme(), R.attr.colorPrimary));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void checkLogin() {
        if (User.isLogin()) {
            PushUtil.newInstance().setAlias(this, String.valueOf(User.newInstance().getId()));
            addSubscription(RetrofitUtil.newInstance()
                    .checkLogin(user.getId(), user.getToken())
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<CheckLoginResult>() {
                        @Override
                        public void accept(@NonNull CheckLoginResult result) throws Exception {
                            if (result.getResult().equals("success")) {
                                User user = User.newInstance();
                                user.setLoginName(result.getData().getLoginName());
                                user.setNickName(result.getData().getNickName());
                                user.setSex(result.getData().getSex());
                                user.setPhoto(result.getData().getPhoto());
                            } else {
                                User.breakLogin();
                                PushUtil.newInstance().logout(BaseActivity.this);
                                if (mTokenErrorDialog == null) {
                                    mTokenErrorDialog = ThemeUtil.getThemeDialogBuilder(BaseActivity.this)
                                            .setTitle(R.string.hint)
                                            .setMessage(R.string.token_error)
                                            .setPositiveButton(R.string.go_to_login, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                                                }
                                            })
                                            .create();
                                }
                                mTokenErrorDialog.show();
                            }
                            EventBus.getDefault().post(new LoginEvent());
                        }
                    }));
        }
    }

    public abstract void init(Bundle savedInstanceState);

    public abstract void setListener();
}
