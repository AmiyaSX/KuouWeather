package com.example.kuouweather.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    /*增加Gson转换注解*/
    @SerializedName("name")
    private String countyName;
    @SerializedName("weather_id")
    private String weatherID;

    private String cityID;


    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(String weatherID) {
        this.weatherID = weatherID;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
