package com.team60.ournews.util;

import android.content.Context;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by wujiaquan on 2017/3/11.
 */

public class PushUtil {
    public static void initPush(Context context) {
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setMessageChannel("Android");
        mPushAgent.setDebugMode(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushAgent.register(new IUmengRegisterCallback() {
                    @Override
                    public void onSuccess(String deviceToken) {

                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
            }
        }).start();
    }
}
