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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kuouweather.adapter.RecyclerViewAdapter;
import com.example.kuouweather.bean.Weather;
import com.example.kuouweather.util.HttpService;
import com.example.kuouweather.util.HttpUtil;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

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

    private int requireCnt;

    private RecyclerView recyclerView;

    private ImageView imageView;

    private ScrollView scrollView;

    private CircularProgressView progressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();
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
        Intent intent = getIntent();
        weatherID = intent.getStringExtra("weatherID");
        imageView = findViewById(R.id.bing_img);
        progressView = findViewById(R.id.progress_view);
        progressView.startAnimation();
        CardView cardView = findViewById(R.id.card_suggestion);
        recyclerView = findViewById(R.id.lv_weather);
        cardView.getBackground().setAlpha(100);
        CardView cardForeCast = findViewById(R.id.card);
        cardForeCast.getBackground().setAlpha(100);
        requireCnt = 0;

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
        toastIfo();
        /*拉取网络图片*/
        Call<String> call = HttpUtil.Httpservice().getImage();

        call.enqueue(new Callback<String>() {
            @SneakyThrows
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                requireCnt = 0;
                /*拿到retrofit内部转换好的对象*/
                urlImage = response.body();
                runOnUiThread(() -> {
                    try {
                        Glide.with(WeatherActivity.this).load(urlImage).into(imageView);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                if (requireCnt < 3) loadImage();
            }
        });
    }

    private void getWeather(String weatherID) {
        toastIfo();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cityid", weatherID);
        hashMap.put("key", getString(R.string.key));
        /*利用请求工具类进行请求*/
        Call<Weather> call = HttpUtil.Httpservice().getWeather(hashMap);

        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NotNull Call<Weather> call, @NotNull Response<Weather> response) {
                requireCnt = 0;

                if (response.isSuccessful()) {
                    /*回到主线程处理UI*/
                    runOnUiThread(() -> {
                        assert response.body() != null;
                        showWeather(response.body());
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<Weather> call, @NotNull Throwable t) {
                if (requireCnt < 3) getWeather(weatherID);
            }
        });
    }

    private void toastIfo() {
        if (requireCnt < 6) {
            requireCnt++;
            Toast.makeText(this, "数据拉取中，请等待", Toast.LENGTH_SHORT).show();
        } else {
            requireCnt++;
            Toast.makeText(this, "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showWeather(Weather weather) {
        Weather.HeWeatherDTO heWeather = weather.getHeWeather().get(0);
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
        List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecasts = weather.getHeWeather().get(0).getDailyForecast();
        recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView, dailyForecasts));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressView.stopAnimation();

    }

}