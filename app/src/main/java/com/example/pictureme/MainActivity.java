package com.example.pictureme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.pictureme.views.ExploreFragment;
import com.example.pictureme.views.FriendsFragment;
import com.example.pictureme.views.HomeFragment;
import com.example.pictureme.views.LetsgoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ExploreFragment exploreFragment = new ExploreFragment();
    LetsgoFragment letsgoFragment = new LetsgoFragment();
    HomeFragment homeFragment = new HomeFragment();
    FriendsFragment friendsFragment = new FriendsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom Nav Bar Configurations
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        // Set initial fragment to homeFragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

        // Add on item click listener for the bottom nav bar
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mi_explore:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exploreFragment).commit();
                        break;
                    case R.id.mi_letsGo:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, letsgoFragment).commit();
                        break;
                    case R.id.mi_friends:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, friendsFragment).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                        break;
                }
                return true;
            }
        });
    }
}