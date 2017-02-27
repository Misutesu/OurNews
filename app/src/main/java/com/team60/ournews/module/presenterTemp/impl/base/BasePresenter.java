package com.team60.ournews.module.presenterTemp.impl.base;

import com.team60.ournews.module.presenterTemp.Presenter;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Misutesu on 2017/2/27.
 */

public class BasePresenter<V> implements Presenter<V> {

    public V mView;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        mView = null;
        onUnSubscribe();
    }

    public void addSubscription(Disposable disposable) {
        if (disposable == null) return;
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    private void onUnSubscribe() {
        if (mCompositeDisposable != null && mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}
