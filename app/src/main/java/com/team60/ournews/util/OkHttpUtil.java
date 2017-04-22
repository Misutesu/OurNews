package com.team60.ournews.util;

import android.util.Log;

import com.team60.ournews.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public class OkHttpUtil {

    private static final String TAG = "OkHttpClient";

    private static OkHttpClient mClient;

    private static OkHttpClient mDownloadClient;

    public static OkHttpClient getOkHttpClient() {
        if (mClient == null) {
            synchronized (OkHttpUtil.class) {
                if (mClient == null) {
                    mClient = new OkHttpClient.Builder()
                            .addInterceptor(new LoggingInterceptor())
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .build();
                }
            }
        }
        return mClient;
    }

    public static OkHttpClient getOkHttpClientForDownload() {
        if (mDownloadClient == null) {
            synchronized (OkHttpUtil.class) {
                if (mDownloadClient == null) {
                    mDownloadClient = new OkHttpClient();
                }
            }
        }
        return mDownloadClient;
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (Constants.IS_DEBUG_MODE) {
                Log.e(TAG, "request: " + request.toString());
                okhttp3.MediaType mediaType = response.body().contentType();
                Log.e(TAG, "mediaType.type: " + mediaType.toString());
                if (mediaType.equals(MediaType.parse("application/json;charset=UTF-8"))
                        || mediaType.equals(MediaType.parse("text/plain;charset=UTF-8"))) {
                    String content = response.body().string();
                    Log.e(TAG, "onResponse: " + format(content));
                    return response.newBuilder().body((okhttp3.ResponseBody
                            .create(mediaType, content))).build();
                }
            }
            return response;
        }
    }

    private static String format(String jsonStr) {
        int level = 0;
        StringBuilder jsonForMatStr = new StringBuilder();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c).append("\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c).append("\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }
}
