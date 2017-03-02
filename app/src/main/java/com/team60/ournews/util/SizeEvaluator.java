package com.team60.ournews.util;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * Created by wujiaquan on 2017/3/2.
 */

public class SizeEvaluator implements TypeEvaluator<Integer[]> {
    @Override
    public Integer[] evaluate(float fraction, Integer[] startValue, Integer[] endValue) {

        Integer[] result = new Integer[2];

        int width;
        if (startValue[0] > endValue[0]) {
            width = (int) (fraction * (startValue[0] - endValue[0])) + endValue[0];
        } else {
            width = (int) (fraction * (endValue[0] - startValue[0])) + startValue[0];
        }
        result[0] = width;

        int height;
        if (startValue[1] > endValue[1]) {
            height = (int) (fraction * (startValue[1] - endValue[1])) + endValue[1];
        } else {
            height = (int) (fraction * (endValue[1] - startValue[1])) + startValue[1];
        }
        result[1] = height;

        Log.d("TAG", "======================================================================");
        Log.d("TAG", "fraction : " + fraction);

        Log.d("TAG", "startValueX : " + startValue[0]);
        Log.d("TAG", "startValueY : " + startValue[1]);

        Log.d("TAG", "endValueX : " + endValue[0]);
        Log.d("TAG", "endValueY : " + endValue[1]);

        Log.d("TAG", "result[0] : " + result[0]);
        Log.d("TAG", "result[1] : " + result[1]);

        return result;
    }
}
