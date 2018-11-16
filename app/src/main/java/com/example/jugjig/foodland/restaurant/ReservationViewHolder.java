package com.example.jugjig.foodland.restaurant;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jugjig.foodland.R;

public class ReservationViewHolder extends RecyclerView.ViewHolder {

    TextView name;
    TextView amount;
    TextView time;
    TextView date;
    TextView comment;

    public ReservationViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.rest_reser_name);
        amount = itemView.findViewById(R.id.rest_reser_person);
        time = itemView.findViewById(R.id.rest_reser_time);
        date = itemView.findViewById(R.id.rest_reser_date);
        comment = itemView.findViewById(R.id.rest_reser_comment);
    }
}
