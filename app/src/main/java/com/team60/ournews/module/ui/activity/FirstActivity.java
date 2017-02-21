package com.team60.ournews.module.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.team60.ournews.R;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void init() {

    }

    @Override
    public void setListener() {

    }
}
