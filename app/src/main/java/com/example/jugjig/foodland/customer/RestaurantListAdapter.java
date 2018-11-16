package com.example.jugjig.foodland.customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.Reservations.ReservationsFragment;
import com.example.jugjig.foodland.model.Reservation;
import com.example.jugjig.foodland.model.Restaurant;

import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    ArrayList<Restaurant> restaurants = new ArrayList<>();
    ArrayList<Restaurant> restaurantsCopy = new ArrayList<>();

    public RestaurantListAdapter() {
        restaurantsCopy.addAll(restaurants);

    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurant_item, viewGroup, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder restaurantViewHolder,final int i) {
        final Restaurant item = restaurants.get(i);
        restaurantViewHolder.restName.setText(item.getName());
        restaurantViewHolder.restLocation.setText(item.getLocation());
        restaurantViewHolder.restType.setText(item.getType());
        restaurantViewHolder.openTime.setText(item.getOpenTime() + " - " + item.getCloseTime());
        Glide.with(restaurantViewHolder.profileImage.getContext()).load(item.getProfileImageURL()).apply(RequestOptions.centerCropTransform().placeholder(R.drawable.logo)).into(restaurantViewHolder.profileImage);

        restaurantViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                ReservationsFragment reservationsFragment = new ReservationsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("restaurantId", item.getRestaurantId());
                bundle.putString("restaurantName", item.getName());
                bundle.putString("restaurantOpen", item.getOpenTime());
                bundle.putString("restaurantClose", item.getCloseTime());
                bundle.putDouble("restaurantLat", item.getLatitude());
                bundle.putDouble("restaurantLng", item.getLongitude());
                bundle.putString("restaurantPhone", item.getTelephone());
                reservationsFragment.setArguments(bundle);

                for (String key: bundle.keySet())
                {
                    Log.d ("myApplication", key + " is a key in the bundle" + String.valueOf(bundle.get(key)));
                }

                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cus_main_view, reservationsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public void setItemList(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
        restaurantsCopy.addAll(restaurants);
        Log.wtf("search",  "copy size " + restaurantsCopy.size() );
        notifyDataSetChanged();
    }

    public void filter(String text) {
        restaurants.clear();
        if(text.isEmpty()){
            restaurants.addAll(restaurantsCopy);
        }
        else {
            text = text.toLowerCase();
            for(Restaurant restaurant: restaurantsCopy){
                if(restaurant.getName().toLowerCase().contains(text)){
                    restaurants.add(restaurant);
                }
                else if (restaurant.getType().toLowerCase().contains(text)) {
                    restaurants.add(restaurant);
                }
                else if (restaurant.getLocation().toLowerCase().contains(text)) {
                    restaurants.add(restaurant);
                }
            }
        }
        notifyDataSetChanged();
    }

}
