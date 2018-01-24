package com.example.android.simpleweatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kirig on 1/21/2018.
 */

public class WeatherListAdapter extends ArrayAdapter<WeatherInfo> {

    WeatherListAdapter(Context context, List<WeatherInfo> weathers) {
        super(context, 0, weathers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.weather_item, parent, false);
        }

        WeatherInfo weatherInfo = getItem(position);

        ((TextView) listView.findViewById(R.id.item_1_weather_main)).setText(weatherInfo.getWeatherMain());
        ((TextView) listView.findViewById(R.id.item_1_weather_temp)).setText(String.valueOf(weatherInfo.getTemp()));
        ((TextView) listView.findViewById(R.id.item_1_weather_time)).setText(weatherInfo.getTime());

        return listView;
    }
}
