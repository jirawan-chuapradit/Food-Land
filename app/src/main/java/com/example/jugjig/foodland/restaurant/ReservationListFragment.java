package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Reservation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

public class ReservationListFragment extends Fragment {

    ArrayList<Reservation> reservations = new ArrayList<>();
    RecyclerView reserRecycler;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth user = FirebaseAuth.getInstance();
    ReservationListAdapter adapter;
    HashMap<String, String> nameList = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        adapter = new ReservationListAdapter();
        return inflater.inflate(R.layout.fragment_reser_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reserRecycler = getActivity().findViewById(R.id.reser_list_list);
        reserRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        firestore.collection("Reservations").whereEqualTo("restaurantId", user.getUid()).whereEqualTo("status", "pending").get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                Log.wtf("reser", "size + " + documentSnapshots.getDocuments().size());
                if (documentSnapshots.getDocuments().size() == 0) {
                    TextView nullText = getActivity().findViewById(R.id.reser_list_null_text);
                    nullText.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                int count = 0;
                final int size = documentSnapshots.getDocuments().size();
                for (DocumentSnapshot doc: documentSnapshots.getDocuments()) {
                    count += 1;
                    final Reservation item = doc.toObject(Reservation.class);
                    reservations.add(item);
                    final int finalCount = count;
                    firestore.collection("UserProfile").document(item.getCustomerId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String name = documentSnapshot.get("fName")+" "+documentSnapshot.get("lName");
                            nameList.put(item.getReservationId(), name);

                            if (finalCount == size) {
                                adapter.setItemList(reservations, nameList);
                                reserRecycler.setAdapter(adapter);
                                progressDialog.dismiss();
                            }

                        }
                    }); }

            }
        });

    }

}
