package com.example.kuouweather.bean;

import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {
    private String countyName;
    private String weatherID;
    private String cityID;

    public County(String countyName, String weatherID, String cityID) {
        this.countyName = countyName;
        this.weatherID = weatherID;
        this.cityID = cityID;
    }


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
