package com.team60.ournews.module.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WellComeActivity extends BaseActivity {

    @BindView(R.id.activity_well_come_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.activity_well_come_view_layout)
    LinearLayout mViewLayout;
    @BindView(R.id.activity_well_come_left_btn)
    ImageButton mLeftBtn;
    @BindView(R.id.activity_well_come_right_btn)
    ImageButton mRightBtn;
    @BindView(R.id.activity_well_come_layout)
    RelativeLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void showSnackBar(String message) {

    }
}
