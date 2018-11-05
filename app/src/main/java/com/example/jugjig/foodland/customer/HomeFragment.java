package com.example.jugjig.foodland.customer;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.RestaurantListAdapter;
import com.example.jugjig.foodland.model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    RecyclerView resList;
    RestaurantListAdapter adapter;
    SearchView search;
    BottomNavigationView navView;
    ArrayList<Restaurant> restaurants;
    Bundle bundle;

    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navView = getActivity().findViewById(R.id.bottom_nav_bar);
        navView.getMenu().getItem(1).setChecked(true);

        setSearchBar();

        resList = getActivity().findViewById(R.id.home_res_list);
        resList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        bundle = getArguments();
        if (bundle == null) {
            adapter = new RestaurantListAdapter();
            getData();
        } else {
            Log.wtf("home", "old fragment");
            restaurants = getArguments().getParcelableArrayList("restaurants");
            setNavigation();
            adapter = new RestaurantListAdapter();
            adapter.setItemList(restaurants);
            resList.setAdapter(adapter);
        }

    }

    void getData() {
        restaurants = new ArrayList<>();
        firestore.collection("Restaurant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot item : task.getResult().getDocuments()) {
                    restaurants.add((item.toObject(Restaurant.class)));
                }
                Log.wtf("home", "size " + restaurants.size());

                putArgument();
                adapter.setItemList(restaurants);
                resList.setAdapter(adapter);
            }
        });
    }

    void putArgument() {
        bundle = new Bundle();
        bundle.putParcelableArrayList("restaurants", restaurants);
        setArguments(bundle);
        setNavigation();
    }

    void setNavigation() {
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_history:
                        HistoryFragment history = new HistoryFragment();
                        history.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, history).addToBackStack(null).commit();
                        return true;
                    case R.id.navigation_profile:
                        CusViewProfileFragment profileFragment = new CusViewProfileFragment();
                        profileFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_view, profileFragment).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });
    }

    void setSearchBar() {
        search = getActivity().findViewById(R.id.home_searchBar);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("restaurants", restaurants);
        Log.wtf("home", "save state");
    }


}
