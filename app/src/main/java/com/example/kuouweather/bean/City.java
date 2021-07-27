package com.example.kuouweather.bean;

public class City {
    private String cityName;
    private int cityID;

    public City(String cityName, int cityID) {
        this.cityName = cityName;
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}
