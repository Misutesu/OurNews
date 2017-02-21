package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenter.NewPresenter;
import com.team60.ournews.module.view.NewView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class NewPresenterImpl implements NewPresenter {

    private NewView mView;

    public NewPresenterImpl(NewView mView) {
        this.mView = mView;
    }

    @Override
    public void getNewContent(final long id, final long uid) {
        Observable.create(new Observable.OnSubscribe<New>() {
            @Override
            public void call(Subscriber<? super New> subscriber) {
                try {
                    JSONObject jsonObject;
                    if (uid == -1) {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance().getNewContentUseId(id).execute().body().string());
                    } else {
                        jsonObject = new JSONObject(RetrofitUtil.newInstance().getNewContentUseId(id, uid).execute().body().string());
                    }
                    if (jsonObject.getString("result").equals("success")) {
                        New n = new New();
                        n.setContent(jsonObject.getJSONObject("new").getString("content"));
                        n.setCommentNum(jsonObject.getJSONObject("new").getInt("comment_num"));
                        subscriber.onNext(n);
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
                .subscribe(new Subscriber<New>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewContentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                    }

                    @Override
                    public void onNext(New n) {
                        try {
                            mView.getNewContentSuccess(n);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            onError(new Exception(MyApplication.getContext().getString(R.string.load_new_image_error)));
                        }
                    }
                });
    }
}
