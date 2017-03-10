package com.team60.ournews.module.ui.fragment.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.team60.ournews.module.bean.User;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public abstract class BaseFragment extends Fragment {

    public User user = User.newInstance();

    private CompositeSubscription mSubscription;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscription != null && mSubscription.hasSubscriptions())
            mSubscription.clear();
    }

    public void addSubscription(@NonNull Subscription subscription) {
        if (mSubscription == null)
            mSubscription = new CompositeSubscription();
        mSubscription.add(subscription);
    }

    public abstract void init();

    public abstract void setListener();
}
