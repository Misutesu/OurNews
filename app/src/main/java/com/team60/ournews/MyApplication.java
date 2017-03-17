package com.team60.ournews;

import android.app.Activity;
import android.app.Application;

import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.util.PushUtil;
import com.team60.ournews.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class MyApplication extends Application {

    private static List<Activity> activityList;

    public static void addActivity(Activity activity) {
        if (activityList == null)
            activityList = new ArrayList<>();
        activityList.add(activity);
    }

    public static void finishAllActivity() {
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoLoader.init(this);
        User.init(this);
        ThemeUtil.init(this);
        PushUtil.init(this);
    }
}
