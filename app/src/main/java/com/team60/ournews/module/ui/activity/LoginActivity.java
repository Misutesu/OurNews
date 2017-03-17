package com.team60.ournews.module.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.team60.ournews.R;
import com.team60.ournews.event.LoginEvent;
import com.team60.ournews.module.presenter.LoginPresenter;
import com.team60.ournews.module.presenter.impl.LoginPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.LoginView;
import com.team60.ournews.util.MyUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;

public class LoginActivity extends BaseActivity implements LoginView {

    public static final int CODE_LOGIN = 101;

    @BindView(R.id.activity_login_login_name_input_layout)
    TextInputLayout mLoginNameInputLayout;
    @BindView(R.id.activity_login_password_input_layout)
    TextInputLayout mPasswordInputLayout;
    @BindView(R.id.activity_login_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_login_login_name_text)
    EditText mLoginNameText;
    @BindView(R.id.activity_login_password_text)
    EditText mPasswordText;
    @BindView(R.id.activity_login_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_login_register_btn)
    Button mRegisterBtn;
    @BindView(R.id.activity_login_login_btn)
    Button mLoginBtn;

    private LoginPresenter mPresenter;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new LoginPresenterImpl(this, this);

        mToolBar.setTitle(getString(R.string.login));
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyUtil.openKeyBord(this, mLoginNameText);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUtil.closeKeyBord(LoginActivity.this, mLoginNameText);
                finish();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLoginNameText.isFocusable())
                    MyUtil.closeKeyBord(LoginActivity.this, mLoginNameText);
                if (mPasswordText.isFocusable())
                    MyUtil.closeKeyBord(LoginActivity.this, mPasswordText);
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), RegisterActivity.CODE_REGISTER);
            }
        });

        mPasswordText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    MyUtil.closeKeyBord(LoginActivity.this, mPasswordText);
                    login();
                    return true;
                }
                return false;
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.CODE_REGISTER)
            if (resultCode == RegisterActivity.CODE_REGISTER) {
                showSnackBar(getString(R.string.register_success));
                mLoginNameText.setText(data.getStringExtra("loginName"));
                mPasswordText.setText(data.getStringExtra("password"));
            }
    }

    private void login() {
        String loginName = mLoginNameText.getText().toString();
        String password = mPasswordText.getText().toString();
        if (!MyUtil.isLoginName(loginName)) {
            mLoginNameInputLayout.setErrorEnabled(true);
            mLoginNameInputLayout.setError(getString(R.string.login_name_length_error));
        } else if (!MyUtil.isPassword(password)) {
            mLoginNameInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setErrorEnabled(true);
            mPasswordInputLayout.setError(getString(R.string.password_length_error));
        } else {
            mLoginNameInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setErrorEnabled(false);

            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setMessage(getString(R.string.is_login));
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            mPresenter.login(loginName, password);
        }
        if (mLoginNameText.isFocusable())
            MyUtil.closeKeyBord(this, mLoginNameText);
        if (mPasswordText.isFocusable())
            MyUtil.closeKeyBord(this, mPasswordText);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void loginEnd() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void loginSuccess() {
        EventBus.getDefault().post(new LoginEvent());
        setResult(CODE_LOGIN);
        finish();
        cn.jiguang.analytics.android.api.LoginEvent loginEvent = new cn.jiguang.analytics.android.api.LoginEvent("native", true);
        JAnalyticsInterface.onEvent(this, loginEvent);
    }

    @Override
    public void loginError(String message) {
        showSnackBar(message);
        cn.jiguang.analytics.android.api.LoginEvent loginEvent = new cn.jiguang.analytics.android.api.LoginEvent("native", false);
        JAnalyticsInterface.onEvent(this, loginEvent);
    }
}
