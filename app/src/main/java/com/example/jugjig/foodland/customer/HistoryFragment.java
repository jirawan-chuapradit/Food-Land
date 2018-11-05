package com.example.jugjig.foodland.customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.R;

import java.util.zip.Inflater;

public class HistoryFragment extends Fragment {

//    BottomNavigationView navView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.wtf("History", "argument "+ getArguments().getParcelableArrayList("restaurants").size());
        HomeFragment home = new HomeFragment();
                        home.setArguments(getArguments());
//        setNavigation();
    }

//    void setNavigation() {
//        navView = getActivity().findViewById(R.id.bottom_nav_bar);
//        navView.getMenu().getItem(0).setChecked(true);
//        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                switch (menuItem.getItemId()) {
//                    case R.id.navigation_restaurants:
//                        HomeFragment home = new HomeFragment();
//                        home.setArguments(getArguments());
//                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, home).commit();
//                        return true;
//                }
//                return false;
//            }
//        });
//    }
}
