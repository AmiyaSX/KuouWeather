package com.example.kuouweather.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    /*增加Gson转换注解*/
    @SerializedName("name")
    private String cityName;
    @SerializedName("id")
    private String cityID;
    private String proID;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }
}
