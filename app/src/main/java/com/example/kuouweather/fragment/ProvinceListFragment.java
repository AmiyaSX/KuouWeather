package com.example.kuouweather.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kuouweather.HttpService;
import com.example.kuouweather.MainActivity;
import com.example.kuouweather.adapter.ListAdapter;
import com.example.kuouweather.bean.Province;
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


public class ProvinceListFragment extends Fragment {
    private String queryProvId;
    private String selectProID;
    private int requireCnt;
    private MainActivity.ChangeToCityFragment changeToCityFragment;
    private FragmentProvinceListBinding binding;
    private List<Province> provinces = new ArrayList<>();

    public ProvinceListFragment() {
    }

    public static ProvinceListFragment newInstance(MainActivity.ChangeToCityFragment changeToCityFragment) {
        ProvinceListFragment provinceListFragment = new ProvinceListFragment();
        provinceListFragment.changeToCityFragment = changeToCityFragment;
        return provinceListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProvinceListBinding.inflate(inflater);
        try {
            requireCnt = 0;
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
            changeToCityFragment.showCityListFragment();
        });
        return binding.getRoot();

    }

    private void getProvince() throws JSONException {
        if (requireCnt < 6) {
            requireCnt++;
            Toast.makeText(requireContext(), "数据拉取中，请等待", Toast.LENGTH_SHORT).show();
        } else {
            requireCnt++;
            Toast.makeText(requireContext(), "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
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
                    requireCnt = 0;
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
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> binding.lvProvince.setAdapter(new ListAdapter(provinces)));
                }
            }

            @SneakyThrows
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                if (requireCnt < 10) getProvince();
            }
        });
    }

    private Boolean findProvinceInDatabase() {
        List<Province> provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0) {
            provinces = provinceList;
            return true;
        }
        return false;
    }

    public String getSelectProID() {
        return selectProID;
    }
}
