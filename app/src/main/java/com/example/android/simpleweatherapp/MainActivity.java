package com.example.android.simpleweatherapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by t on 1/23/2018.
 */

public class MainActivity extends AppCompatActivity {

    private Fragment cardFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardFragment = new Fragments.CardFragment();
        cardFragment.setRetainInstance(true);

        getFragmentManager().beginTransaction()
                .add(R.id.main_current_container, cardFragment, "card").commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((Fragments.CardFragment) cardFragment).startLoader("new york");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
}
