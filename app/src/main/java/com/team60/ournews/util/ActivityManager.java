package com.team60.ournews.util;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiaquan on 2017/4/19.
 */

public class ActivityManager {

    private static ActivityManager instance;

    private List<Activity> mActivities;

    public static ActivityManager newInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    private ActivityManager() {
        mActivities = new ArrayList<>();
    }

    public void addActivity(@NonNull Activity activity) {
        mActivities.add(activity);
    }

    public void removeActivity(@NonNull Activity activity) {
        if (mActivities.size() > 0) {
            mActivities.remove(activity);
        }
    }

    public void finishAll() {
        for (Activity activity : mActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public boolean hasActivity() {
        return mActivities.size() != 1;
    }
}
