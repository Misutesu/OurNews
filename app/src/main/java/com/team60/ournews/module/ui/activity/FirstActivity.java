package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.team60.ournews.module.ui.activity.base.BaseActivity;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        startMain();
        finish();
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void setListener() {

    }

    @Override
    public void showSnackBar(String message) {

    }
}
