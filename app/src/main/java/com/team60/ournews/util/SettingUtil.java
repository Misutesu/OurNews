package com.team60.ournews.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.team60.ournews.common.Constants;

/**
 * Created by Misutesu on 2017/5/24 0024.
 */

public class SettingUtil {

    private static SharedPreferences sharedPreferences;

    private static SettingUtil settingUtil;

    private boolean isPush = true;

    private SettingUtil() {
    }

    public static void init(Context context) {
        if (sharedPreferences == null) {
            synchronized (SettingUtil.class) {
                if (sharedPreferences == null) {
                    sharedPreferences = MyUtil.getSharedPreferences(context, Constants.SHARED_PREFERENCES_SETTING);
                    settingUtil = new SettingUtil();
                }
            }
        }
    }

    public static SettingUtil newInstance() {
        if (sharedPreferences == null)
            throw new UnsupportedOperationException("No Init SettingUtil");
        return settingUtil;
    }

    public void setPushState(boolean isPush) {
        this.isPush = isPush;
        sharedPreferences.edit().putBoolean("isPush", isPush).apply();
    }

    public boolean getPushState() {
        isPush = sharedPreferences.getBoolean("isPush", true);
        return isPush;
    }
}
