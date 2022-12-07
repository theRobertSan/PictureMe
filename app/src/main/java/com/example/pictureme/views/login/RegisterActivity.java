package com.example.pictureme.views.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pictureme.R;
import com.example.pictureme.viewmodel.PicmeListViewModel;
import com.example.pictureme.viewmodel.AuthViewModel;
import com.example.pictureme.views.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText passwordConfirmation;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.edit_username);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        passwordConfirmation = findViewById(R.id.edit_password_confirmation);

        // Go to login
        setUpGoToLogin();

        setUpRegister();

        // Hide header bar
        getSupportActionBar().hide();
    }

    private void setUpRegister() {
        Button signInButton = findViewById(R.id.button_signup);

        PicmeListViewModel picmeListViewModel = new ViewModelProvider(this).get(PicmeListViewModel.class);

        authViewModel.getLoggedInLiveData().observe(this, loggedIn -> {
            if (loggedIn) {
                // Load user picmes
                picmeListViewModel.loadUserPicmes();

                Intent myIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(myIntent);
            } else {
                Toast t = Toast.makeText(this, "Account already exists", Toast.LENGTH_SHORT);
                t.show();
            }
        });

        signInButton.setOnClickListener(view -> {
            if (authViewModel.matchingPasswords(password.getText().toString(), passwordConfirmation.getText().toString())) {
                authViewModel.register(email.getText().toString(), password.getText().toString(), username.getText().toString());
            } else {
                Toast t = Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT);
                t.show();
            }
        });
    }

    private void setUpGoToLogin() {
        TextView register = findViewById(R.id.text_login);

        register.setOnClickListener(view -> {
            Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(myIntent);
        });
    }
}