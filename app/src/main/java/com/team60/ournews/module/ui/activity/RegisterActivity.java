package com.team60.ournews.module.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;

import com.team60.ournews.R;
import com.team60.ournews.module.presenter.RegisterPresenter;
import com.team60.ournews.module.presenter.impl.RegisterPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.RegisterView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jiguang.analytics.android.api.RegisterEvent;

public class RegisterActivity extends BaseActivity implements RegisterView {

    public static final int CODE_REGISTER = 102;

    private RegisterPresenter mPresenter;

    @BindView(R.id.activity_register_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_register_login_name_text)
    AppCompatEditText mLoginNameText;
    @BindView(R.id.activity_register_password_text)
    AppCompatEditText mPasswordText;
    @BindView(R.id.activity_register_password_text_2)
    AppCompatEditText mPasswordText2;
    @BindView(R.id.activity_register_btn)
    AppCompatButton mRegisterBtn;
    @BindView(R.id.activity_login_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_register_login_name_input_layout)
    TextInputLayout mLoginNameInputLayout;
    @BindView(R.id.activity_register_password_input_layout)
    TextInputLayout mPasswordInputLayout;
    @BindView(R.id.activity_register_password_2_input_layout)
    TextInputLayout mPassword2InputLayout;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new RegisterPresenterImpl(this, this);

        mToolBar.setTitle(getString(R.string.register));
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MyUtil.openKeyBord(this, mLoginNameText);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.closeKeyBord(RegisterActivity.this, mLoginNameText);
                finish();
            }
        });

        mPasswordText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    MyUtil.closeKeyBord(RegisterActivity.this, mPasswordText);
                    register();
                    return true;
                }
                return false;
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {
        String loginName = mLoginNameText.getText().toString();
        String password = mPasswordText.getText().toString();
        if (!MyUtil.isLoginName(loginName)) {
            mLoginNameInputLayout.setErrorEnabled(true);
            mLoginNameInputLayout.setError(getString(R.string.login_name_length_error));
        } else if (!MyUtil.isLoginName(password)) {
            mLoginNameInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setErrorEnabled(true);
            mPasswordInputLayout.setError(getString(R.string.password_length_error));
        } else if (!password.equals(mPasswordText2.getText().toString())) {
            mLoginNameInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setErrorEnabled(false);
            mPassword2InputLayout.setErrorEnabled(true);
            mPassword2InputLayout.setError(getString(R.string.two_password_no_same));
        } else {
            mLoginNameInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setErrorEnabled(false);
            mPassword2InputLayout.setErrorEnabled(false);

            if (mProgressDialog == null) {
                if (ThemeUtil.newInstance().isNightMode()) {
                    mProgressDialog = new ProgressDialog(RegisterActivity.this, R.style.NightDialogTheme);
                } else {
                    mProgressDialog = new ProgressDialog(RegisterActivity.this);
                }
                mProgressDialog.setMessage(getString(R.string.is_register));
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
            mPresenter.register(loginName, password);
        }
        if (mLoginNameText.isFocusable())
            MyUtil.closeKeyBord(this, mLoginNameText);
        if (mPasswordText.isFocusable())
            MyUtil.closeKeyBord(this, mPasswordText);
        if (mPasswordText2.isFocusable())
            MyUtil.closeKeyBord(this, mPasswordText);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void registerEnd() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void registerSuccess() {
        RegisterEvent registerEvent = new RegisterEvent("native", true);
        JAnalyticsInterface.onEvent(this, registerEvent);
        Intent intent = new Intent();
        intent.putExtra("loginName", mLoginNameText.getText().toString());
        intent.putExtra("password", mPasswordText.getText().toString());
        setResult(CODE_REGISTER, intent);
        finish();
    }

    @Override
    public void registerError(String message) {
        showSnackBar(message);
        RegisterEvent registerEvent = new RegisterEvent("native", false);
        JAnalyticsInterface.onEvent(this, registerEvent);
    }
}
