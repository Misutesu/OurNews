package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.ListNewResult;
import com.team60.ournews.module.presenter.TypePresenter;
import com.team60.ournews.module.view.TypeView;
import com.team60.ournews.util.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */

public class TypePresenterImpl implements TypePresenter {

    private Context mContext;
    private TypeView mView;

    public TypePresenterImpl(Context context, TypeView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getNewList(final int type, final int page, final int sort) {
        mView.addSubscription(RetrofitUtil.newInstance().getNewListUseType(
                type, page, Constants.NEW_EVERY_PAGE_SIZE, sort
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ListNewResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.getNewListEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.getNewListError(mContext.getString(R.string.internet_error), page);
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
                            mView.getNewListSuccess(news, page);
                        } else {
                            mView.getNewListError(ErrorUtil.getErrorMessage(result.getErrorCode()), page);
                        }
                    }
                }));
    }
}
