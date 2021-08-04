package com.example.kuouweather;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
        LitePal.deleteAll("province");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProvinceFragment();
    }

    private void showProvinceFragment() {
        /*这里已经回答过了，持有fragment应该没什么关系，方便进行界面转换，但是要小心fragment传参构造的坑*/
        FragmentTransaction ft = fm.beginTransaction();
        provinceListFragment = ProvinceListFragment.newInstance(() -> {
            FragmentTransaction ft1 = fm.beginTransaction();
            ft1.hide(provinceListFragment);
            cityListFragment = CityListFragment.newInstance(provinceListFragment.getSelectProID(), () -> {
                FragmentTransaction ft11 = fm.beginTransaction();
                ft11.hide(cityListFragment);
                countyListFragment = CountyListFragment.newInstance(cityListFragment.getSelectCityID(), cityListFragment.getSelectProID());
                ft11.replace(R.id.content, countyListFragment, "CityListFragment").addToBackStack("").commit();
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