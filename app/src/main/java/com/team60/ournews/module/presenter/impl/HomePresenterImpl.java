package com.team60.ournews.module.presenter.impl;

import android.util.SparseArray;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenter.HomePresenter;
import com.team60.ournews.module.view.HomeView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class HomePresenterImpl implements HomePresenter {

    private HomeView mView;

    public HomePresenterImpl(HomeView mView) {
        this.mView = mView;
    }

    @Override
    public void getHomeNews(final int type) {
        mView.addSubscription(Observable.create(new Observable.OnSubscribe<SparseArray<List<New>>>() {
            @Override
            public void call(Subscriber<? super SparseArray<List<New>>> subscriber) {
                try {
                    SparseArray<List<New>> news;
                    if (type == -1) {
                        news = New.getHomeNews(RetrofitUtil.newInstance().getHomeNews()
                                .execute().body().string());
                    } else {
                        news = New.getHomeNewsUseType(RetrofitUtil.newInstance().getHomeNewsUseType(type)
                                .execute().body().string(), type);
                    }
                    if (news == null) {
                        subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                    } else {
                        subscriber.onNext(news);
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
                .subscribe(new Subscriber<SparseArray<List<New>>>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.getNewsError(e.getMessage());
                    }

                    @Override
                    public void onNext(SparseArray<List<New>> news) {
                        mView.getNewsSuccess(news, type);
                    }
                }));
    }
}
