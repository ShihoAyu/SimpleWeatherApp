package com.example.android.simpleweatherapp;

import android.os.Bundle;

import java.text.SimpleDateFormat;

/**
 * Created by kirig on 1/21/2018.
 */

public class WeatherInfo {
    public static final String KEY_TEMP = "temp";
    public static final String KEY_TEMP_MIN = "temp_min";
    public static final String KEY_TEMP_MAX = "temp_max";
    public static final String KEY_WEATHER_MAIN = "main";
    public static final String KEY_WEATHER_DESCRIPTION = "description";
    public static final String KEY_TIME_UNIX = "dt";


    private double tempMin, tempMax, temp;
    private String weatherMain, weatherDescription, time;

    public WeatherInfo(Bundle bundle) {
        temp = bundle.getDouble(KEY_TEMP);
        tempMin = bundle.getDouble(KEY_TEMP_MIN);
        tempMax = bundle.getDouble(KEY_TEMP_MAX);
        weatherMain = bundle.getString(KEY_WEATHER_MAIN);
        weatherDescription = bundle.getString(KEY_WEATHER_DESCRIPTION);
        long timeUnix = bundle.getLong(KEY_TIME_UNIX) * 1000L;

        SimpleDateFormat format = new SimpleDateFormat("hh:mm, MM/dd/yyyy");
        time = format.format(timeUnix);
    }


    public double getTemp() {
        return temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getTime() {
        return time;
    }
}
