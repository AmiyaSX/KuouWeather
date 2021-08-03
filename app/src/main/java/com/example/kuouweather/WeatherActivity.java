package com.example.kuouweather;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kuouweather.adapter.RecyclerViewAdapter;
import com.example.kuouweather.bean.Weather;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WeatherActivity extends FragmentActivity {
    private String weatherID;
    private String urlImage;
    private TextView title;
    private TextView time;
    private TextView temperature;
    private TextView weatherTxt;
    private TextView aqi;
    private TextView pm;
    private TextView comf;
    private TextView sport;
    private TextView cw;
    private RecyclerView recyclerView;
    private ImageView imageView;
    private ScrollView scrollView;
    private CardView cardView;
    private CardView cardForeCast;
    private CircularProgressView progressView;
    private List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecasts;
    String TAG = "aaa";
    private final Handler handler = new Handler(msg -> {
        Weather weather = (Weather) msg.obj;
        showWeather(weather);
        return false;
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
        Log.d(TAG, "onCreate: " + weatherID);
        loadImage();
        getWeather(weatherID);

    }

    private void initView() {
        scrollView = findViewById(R.id.mscrollview);
        title = findViewById(R.id.weather_title);
        time = findViewById(R.id.time);
        temperature = findViewById(R.id.temperature);
        weatherTxt = findViewById(R.id.txt);
        aqi = findViewById(R.id.aqi);
        pm = findViewById(R.id.pm2_5);
        comf = findViewById(R.id.comf);
        sport = findViewById(R.id.sport);
        cw = findViewById(R.id.cw);
//        listView = findViewById(R.id.lv_weather);
        Intent intent = getIntent();
        weatherID = intent.getStringExtra("weatherID");
        imageView = findViewById(R.id.bing_img);
        progressView = findViewById(R.id.progress_view);
        progressView.startAnimation();
        cardView = findViewById(R.id.card_suggestion);
        recyclerView = findViewById(R.id.lv_weather);
        cardView.getBackground().setAlpha(100);
        cardForeCast = findViewById(R.id.card);
        cardForeCast.getBackground().setAlpha(100);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            /*状态栏设置透明*/
            window.setStatusBarColor(Color.TRANSPARENT);
            int visibility = window.getDecorView().getSystemUiVisibility();
            /*布局内容全屏显示*/
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            visibility |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            /*隐藏虚拟导航栏*/
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            window.getDecorView().setSystemUiVisibility(visibility);
        }

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
        Log.d(TAG, "getWeather: " + "http://guolin.tech/api/weather?cityid=" + weatherID + getString(R.string.key));
        HttpService httpService = retrofit.create(HttpService.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cityid", weatherID);
        hashMap.put("key", getString(R.string.key));
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

    @SuppressLint("SetTextI18n")
    private void showWeather(Weather weather) {
        Weather.HeWeatherDTO heWeather = weather.getHeWeather().get(0);
        Log.d(TAG, "showWeather: ");
        scrollView.smoothScrollTo(0, 0);
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
        dailyForecasts = weather.getHeWeather().get(0).getDailyForecast();
        recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView,dailyForecasts));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressView.stopAnimation();
    }

}