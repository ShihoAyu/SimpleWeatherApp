package com.example.android.simpleweatherapp;

import android.Manifest;
import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.android.simpleweatherapp.utils.NetworkUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private EditText editSearchCity;
    private LoaderManager loaderManager;

    private ListFragment listFragment;

    // private FusedLocationProviderClient locationProviderClient;

    WeatherListAdapter WListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            findViewById(R.id.button_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search();
                }
            });

            loaderManager = getSupportLoaderManager();

            editSearchCity = (EditText) findViewById(R.id.edit_search_city);

            ArrayList<WeatherInfo> weatherList = new ArrayList<>();
            WListAdapter = new WeatherListAdapter(this, weatherList);

            listFragment = new MainFragment.ForecastListFragment();
            listFragment.setListAdapter(WListAdapter);

            getFragmentManager().beginTransaction().add(R.id.container_forecast_list_2, listFragment, "forecast_list_2").commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

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

    }

    private void search() {
        String city = editSearchCity.getText().toString();
        String queryUrl = NetworkUtils.createQuery(this, city);

        Bundle inputData = new Bundle();
        inputData.putString("url", queryUrl);

        loaderManager.restartLoader(1, inputData, this);
    }


}
