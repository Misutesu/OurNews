package com.team60.ournews.listener;

import android.animation.Animator;

/**
 * Created by wujiaquan on 2017/3/14.
 */

public abstract class MyObjectAnimatorListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public abstract void onAnimationEnd(Animator animation);

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
