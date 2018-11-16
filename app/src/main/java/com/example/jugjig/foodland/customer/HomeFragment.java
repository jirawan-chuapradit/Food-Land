package com.example.jugjig.foodland.customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.R;
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
    ArrayList<Restaurant> restaurants;

    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        adapter = new RestaurantListAdapter();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setSearchBar();

        resList = getActivity().findViewById(R.id.home_res_list);
        resList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        if (restaurants != null) {
            adapter.setItemList(restaurants);
            resList.setAdapter(adapter);
        }
        else {
            getData();
        }
    }

    void getData() {
        restaurants = new ArrayList<>();
        firestore.collection("Restaurant").whereEqualTo("status", "open").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot item : task.getResult().getDocuments()) {
                    restaurants.add(item.toObject(Restaurant.class));
                }
//                System.out.println(restaurants.get(0).toString());
                Log.wtf("home", "size " + restaurants.size());
                adapter.setItemList(restaurants);
                resList.setAdapter(adapter);
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
}
