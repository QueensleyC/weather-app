package com.example.location.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.location.R;
import com.example.location.fragments.MapFragment;
import com.example.location.fragments.WeatherFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Dashboard extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.weather, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WeatherFragment()).commit();

        bottomMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity(); //closes application
    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.weather:
                        fragment = new WeatherFragment();
                        break;
                    case R.id.maps:
                        fragment = new MapFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}