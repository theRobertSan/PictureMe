package com.example.pictureme.views.drawBar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pictureme.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setElevation(0);
    }
}