package com.team60.ournews.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.team60.ournews.common.Constants;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by wujiaquan on 2017/3/11.
 */

public class PushUtil {

    private static PushUtil pushUtil;

    private boolean isStartSetAlias;

    private boolean isLogout;

    private PushUtil() {
    }

    public static PushUtil newInstance() {
        if (pushUtil == null) {
            synchronized (PushUtil.class) {
                if (pushUtil == null)
                    pushUtil = new PushUtil();
            }
        }
        return pushUtil;
    }

    public void initPush(Context context) {
        if (Constants.IS_DEBUG_MODE) JPushInterface.setDebugMode(true);
        JPushInterface.init(context);
    }

    public void setAlias(final Context context, String alias) {
        final SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(Constants.SHARED_PREFERENCES_PUSH);
        if (!isStartSetAlias && !sharedPreferences.getBoolean("isSetAlias", false)) {
            isStartSetAlias = true;
            JPushInterface.setAlias(context, alias, new TagAliasCallback() {
                @Override
                public void gotResult(int code, String alias, Set<String> tags) {
                    if (code == 0) {
                        sharedPreferences.edit().putBoolean("isSetAlias", true).apply();
                        isStartSetAlias = false;
                    } else {
                        if (!isLogout) {
                            JPushInterface.setAlias(context, alias, this);
                        } else {
                            isLogout = false;
                            isStartSetAlias = false;
                        }
                    }
                }
            });
        }
    }

    public void logout(final Context context) {
        isLogout = true;
        final SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(Constants.SHARED_PREFERENCES_PUSH);
        if (sharedPreferences.getBoolean("isSetAlias", false))
            JPushInterface.setAlias(context, "", new TagAliasCallback() {
                @Override
                public void gotResult(int code, String alias, Set<String> tags) {
                    if (code == 0) {
                        sharedPreferences.edit().putBoolean("isSetAlias", false).apply();
                    } else {
                        JPushInterface.setAlias(context, "", this);
                    }
                }
            });
    }
}
