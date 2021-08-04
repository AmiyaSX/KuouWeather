package com.example.kuouweather.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kuouweather.MainActivity;
import com.example.kuouweather.adapter.CityListAdapter;
import com.example.kuouweather.bean.City;
import com.example.kuouweather.databinding.FragmentCityListBinding;
import com.example.kuouweather.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityListFragment extends Fragment {

    private FragmentCityListBinding binding;

    private String selectProID;

    private String selectCityID;

    private MainActivity.ChangeToCountyFragment changeToCountyFragment;

    private List<City> cities = new ArrayList<>();

    private int cnt = 0;


    public static CityListFragment newInstance(String selectProID, MainActivity.ChangeToCountyFragment changeToCountyFragment) {
        CityListFragment cityListFragment = new CityListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("selectProID", selectProID);
        cityListFragment.setArguments(bundle);
        cityListFragment.changeToCountyFragment = changeToCountyFragment;
        return cityListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        assert getArguments() != null;
        this.selectProID = getArguments().getString("selectProID");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCityListBinding.inflate(inflater);
        binding.lvCity.setAdapter(new CityListAdapter(cities));
        binding.lvCity.setOnItemClickListener((parent, view, position, id) -> {
            selectCityID = cities.get(position).getCityID();
            changeToCountyFragment.showCountyListFragment();
        });
        getCity();
        return binding.getRoot();
    }

    public void getCity() {
        toastIfo();
        if (getCityInDatabase()) {
            binding.lvCity.setAdapter(new CityListAdapter(cities));
            return;
        }
        /*利用请求工具类进行请求*/
        Call<List<City>> call = HttpUtil.Httpservice().getCities(selectProID);

        call.enqueue(new Callback<List<City>>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(@NotNull Call<List<City>> call, @NotNull Response<List<City>> response) {
                if (response.isSuccessful()) {
                    cnt = 0;
                    /*拿到retrofit内部转换好的对象*/
                    cities = response.body();
                    /*回到主线程更新UI*/
                    Objects.requireNonNull(getActivity()).runOnUiThread(() -> binding.lvCity.setAdapter(new CityListAdapter(cities)));
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<City>> call, @NotNull Throwable t) {
                if (cnt < 6) getCity();
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

    public String getSelectProID() {
        return selectProID;
    }

    public String getSelectCityID() {
        return selectCityID;
    }
}