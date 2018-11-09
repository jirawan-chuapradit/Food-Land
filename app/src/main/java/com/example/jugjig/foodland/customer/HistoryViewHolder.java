package com.example.jugjig.foodland.customer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jugjig.foodland.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    TextView restName;
    TextView restTel;
    TextView reserDate;
    TextView reserTime;
    TextView reserPerson;
    ImageView profileImage;


    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        restName = itemView.findViewById(R.id.reser_item_name);
        restTel = itemView.findViewById(R.id.reser_item_tel);
        reserDate = itemView.findViewById(R.id.reser_item_date);
        reserTime = itemView.findViewById(R.id.reser_item_time);
        reserPerson = itemView.findViewById(R.id.reser_item_person);
        profileImage = itemView.findViewById(R.id.reser_item_profile_image);
    }
}
