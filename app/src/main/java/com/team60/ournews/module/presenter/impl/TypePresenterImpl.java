package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenter.TypePresenter;
import com.team60.ournews.module.view.TypeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */

public class TypePresenterImpl implements TypePresenter {

    private TypeView mView;

    public TypePresenterImpl(TypeView view) {
        mView = view;
    }

    @Override
    public void getNewList(final int type, final int page, final int sort) {
        mView.addSubscription(Observable.create(new Observable.OnSubscribe<List<New>>() {
            @Override
            public void call(Subscriber<? super List<New>> subscriber) {
                try {
                    JSONObject jsonObject = new JSONObject(RetrofitUtil.newInstance().getNewListUseType(type, page, Constants.New_EVERY_PAGE_SIZE, sort).execute().body().string());
                    if (jsonObject.getString("result").equals("success")) {
                        subscriber.onNext(New.getNewList(jsonObject));
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
                .subscribe(new Subscriber<List<New>>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewListEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getNewListError(e.getMessage(), page);
                        onCompleted();
                    }

                    @Override
                    public void onNext(List<New> news) {
                        mView.getNewListSuccess(news, page);
                    }
                }));
    }
}
