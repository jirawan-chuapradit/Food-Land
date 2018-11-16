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

        name = itemView.findViewById(R.id.cus_name);
        amount = itemView.findViewById(R.id.cus_amount);
        time = itemView.findViewById(R.id.cus_time);
        date = itemView.findViewById(R.id.cus_date);
        comment = itemView.findViewById(R.id.cus_comment);
    }
}
