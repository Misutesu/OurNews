package com.team60.ournews.module.presenter.impl;

import android.util.Log;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenter.SearchResultPresenter;
import com.team60.ournews.module.view.SearchResultView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */

public class SearchResultPresenterImpl implements SearchResultPresenter {

    private SearchResultView mView;

    public SearchResultPresenterImpl(SearchResultView view) {
        mView = view;
    }

    @Override
    public void searchNews(final String name, final int page, final int sort) {
        mView.addSubscription(Observable.create(new Observable.OnSubscribe<List<New>>() {
            @Override
            public void call(Subscriber<? super List<New>> subscriber) {
                try {
                    JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().searchNew(name, page, Constants.New_EVERY_PAGE_SIZE, sort).execute().body().string());
                    Log.d("TAG", jsonObject.toString());
                    if (jsonObject.getString("result").equals("success")) {
                        subscriber.onNext(New.getNewList(jsonObject));
                    } else {
                        subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.internet_error)));
                } catch (JSONException e) {
                    e.printStackTrace();
                    subscriber.onError(new Exception(MyApplication.getContext().getString(R.string.server_error)));
                } finally {
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<New>>() {
                    @Override
                    public void onCompleted() {
                        mView.searchEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.searchError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<New> news) {
                        mView.searchSuccess(news, page);
                    }
                }));
    }
}
