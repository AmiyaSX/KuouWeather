package com.example.kuouweather.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuouweather.R;
import com.example.kuouweather.bean.Province;

import java.util.ArrayList;
import java.util.List;

public class MListAdapter extends BaseAdapter {

    private final List<Province> provinces;

    public MListAdapter(List<Province> provinces) {
        this.provinces = provinces;
    }

    @Override
    public int getCount() {
        return provinces.size();
    }

    @Override
    public Object getItem(int position) {
        return provinces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return provinces.get(position).getProvinceID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder")
        View view = View.inflate(parent.getContext(), R.layout.list_item, null);
        TextView textView = view.findViewById(R.id.name);
        textView.setText(provinces.get(position).getProvinceName());
        return view;
    }

}
