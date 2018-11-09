package com.example.jugjig.foodland.customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Restaurant;

import java.util.ArrayList;

public class HistoryListAdapter extends RecyclerView.Adapter {
    ArrayList<Restaurant> reserList = new ArrayList<>();

    public HistoryListAdapter() {
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_item, viewGroup, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return reserList.size();
    }

    public void setItemList(ArrayList<Restaurant> restaurants) {
        this.reserList = reserList;
        notifyDataSetChanged();
    }
}
