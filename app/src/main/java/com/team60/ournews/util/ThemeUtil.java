package com.team60.ournews.util;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;

/**
 * Created by Misutesu on 2016/10/19 0019.
 */

public class ThemeUtil {
    private static final int[] styles = new int[9];

    private static ThemeUtil themeUtil;

    private static SharedPreferences sharedPreferences;

    public interface OnColorChangeListener {
        void onColorChange(int color);
    }

    private ThemeUtil() {
    }

    public static AlertDialog.Builder getThemeDialogBuilder(Context context) {
        AlertDialog.Builder builder;
        if (ThemeUtil.newInstance().isNightMode()) {
            builder = new AlertDialog.Builder(context, R.style.NightDialogTheme);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        return builder;
    }

    public static int getColor(Resources.Theme theme, @AttrRes int id) {
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(id, typedValue, true);
        return typedValue.data;
    }

    public static void changeColor(int[] colors, @NonNull final OnColorChangeListener onColorChangeListener) {
        ValueAnimator animator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colors[0], colors[1])
                .setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onColorChangeListener.onColorChange((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public static void init(Context context) {
        if (sharedPreferences == null) {
            synchronized (ThemeUtil.class) {
                if (sharedPreferences == null) {
                    sharedPreferences = MyUtil.getSharedPreferences(context, Constants.SHARED_PREFERENCES_THEME);
                    styles[0] = R.style.BlueTheme;
                    styles[1] = R.style.BlueBlackTheme;
                    styles[2] = R.style.GreenTheme;
                    styles[3] = R.style.GreenBlackTheme;
                    styles[4] = R.style.YellowTheme;
                    styles[5] = R.style.RedTheme;
                    styles[6] = R.style.PinkTheme;
                    styles[7] = R.style.BrownTheme;
                    styles[8] = R.style.BlackTheme;
                }
            }
        }
    }

    public static ThemeUtil newInstance() {
        if (sharedPreferences == null)
            throw new UnsupportedOperationException("No Init ThemeUtil");
        if (themeUtil == null)
            themeUtil = new ThemeUtil();
        return themeUtil;
    }

    public int getStyleNum() {
        return sharedPreferences.getInt(Resources.Theme.class.getName(), 0);
    }

    public int getStyle() {
        return styles[sharedPreferences.getInt(Resources.Theme.class.getName(), 0)];
    }

    public void setStyle(int num) {
        sharedPreferences.edit().putInt(Resources.Theme.class.getName(), num).apply();
    }

    public boolean isNightMode() {
        return sharedPreferences.getBoolean("isNight", false);
    }

    public void setNightMode(boolean type) {
        sharedPreferences.edit().putBoolean("isNight", type).apply();
    }
}
