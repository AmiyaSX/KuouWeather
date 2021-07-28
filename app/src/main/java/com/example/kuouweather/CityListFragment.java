package com.example.kuouweather;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuouweather.adapter.CityListAdapter;
import com.example.kuouweather.adapter.CountyListAdapter;
import com.example.kuouweather.adapter.ListAdapter;
import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.databinding.FragmentCityListBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.kuouweather.R.string.China;


public class CityListFragment extends Fragment {
    private Integer queryProvId;
    private String choosePro;
    private String chooseCity;
    private Integer queryCityId;
    private String weatherId;
    private FragmentCityListBinding binding;
    private final List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();
    private List<County> counties = new ArrayList<>();
    private final Handler handler1 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            cities = (List<City>) msg.obj;
            binding.lvCity.setAdapter(new CityListAdapter(cities));
            binding.lvCity.setOnItemClickListener((parent, view, position, id) -> {
                queryCityId = cities.get(position).getCityID();
                getCounty(queryProvId.toString(),queryCityId.toString());
                chooseCity = cities.get(position).getCityName();
                binding.title.setText(chooseCity);
            });
            binding.backBtn.setOnClickListener(v -> {
                if (queryCityId != null)
                binding.lvCity.setAdapter(new ListAdapter(provinces));
            });
            binding.title.setText(choosePro);
            return false;
        }
    });

    private final Handler handler2 = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            counties = (List<County>) msg.obj;
            binding.lvCity.setAdapter(new CountyListAdapter(counties));
            binding.lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    weatherId = counties.get(position).getWeatherID();
                    getWeather(weatherId);
                }
            });
            return false;
        }
    });

    public CityListFragment() {

    }

    String TAG = "aaa";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCityListBinding.inflate(inflater);
        try {
            getProvince();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter listAdapter = new ListAdapter(provinces);
        binding.lvCity.setAdapter(listAdapter);
        binding.lvCity.setOnItemClickListener((parent, view, position, id) -> {
            queryProvId = provinces.get(position).getProvinceID();
            Log.d("aaa", "onItemClick: " + provinces.get(position).getProvinceName() +  ": id =  " + id + " proID: " + provinces.get(position).getProvinceID() + "  " + queryProvId);
            choosePro = provinces.get(position).getProvinceName();
            binding.title.setText(choosePro);
            getCity(queryProvId.toString());
        });
        binding.title.setText(China);
        return binding.getRoot();

    }

    private void getProvince() throws JSONException {
        String json = getString(R.string.provinces);
        JSONArray jsonArray = new JSONArray(json);
        Log.d("aaa", "onResponse: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Province province = new Province(jsonObject.getString("name"), jsonObject.getInt("id"));
            provinces.add(province);
        }

    }

    private void getWeather(String weatherId) {
        Log.d(TAG, "getWeather: " + weatherId);
        Intent intent = new Intent(getContext(),WeatherActivity.class);
        intent.putExtra("weatherID",weatherId);
        startActivity(intent);
    }

    private void getCounty(String id_1,String id_2) {
        String url = "http://guolin.tech/api/china/";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        Log.d(TAG, "getCounty: " + url);
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getCounties(queryProvId.toString(),queryCityId.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    Log.d(TAG, "onResponse: " + jsonResponse);
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        County county = new County(jsonObject.getString("name"), jsonObject.getString("weather_id"));
                        counties.add(county);
                    }
                    Message message = new Message();
                    message.obj = counties;
                    handler2.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get county");
                getCounty(queryProvId.toString(),queryCityId.toString());
            }
        });
    }


    public void getCity(String id) {
        List<City> cities = new ArrayList<>();
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
                        City city = new City(jsonObject.getString("name"), jsonObject.getInt("id"));
                        cities.add(city);
                    }
                    Message message = new Message();
                    message.obj = cities;
                    handler1.sendMessage(message);
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get city");
                getCity(queryProvId.toString());
            }
        });
    }

}
//}
