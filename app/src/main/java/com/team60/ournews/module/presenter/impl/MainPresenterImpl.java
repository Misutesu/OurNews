package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.module.bean.User;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.CheckLoginResult;
import com.team60.ournews.module.presenter.MainPresenter;
import com.team60.ournews.module.view.MainView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2017/3/12 0012.
 */

public class MainPresenterImpl implements MainPresenter {

    private Context mContext;
    private MainView mView;

    public MainPresenterImpl(Context context, MainView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void checkLogin(long id, String token, String umengToken) {
        mView.addSubscription(RetrofitUtil.newInstance().checkLogin(id, token, umengToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CheckLoginResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.checkLoginError();
                    }

                    @Override
                    public void onNext(CheckLoginResult result) {
                        if (result.getResult().equals("success")) {
                            User user = User.newInstance();
                            if (user.getId() != result.getData().getId()) {
                                mView.checkLoginError();
                            } else {
                                user.setLoginName(result.getData().getLoginName());
                                user.setNickName(result.getData().getNickName());
                                user.setSex(result.getData().getSex());
                                user.setPhoto(result.getData().getPhoto());
                                user.setPushState(result.getData().getPushState());
                                mView.checkLoginSuccess();
                            }
                        } else {
                            mView.checkLoginError();
                        }
                    }
                }));
    }
}
