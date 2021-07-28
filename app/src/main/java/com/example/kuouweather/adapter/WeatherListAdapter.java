package com.example.kuouweather.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.kuouweather.bean.Weather;
import com.example.kuouweather.databinding.ForecastItemBinding;
import java.util.List;

public class WeatherListAdapter extends BaseAdapter {
    private final List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecastList;

    public WeatherListAdapter(List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }


    @Override
    public int getCount() {
        Log.d("aaa", "getCount: " +dailyForecastList.size());
        return dailyForecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return dailyForecastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Weather.HeWeatherDTO.DailyForecastDTO dailyForecast = dailyForecastList.get(position);
        @SuppressLint("ViewHolder")
        ForecastItemBinding binding = ForecastItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        Log.d("aaa", "getView: ");
        binding.date.setText(dailyForecast.getDate());
        binding.cond.setText(dailyForecast.getCond().getTxtD());
        binding.maxTmp.setText(dailyForecast.getTmp().getMax());
        binding.minTmp.setText(dailyForecast.getTmp().getMin());
        return binding.getRoot();
    }
}
