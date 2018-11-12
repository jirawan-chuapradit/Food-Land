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
    Bundle bundle;
    AppBarLayout topBG;
    boolean check_ScrollingUp = false;

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
        topBG = getActivity().findViewById(R.id.app_bar);

        setOnScroll();

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
        firestore.collection("Restaurant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot item : task.getResult().getDocuments()) {
                    restaurants.add((item.toObject(Restaurant.class)));
                }
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

    void setOnScroll() {
        resList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    if(check_ScrollingUp)
                    {
                        topBG.setExpanded(true);
                    }

                } else {
                    // Scrolling down
                    if(!check_ScrollingUp ) {
                        topBG.setExpanded(false);
                    }


                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
