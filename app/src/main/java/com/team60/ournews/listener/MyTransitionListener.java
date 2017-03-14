package com.team60.ournews.listener;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.transition.Transition;

/**
 * Created by wujiaquan on 2017/3/14.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public abstract class MyTransitionListener implements android.transition.Transition.TransitionListener {
    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }
}
