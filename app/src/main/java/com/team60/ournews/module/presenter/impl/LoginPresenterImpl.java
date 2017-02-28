package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.User;
import com.team60.ournews.module.presenter.LoginPresenter;
import com.team60.ournews.module.view.LoginView;

import org.json.JSONException;

import java.io.IOException;

import rx.Observable;
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
        mView.addSubscription(Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    int result = User.getUserInfo(RetrofitUtil.newInstance().login(loginName, password).execute().body().string());
                    if (result == 0) {
                        subscriber.onNext(0);
                    } else if (result == 1) {
                        subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.no_login_name_or_password_error)));
                    } else if (result == 2) {
                        subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.internet_error)));
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        mView.loginEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.loginError(e.getMessage());
                        onCompleted();
                    }

                    @Override
                    public void onNext(Integer errorCode) {
                        mView.loginSuccess();
                    }
                }));
    }
}
