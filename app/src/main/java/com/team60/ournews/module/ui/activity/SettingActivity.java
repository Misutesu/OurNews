package com.team60.ournews.module.ui.activity;

import android.os.Bundle;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void showSnackBar(String message) {

    }
}
