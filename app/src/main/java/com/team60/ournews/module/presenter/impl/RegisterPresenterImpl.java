package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.presenter.RegisterPresenter;
import com.team60.ournews.module.view.RegisterView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MD5Util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class RegisterPresenterImpl implements RegisterPresenter {

    private Context mContext;
    private RegisterView mView;

    public RegisterPresenterImpl(Context context, RegisterView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void register(final String loginName, final String password) {
        long time = System.currentTimeMillis();
        mView.addSubscription(RetrofitUtil.newInstance()
                .register(loginName, password, time, MD5Util.getMD5(Constants.KEY + time))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<NoDataResult>() {
                    @Override
                    public void onComplete() {
                        mView.registerEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.registerError(mContext.getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(NoDataResult result) {
                        if (result.getResult().equals("success")) {
                            mView.registerSuccess();
                        } else {
                            mView.registerError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
