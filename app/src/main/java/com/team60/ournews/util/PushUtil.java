package com.team60.ournews.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;

import java.util.Set;

import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.BasicPushNotificationBuilder;
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

    public static void init(Context context) {
        if (pushUtil == null) {
            synchronized (PushUtil.class) {
                if (pushUtil == null) {
                    pushUtil = new PushUtil();
                    JPushInterface.setDebugMode(Constants.IS_DEBUG_MODE);
                    JAnalyticsInterface.setDebugMode(Constants.IS_DEBUG_MODE);
                    JPushInterface.init(context);
                    JAnalyticsInterface.init(context);
                    BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(context);
                    builder.statusBarDrawable = R.drawable.min_logo;
                    JPushInterface.setDefaultPushNotificationBuilder(builder);
                }
            }
        }
    }

    public static PushUtil newInstance() {
        if (pushUtil == null)
            throw new UnsupportedOperationException("No Init PushUtil");
        return pushUtil;
    }

    public void setAlias(final Context context, String alias) {
        final SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(context, Constants.SHARED_PREFERENCES_PUSH);
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
        final SharedPreferences sharedPreferences = MyUtil.getSharedPreferences(context, Constants.SHARED_PREFERENCES_PUSH);
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
