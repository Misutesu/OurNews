package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.LoginResult;
import com.team60.ournews.module.presenter.LoginPresenter;
import com.team60.ournews.module.view.LoginView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MD5Util;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mView;

    public LoginPresenterImpl(LoginView loginView) {
        mView = loginView;
    }

    @Override
    public void login(final String loginName, final String password) {
        long time = System.currentTimeMillis();
        mView.addSubscription(RetrofitUtil.newInstance()
                .login(loginName, MD5Util.getMD5(Constants.KEY + password + time), time)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        mView.loginEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.loginError(MyApplication.getContext().getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(LoginResult result) {
                        if (result.getResult().equals("success")) {
                            User user = User.newInstance();
                            user.setId(result.getData().getId());
                            user.setLoginName(result.getData().getLoginName());
                            user.setNickName(result.getData().getNickName());
                            user.setSex(result.getData().getSex());
                            user.setPhoto(result.getData().getPhoto());
                            user.setToken(result.getData().getToken());
                            mView.loginSuccess();
                        } else {
                            mView.loginError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
