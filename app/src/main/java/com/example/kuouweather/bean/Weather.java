package com.example.kuouweather.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Weather {

    @SerializedName("HeWeather")
    private List<HeWeatherDTO> heWeather;

    @NoArgsConstructor
    @Data
    public static class HeWeatherDTO {
        @SerializedName("basic")
        private BasicDTO basic;
        @SerializedName("update")
        private UpdateDTO update;
        @SerializedName("status")
        private String status;
        @SerializedName("now")
        private NowDTO now;
        @SerializedName("daily_forecast")
        private List<DailyForecastDTO> dailyForecast;
        @SerializedName("aqi")
        private AqiDTO aqi;
        @SerializedName("suggestion")
        private SuggestionDTO suggestion;
        @SerializedName("msg")
        private String msg;

        @NoArgsConstructor
        @Data
        public static class BasicDTO {
            @SerializedName("cid")
            private String cid;
            @SerializedName("location")
            private String location;
            @SerializedName("parent_city")
            private String parentCity;
            @SerializedName("admin_area")
            private String adminArea;
            @SerializedName("cnty")
            private String cnty;
            @SerializedName("lat")
            private String lat;
            @SerializedName("lon")
            private String lon;
            @SerializedName("tz")
            private String tz;
            @SerializedName("city")
            private String city;
            @SerializedName("id")
            private String id;
            @SerializedName("update")
            private UpdateDTO update;

            @NoArgsConstructor
            @Data
            public static class UpdateDTO {
                @SerializedName("loc")
                private String loc;
                @SerializedName("utc")
                private String utc;
            }
        }

        @NoArgsConstructor
        @Data
        public static class UpdateDTO {
            @SerializedName("loc")
            private String loc;
            @SerializedName("utc")
            private String utc;
        }

        @NoArgsConstructor
        @Data
        public static class NowDTO {
            @SerializedName("cloud")
            private String cloud;
            @SerializedName("cond_code")
            private String condCode;
            @SerializedName("cond_txt")
            private String condTxt;
            @SerializedName("fl")
            private String fl;
            @SerializedName("hum")
            private String hum;
            @SerializedName("pcpn")
            private String pcpn;
            @SerializedName("pres")
            private String pres;
            @SerializedName("tmp")
            private String tmp;
            @SerializedName("vis")
            private String vis;
            @SerializedName("wind_deg")
            private String windDeg;
            @SerializedName("wind_dir")
            private String windDir;
            @SerializedName("wind_sc")
            private String windSc;
            @SerializedName("wind_spd")
            private String windSpd;
            @SerializedName("cond")
            private CondDTO cond;

            @NoArgsConstructor
            @Data
            public static class CondDTO {
                @SerializedName("code")
                private String code;
                @SerializedName("txt")
                private String txt;
            }
        }

        @NoArgsConstructor
        @Data
        public static class AqiDTO {
            @SerializedName("city")
            private CityDTO city;

            @NoArgsConstructor
            @Data
            public static class CityDTO {
                @SerializedName("aqi")
                private String aqi;
                @SerializedName("pm25")
                private String pm25;
                @SerializedName("qlty")
                private String qlty;
            }
        }

        @NoArgsConstructor
        @Data
        public static class SuggestionDTO {
            @SerializedName("comf")
            private ComfDTO comf;
            @SerializedName("sport")
            private SportDTO sport;
            @SerializedName("cw")
            private CwDTO cw;

            @NoArgsConstructor
            @Data
            public static class ComfDTO {
                @SerializedName("type")
                private String type;
                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
                private String txt;
            }

            @NoArgsConstructor
            @Data
            public static class SportDTO {
                @SerializedName("type")
                private String type;
                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
                private String txt;
            }

            @NoArgsConstructor
            @Data
            public static class CwDTO {
                @SerializedName("type")
                private String type;
                @SerializedName("brf")
                private String brf;
                @SerializedName("txt")
                private String txt;
            }
        }

        @NoArgsConstructor
        @Data
        public static class DailyForecastDTO {
            @SerializedName("date")
            private String date;
            @SerializedName("cond")
            private CondDTO cond;
            @SerializedName("tmp")
            private TmpDTO tmp;

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
    }
}
