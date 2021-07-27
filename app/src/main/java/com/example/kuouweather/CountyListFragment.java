package com.example.kuouweather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuouweather.databinding.FragmentCityListBinding;
import com.example.kuouweather.databinding.FragmentCountyListBinding;

import org.jetbrains.annotations.NotNull;

public class CountyListFragment extends Fragment {
    FragmentCountyListBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCountyListBinding.inflate(inflater);
        return binding.getRoot();
    }
}