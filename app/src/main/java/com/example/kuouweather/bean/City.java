package com.example.kuouweather.bean;

import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {
    private String cityName;
    private String cityID;
    private String proID;

    public City(String cityName, String cityID, String proID) {
        this.cityName = cityName;
        this.cityID = cityID;
        this.proID = proID;
    }

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
