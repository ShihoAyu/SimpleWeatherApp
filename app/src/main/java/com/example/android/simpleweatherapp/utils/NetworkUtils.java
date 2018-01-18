package com.example.android.simpleweatherapp.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by t on 1/17/2018.
 */

public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

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
}
