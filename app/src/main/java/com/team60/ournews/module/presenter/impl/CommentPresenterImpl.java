package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.Comment;
import com.team60.ournews.module.presenter.CommentPresenter;
import com.team60.ournews.module.view.CommentVIew;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class CommentPresenterImpl implements CommentPresenter {

    private CommentVIew mView;

    public CommentPresenterImpl(CommentVIew mView) {
        this.mView = mView;
    }

    @Override
    public void getComments(final long nid, final int page, final int sort) {
        mView.addSubscription(Observable.create(new Observable.OnSubscribe<List<Comment>>() {
            @Override
            public void call(Subscriber<? super List<Comment>> subscriber) {
                try {
                    JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().getCommentsUseId(nid, page, Constants.COMMENT_EVERY_PAGE_SIZE, sort).execute().body().string());
                    if (jsonObject.getString("result").equals("success")) {
                        subscriber.onNext(Comment.getComments(nid, jsonObject));
                    } else {
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
                .subscribe(new Subscriber<List<Comment>>() {
                    @Override
                    public void onCompleted() {
                        mView.getCommentsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getCommentsError(e.getMessage(), page);
                        onCompleted();
                    }

                    @Override
                    public void onNext(List<Comment> comments) {
                        mView.getCommentsSuccess(comments, page);
                    }
                }));
    }
}
