package com.example.kuouweather.bean;


import org.litepal.crud.LitePalSupport;

public class DailyForecast extends LitePalSupport {
        private String date;
        private String weatherQuality;
        private String maxTmp;
        private String minTmp;

    public DailyForecast(String date, String weatherQuality, String maxTmp, String minTmp) {
        this.date = date;
        this.weatherQuality = weatherQuality;
        this.maxTmp = maxTmp;
        this.minTmp = minTmp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeatherQuality() {
        return weatherQuality;
    }

    public void setWeatherQuality(String weatherQuality) {
        this.weatherQuality = weatherQuality;
    }

    public String getMaxTmp() {
        return maxTmp;
    }

    public void setMaxTmp(String maxTmp) {
        this.maxTmp = maxTmp;
    }

    public String getMinTmp() {
        return minTmp;
    }

    public void setMinTmp(String minTmp) {
        this.minTmp = minTmp;
    }
}
