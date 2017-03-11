package com.team60.ournews.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.team60.ournews.util.UiUtil;

/**
 * Created by wujiaquan on 2017/3/11.
 */

public class CompatToolbar extends Toolbar {

    int statusBarHeight;

    public CompatToolbar(Context context) {
        super(context);
        setup();
    }

    public CompatToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public CompatToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
    }

    private void setup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarHeight = UiUtil.getStatusBarHeight();
        }
        setPadding(getPaddingLeft(), getPaddingTop() + statusBarHeight, getPaddingRight(), getPaddingBottom());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec + statusBarHeight);
    }
}
