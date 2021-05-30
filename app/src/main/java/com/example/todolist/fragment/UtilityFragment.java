package com.example.todolist.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.todolist.activity.BMIActivity;
import com.example.todolist.R;
import com.example.todolist.activity.CovidActivity;
import com.example.todolist.activity.MapActivity;
import com.example.todolist.activity.RandomCat;
import com.example.todolist.activity.TableSheetActivity;
import com.example.todolist.activity.WeatherActivity;

public class UtilityFragment extends Fragment {

    private ImageView imgBmi;
    private ImageView imgCat;
    private ImageView imgMap;
    private ImageView imgWeather;
    private ImageView imgCovid;
    private ImageView imgTable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_utility, container, false);
        init(rootView);

        imgBmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), BMIActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });
        imgCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), RandomCat.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), MapActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });

        imgWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), WeatherActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });

        imgCovid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), CovidActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });

        imgTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t = new Intent(getActivity(), TableSheetActivity.class);
                t.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t);
            }
        });
        return rootView;
    }

    public void init(View view) {

        imgBmi = view.findViewById(R.id.imgBmi);
        imgCat = view.findViewById(R.id.imgCat);
        imgMap = view.findViewById(R.id.imgMap);
        imgWeather = view.findViewById(R.id.imgWeather);
        imgCovid = view.findViewById(R.id.imgCovid);
        imgTable = view.findViewById(R.id.imgTable);
    }
}