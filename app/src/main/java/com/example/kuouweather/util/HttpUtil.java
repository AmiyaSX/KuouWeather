package com.example.kuouweather.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    /*
     * 创建一个请求的工具类，全局只需要一个retrofit即可，同时基于一个baseurl进行网络请求
     * 并加入gson的转换工厂进行retrofit自身对请求到的数据进行Gson解析
     */

    public static HttpService Httpservice() {
        String URL = "http://guolin.tech/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(HttpService.class);
    }

}
