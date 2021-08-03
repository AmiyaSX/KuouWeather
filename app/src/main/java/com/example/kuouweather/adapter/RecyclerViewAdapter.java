package com.example.kuouweather.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kuouweather.R;
import com.example.kuouweather.bean.Weather;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerHolder> {
    private final Context context;
    String TAG = "aaa";
    private final List<Weather.HeWeatherDTO.DailyForecastDTO> dailyForecastList = new ArrayList<>();

    public RecyclerViewAdapter(RecyclerView recyclerView, List<Weather.HeWeatherDTO.DailyForecastDTO> data) {
        this.context = recyclerView.getContext();
        setData(data);
    }
    public void setData(List<Weather.HeWeatherDTO.DailyForecastDTO> data) {
        if (null != data) {
            this.dailyForecastList.clear();
            this.dailyForecastList.addAll(data);
            Log.d(TAG, "setData: " + dailyForecastList.size());
            notifyDataSetChanged();
        }
    }
    @NonNull
    @NotNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.forecast_item,parent,false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerHolder holder, int position) {
        holder.tvDate.setText(dailyForecastList.get(position).getDate());
        holder.tvWeather.setText(dailyForecastList.get(position).getCond().getTxtD());
        holder.tvMax.setText(dailyForecastList.get(position).getTmp().getMax());
        holder.tvMin.setText(dailyForecastList.get(position).getTmp().getMin());
    }


    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }

    static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvWeather;
        TextView tvMax;
        TextView tvMin;
        public RecyclerHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.date);
            tvWeather = itemView.findViewById(R.id.cond);
            tvMax = itemView.findViewById(R.id.max_tmp);
            tvMin = itemView.findViewById(R.id.min_tmp);
        }
    }
}
