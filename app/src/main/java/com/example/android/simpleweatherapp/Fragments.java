package com.example.android.simpleweatherapp;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.util.List;

/**
 * Created by kirig on 1/24/2018.
 */

public class Fragments {

    private Fragments() {}


    public static class CardFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {

        public CardFragment() { }

        public void startLoader(String city) {
            Bundle bundle = new Bundle();
            bundle.putString("url", NetworkUtils.createQuery(getActivity(), city, "weather"));
            getLoaderManager().restartLoader(10, bundle, this);
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.card_weather, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public Loader<List<WeatherInfo>> onCreateLoader(int id, Bundle args) {
            return new HttpQueryTask(getActivity(), args.getString("url"));
        }

        @Override
        public void onLoadFinished(Loader<List<WeatherInfo>> loader, List<WeatherInfo> data) {
            if (data != null && !data.isEmpty()) {
                ((TextView) getView().findViewById(R.id.card_weather_main)).setText(data.get(0).getWeatherDescription());
                ((TextView) getView().findViewById(R.id.card_weather_temp)).setText(String.valueOf(data.get(0).getTemp()));
            }
        }

        @Override
        public void onLoaderReset(Loader<List<WeatherInfo>> loader) { }
    }


    public static class ForecastListFragment extends ListFragment {

        public ForecastListFragment() { }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ListView listView = getListView();
            listView.setDivider(null);
            listView.setDividerHeight(0);
        }
    }

}
