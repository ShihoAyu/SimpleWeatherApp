package com.example.android.simpleweatherapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by t on 1/17/2018.
 */

public class HttpQueryTask extends AsyncTaskLoader<List<WeatherInfo>> {

    private static final String LOG_TAG = HttpQueryTask.class.getSimpleName();

    private String mUrl;

    public HttpQueryTask(Context context, String url) {
        super(context);

        if (NetworkUtils.checkNetworkConnection(context)) {
            mUrl = url;
        } else {
            mUrl = null;
            Toast.makeText(context, "Cannot connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public List<WeatherInfo> loadInBackground() {
        List<WeatherInfo> weathers = new ArrayList<WeatherInfo>();

        try {
            if (mUrl != null && !mUrl.isEmpty()) {
                URL mHttpUrl = NetworkUtils.createURL(mUrl);
                String data = NetworkUtils.requestData(mHttpUrl);

                Log.d(LOG_TAG, "Data retrieved: " + data);

                weathers = NetworkUtils.parseData(data);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "cannot close InputStream object");
        }

        return weathers;
    }

}
