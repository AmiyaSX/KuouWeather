package com.example.kuouweather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;
import com.example.kuouweather.database.DBOpenHelper;
import com.example.kuouweather.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.kuouweather.R.string.China;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        createDatabase();   /*初始化数据库*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showCityListFragment();
    }

    private void showCityListFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.city_content,new CityListFragment(), "CityListFragment").commit();

    }

    public void createDatabase() {
        SQLiteOpenHelper dbOpenHelper = DBOpenHelper.getInstance(this);
        SQLiteDatabase readableDatabase = dbOpenHelper.getReadableDatabase();
    }


}