package com.team60.ournews;

import android.app.Application;

import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.module.bean.User;
import com.team60.ournews.util.DownloadManager;
import com.team60.ournews.util.PushUtil;
import com.team60.ournews.util.ThemeUtil;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoLoader.init(this);
        User.init(this);
        ThemeUtil.init(this);
        DownloadManager.init();
        PushUtil.init(this);
    }
}
