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
import com.google.android.gms.tasks.OnSuccessListener;
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

    HashMap<String, Restaurant> resList;

    ArrayList<Reservation> pendingList;
    HistoryListAdapter pendingListAdapter = new HistoryListAdapter();
    RecyclerView pendingRecycler;

    ArrayList<Reservation> historyList;
    HistoryListAdapter historyListAdapter = new HistoryListAdapter();
    RecyclerView historyRecycler;

    ProgressBar progressBar;
    ProgressBar progressBar2;

    TextView pendingEmptyText;
    TextView historyEmptyText;

    Boolean pendingEmpty = true;
    Boolean historyEmpty = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRecycle();

        progressBar = getActivity().findViewById(R.id.history_progress_bar);
        progressBar2 = getActivity().findViewById(R.id.history_progress_bar2);

        getData();

    }

    void getData() {
        pendingList = new ArrayList<>();
        historyList = new ArrayList<>();
        resList = new HashMap<>();

        setLoading(true);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        firestore.collection("Reservations").whereEqualTo("customerId", user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                for (int i = 0; i < documentSnapshots.getDocuments().size(); i++) {
                    DocumentSnapshot item = documentSnapshots.getDocuments().get(i);
                    final Reservation reservation = item.toObject(Reservation.class);
                    Log.wtf("document", reservation.getReservationId());
                    setAdapter(reservation.getStatus(), reservation, i, documentSnapshots.getDocuments().size());
                }

                //null data
                if (documentSnapshots.getDocuments().size() == 0) {
                    setEmptyList();
                }
            }

        });

    }

    void setAdapter(final String status, final Reservation reservation, final int count, final int size) {
        firestore.collection("Restaurant").document(reservation.getRestaurantId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                resList.put(reservation.getRestaurantId(), task2.getResult().toObject(Restaurant.class));
                if (status.equals("pending")) {
                    pendingList.add(reservation);
                    pendingEmpty = false;
                } else {
                    Log.wtf("reser", "false");
                    historyList.add(reservation);
                    historyEmpty = false;
                }
                //last loop
                if (count == size - 1) {
                    pendingListAdapter.setItemList(pendingList, resList);
                    pendingRecycler.setAdapter(pendingListAdapter);
                    historyListAdapter.setItemList(historyList, resList);
                    historyRecycler.setAdapter(historyListAdapter);
                    setEmptyList();
                }
            }
        });
    }

    void setRecycle() {
        pendingEmptyText = getActivity().findViewById(R.id.history_pending_empty);
        historyEmptyText = getActivity().findViewById(R.id.history_complete_empty);

        pendingRecycler = getActivity().findViewById(R.id.history_pending_list);
        pendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        historyRecycler = getActivity().findViewById(R.id.history_complete_list);
        historyRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    void showText(TextView text) {
        text.setVisibility(View.VISIBLE);
    }

    void setEmptyList() {
        if (pendingEmpty) {
            showText(pendingEmptyText);
        }
        if (historyEmpty) {
            showText(historyEmptyText);
        }
        setLoading(false);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar2.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            progressBar2.setVisibility(View.GONE);
        }

    }
}
