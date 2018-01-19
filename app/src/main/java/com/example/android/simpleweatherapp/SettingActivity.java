package com.example.android.simpleweatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {
    private static final String LOG_TAG = SettingActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;

    public static final int UNIT_KELVIN = 0;
    public static final int UNIT_CELSIUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sharedPreferences = getSharedPreferences(getString(R.string.query_setting), MODE_PRIVATE);

        RadioGroup groupUnits = (RadioGroup) findViewById(R.id.radio_group_units);

        groupUnits.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setUnit(checkedId);
            }
        });
    }

    private void setUnit(int checkedId) {
        Log.d(LOG_TAG, "Radio button set has changed: " + checkedId);
        SharedPreferences.Editor sPreferenceEditor = sharedPreferences.edit();

        switch (checkedId) {
            case R.id.radio_kelvin:
                sPreferenceEditor.putInt(getString(R.string.units_key), UNIT_KELVIN);
                break;
            case R.id.radio_celsius:
                sPreferenceEditor.putInt(getString(R.string.units_key), UNIT_CELSIUS);
                break;
        }

        sPreferenceEditor.apply();
    }
}
