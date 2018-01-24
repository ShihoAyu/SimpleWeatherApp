package com.example.android.simpleweatherapp;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private EditText editSearchCity;
    private LoaderManager loaderManager;

    private ListFragment listFragment;
    private Fragment currentWeatherFragment;

    // private FusedLocationProviderClient locationProviderClient;

    WeatherListAdapter WListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        loaderManager = getLoaderManager();
        editSearchCity = (EditText) findViewById(R.id.edit_search_city);

        findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        ArrayList<WeatherInfo> weatherList = new ArrayList<>();
        WListAdapter = new WeatherListAdapter(this, weatherList);

        listFragment = new Fragments.ForecastListFragment();
        listFragment.setRetainInstance(true);
        listFragment.setListAdapter(WListAdapter);

        currentWeatherFragment = new Fragments.CardFragment();
        currentWeatherFragment.setRetainInstance(true);

        getFragmentManager().beginTransaction()
                .add(R.id.search_current_container, currentWeatherFragment, "current_weather")
                .add(R.id.search_list_container, listFragment, "search_list")
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<WeatherInfo>> onCreateLoader(int id, Bundle args) {
        return new HttpQueryTask(this, args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<List<WeatherInfo>> loader, List<WeatherInfo> weathers) {
        WListAdapter.clear();

        if (weathers != null) {
            WListAdapter.addAll(weathers);
            listFragment.setListAdapter(WListAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<WeatherInfo>> loader) {
        if (!WListAdapter.isEmpty()) {
            WListAdapter.clear();
        }
    }

    private void search() {
        String city = editSearchCity.getText().toString();
        String queryUrl = NetworkUtils.createQuery(this, city, "forecast");

        Bundle inputData = new Bundle();
        inputData.putString("url", queryUrl);

        loaderManager.restartLoader(1, inputData, this);
        ((Fragments.CardFragment) currentWeatherFragment).startLoader(city);
    }

}
