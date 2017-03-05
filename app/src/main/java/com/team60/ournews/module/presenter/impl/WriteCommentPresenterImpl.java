package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.presenter.WriteCommentPresenter;
import com.team60.ournews.module.view.WriteCommentView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MD5Util;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class WriteCommentPresenterImpl implements WriteCommentPresenter {

    private WriteCommentView mView;

    public WriteCommentPresenterImpl(WriteCommentView mView) {
        this.mView = mView;
    }

    @Override
    public void sendComment(final long uid, final long nid, final String content) {
        long time = System.currentTimeMillis();
        mView.addSubscription(RetrofitUtil.newInstance()
                .sendCommentUseNewId(uid, nid, content, time, MD5Util.getMD5(Constants.KEY + time))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataResult>() {
                    @Override
                    public void onCompleted() {
                        mView.sendCommentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.sendCommentError(MyApplication.getContext().getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(NoDataResult result) {
                        if (result.getResult().equals("success")) {
                            mView.sendCommentSuccess();
                        } else {
                            mView.sendCommentError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
