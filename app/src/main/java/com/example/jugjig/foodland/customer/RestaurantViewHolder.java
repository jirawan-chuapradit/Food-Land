package com.example.jugjig.foodland.customer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jugjig.foodland.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder{

    TextView restName;
    TextView restLocation;
    TextView restType;
    TextView openTime;
    ImageView profileImage;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        restName = itemView.findViewById(R.id.res_item_name);
        restLocation = itemView.findViewById(R.id.res_item_location);
        restType = itemView.findViewById(R.id.res_item_type);
        openTime = itemView.findViewById(R.id.res_item_time);
        profileImage = itemView.findViewById(R.id.res_item_profile_image);
    }

}
