package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.common.Constants;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.module.model.CheckUpdateResult;
import com.team60.ournews.module.presenter.MainPresenter;
import com.team60.ournews.module.view.MainView;
import com.team60.ournews.util.MD5Util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public class MainPresenterImpl implements MainPresenter {

    private Context context;
    private MainView mView;

    public MainPresenterImpl(Context context, MainView mView) {
        this.context = context;
        this.mView = mView;
    }

    @Override
    public void checkUpdate() {
        long time = System.currentTimeMillis();
        String key = MD5Util.getMD5(Constants.KEY + time);
        mView.addSubscription(RetrofitUtil.newInstance().checkUpdate(time, key)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CheckUpdateResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onNext(CheckUpdateResult result) {
                        if (result.getResult().equals("success")) {
                            mView.hasNewVersion(result);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
