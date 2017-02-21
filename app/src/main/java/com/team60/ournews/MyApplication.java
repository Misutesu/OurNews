package com.team60.ournews;

import android.app.Application;
import android.content.Context;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class MyApplication extends Application {

    private static Context mApplication;

    public static Context getContext() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
