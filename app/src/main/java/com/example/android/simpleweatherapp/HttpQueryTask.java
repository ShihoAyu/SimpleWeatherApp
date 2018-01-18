package com.example.android.simpleweatherapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by t on 1/17/2018.
 */

public class HttpQueryTask extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = HttpQueryTask.class.getSimpleName();
    private String mUrl;

    public HttpQueryTask(Context context, String url) {
        super(context);
        mUrl = url;
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
    public String loadInBackground() {
        String data = "";

        try {
            if (mUrl != null && !mUrl.isEmpty()) {
                URL mHttpUrl = NetworkUtils.createURL(mUrl);
                data = NetworkUtils.requestData(mHttpUrl);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "cannot close InputStream object");
        }

        return data;
    }
}
