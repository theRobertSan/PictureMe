package com.example.pictureme.views.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureme.viewmodel.PicmeListViewModel;
import com.example.pictureme.viewmodel.AuthViewModel;
import com.example.pictureme.views.MainActivity;
import com.example.pictureme.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);

        // Go to register
        setUpGoToRegister();

        // Login
        setUpLogin();

        // Hide header bar
        getSupportActionBar().hide();
    }

    private void setUpGoToRegister() {
        TextView register = findViewById(R.id.text_register);

        register.setOnClickListener(view -> {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(myIntent);
        });
    }

    private void setUpLogin() {
        Button login = findViewById(R.id.button_login);

        PicmeListViewModel picmeListViewModel = new ViewModelProvider(this).get(PicmeListViewModel.class);

        authViewModel.getLoggedInLiveData().observe(this, loggedIn -> {
            if (loggedIn) {
                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(myIntent);
            } else {
                Toast t = Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        login.setOnClickListener(view -> {
            authViewModel.login(email.getText().toString(), password.getText().toString());
        });
    }
}