package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.module.model.LoginResult;
import com.team60.ournews.module.presenter.LoginPresenter;
import com.team60.ournews.module.view.LoginView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MD5Util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class LoginPresenterImpl implements LoginPresenter {

    private Context mContext;
    private LoginView mView;

    public LoginPresenterImpl(Context context, LoginView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void login(String loginName, String password) {
        long time = System.currentTimeMillis();
        mView.addSubscription(RetrofitUtil.newInstance()
                .login(loginName, MD5Util.getMD5(Constants.KEY + password + time), time)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<LoginResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.loginEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.loginError(mContext.getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(LoginResult result) {
                        if (result.getResult().equals("success")) {
                            User user = User.newInstance();
                            user.setId(result.getData().getId());
                            user.setLoginName(result.getData().getLoginName());
                            user.setNickName(result.getData().getNickName());
                            user.setSex(result.getData().getSex());
                            user.setSign(result.getData().getSign());
                            user.setBirthday(result.getData().getBirthday());
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
