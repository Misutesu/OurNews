package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.presenter.SearchResultPresenter;
import com.team60.ournews.module.view.SearchResultView;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

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
        mView.addSubscription(RetrofitUtil.newInstance().searchNew(
                name, page, Constants.New_EVERY_PAGE_SIZE, sort
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListNewResult>() {
                    @Override
                    public void onCompleted() {
                        mView.searchEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.searchError(MyApplication.getContext().getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(ListNewResult result) {
                        if (result.getResult().equals("success")) {
                            List<New> news = new ArrayList<>();
                            for (int i = 0; i < result.getData().size(); i++) {
                                ListNewResult.DataBean bean = result.getData().get(i);
                                New n = new New();
                                n.setId(bean.getId());
                                n.setTitle(bean.getTitle());
                                n.setCover(bean.getCover());
                                n.setAbstractContent(bean.getAbstact());
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
