package com.team60.ournews.util;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created by wujiaquan on 2017/3/11.
 */

public class PushUtil {
    private static PushUtil pushUtil;

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

    public String getUmengToken(Context context) {
        String umengToken = PushAgent.getInstance(context).getRegistrationId();
        if (umengToken == null)
            umengToken = "";
        return umengToken;
    }

    public void initPush(Context context) {
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        mPushAgent.setMessageChannel("Android");
        mPushAgent.setDebugMode(false);
        final int[] getTokenNum = {0};
        final IUmengRegisterCallback callback = new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                if (TextUtils.isEmpty(deviceToken) && getTokenNum[0] <= 10) {
                    mPushAgent.register(this);
                    getTokenNum[0]++;
                    waitOneSecond();
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                if (getTokenNum[0] <= 10) {
                    mPushAgent.register(this);
                    getTokenNum[0]++;
                    waitOneSecond();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushAgent.register(callback);
            }
        }).start();
    }

    public void enablePush(Context context) {
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        final int[] enableNum = {0};
        final IUmengCallback callback = new IUmengCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {
                if (enableNum[0] <= 10) {
                    mPushAgent.enable(this);
                    enableNum[0]++;
                    waitOneSecond();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushAgent.enable(callback);
            }
        }).start();
    }

    public void disablePush(Context context) {
        final PushAgent mPushAgent = PushAgent.getInstance(context);
        final int[] disableNum = {0};
        final IUmengCallback callback = new IUmengCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {
                if (disableNum[0] <= 10) {
                    mPushAgent.disable(this);
                    disableNum[0]++;
                    waitOneSecond();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPushAgent.disable(callback);
            }
        }).start();
    }

    private void waitOneSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
