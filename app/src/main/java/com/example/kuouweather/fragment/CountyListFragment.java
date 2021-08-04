package com.example.kuouweather.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.kuouweather.HttpService;
import com.example.kuouweather.MainActivity;
import com.example.kuouweather.WeatherActivity;
import com.example.kuouweather.adapter.CountyListAdapter;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.databinding.FragmentCountyListBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CountyListFragment extends Fragment {
    private FragmentCountyListBinding binding;
    private String selectCityID;
    private String selectProID;
    private String weatherId;
    private List<County> counties = new ArrayList<>();
    private int cnt = 0;

    public CountyListFragment() {

    }

    public static CountyListFragment newInstance(String selectCityID, String selectProID) {
        CountyListFragment countyListFragment = new CountyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectCityID", selectCityID);
        bundle.putString("selectProID", selectProID);
        countyListFragment.setArguments(bundle);
        return countyListFragment;
    }

    private final Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            counties = (List<County>) msg.obj;
            binding.lvCounty.setAdapter(new CountyListAdapter(counties));

            return false;
        }
    });
    String TAG = "aaa";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("aaa", "onCreate:  CountyFragment");
        assert getArguments() != null;
        this.selectProID = getArguments().getString("selectProID");
        this.selectCityID = getArguments().getString("selectCityID");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCountyListBinding.inflate(inflater);
        binding.lvCounty.setOnItemClickListener((parent, view, position, id) -> {
            weatherId = counties.get(position).getWeatherID();
            getWeather(weatherId);
        });
        getCounty();
        return binding.getRoot();
    }

    private void getCounty() {
        if (cnt < 6) {
            cnt++;
            try {
                Toast.makeText(requireContext(), "数据拉取中，请等待", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        } else {
            cnt++;
            try {
                Toast.makeText(requireContext(), "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }

        if (getCountyInDatabase()) {
            binding.lvCounty.setAdapter(new CountyListAdapter(counties));
            return;
        }
        String url = "http://guolin.tech/api/china/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        Log.d(TAG, "getCounty: " + url);
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getCounties(selectProID, selectCityID);
        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    cnt = 0;
                    assert response.body() != null;
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "onResponse: " + jsonResponse);
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        County county = new County(jsonObject.getString("name"), jsonObject.getString("weather_id"), selectCityID);
                        counties.add(county);
                        county.save();
                    }
                    Message message = new Message();
                    message.obj = counties;
                    handler2.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + "getCounty");
                if (cnt < 6) getCounty();
            }
        });
    }

    private boolean getCountyInDatabase() {
        List<County> countyList = LitePal.where("cityID = ?", selectCityID).find(County.class);
        if (countyList.size() > 0) {
            counties = countyList;
            return true;
        }
        return false;
    }

    private void getWeather(String weatherId) {
        Log.d(TAG, "getWeather: " + weatherId);
        Intent intent = new Intent(getContext(), WeatherActivity.class);
        intent.putExtra("weatherID", weatherId);
        startActivity(intent);
    }
}
