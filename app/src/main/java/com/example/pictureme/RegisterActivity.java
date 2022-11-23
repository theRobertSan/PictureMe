package com.example.pictureme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Go to login
        setUpGoToLogin();
        
    }

    private void setUpGoToLogin() {
        TextView register = findViewById(R.id.text_login);

        register.setOnClickListener(view -> {
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(myIntent);
        });
    }
}