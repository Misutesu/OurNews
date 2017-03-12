package com.team60.ournews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.AttrRes;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;

/**
 * Created by Misutesu on 2016/10/19 0019.
 */

public class ThemeUtil {
    private static final int[] styles = new int[9];

    private static SharedPreferences sharedPreferences;

    public static AlertDialog.Builder getThemeDialogBuilder(Context context) {
        AlertDialog.Builder builder;
        if (ThemeUtil.isNightMode()) {
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

    private static void init() {
        if (sharedPreferences == null)
            synchronized (ThemeUtil.class) {
                sharedPreferences = MyUtil.getSharedPreferences(Constants.SHARED_PREFERENCES_THEME);
                styles[0] = -1;
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

    public static int getStyleNum() {
        init();
        return sharedPreferences.getInt(Resources.Theme.class.getName(), 0);
    }

    public static int getStyle() {
        init();
        return styles[sharedPreferences.getInt(Resources.Theme.class.getName(), 0)];
    }

    public static void setStyle(int num) {
        init();
        sharedPreferences.edit().putInt(Resources.Theme.class.getName(), num).apply();
    }

    public static boolean isNightMode() {
        init();
        return sharedPreferences.getBoolean("isNight", false);
    }

    public static void setNightMode(boolean type) {
        init();
        sharedPreferences.edit().putBoolean("isNight", type).apply();
    }
}
