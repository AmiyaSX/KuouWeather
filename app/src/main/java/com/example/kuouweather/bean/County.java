package com.example.kuouweather.bean;

public class County {
    private String countyName;
    private String weatherID;

    public County(String countyName, String weatherID) {
        this.countyName = countyName;
        this.weatherID = weatherID;
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
}
