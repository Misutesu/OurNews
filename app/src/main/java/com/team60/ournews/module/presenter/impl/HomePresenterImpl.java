package com.team60.ournews.module.presenter.impl;

import android.util.SparseArray;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.HomeNewResult;
import com.team60.ournews.module.presenter.HomePresenter;
import com.team60.ournews.module.view.HomeView;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
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
        Observable<HomeNewResult> observable;
        if (type == -1) {
            observable = RetrofitUtil.newInstance().getHomeNews();
        } else {
            observable = RetrofitUtil.newInstance().getHomeNewsUseType(type);
        }
        mView.addSubscription(observable
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeNewResult>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.getNewsError(MyApplication.getContext().getString(R.string.server_error));
                    }

                    @Override
                    public void onNext(HomeNewResult result) {
                        if (result.getResult().equals("success")) {
                            SparseArray<List<New>> news = new SparseArray<>();
                            List<HomeNewResult.DataBean> beanList = result.getData();
                            for (int i = 0; i < beanList.size(); i++) {
                                List<HomeNewResult.DataBean.ListBean> list = beanList.get(i).getList();
                                List<New> newList = new ArrayList<>();
                                for (int j = 0; j < list.size(); j++) {
                                    HomeNewResult.DataBean.ListBean bean = list.get(j);
                                    New n = new New();
                                    n.setId(bean.getId());
                                    n.setTitle(bean.getTitle());
                                    n.setCover(bean.getCover());
                                    n.setAbstractContent(bean.getAbstractContent());
                                    n.setCreateTime(bean.getCreateTime());
                                    newList.add(n);
                                }
                                news.append(Integer.valueOf(beanList.get(i).getType()), newList);
                            }
                            mView.getNewsSuccess(news, type);
                        } else {
                            mView.getNewsError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
