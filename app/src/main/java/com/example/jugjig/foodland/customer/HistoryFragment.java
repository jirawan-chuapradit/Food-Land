package com.example.jugjig.foodland.customer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Reservation;
import com.example.jugjig.foodland.model.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryFragment extends Fragment {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth user = FirebaseAuth.getInstance();

    ArrayList<Reservation> pendingList;
    HashMap<String, Restaurant> pendingResList;
    HistoryListAdapter pendingListAdapter;
    RecyclerView pendingRecycler;

    ArrayList<Reservation> historyList;
    HashMap<String, Restaurant> historyResList;
    HistoryListAdapter historyListAdapter;
    RecyclerView historyRecycler;

    ProgressBar progressBar;
    ProgressBar progressBar2;

    TextView pendingEmptyText;
    TextView historyEmptyText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pendingListAdapter = new HistoryListAdapter();
        historyListAdapter = new HistoryListAdapter();
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        pendingEmptyText = getActivity().findViewById(R.id.history_pending_empty);
        historyEmptyText = getActivity().findViewById(R.id.history_complete_empty);

        pendingRecycler = getActivity().findViewById(R.id.history_pending_list);
        pendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        historyRecycler = getActivity().findViewById(R.id.history_complete_list);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        progressBar = getActivity().findViewById(R.id.history_progress_bar);
        progressBar2 = getActivity().findViewById(R.id.history_progress_bar2);

        if (pendingList == null) {
            getData();
        } else {
            pendingListAdapter.setItemList(pendingList, pendingResList);
            pendingRecycler.setAdapter(pendingListAdapter);

            if (pendingList.isEmpty()) {
                pendingEmptyText.setVisibility(View.VISIBLE);
            }
            if (historyList.isEmpty()) {
                historyEmptyText.setVisibility(View.VISIBLE);
            }

            historyListAdapter.setItemList(historyList, historyResList);
            historyRecycler.setAdapter(historyListAdapter);
        }

    }

    void getData() {
        pendingList = new ArrayList<>();
        historyList = new ArrayList<>();
        pendingResList = new HashMap<>();
        historyResList = new HashMap<>();
        progressBar.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        firestore.collection("Reservations").whereEqualTo("customerId", user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                Boolean pendingEmpty = true;
                Boolean historyEmpty = true;
                for (int i = 0; i < task.getResult().getDocuments().size(); i++) {
                    DocumentSnapshot item = task.getResult().getDocuments().get(i);
                    final int finalI = i;
                    final Reservation reservation = item.toObject(Reservation.class);

                    if (reservation.getStatus().equals("pending")) {
                        pendingEmpty = false;
                        pendingList.add(reservation);
                        final Boolean finalPendingEmpty = pendingEmpty;
                        final Boolean finalHistoryEmpty = historyEmpty;
                        firestore.collection("Restaurant").document(reservation.getRestaurantId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                pendingResList.put(reservation.getRestaurantId(), task2.getResult().toObject(Restaurant.class));
                                pendingListAdapter.setItemList(pendingList, pendingResList);
                                pendingRecycler.setAdapter(pendingListAdapter);

                                if (finalI == task.getResult().size() - 1) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBar2.setVisibility(View.GONE);
                                    if (finalPendingEmpty) {
                                        pendingEmptyText.setVisibility(View.VISIBLE);
                                    }
                                    if (finalHistoryEmpty) {
                                        historyEmptyText.setVisibility(View.VISIBLE);
                                    }
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            }
                        });
                    } else {
                        historyList.add(reservation);
                        historyEmpty = false;
                        final Boolean finalPendingEmpty1 = pendingEmpty;
                        final Boolean finalHistoryEmpty1 = historyEmpty;
                        firestore.collection("Restaurant").document(reservation.getRestaurantId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                historyResList.put(reservation.getRestaurantId(), task2.getResult().toObject(Restaurant.class));
                                historyListAdapter.setItemList(historyList, historyResList);
                                historyRecycler.setAdapter(historyListAdapter);

                                if (finalI == task.getResult().size() - 1) {
                                    progressBar.setVisibility(View.GONE);
                                    progressBar2.setVisibility(View.GONE);
                                    if (finalPendingEmpty1) {
                                        pendingEmptyText.setVisibility(View.VISIBLE);
                                    }
                                    if (finalHistoryEmpty1) {
                                        historyEmptyText.setVisibility(View.VISIBLE);
                                    }
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }

                            }
                        });

                    }
                }

                if (task.getResult().size() == 0) {
                    progressBar.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.GONE);
                    pendingEmptyText.setVisibility(View.VISIBLE);
                    historyEmptyText.setVisibility(View.VISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }
        });

    }
}
