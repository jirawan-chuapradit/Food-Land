package com.example.jugjig.foodland;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.HistoryFragment;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;

public class CusMainActivity extends AppCompatActivity {

//    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
//                    HomeFragment home = new HomeFragment();
//                        home.setArguments(getArguments());
                   getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, new HistoryFragment())
                            .commit();
                    Log.d("CUSTOMER", "GOTO  HISTORY");
                    return true;
                case R.id.navigation_restaurants:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, new HomeFragment())
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

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cus_main_view,
                            new HomeFragment()).commit();
//            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
    }

}
