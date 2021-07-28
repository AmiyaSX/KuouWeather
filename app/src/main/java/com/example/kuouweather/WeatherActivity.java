package com.example.kuouweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kuouweather.adapter.WeatherListAdapter;
import com.example.kuouweather.bean.Weather;
import com.example.kuouweather.databinding.ActivityWeatherBinding;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherActivity extends AppCompatActivity {
    private String weatherID;
    private String urlImage;
    private List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecasts = new ArrayList<>();
    private TextView title;
    private TextView time;
    private TextView temperature;
    private TextView weatherTxt;
    private TextView aqi;
    private TextView pm;
    private TextView comf;
    private TextView sport;
    private TextView cw;
    private ListView listView;
    private ImageView imageView;
    String TAG = "aaa";
    private final Handler handler = new Handler(msg -> {
        Weather weather = (Weather) msg.obj;
        showWeather(weather);
        return false;
    });

    @SuppressLint("SetTextI18n")
    private void showWeather(Weather weather) {
        Weather.HeWeatherDTO heWeather = weather.getHeWeather().get(0);
        Log.d(TAG, "showWeather: ");
        title.setText(heWeather.getBasic().getLocation());
        Date date = new Date();
        time.setText(date.toString());
        temperature.setText(heWeather.getNow().getTmp() + "℃");
        weatherTxt.setText(heWeather.getNow().getCondTxt());
        aqi.setText(heWeather.getAqi().getCity().getAqi());
        pm.setText(heWeather.getAqi().getCity().getPm25());
        comf.setText("舒适度：" + heWeather.getSuggestion().getComf().getTxt());
        sport.setText("运动建议：" + heWeather.getSuggestion().getSport().getTxt());
        cw.setText("洗车指数：" + heWeather.getSuggestion().getCw().getTxt());
        listView.setAdapter(new WeatherListAdapter(dailyForecasts));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.kuouweather.databinding.ActivityWeatherBinding binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        title = findViewById(R.id.weather_title);
        time = findViewById(R.id.time);
        temperature = findViewById(R.id.temperature);
        weatherTxt = findViewById(R.id.txt);
        aqi = findViewById(R.id.aqi);
        pm = findViewById(R.id.pm2_5);
        comf = findViewById(R.id.comf);
        sport = findViewById(R.id.sport);
        cw = findViewById(R.id.cw);
        listView = findViewById(R.id.lv_weather);
        Intent intent = getIntent();
        weatherID = intent.getStringExtra("weatherID");
        imageView = findViewById(R.id.bing_img);
        Log.d(TAG, "onCreate: " + weatherID);
        loadImage();
        getWeather(weatherID);

    }

    private void loadImage() {
        /*拉取网络图片*/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://guolin.tech/api/bing_pic/")
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getImage();
        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                try {
                     urlImage = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(() -> Glide.with(WeatherActivity.this).load(urlImage).into(imageView));
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: ");
                loadImage();
            }
        });
    }

    private void getWeather(String weatherID) {
        Log.d(TAG, "getWeather: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://guolin.tech/api/")
                .build();
        Log.d(TAG, "getWeather: " + "http://guolin.tech/api/weather?cityid=" + weatherID + "&key=b2fc2389874847f999b25c4c4b933d68/");
        HttpService httpService = retrofit.create(HttpService.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cityid", weatherID);
        hashMap.put("key", "b2fc2389874847f999b25c4c4b933d68");
        Call<ResponseBody> call = httpService.getWeather(hashMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    try {
                        assert response.body() != null;
                        String jsonResponse = response.body().string();
                        Log.d("aaa", "onResponse: " + jsonResponse);
                        Gson gson = new Gson();
                        Weather weather = gson.fromJson(jsonResponse, Weather.class);
                        if (weather.getHeWeather() == null)
                            Log.d(TAG, "onResponse: " + "gson.fromJson fail");
                        dailyForecasts = weather.getHeWeather().get(0).getDailyForecast();
                        Log.d(TAG, "onResponse: " + weather.getHeWeather().get(0).getBasic().getCid() + "  地点： " + weather.getHeWeather().get(0).getBasic().getLocation());
                        Message message = new Message();
                        message.obj = weather;
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get weather");
                getWeather(weatherID);
            }
        });
    }
}