package com.example.kuouweather.util;

import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.bean.Weather;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface HttpService {
    /*
     * 请求数据并转换为实例对象，请求到的json数据信息直接转换成
     * 对应的实体对象，可以通过response.body()进行取出
     */

    @GET("weather")
    Call<Weather> getWeather(@QueryMap Map<String, String> queryParams);

    @GET("china/")
    Call<List<Province>> getProvinces();

    @GET("china/{id}/")
    Call<List<City>> getCities(@Path("id") String id);

    @GET("china/{proid}/{cityid}/")
    Call<List<County>> getCounties(@Path("proid") String proId, @Path("cityid") String cityId);

    @GET("bing_pic/")
    Call<String> getImage();

}
