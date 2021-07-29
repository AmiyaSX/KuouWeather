package com.example.kuouweather.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuouweather.HttpService;
import com.example.kuouweather.MainActivity;
import com.example.kuouweather.adapter.CityListAdapter;
import com.example.kuouweather.bean.City;
import com.example.kuouweather.databinding.FragmentCityListBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CityListFragment extends Fragment {
    private FragmentCityListBinding binding;
    private final String selectProID;
    private String selectCityID;
    private final MainActivity.ChangeToCountyFragment changeToCountyFragment;
    private List<City> cities = new ArrayList<>();

    public CityListFragment(String selectProID, MainActivity.ChangeToCountyFragment changeToCountyFragment) {
        this.selectProID = selectProID;
        this.changeToCountyFragment = changeToCountyFragment;
    }

    private final Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            cities = (List<City>) msg.obj;
            binding.lvCity.setAdapter(new CityListAdapter(cities));
            return true;
        }
    });

    String TAG = "aaa";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("aaa", "onCreate:  CountyFragment");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCityListBinding.inflate(inflater);
        binding.lvCity.setAdapter(new CityListAdapter(cities));
        binding.lvCity.setOnItemClickListener((parent, view, position, id) -> {
            Log.d(TAG, "handleMessage: " + "click to get County");
            selectCityID = cities.get(position).getCityID();
            changeToCountyFragment.showCountyListFragment();
        });
        getCity(selectProID);
        return binding.getRoot();
    }

    public void getCity(String id) {
        if (getCityInDatabase()) {
            binding.lvCity.setAdapter(new CityListAdapter(cities));
            return;
        }
        String url = "http://guolin.tech/api/china/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getCities(id);

        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "onResponse: json" + jsonResponse + "  " + response.body().toString());
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        City city = new City(jsonObject.getString("name"), jsonObject.getString("id"), selectProID);
                        cities.add(city);
                        city.save();
                    }
                    Message message = new Message();
                    message.obj = cities;
                    handler1.sendMessage(message);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get city");
                getCity(selectProID);
            }
        });
    }

    private boolean getCityInDatabase() {
        List<City> cityList = LitePal.where("proID = ?", selectProID).find(City.class);
        if (cityList.size() > 0) {
            cities = cityList;
            return true;
        }
        return false;
    }


    public String getSelectProID() {
        return selectProID;
    }

    public String getSelectCityID() {
        return selectCityID;
    }
}