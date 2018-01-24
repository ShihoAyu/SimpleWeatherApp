package com.example.android.simpleweatherapp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.simpleweatherapp.utils.NetworkUtils;

import java.util.List;

/**
 * Created by t on 1/23/2018.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<WeatherInfo>> {
    Fragment cardFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            cardFragment = new MainFragment();

            FragmentTransaction ft = getFragmentManager().beginTransaction();

            ft.add(R.id.container_card, cardFragment, "card").commit();
        }
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
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            case R.id.menu_setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<WeatherInfo>> onCreateLoader(int id, Bundle args) {
        return new HttpQueryTask(this, args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<List<WeatherInfo>> loader, List<WeatherInfo> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<WeatherInfo>> loader) {

    }
}
