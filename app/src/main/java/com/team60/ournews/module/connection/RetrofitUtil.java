package com.team60.ournews.module.connection;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.team60.ournews.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class RetrofitUtil {

    /**
     * http://192.168.0.104:8080/
     * http://112.74.52.72:8080/OurNews/
     */
//    public static final String BASE_URL = "http://192.168.31.57:8080/";
    public static final String BASE_URL = "http://112.74.52.72:8080/OurNews/";

    private static ApiStore apiStore;

    public static ApiStore newInstance() {
        if (apiStore == null) {
            synchronized (RetrofitUtil.class) {
                if (apiStore == null) {
                    OkHttpClient.Builder client = new OkHttpClient.Builder()
                            .addInterceptor(new LoggingInterceptor())
                            .connectTimeout(6, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);

//                    if (Constants.IS_DEBUG_MODE) {
//                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
//                                new HttpLoggingInterceptor.Logger() {
//                                    @Override
//                                    public void log(String message) {
//                                        if (TextUtils.isEmpty(message)) return;
//                                        HttpLoggingInterceptor.Logger.DEFAULT.log("Get Response " + message);
//                                    }
//                                });
//                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//                        client.addInterceptor(logging);
//                    }

                    apiStore = new Retrofit.Builder().baseUrl(BASE_URL)
                            .client(client.build())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                            .create(ApiStore.class);
                }
            }
        }
        return apiStore;
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
