package com.example.pictureme.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.pictureme.viewmodel.PicmeListViewModel;
import com.example.pictureme.viewmodel.AuthViewModel;
import com.example.pictureme.views.camera.CameraActivity;
import com.example.pictureme.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FloatingActionButton cameraFab;

    private AuthViewModel userViewModel;
    private PicmeListViewModel picmeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picmeListViewModel = new ViewModelProvider(this).get(PicmeListViewModel.class);
        picmeListViewModel.loadUserPicmes();

//        // Hide header bar
//        getSupportActionBar().hide();
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);

        setUpBottomNav();
        setUpCameraButton();

        // If permission haven't been granted yet
        if (!checkPermissions()) {
            requestPermission();
        }
    }

    private void setUpCameraButton() {
        cameraFab = findViewById(R.id.button_picme);
        cameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpBottomNav() {
        // Bottom Nav Bar Configurations
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    private boolean checkPermissions() {
        int resultLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int resultCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        System.out.println(resultLocation + " --- " + resultCamera);
        return resultLocation == PackageManager.PERMISSION_GRANTED && resultCamera == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean locationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (!(locationAccepted && cameraAccepted)) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    (dialog, which) -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                                                    PERMISSION_REQUEST_CODE);
                                        }
                                    });
                            return;
                        }
                    }

                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}