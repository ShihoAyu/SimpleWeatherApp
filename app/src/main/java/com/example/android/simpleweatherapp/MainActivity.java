package com.example.android.simpleweatherapp;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BASE_QUERY_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String AUTHORITY = "e81cc0aeb1ec62bb64454c103d60f5e5";

    private EditText editSearchCity;
    private LoaderManager loaderManager;
    private String units;

    private FusedLocationProviderClient locationProviderClient;

    WeatherListAdapter WListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUnits();

        editSearchCity = (EditText) findViewById(R.id.edit_search_city);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        Log.d(LOG_TAG, "permission check: " + permissionCheck);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        String latitude = String.valueOf(location.getLatitude());
                        String longitude = String.valueOf(location.getLongitude());

                        Bundle bundle = new Bundle();
                        bundle.putString("url", createQuery(latitude, longitude));

                        loaderManager.initLoader(2, bundle, MainActivity.this);
                    }
                }
            });
        }

        List<WeatherInfo> weatherInfoList = new ArrayList<>();
        WListAdapter = new WeatherListAdapter(this, weatherInfoList);

        ListView resultList = (ListView) findViewById(R.id.list_result);
        resultList.setAdapter(WListAdapter);

        loaderManager = getSupportLoaderManager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getUnits();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                search();
                break;
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
        }
    }

    @Override
    public void onLoaderReset(Loader<List<WeatherInfo>> loader) {

    }

    private void search() {
        String city = editSearchCity.getText().toString();
        String queryUrl = createQuery(city);

        Bundle inputData = new Bundle();
        inputData.putString("url", queryUrl);

        loaderManager.restartLoader(1, inputData, this);
    }

    private String createQuery(String city) {
        String queryUrl = null;

        if (city != null && !city.isEmpty()) {
            Uri.Builder builder = (Uri.parse(BASE_QUERY_URL)).buildUpon()
                    .appendQueryParameter("q", city)
                    .appendQueryParameter("units", units)
                    .appendQueryParameter("appid", AUTHORITY);
            queryUrl = builder.toString();

            Log.d(LOG_TAG, "query URL created: " + queryUrl);
        }

        return queryUrl;
    }

    private String createQuery(String latitude, String longitude) {
        String queryUrl;

        Uri.Builder builder = Uri.parse(BASE_QUERY_URL).buildUpon()
                .appendQueryParameter("lat", latitude)
                .appendQueryParameter("lon", longitude)
                .appendQueryParameter("units", units)
                .appendQueryParameter("appid", AUTHORITY);

        queryUrl = builder.toString();

        Log.d(LOG_TAG, "query URL created: " + queryUrl);

        return queryUrl;
    }

    private void getUnits() {
        units = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.units_key), "standard");

        Log.d(LOG_TAG, "Units value: " + units);
    }

}
