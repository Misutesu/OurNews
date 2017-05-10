package com.team60.ournews.module.ui.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.fragment.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WellComeFragment extends BaseFragment {

    @BindView(R.id.fragment_well_come_img)
    ImageView mImg;
    @BindView(R.id.fragment_well_come_text)
    AppCompatTextView mText;

    Unbinder unbinder;

    private int imgRes;
    private String text;

    public static WellComeFragment instance(@DrawableRes int imgRes, @NonNull String text) {
        WellComeFragment fragment = new WellComeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("imgRes", imgRes);
        bundle.putString("text", text);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imgRes = bundle.getInt("imgRes");
            text = bundle.getString("text");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_well_come, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void init() {
        mImg.setImageResource(imgRes);
        mText.setText(text);
    }

    @Override
    public void setListener() {

    }
}
