package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.presenter.WriteCommentPresenter;
import com.team60.ournews.module.view.WriteCommentView;
import com.team60.ournews.util.DateUtil;
import com.team60.ournews.util.MyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import rx.Observable;
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
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                try {
                    JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().sendCommentUseNewId(uid, nid, content, DateUtil.getNowTime()).execute().body().string());
                    if (jsonObject.getString("result").equals("success")) {
                        subscriber.onNext(null);
                    } else {
                        if (jsonObject.getInt("error_code") == Constants.USER_OR_NEW_NO_HAVE) {
                            subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.id_error)));
                        } else {
                            subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
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
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        mView.sendCommentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.sendCommentError(e.getMessage());
                        onCompleted();
                    }

                    @Override
                    public void onNext(Object o) {
                        mView.sendCommentSuccess();
                    }
                });
    }
}
