package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.presenter.SearchResultPresenter;
import com.team60.ournews.module.view.SearchResultView;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */

public class SearchResultPresenterImpl implements SearchResultPresenter {

    private Context mContext;
    private SearchResultView mView;

    public SearchResultPresenterImpl(Context context, SearchResultView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void searchNews(final String name, final int page, final int sort) {
        mView.addSubscription(RetrofitUtil.newInstance().searchNew(
                name, page, Constants.NEW_EVERY_PAGE_SIZE, sort
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ListNewResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.searchEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.searchError(mContext.getString(R.string.internet_error));
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
                            mView.searchSuccess(news, page);
                        } else {
                            mView.searchError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
