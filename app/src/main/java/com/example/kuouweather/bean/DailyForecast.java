package com.example.kuouweather.bean;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

public class DailyForecast {
        @SerializedName("date")
        private String date;
        @SerializedName("cond")
        private Weather.HeWeatherDTO.DailyForecastDTO.CondDTO cond;
        @SerializedName("tmp")
        private Weather.HeWeatherDTO.DailyForecastDTO.TmpDTO tmp;

        @NoArgsConstructor
        @Data
        public static class CondDTO {
            @SerializedName("txt_d")
            private String txtD;
        }

        @NoArgsConstructor
        @Data
        public static class TmpDTO {
            @SerializedName("max")
            private String max;
            @SerializedName("min")
            private String min;
        }
    }
