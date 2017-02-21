package com.team60.ournews.module.connection;

import retrofit2.Retrofit;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public class RetrofitUtil {
    /**
     * http://192.168.0.104:8080/
     * http://112.74.52.72:8080/OurNews/
     */
//    public static final String BASE_URL = "http://192.168.0.104:8080/";
    public static final String BASE_URL = "http://112.74.52.72:8080/OurNews/";

    private static ApiStore apiStore;

    public static ApiStore newInstance() {
        if (apiStore == null) {
            synchronized (RetrofitUtil.class) {
                apiStore = new Retrofit.Builder().baseUrl(BASE_URL).build().create(ApiStore.class);
            }
        }
        return apiStore;
    }
}
