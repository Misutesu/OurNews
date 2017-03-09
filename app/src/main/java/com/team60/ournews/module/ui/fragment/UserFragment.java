package com.team60.ournews.module.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.fragment.base.BaseFragment;

import butterknife.ButterKnife;

public class UserFragment extends BaseFragment {

    private boolean isCreated;
    private boolean isUIVisible;

    public UserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreated = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isUIVisible = true;
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void setListener() {

    }

    private void
}
