package com.example.android.simpleweatherapp.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.simpleweatherapp.R;
import com.example.android.simpleweatherapp.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by t on 1/17/2018.
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_QUERY_URL = "http://api.openweathermap.org/data/2.5";
    private static final String AUTHORITY = "e81cc0aeb1ec62bb64454c103d60f5e5";

    private NetworkUtils() {}

    public static URL createURL(@NonNull String url) {
        Log.d(LOG_TAG, "String form URL entered: " + url);

        URL urlObject = null;

        try {
            urlObject = new URL(url);
            Log.d(LOG_TAG, "URL object is created: " + urlObject);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlObject;
    }

    public static String requestData(URL url) throws IOException {
        String data = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        Scanner scanner = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            Log.d(LOG_TAG, "HttpURLConnection response code: " + httpURLConnection.getResponseCode());

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                if (scanner.hasNext()) {
                    data = scanner.next();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) inputStream.close();
            if (scanner != null) scanner.close();
        }

        return data;
    }

    public static List<WeatherInfo> parseData(String data) {
        if (data == null || data.isEmpty()) return null;

        List<WeatherInfo> result = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(data);
            if (root.has("list")) {
                JSONArray list = root.getJSONArray("list");

                for (int i=0; i<list.length(); ++i) result.add(getWeatherInfo(list.getJSONObject(i)));
            } else {
                result.add(getWeatherInfo(root));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static WeatherInfo getWeatherInfo(JSONObject dataObject) throws JSONException {
        long time = dataObject.getLong(WeatherInfo.KEY_TIME_UNIX);

        JSONObject main = dataObject.getJSONObject("main");

        double temp = main.getDouble(WeatherInfo.KEY_TEMP);
        double tempMin = main.getDouble(WeatherInfo.KEY_TEMP_MIN);
        double tempMAX = main.getDouble(WeatherInfo.KEY_TEMP_MAX);

        JSONObject weather = dataObject.getJSONArray("weather").getJSONObject(0);

        String weatherMain = weather.getString(WeatherInfo.KEY_WEATHER_MAIN);
        String weatherDescription = weather.getString(WeatherInfo.KEY_WEATHER_DESCRIPTION);

        Bundle bundle = new Bundle();
        bundle.putDouble(WeatherInfo.KEY_TEMP, temp);
        bundle.putDouble(WeatherInfo.KEY_TEMP_MIN, tempMin);
        bundle.putDouble(WeatherInfo.KEY_TEMP_MAX, tempMAX);
        bundle.putString(WeatherInfo.KEY_WEATHER_MAIN, weatherMain);
        bundle.putString(WeatherInfo.KEY_WEATHER_DESCRIPTION, weatherDescription);
        bundle.putLong(WeatherInfo.KEY_TIME_UNIX, time);

        return new WeatherInfo(bundle);
    }

    public static String createQuery(Context context, String city, String type) {
        String queryUrl = null;

        if (city != null && !city.isEmpty()) {
            Uri.Builder builder = (Uri.parse(BASE_QUERY_URL)).buildUpon()
                    .appendPath(type)
                    .appendQueryParameter("q", city)
                    .appendQueryParameter("units", PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.units_key), "standard"))
                    .appendQueryParameter("appid", AUTHORITY);
            queryUrl = builder.toString();

            Log.d(LOG_TAG, "query URL created: " + queryUrl);
        }

        return queryUrl;
    }

    public static String createQuery(Context context, String latitude, String longitude, String type) {
        String queryUrl;

        Uri.Builder builder = Uri.parse(BASE_QUERY_URL).buildUpon()
                .appendPath(type)
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude)
                .appendQueryParameter("units", PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.units_key), "standard"))
                .appendQueryParameter("appid", AUTHORITY);

        queryUrl = builder.toString();

        Log.d(LOG_TAG, "query URL created: " + queryUrl);

        return queryUrl;
    }
}
