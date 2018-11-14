package com.example.jugjig.foodland;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.HistoryFragment;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;

public class CusMainActivity extends AppCompatActivity {

    HomeFragment homeFragment = new HomeFragment();
    HistoryFragment historyFragment = new HistoryFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                   getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, historyFragment)
                            .commit();
                    Log.d("CUSTOMER", "GOTO  HISTORY");
                    return true;
                case R.id.navigation_restaurants:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, homeFragment)
                            .commit();
                    Log.d("CUSTOMER", "GOTO  Home");
                    return true;
                case R.id.navigation_profile:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, new CusViewProfileFragment())
                            .commit();
                    Log.d("CUSTOMER", "GOTO  Profile Customer");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.cus_bottom_nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //home click
        Button homeBtn = findViewById(R.id.homeBtn);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cus_main_view, homeFragment)
                        .commit();
                Log.d("CUSTOMER", "GOTO  Home");
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cus_main_view,
                            homeFragment).commit();
            navigation.setSelectedItemId(R.id.navigation_restaurants);
        }
    }

}
