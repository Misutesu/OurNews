package com.team60.ournews.module.evaluator;

import android.animation.TypeEvaluator;

/**
 * Created by wujiaquan on 2017/3/2.
 */

public class SizeEvaluator implements TypeEvaluator<Integer[]> {
    @Override
    public Integer[] evaluate(float fraction, Integer[] startValue, Integer[] endValue) {
        Integer[] result = new Integer[2];
        result[0] = (int) (fraction * (endValue[0] - startValue[0])) + startValue[0];
        result[1] = (int) (fraction * (endValue[1] - startValue[1])) + startValue[1];
        return result;
    }
}
