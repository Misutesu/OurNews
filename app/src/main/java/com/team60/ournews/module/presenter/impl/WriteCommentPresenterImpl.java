package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.presenter.WriteCommentPresenter;
import com.team60.ournews.module.view.WriteCommentView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MD5Util;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class WriteCommentPresenterImpl implements WriteCommentPresenter {

    private Context mContext;
    private WriteCommentView mView;

    public WriteCommentPresenterImpl(Context context, WriteCommentView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void sendComment(final long uid, final long nid, final String content, String token) {
        long time = System.currentTimeMillis();
        mView.addSubscription(RetrofitUtil.newInstance()
                .sendCommentUseNewId(uid, nid, content, time, token, MD5Util.getMD5(Constants.KEY + token + time))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<NoDataResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.sendCommentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.sendCommentError(mContext.getString(R.string.internet_error));
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
