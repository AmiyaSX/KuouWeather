package com.example.kuouweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.databinding.FragmentCityListBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.kuouweather.R.string.China;


public class CityListFragment extends Fragment {
    private String queryProvId;
    private String queryCityId;
    private String weatherId;
    private FragmentCityListBinding binding;
    private final List<Province> provinces = new ArrayList<>();
    //    private final ListAdapter listAdapter = new ListAdapter(provinces);
    private Context context;

    public CityListFragment() {

    }

    String TAG = "aaa";

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("aaa", "onCreate: ");
//        Objects.requireNonNull(getActivity()).runOnUiThread(() -> listAdapter.notifyDataSetChanged());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCityListBinding.inflate(inflater);
        Log.d(TAG, "onCreateView: ");
        try {
            getProvince();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter listAdapter = new ListAdapter(provinces);
        binding.lvCity.setAdapter(listAdapter);
        binding.lvCity.setOnItemClickListener((parent, view, position, id) -> {
            queryProvId = String.valueOf(provinces.get(position).getProvinceID());
            Log.d("aaa", "onItemClick: " + provinces.get(position).getProvinceName() +  ": id =  " + id + " proID: " + provinces.get(position).getProvinceID());
            getCity(queryCityId);
        });

        binding.title.setText(China);
//        Log.d(TAG, "onCreateView: " + provinces.size());
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
//
//    @Override
//    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        binding = FragmentCityListBinding.inflate(getLayoutInflater());
//        Log.d(TAG, "onActivityCreated: ");
//        binding.title.setText(China);
//        getProvince();
//    }
//
//    public void getProvince() {
//            new Thread() {
//                @Override
//                public void run() {
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl("http://guolin.tech/api/china/")
//                            .build();
//                    HttpService httpService = retrofit.create(HttpService.class);
//                    Call<ResponseBody> call = httpService.getProvinces();
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @SneakyThrows
//                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                        @Override
//                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
//                            if (response.isSuccessful()) {
//                                assert response.body() != null;
//                                String jsonResponse = response.body().string();
//                                Log.d("aaa", "onResponse: " + jsonResponse);
//                                JSONArray jsonArray = new JSONArray(jsonResponse);
//                                Log.d("aaa", "onResponse: " + jsonArray.length());
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                    Province province = new Province(jsonObject.getString("name"), jsonObject.getInt("id"));
//                                    provinces.add(province);
//                                }
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        listAdapter.notifyDataSetChanged();
//                                    }
//                                });
//                            }
//                        }
//                        @Override
//                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                            Log.d("aaa", "onFailure: " + "get province");
//                            getProvince();
//                        }
//                    });
//                }
//            }.start();
//        Log.d(TAG, "getProvince: " + provinces.size());
//        listAdapter.notifyDataSetChanged();
//
//    }
//
    private void getWeather(String weatherId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://guolin.tech/api/weather")
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getWeather(weatherId, "abc");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Log.d("aaa", "onResponse: " + response.body().toString());
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get weather");
            }
        });

    }

    private void getCounty(String id_1,String id_2) {
        List<County> counties = new ArrayList<>();
        String url = "http://guolin.tech/api/china" + "/" + id_1 + "/" + id_2 + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getCounties(queryProvId,queryCityId);
        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String jsonResponse = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        County county = new County(jsonObject.getString("name"), jsonObject.getString("weather_id"));
                        counties.add(county);
                    }
                    CountyListFragment countyListFragment = new CountyListFragment();
                    countyListFragment.binding.lvCounty.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return counties.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return counties.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View countyView = View.inflate(parent.getContext(),R.layout.list_item,null);
                            TextView tv = countyView.findViewById(R.id.name);
                            tv.setText(counties.get(position).getCountyName());
                            return countyView;
                        }
                    });
                    countyListFragment.binding.lvCounty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            weatherId = counties.get(position).getWeatherID();
                            getWeather(weatherId);
                        }
                    });
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get county");
                getCounty(queryProvId,queryCityId);
            }
        });
    }


    public void getCity(String id) {
        List<City> cities = new ArrayList<>();
        String url = "http://guolin.tech/api/china" + "/" + id + "/";
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
                    JSONArray jsonArray = new JSONArray(jsonResponse);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        City city = new City(jsonObject.getString("name"), jsonObject.getInt("id"));
                        cities.add(city);
                    }

                    binding.lvCity.setAdapter(new BaseAdapter() {

                        @Override
                        public int getCount() {
                            return cities.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return cities.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View city_view = View.inflate(parent.getContext(),R.layout.list_item,null);
                            TextView tv = city_view.findViewById(R.id.name);
                            tv.setText(cities.get(position).getCityName());
                            return city_view;
                        }
                    });
                    binding.lvCity.setOnItemClickListener((parent, view, position, id1) -> {
                        Log.d(TAG, "onResponse: " + position);
                        queryCityId = String.valueOf(cities.get(position).getCityID());
//                        getCounty(queryProvId,queryCityId);
                    });
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d("aaa", "onFailure: " + "get city");
                getCity(queryProvId);
            }
        });
    }

}
//}
