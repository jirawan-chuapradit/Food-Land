package com.example.jugjig.foodland.restaurant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.customer.RestaurantViewHolder;
import com.example.jugjig.foodland.model.Reservation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationViewHolder> {

    ArrayList<Reservation> reser = new ArrayList<>();
    HashMap<String, String> nameList = new HashMap<>();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ReservationListAdapter() {

    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rest_reser_item, viewGroup, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder reservationViewHolder, int i) {
        final Reservation item = reser.get(i);
        reservationViewHolder.name.setText(nameList.get(item.getReservationId()));
        reservationViewHolder.amount.setText(Integer.toString(item.getAmount()));
        reservationViewHolder.time.setText(item.getTime());
        reservationViewHolder.date.setText(item.getDate());
        reservationViewHolder.comment.setText(item.getComment());

        reservationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AppCompatActivity activity = (AppCompatActivity) v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("รายการจองนี้เสร็จสิ้นแล้วใช่หรือไม่");
                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firestore.collection("Reservations").document(item.getReservationId()).update("status", "complete");
                        dialog.dismiss();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.rest_main_view, new ReservationListFragment()).commit();
                    }
                });

                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return reser.size();
    }

    public void setItemList(ArrayList<Reservation> reser, HashMap<String, String> nameList) {
        this.reser = reser;
        this.nameList = nameList;
        notifyDataSetChanged();
    }
}
