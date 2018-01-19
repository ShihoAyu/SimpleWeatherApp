package com.example.android.simpleweatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String BASE_QUERY_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String AUTHORITY = "e81cc0aeb1ec62bb64454c103d60f5e5";

    private EditText editSearchCity;
    private LoaderManager loaderManager;
    private String units;

    private FusedLocationProviderClient locationProviderClient;


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
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new HttpQueryTask(this, args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(LOG_TAG, "Data retrieved: " + data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    private void search() {
        String city = editSearchCity.getText().toString();
        String queryUrl = createQuery(city);

        Bundle inputData = new Bundle();
        inputData.putString("url", queryUrl);

        loaderManager.initLoader(1, inputData, this);
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

    private String createQuery(@NonNull String latitude, @NonNull String longitude) {
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
        int unitsValue = getSharedPreferences(getString(R.string.query_setting), MODE_PRIVATE).getInt(getString(R.string.units_key), 0);

        Log.d(LOG_TAG, "Units value: " + unitsValue);

        switch (unitsValue) {
            case SettingActivity.UNIT_KELVIN:
                units = "standard";
                break;
            case SettingActivity.UNIT_CELSIUS:
                units = "metric";
                break;
        }
    }

}
