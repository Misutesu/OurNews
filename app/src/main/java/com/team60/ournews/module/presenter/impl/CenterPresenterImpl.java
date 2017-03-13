package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.presenter.CenterPresenter;
import com.team60.ournews.module.view.CenterView;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by wujiaquan on 2017/3/10.
 */

public class CenterPresenterImpl implements CenterPresenter {

    private Context mContext;
    private CenterView mView;

    public CenterPresenterImpl(Context context, CenterView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getNewList(long id, String token, long uid, int type, final int page, int sort) {
        Flowable<ListNewResult> flowable;
        if (type == 0) {
            flowable = RetrofitUtil.newInstance().getCollections(id, token, uid, page, Constants.NEW_EVERY_PAGE_SIZE, sort);
        } else {
            flowable = RetrofitUtil.newInstance().getHistory(id, token, uid, page, Constants.NEW_EVERY_PAGE_SIZE, sort);
        }
        mView.addSubscription(flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ListNewResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.onGetNewsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.onGetNewsError(MyApplication.getContext().getString(R.string.internet_error), page);
                    }

                    @Override
                    public void onNext(ListNewResult result) {
                        if (result.getResult().equals("success")) {
                            List<New> news = new ArrayList<>();
                            for (int i = 0; i < result.getData().getNews().size(); i++) {
                                ListNewResult.DataBean.NewsBean bean = result.getData().getNews().get(i);
                                New n = new New();
                                n.setId(bean.getId());
                                n.setTitle(bean.getTitle());
                                n.setCover(bean.getCover());
                                n.setAbstractContent(bean.getAbstractContent());
                                n.setCreateTime(bean.getCreateTime());
                                n.setType(bean.getType());
                                news.add(n);
                            }
                            mView.onGetNewsSuccess(news, page);
                        } else {
                            mView.onGetNewsError(ErrorUtil.getErrorMessage(result.getErrorCode()), page);
                        }
                    }
                }));
    }
}
