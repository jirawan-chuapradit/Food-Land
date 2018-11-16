package com.example.jugjig.foodland;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.restaurant.ReservationListFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RestMainActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    String status;
    FirebaseAuth user = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ImageButton homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_main);

        homeBtn = findViewById(R.id.switchBtn);

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.rest_bottom_nav_bar);

        builder = new AlertDialog.Builder(this);

        firestore.collection("Restaurant").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                status = documentSnapshot.getString("status");
                if (status.equals("close")) {
                    homeBtn.setBackgroundResource(R.drawable.close);
                }
                setDialog();
            }
        });

        navigation.getMenu().getItem(0).setCheckable(false);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_history:
                        Log.d("RESTAURANT", "Click Home");
                        navigation.getMenu().getItem(0).setCheckable(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.rest_main_view, new ReservationListFragment()).commit();
                        return true;
                    case R.id.navigation_profile:
                        navigation.getMenu().getItem(1).setCheckable(true);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.rest_main_view, new RestViewProfileFragment())
                                .commit();
                        Log.d("RESTAURANT", "Click  Profile Restaurant");
                        return true;
                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
                Log.d("CUSTOMER", "GOTO  Home");
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rest_main_view,
                            new RestViewProfileFragment()).commit();
            navigation.setSelectedItemId(R.id.navigation_profile);
        }
    }

    void setDialog() {
        if (status.equals("open")) {
            builder.setMessage("คุณต้องการปิดรับการจองใช่หรือไม่");
        }
        else  {
            builder.setMessage("คุณต้องการเปิดรับการจองใช่หรือไม่");
        }

        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (status.equals("open")) {
                    status = "close";
                    firestore.collection("Restaurant").document(user.getUid()).update("status", status);
                    builder.setMessage("คุณต้องการเปิดรับการจองใช่หรือไม่");
                    homeBtn.setBackgroundResource(R.drawable.close);
                }
                else {
                    status = "open";
                    firestore.collection("Restaurant").document(user.getUid()).update("status", status);
                    builder.setMessage("คุณต้องการปิดรับการจองใช่หรือไม่");
                    homeBtn.setBackgroundResource(R.drawable.open);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

}


