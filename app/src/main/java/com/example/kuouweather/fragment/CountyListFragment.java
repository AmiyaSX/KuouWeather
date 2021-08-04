package com.example.kuouweather.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.kuouweather.WeatherActivity;
import com.example.kuouweather.adapter.CountyListAdapter;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.databinding.FragmentCountyListBinding;
import com.example.kuouweather.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountyListFragment extends Fragment {

    private FragmentCountyListBinding binding;

    private String selectCityID;

    private String selectProID;

    private String weatherId;

    private List<County> counties = new ArrayList<>();

    private int cnt = 0;


    public static CountyListFragment newInstance(String selectCityID, String selectProID) {
        CountyListFragment countyListFragment = new CountyListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectCityID", selectCityID);
        bundle.putString("selectProID", selectProID);
        countyListFragment.setArguments(bundle);
        return countyListFragment;
    }

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
        toastIfo();

        if (getCountyInDatabase()) {
            binding.lvCounty.setAdapter(new CountyListAdapter(counties));
            return;
        }

        /*利用请求工具类进行请求*/
        Call<List<County>> call = HttpUtil.Httpservice().getCounties(selectProID, selectCityID);
        call.enqueue(new Callback<List<County>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<List<County>> call, @NotNull Response<List<County>> response) {
                if (response.isSuccessful()) {
                    cnt = 0;
                    /*拿到retrofit内部转换好的对象*/
                    counties = response.body();

                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> binding.lvCounty.setAdapter(new CountyListAdapter(counties)));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<County>> call, @NotNull Throwable t) {
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

    private void toastIfo() {
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
    }

    private void getWeather(String weatherId) {
        Intent intent = new Intent(getContext(), WeatherActivity.class);
        intent.putExtra("weatherID", weatherId);
        startActivity(intent);
    }
}
