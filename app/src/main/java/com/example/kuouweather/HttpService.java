package com.example.kuouweather;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpService {

    @GET("http://guolin.tech/api/weather?cityid=?&key=?")
    Call<ResponseBody> getWeather(@Query("cityid") String weatherID,@Query("key") String key);

    @GET("http://guolin.tech/api/china/")
    Call<ResponseBody> getProvinces();

    @GET("http://guolin.tech/api/china/?")
    Call<ResponseBody> getCities(@Query("?") String id);

    @GET("http://guolin.tech/api/china/?/??")
    Call<ResponseBody> getCounties(@Query("?") String proId, @Query("??") String cityId);

}
