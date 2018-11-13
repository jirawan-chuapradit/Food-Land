package com.example.jugjig.foodland.customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Reservation;
import com.example.jugjig.foodland.model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    ArrayList<Reservation> reserList = new ArrayList<>();
    HashMap<String, Restaurant> resList = new HashMap<>();

    public HistoryListAdapter() {
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reservation_item, viewGroup, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i) {
        Reservation item = reserList.get(i);

        historyViewHolder.restName.setText(resList.get(item.getRestaurantId()).getName());
        historyViewHolder.reserPerson.setText(String.valueOf(item.getAmount()));
        historyViewHolder.reserTime.setText(item.getTime());
        historyViewHolder.reserDate.setText(item.getDate());
        historyViewHolder.restLocation.setText(resList.get(item.getRestaurantId()).getLocation());
        historyViewHolder.restTel.setText(resList.get(item.getRestaurantId()).getTelephone());
        Glide.with(historyViewHolder.profileImage.getContext()).load(resList.get(item.getRestaurantId()).getProfileImageURL()).apply(RequestOptions.centerCropTransform().placeholder(R.drawable.logo)).into(historyViewHolder.profileImage);
    }

    @Override
    public int getItemCount() {
        return reserList.size();
    }

    public void setItemList(ArrayList<Reservation> reserList, HashMap<String, Restaurant> resList) {
        this.reserList = reserList;
        this.resList = resList;
        notifyDataSetChanged();
    }
}
