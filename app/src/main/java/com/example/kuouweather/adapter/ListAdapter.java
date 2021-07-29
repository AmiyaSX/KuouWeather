package com.example.kuouweather.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kuouweather.R;
import com.example.kuouweather.bean.City;
import com.example.kuouweather.bean.County;
import com.example.kuouweather.bean.Province;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private final List<Province> list;

    public ListAdapter(List<Province> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = View.inflate(parent.getContext(), R.layout.list_item,null);
        TextView tv = view.findViewById(R.id.name);
        tv.setText(list.get(position).getProvinceName());
        return view;
    }
}