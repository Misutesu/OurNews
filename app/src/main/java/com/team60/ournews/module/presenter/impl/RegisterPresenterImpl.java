package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.presenter.RegisterPresenter;
import com.team60.ournews.module.view.RegisterView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private RegisterView mView;

    public RegisterPresenterImpl(RegisterView mView) {
        this.mView = mView;
    }

    @Override
    public void register(final String loginName, final String password) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().register(loginName, password).execute().body().string());
                    if (jsonObject.getString("result").equals("success")) {
                        subscriber.onNext(0);
                    } else {
                        int errorCode = jsonObject.getInt("error_code");
                        if (errorCode == Constants.LOGINNAME_OR_PASSWORD_ERROR) {
                            subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.login_name_or_password_error)));
                        } else if (errorCode == Constants.LOGINNAME_EXISTS) {
                            subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.login_name_is_same)));
                        }
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
                        mView.registerEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.registerError(e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mView.registerSuccess();
                    }
                });
    }
}
