package com.example.kuouweather;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HttpService {

    @GET("weather")
    Call<ResponseBody> getWeather(@QueryMap Map<String, String> queryParams);

    @GET("http://guolin.tech/api/china/")
    Call<ResponseBody> getProvinces();

    @GET("{id}/")
    Call<ResponseBody> getCities(@Path("id") String id);

    @GET("{proid}/{cityid}/")
    Call<ResponseBody> getCounties(@Path("proid") String proId, @Path("cityid") String cityId);

    @GET("http://guolin.tech/api/bing_pic/")
    Call<ResponseBody> getImage();

}
