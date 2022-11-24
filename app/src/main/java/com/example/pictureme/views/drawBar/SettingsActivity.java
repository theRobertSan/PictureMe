package com.example.pictureme.views.drawBar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pictureme.R;
import com.example.pictureme.views.MainActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setElevation(0);


        Button btnchangepwd = findViewById(R.id.buttonchangepwd);
        btnchangepwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent;
                myIntent = new Intent(SettingsActivity.this, SettingsChangePwdActivity.class);
                startActivity(myIntent);
                System.out.println("AAA");
            }
        });


    }
}