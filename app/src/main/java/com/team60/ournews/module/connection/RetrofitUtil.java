package com.team60.ournews.module.connection;

import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.team60.ournews.common.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

                    if (Constants.IS_DEBUG_MODE) {
                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(
                                new HttpLoggingInterceptor.Logger() {
                                    @Override
                                    public void log(String message) {
                                        if (TextUtils.isEmpty(message)) return;
                                        HttpLoggingInterceptor.Logger.DEFAULT.log("Get Response " + message);
                                    }
                                });
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                        client.addInterceptor(logging);
                    }

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

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (Constants.IS_DEBUG_MODE) {
                HttpLoggingInterceptor.Logger.DEFAULT.log(String.format("Send Request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));
            }
            return chain.proceed(request);
        }
    }
}
