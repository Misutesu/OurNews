package com.team60.ournews.module.presenter.impl;

import android.content.Context;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.ContentResult;
import com.team60.ournews.module.model.NoDataResult;
import com.team60.ournews.module.presenter.NewPresenter;
import com.team60.ournews.module.view.NewView;
import com.team60.ournews.util.ErrorUtil;

import org.json.JSONException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class NewPresenterImpl implements NewPresenter {

    private Context mContext;
    private NewView mView;

    public NewPresenterImpl(Context context, NewView view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void getNewContent(final long id, final long uid) {
        Flowable<ContentResult> flowable;
        if (uid == -1) {
            flowable = RetrofitUtil.newInstance().getNewContentUseId(id);
        } else {
            flowable = RetrofitUtil.newInstance().getNewContentUseId(id, uid);
        }
        mView.addSubscription(flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ContentResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.getNewContentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.getNewContentError(mContext.getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(ContentResult result) {
                        if (result.getResult().equals("success")) {
                            New n = new New();
                            n.setContent(result.getData().getContent());
                            n.setIsCollection(result.getData().getIsCollected());
                            n.setCommentNum(result.getData().getCommentNum());
                            n.setHistoryNum(result.getData().getHistoryNum());
                            n.setCollectionNum(result.getData().getCollectionNum());
                            try {
                                mView.getNewContentSuccess(n);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mView.getNewContentError(mContext.getString(R.string.load_new_image_error));
                            }
                        } else {
                            mView.getNewContentError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }

    @Override
    public void collectNew(long nid, long uid, String token, int type) {
        mView.addSubscription(RetrofitUtil.newInstance().collectNew(nid, uid, token, type)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<NoDataResult>() {
                    @Override
                    protected void onStart() {
                        request(1);
                    }

                    @Override
                    public void onComplete() {
                        mView.collectNewEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onComplete();
                        mView.collectNewError(mContext.getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(NoDataResult result) {
                        if (result.getResult().equals("success")) {
                            mView.collectNewSuccess();
                        } else {
                            mView.collectNewError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
