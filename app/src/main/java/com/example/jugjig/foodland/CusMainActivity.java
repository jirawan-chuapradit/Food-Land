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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.HistoryFragment;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;

public class CusMainActivity extends AppCompatActivity {

    HomeFragment homeFragment = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_main);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.cus_bottom_nav_bar);
        final ImageButton homeBtn = findViewById(R.id.homeBtn);

        navigation.getMenu().getItem(0).setCheckable(false);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        navigation.getMenu().getItem(0).setCheckable(true);
                        homeBtn.setBackgroundResource(R.drawable.icn_add_black);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.cus_main_view, new HistoryFragment())
                                .commit();
                        Log.d("CUSTOMER", "GOTO  HISTORY");
                        return true;
//                case R.id.navigation_restaurants:
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .replace(R.id.cus_main_view, homeFragment)
//                            .commit();
//                    Log.d("CUSTOMER", "GOTO  Home");
//                    return true;
                    case R.id.navigation_profile:
                        navigation.getMenu().getItem(1).setCheckable(true);
                        homeBtn.setBackgroundResource(R.drawable.icn_add_black);
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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //home click
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cus_main_view, homeFragment)
                        .commit();

                homeBtn.setBackgroundResource(R.drawable.icn_add);
                for (int i = 0; i < navigation.getMenu().size(); i++) {
                    navigation.getMenu().getItem(i).setChecked(false);
                    navigation.getMenu().getItem(i).setCheckable(false);
                }

                Log.d("CUSTOMER", "GOTO  Home");
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.cus_main_view,
                            homeFragment).commit();

            getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.cus_main_view, homeFragment)
                            .commit();
                    Log.d("CUSTOMER", "GOTO  Home");
        }
    }

}
