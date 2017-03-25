package com.team60.ournews.module.ui.fragment.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.team60.ournews.module.bean.User;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public abstract class BaseFragment extends Fragment {

    public User user = User.newInstance();

    private CompositeDisposable mSubscription;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            JAnalyticsInterface.onPageStart(getContext(), this.getClass().getCanonicalName());
        } else {
            JAnalyticsInterface.onPageEnd(getContext(), this.getClass().getCanonicalName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null)
            mSubscription.clear();
    }

    public void addSubscription(@NonNull Disposable disposable) {
        if (mSubscription == null)
            mSubscription = new CompositeDisposable();
        mSubscription.add(disposable);
    }

    public abstract void init();

    public abstract void setListener();
}
