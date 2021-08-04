package com.example.kuouweather.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kuouweather.MainActivity;
import com.example.kuouweather.adapter.ListAdapter;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.databinding.FragmentProvinceListBinding;
import com.example.kuouweather.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProvinceListFragment extends Fragment {

    private String queryProvId;

    private String selectProID;

    private int requireCnt;

    private MainActivity.ChangeToCityFragment changeToCityFragment;

    private FragmentProvinceListBinding binding;

    private List<Province> provinces = new ArrayList<>();


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
        requireCnt = 0;
        getProvince();
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

    private void getProvince() {
        toastIfo();
        if (findProvinceInDatabase()) {
            binding.lvProvince.setAdapter(new ListAdapter(provinces));
            return;
        }

        /*利用请求工具类进行请求*/
        Call<List<Province>> call = HttpUtil.Httpservice().getProvinces();

        call.enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(@NotNull Call<List<Province>> call, @NotNull Response<List<Province>> response) {
                if (response.isSuccessful()) {
                    requireCnt = 0;
                    /*拿到retrofit内部转换好的对象*/
                    provinces = response.body();

                    Log.d("aaa", "onResponse: " + provinces.size());
                    getActivity().runOnUiThread(() -> binding.lvProvince.setAdapter(new ListAdapter(provinces)));
                }
            }

            @SneakyThrows
            @Override
            public void onFailure(@NotNull Call<List<Province>> call, @NotNull Throwable t) {
                if (requireCnt < 5) getProvince();
                else
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> Toast.makeText(requireContext(), "请求失败", Toast.LENGTH_SHORT).show());
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

    private void toastIfo() {
        if (requireCnt < 6) {
            requireCnt++;
            Toast.makeText(requireContext(), "数据拉取中，请等待", Toast.LENGTH_SHORT).show();
        } else {
            requireCnt++;
            Toast.makeText(requireContext(), "网络请求失败，请检查网络", Toast.LENGTH_SHORT).show();
        }

    }

    public String getSelectProID() {
        return selectProID;
    }
}
