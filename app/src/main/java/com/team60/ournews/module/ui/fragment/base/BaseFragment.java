package com.team60.ournews.module.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private boolean isOpenStatistics = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isOpenStatistics) {
            if (isVisibleToUser) {
                JAnalyticsInterface.onPageStart(getContext(), this.getClass().getCanonicalName());
            } else {
                JAnalyticsInterface.onPageEnd(getContext(), this.getClass().getCanonicalName());
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (mSubscription != null)
            mSubscription.clear();
        super.onDestroyView();
    }

    public void addSubscription(@NonNull Disposable disposable) {
        if (mSubscription == null)
            mSubscription = new CompositeDisposable();
        mSubscription.add(disposable);
    }

    public void setStatistics(boolean isOpen) {
        isOpenStatistics = isOpen;
    }

    public abstract void init();

    public abstract void setListener();
}
