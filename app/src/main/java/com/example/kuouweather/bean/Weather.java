package com.example.kuouweather.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Weather {
    private int id;
    private String countryName;

    private List<HeWeatherDTO> heWeather;

    @NoArgsConstructor
    @Data
    public static class HeWeatherDTO {
        private HeWeatherDTO.BasicDTO basic;
        private HeWeatherDTO.UpdateDTO update;
        private String status;
        private HeWeatherDTO.NowDTO now;
        private List<DailyForecastDTO> dailyForecast;
        private HeWeatherDTO.AqiDTO aqi;
        private HeWeatherDTO.SuggestionDTO suggestion;
        private String msg;

        @NoArgsConstructor
        @Data
        public static class BasicDTO {
            private String cid;
            private String location;
            private String parentCity;
            private String adminArea;
            private String cnty;
            private String lat;
            private String lon;
            private String tz;
            private String city;
            private String idX;
            private HeWeatherDTO.BasicDTO.UpdateDTO update;

            @NoArgsConstructor
            @Data
            public static class UpdateDTO {
                private String loc;
                private String utc;
            }
        }

        @NoArgsConstructor
        @Data
        public static class UpdateDTO {
            private String loc;
            private String utc;
        }

        @NoArgsConstructor
        @Data
        public static class NowDTO {
            private String cloud;
            private String condCode;
            private String condTxt;
            private String fl;
            private String hum;
            private String pcpn;
            private String pres;
            private String tmp;
            private String vis;
            private String windDeg;
            private String windDir;
            private String windSc;
            private String windSpd;
            private HeWeatherDTO.NowDTO.CondDTO cond;

            @NoArgsConstructor
            @Data
            public static class CondDTO {
                private String code;
                private String txt;
            }
        }

        @NoArgsConstructor
        @Data
        public static class AqiDTO {
            private HeWeatherDTO.AqiDTO.CityDTO city;

            @NoArgsConstructor
            @Data
            public static class CityDTO {
                private String aqi;
                private String pm25;
                private String qlty;
            }
        }

        @NoArgsConstructor
        @Data
        public static class SuggestionDTO {
            private HeWeatherDTO.SuggestionDTO.ComfDTO comf;
            private HeWeatherDTO.SuggestionDTO.SportDTO sport;
            private HeWeatherDTO.SuggestionDTO.CwDTO cw;

            @NoArgsConstructor
            @Data
            public static class ComfDTO {
                private String type;
                private String brf;
                private String txt;
            }

            @NoArgsConstructor
            @Data
            public static class SportDTO {
                private String type;
                private String brf;
                private String txt;
            }

            @NoArgsConstructor
            @Data
            public static class CwDTO {
                private String type;
                private String brf;
                private String txt;
            }
        }

        @NoArgsConstructor
        @Data
        public static class DailyForecastDTO {
            private String date;
            private HeWeatherDTO.DailyForecastDTO.CondDTO cond;
            private HeWeatherDTO.DailyForecastDTO.TmpDTO tmp;

            @NoArgsConstructor
            @Data
            public static class CondDTO {
                private String txtD;
            }

            @NoArgsConstructor
            @Data
            public static class TmpDTO {
                private String max;
                private String min;
            }
        }
    }
}
