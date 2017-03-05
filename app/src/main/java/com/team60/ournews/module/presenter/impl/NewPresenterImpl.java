package com.team60.ournews.module.presenter.impl;

import com.team60.ournews.MyApplication;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.connection.RetrofitUtil;
import com.team60.ournews.module.model.ContentResult;
import com.team60.ournews.module.presenter.NewPresenter;
import com.team60.ournews.module.view.NewView;
import com.team60.ournews.util.ErrorUtil;

import org.json.JSONException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public class NewPresenterImpl implements NewPresenter {

    private NewView mView;

    public NewPresenterImpl(NewView mView) {
        this.mView = mView;
    }

    @Override
    public void getNewContent(final long id, final long uid) {
        Observable<ContentResult> observable;
        if (uid == -1) {
            observable = RetrofitUtil.newInstance().getNewContentUseId(id);
        } else {
            observable = RetrofitUtil.newInstance().getNewContentUseId(id, uid);
        }
        mView.addSubscription(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContentResult>() {
                    @Override
                    public void onCompleted() {
                        mView.getNewContentEnd();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onCompleted();
                        mView.getNewContentError(MyApplication.getContext().getString(R.string.internet_error));
                    }

                    @Override
                    public void onNext(ContentResult result) {
                        if (result.getResult().equals("success")) {
                            New n = new New();
                            n.setContent(result.getData().getContent());
                            n.setCommentNum(result.getData().getCommentNum());
                            n.setHistoryNum(result.getData().getHistoryNum());
                            n.setCollectionNum(result.getData().getCollectionNum());
                            try {
                                mView.getNewContentSuccess(n);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mView.getNewContentError(MyApplication.getContext().getString(R.string.load_new_image_error));
                            }
                        } else {
                            mView.getNewContentError(ErrorUtil.getErrorMessage(result.getErrorCode()));
                        }
                    }
                }));
    }
}
