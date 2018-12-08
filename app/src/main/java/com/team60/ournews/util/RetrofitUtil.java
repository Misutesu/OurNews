package com.team60.ournews.util;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.team60.ournews.module.connection.ApiStore;

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
//    public static final String BASE_URL = "http://112.74.52.72:8080/OurNews/";
    public static final String BASE_URL = "http://119.29.58.134:8080/OurNews/";

    private static ApiStore apiStore;

    /**
     * 使用单例模式获取ApiStore对象
     * @return ApiStore
     */
    public static ApiStore newInstance() {
        if (apiStore == null) {
            synchronized (RetrofitUtil.class) {
                if (apiStore == null) {
                    apiStore = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(OkHttpUtil.getOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build()
                            .create(ApiStore.class);
                }
            }
        }
        return apiStore;
    }
}
