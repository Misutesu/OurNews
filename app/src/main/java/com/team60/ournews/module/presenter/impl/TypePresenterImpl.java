package com.team60.ournews.module.presenter.impl;

import android.util.Log;

import com.team60.ournews.MyApplication;
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
        mView.addSubscription(RetrofitUtil.newInstance().getNewListUseType(
                type, page, Constants.New_EVERY_PAGE_SIZE, sort
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ListNewResult>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewListEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.getNewListError(MyApplication.getContext().getString(R.string.internet_error), page);
                    }

                    @Override
                    public void onNext(ListNewResult result) {
                        if (result.getResult().equals("success")) {
                            Log.d("TAG", result.toString());
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
                            mView.getNewListSuccess(news, page);
                        } else {
                            mView.getNewListError(ErrorUtil.getErrorMessage(result.getErrorCode()), page);
                        }
                    }
                }));
    }
}
