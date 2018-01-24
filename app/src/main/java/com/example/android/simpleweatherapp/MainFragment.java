package com.example.android.simpleweatherapp;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.util.List;

/**
 * Created by t on 1/23/2018.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = new Bundle();
        bundle.putString("url", NetworkUtils.createQuery(getActivity(), "new york"));
        getLoaderManager().initLoader(10, bundle, MainFragment.this);
    }

    @Override
    public Loader<List<WeatherInfo>> onCreateLoader(int id, Bundle args) {
        return new HttpQueryTask(getActivity(), args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<List<WeatherInfo>> loader, List<WeatherInfo> data) {
        ((TextView) getView().findViewById(R.id.text_weather_main_2)).setText(data.get(0).getWeatherDescription());
        ((TextView) getView().findViewById(R.id.text_temp_2)).setText(String.valueOf(data.get(0).getTemp()));
    }

    @Override
    public void onLoaderReset(Loader<List<WeatherInfo>> loader) {

    }

    public static class ForecastListFragment extends ListFragment {

        public ForecastListFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }
}
