package com.example.pictureme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Go to register
        setUpGoToRegister();

        // Login
        setUpGoToApp();


    }

    private void setUpGoToRegister() {
        TextView register = findViewById(R.id.text_register);

        register.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(myIntent);
        });
    }

    private void setUpGoToApp() {
        Button login = findViewById(R.id.button_login);

        login.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
        });
    }
}