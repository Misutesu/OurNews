package com.team60.ournews.module.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.listener.MyObjectAnimatorListener;
import com.team60.ournews.module.ui.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstActivity extends BaseActivity {

    @BindView(R.id.activity_first_text)
    TextView mText;

    private AnimatorSet mAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        hideNavigationBar();
        init(savedInstanceState);
        setListener();
        mAnimatorSet.start();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(ObjectAnimator.ofFloat(mText, "alpha", 1.0f, 1.0f).setDuration(500))
                .after(ObjectAnimator.ofFloat(mText, "alpha", 0.0f, 1.0f).setDuration(1000));
    }

    @Override
    public void setListener() {
        mAnimatorSet.addListener(new MyObjectAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startMain();
            }
        });
    }

    @Override
    public void showSnackBar(String message) {

    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
