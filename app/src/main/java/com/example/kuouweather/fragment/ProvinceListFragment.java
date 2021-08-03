package com.example.kuouweather.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuouweather.HttpService;
import com.example.kuouweather.MainActivity;
import com.example.kuouweather.adapter.CityListAdapter;
import com.example.kuouweather.adapter.CountyListAdapter;
import com.example.kuouweather.adapter.ListAdapter;
import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.databinding.FragmentCityListBinding;
import com.example.kuouweather.databinding.FragmentProvinceListBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
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


public class ProvinceListFragment extends Fragment {
    private String queryProvId;
    private String choosePro;
    private String selectProID;
    private MainActivity.ChangeToCityFragment changeToCityFragment;
    private FragmentProvinceListBinding binding;
    private List<Province> provinces = new ArrayList<>();

    public ProvinceListFragment() {
    }

    public ProvinceListFragment(MainActivity.ChangeToCityFragment changeToCityFragment) {
        this.changeToCityFragment = changeToCityFragment;
    }

    String TAG = "aaa";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProvinceListBinding.inflate(inflater);
        try {
            getProvince();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter listAdapter = new ListAdapter(provinces);
        binding.lvProvince.setAdapter(listAdapter);
        /*设置点击监听*/
        binding.lvProvince.setOnItemClickListener((parent, view, position, id) -> {
            queryProvId = provinces.get(position).getProvinceID();
            selectProID = queryProvId;
            Log.d("aaa", "onItemClick: size =" + provinces.size() + "   " + provinces.get(position).getProvinceName() + ": id =  " + id + " proID: " + provinces.get(position).getProvinceID() + "  " + queryProvId);
            choosePro = provinces.get(position).getProvinceName();
            changeToCityFragment.showCityListFragment();
        });
        return binding.getRoot();

    }

    private void getProvince() throws JSONException {
//        String json = getString(R.string.provinces);
        if (findProvinceInDatabase()) {
            binding.lvProvince.setAdapter(new ListAdapter(provinces));
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://guolin.tech/api/china/")
                .build();
        HttpService httpService = retrofit.create(HttpService.class);
        Call<ResponseBody> call = httpService.getProvinces();
        call.enqueue(new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String jsonBody = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonBody);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Province province = new Province(jsonObject.getString("name"), jsonObject.getString("id"));
                            provinces.add(province);
                            province.save();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.lvProvince.setAdapter(new ListAdapter(provinces));
                        }
                    });
                }
            }

            @SneakyThrows
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: ");
                getProvince();
            }
        });
    }

    private Boolean findProvinceInDatabase() {
        List<Province> provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0) {
            Log.d(TAG, "findProvinceInDatabase: ");
            provinces = provinceList;
            return true;
        }
        return false;
    }

    public String getSelectProID() {
        return selectProID;
    }
}
