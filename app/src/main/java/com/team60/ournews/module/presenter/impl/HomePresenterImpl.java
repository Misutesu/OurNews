package com.team60.ournews.module.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.module.bean.ManagerUser;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.util.RetrofitUtil;
import com.team60.ournews.module.model.HomeNewResult;
import com.team60.ournews.module.presenter.HomePresenter;
import com.team60.ournews.module.view.HomeView;
import com.team60.ournews.util.ErrorUtil;
import com.team60.ournews.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class HomePresenterImpl implements HomePresenter {

    private Context mContext;
    private HomeView mView;

    public HomePresenterImpl(Context context, HomeView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getHomeNews(final int type) {
        Flowable<HomeNewResult> flowable;
        if (type == -1) {
            flowable = RetrofitUtil.newInstance().getHomeNews();
        } else {
            flowable = RetrofitUtil.newInstance().getHomeNewsUseType(type);
        }
        mView.addSubscription(flowable
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<HomeNewResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.getNewsEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.getNewsError(mContext.getString(R.string.internet_error));
                        HomeNewResult result = getHomeNewsFromData();
                        if (result != null) {
                            onNext(getHomeNewsFromData());
                        }
                    }

                    @Override
                    public void onNext(HomeNewResult result) {
                        if (result.getResult().equals("success")) {
                            SparseArray<List<New>> news = new SparseArray<>();
                            List<HomeNewResult.DataBean.NewsBean> beanList = result.getData().getNews();
                            for (int i = 0; i < beanList.size(); i++) {
                                List<HomeNewResult.DataBean.NewsBean.ListBean> list = beanList.get(i).getList();
                                List<New> newList = new ArrayList<>();
                                for (int j = 0; j < list.size(); j++) {
                                    HomeNewResult.DataBean.NewsBean.ListBean bean = list.get(j);
                                    New n = new New();
                                    n.setId(bean.getId());
                                    n.setTitle(bean.getTitle());
                                    n.setCover(bean.getCover());
                                    n.setAbstractContent(bean.getAbstractContent());
                                    n.setCreateTime(bean.getCreateTime());
                                    n.setType(bean.getType());
                                    n.setCommentNum(bean.getCommentNum());
                                    n.setCollectionNum(bean.getCollectionNum());
                                    n.setHistoryNum(bean.getHistoryNum());
                                    ManagerUser managerUser = new ManagerUser();
                                    managerUser.setId(bean.getManager().getId());
                                    managerUser.setNickName(bean.getManager().getNickName());
                                    managerUser.setSex(bean.getManager().getSex());
                                    managerUser.setSign(bean.getManager().getSign());
                                    managerUser.setBirthday(bean.getManager().getBirthday());
                                    managerUser.setPhoto(bean.getManager().getPhoto());
                                    n.setManagerUser(managerUser);
                                    newList.add(n);
                                }
                                news.append(beanList.get(i).getType(), newList);
                            }
                            mView.getNewsSuccess(news, type);
                            saveHomeNewsToData(result);
                        } else {
                            mView.getNewsError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }

    private HomeNewResult getHomeNewsFromData() {
        SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(mContext, Constants.SHARED_PREFERENCES_CACHE);
        String homeTemp = sharedPreferences.getString("home_temp", null);
        if (homeTemp == null)
            return null;
        return new Gson().fromJson(homeTemp, HomeNewResult.class);
    }

    private void saveHomeNewsToData(HomeNewResult result) {
        SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(mContext, Constants.SHARED_PREFERENCES_CACHE);
        sharedPreferences.edit().putString("home_temp", new Gson().toJson(result)).apply();
    }
}
