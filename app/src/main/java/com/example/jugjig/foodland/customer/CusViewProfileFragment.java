package com.example.jugjig.foodland.customer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.ProfileCustomer;
import com.example.jugjig.foodland.model.UserProfile;
import com.example.jugjig.foodland.restaurant.UpdateRestProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class CusViewProfileFragment extends Fragment implements View.OnClickListener {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_cus, container, false);
    }

    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private TextView profileName, profilePhone, profileDesc, profileEmail;
    private String uid,fname,lname,phone,email;
    Button updateBtn;
    ProgressDialog progressDialog;

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

        // Loading data dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();

        getParameter();
        setParmeter();

        updateBtn = getView().findViewById(R.id.update_profile);
        updateBtn.setOnClickListener(this);


    }

    private void setParmeter() {
        firestore.collection("UserProfile")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ProfileCustomer cusProfile = documentSnapshot.toObject(ProfileCustomer.class);
                        fname = cusProfile.getfName();
                        lname = cusProfile.getlName();
                        phone = cusProfile.getPhone();
                        email = cusProfile.getEmail();

                        profileName.setText("  "+fname+"  "+lname);
                        profileEmail.setText("  "+email);
                        profilePhone.setText("  "+phone);
                        Log.d("USER", "SHOW USER INFORMATION");
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER", "ERROR = " + e.getMessage());
                    }
                });
    }

    private void getParameter() {

        profileName = getView().findViewById(R.id.cusName);
        profileEmail = getView().findViewById(R.id.cusEmail);
        profilePhone = getView().findViewById(R.id.cusPhone);

    }

    @Override
    public void onClick(View v) {
        if(v==updateBtn){
            Log.d("USER ","CLICK UPDATE PROFILE");
            System.out.println("aaaaaaa");
            updateProfile();
        }
    }

    private void updateProfile() {
        SharedPreferences.Editor prefs = getContext().getSharedPreferences("FoodLand",MODE_PRIVATE).edit();
        prefs.putString("cus_f_name", fname);
        prefs.putString("cus_l_name", lname);
        prefs.putString("cus_email", email);
        prefs.putString("cus_phone", phone);
        prefs.apply();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.cus_main_view, new UpdateCusProfile())
                .commit();

        Log.d("USER ", "GO TO UPDATE PROFILE");
    }
}
