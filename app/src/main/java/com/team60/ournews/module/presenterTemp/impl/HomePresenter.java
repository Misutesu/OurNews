package com.team60.ournews.module.presenterTemp.impl;

import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.presenterTemp.impl.base.BasePresenter;
import com.team60.ournews.module.view.HomeView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Administrator on 2017/2/27.
 */

public class HomePresenter extends BasePresenter<HomeView> {

    public HomePresenter(HomeView mView) {
        attachView(mView);
    }

    public void getHomeNews(int type) {

        addSubscription(RetrofitUtil.newInstance().getHomeNews()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<New>>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onNext(List<New> news) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        onComplete();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }
}
