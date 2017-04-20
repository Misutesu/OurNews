package com.team60.ournews.util;

import okhttp3.OkHttpClient;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public class OkHttpUtil {
    private static OkHttpClient mClient;

    public static OkHttpClient getOkHttpClient() {
        if (mClient == null) {
            synchronized (OkHttpUtil.class) {
                if (mClient == null) {
                    mClient = new OkHttpClient();
                }
            }
        }
        return mClient;
    }
}
