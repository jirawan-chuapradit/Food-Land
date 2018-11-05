package com.example.jugjig.foodland;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;

public class RestMainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rest_nav_home:
                    Log.d("RESTAURANT", "Click Home");
                    return true;
                case R.id.rest_nav_power:
                    Log.d("RESTAURANT", "Click Power");
                    return true;
                case R.id.rest_nav_profile:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.rest_main_view, new RestViewProfileFragment())
                            .commit();
                    Log.d("RESTAURANT", "Click  Profile Restaurant");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rest_bottom_nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rest_main_view,
                            new RestViewProfileFragment()).commit();
            navigation.setSelectedItemId(R.id.rest_nav_profile);
        }
    }

}
