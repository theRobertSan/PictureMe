package com.example.pictureme.views.drawBar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pictureme.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setElevation(0);

    }
}