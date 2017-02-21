package com.team60.ournews.module.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team60.ournews.R;

public class TempFragment extends Fragment {

    private ViewPager mViewPager;

    public TempFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_temp, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        return view;
    }
}
