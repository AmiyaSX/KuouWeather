package com.example.kuouweather;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.kuouweather.fragment.CityListFragment;
import com.example.kuouweather.fragment.CountyListFragment;
import com.example.kuouweather.fragment.ProvinceListFragment;

import org.litepal.LitePal;

public class MainActivity extends FragmentActivity {
    private ProvinceListFragment provinceListFragment;
    private CityListFragment cityListFragment;
    private CountyListFragment countyListFragment;
    private final FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        LitePal.getDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProvinceFragment();
    }

    private void showProvinceFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        provinceListFragment = new ProvinceListFragment(() -> {
            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.hide(provinceListFragment);
            cityListFragment = new CityListFragment(provinceListFragment.getSelectProID(), new ChangeToCountyFragment() {
                @Override
                public void showCountyListFragment() {
                    FragmentTransaction ft1 = fm.beginTransaction();
                    ft1.hide(cityListFragment);
                    countyListFragment = new CountyListFragment(cityListFragment.getSelectCityID(), cityListFragment.getSelectProID());
                    ft1.replace(R.id.content, countyListFragment, "CityListFragment").addToBackStack("").commit();
                }

            });
            ft1.replace(R.id.content, cityListFragment, "CityListFragment").addToBackStack("").commit();
        });
        ft.replace(R.id.content, provinceListFragment, "CityListFragment").commit();
    }

    public interface ChangeToCityFragment {
        void showCityListFragment();

    }

    public interface ChangeToCountyFragment {
        void showCountyListFragment();

    }

}