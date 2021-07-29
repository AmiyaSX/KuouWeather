package com.example.kuouweather.bean;

import org.litepal.crud.LitePalSupport;

public class WeatherInfo extends LitePalSupport {
    private String weatherID;
    private String city;
    private String weatherQuality;
    private String aqi;
    private String pm;
    private String tmp;

    public WeatherInfo(String weatherID, String city, String weatherQuality, String aqi, String pm, String tmp) {
        this.weatherID = weatherID;
        this.city = city;
        this.weatherQuality = weatherQuality;
        this.aqi = aqi;
        this.pm = pm;
        this.tmp = tmp;
    }

    public String getWeatherID() {
        return weatherID;
    }

    public void setWeatherID(String weatherID) {
        this.weatherID = weatherID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherQuality() {
        return weatherQuality;
    }

    public void setWeatherQuality(String weatherQuality) {
        this.weatherQuality = weatherQuality;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }
}
