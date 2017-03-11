package com.team60.ournews;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.util.PushUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class MyApplication extends Application {

    private static Context mApplication;

    private static List<Activity> activityList;

    public static Context getContext() {
        return mApplication;
    }

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
        mApplication = this;
        FrescoLoader.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                PushUtil.initPush(this);
            }
        } else {
            PushUtil.initPush(this);
        }
    }
}
