package com.team60.ournews.module.ui.activity;

import android.os.Bundle;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

public class LoginTempActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        hideNavigationBar();
        setContentView(R.layout.activity_login_temp);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void init(Bundle savedInstanceState) {
    }

    @Override
    public void setListener() {

    }
}
