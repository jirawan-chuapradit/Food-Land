package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.MainActivity;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.RestMainActivity;
import com.example.jugjig.foodland.UpdatePassword;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class RestViewProfileFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_rest, container, false);
    }


    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private TextView profileName, profilePhone, profileDesc, profileEmail;
    private String uid,fname,lname,phone,desc,email;
    Button updateBtn, logoutBtn,updatePasswordBtn;
    ProgressDialog progressDialog;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

        // Loading data dialog
        delay();

        getParameter();
        setParmeter();

        //getParameter from fragment
        updateBtn = getView().findViewById(R.id.update_profile);
        logoutBtn = getView().findViewById(R.id.log_out_btn);
        updatePasswordBtn = getView().findViewById(R.id.update_password_btn);

        updateBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        updatePasswordBtn.setOnClickListener(this);

    }

    private void setParmeter() {
        firestore.collection("UserProfile")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserProfile restProfile = documentSnapshot.toObject(UserProfile.class);
                        fname = restProfile.getfName();
                        lname = restProfile.getlName();
                        phone = restProfile.getPhone();
                        email = restProfile.getEmail();
                        desc = restProfile.getDesc();

                        profileName.setText(fname +"  "+ lname);
                        profileEmail.setText(email);
                        profilePhone.setText(phone);
                        profileDesc.setText(desc);
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

        profileName = getView().findViewById(R.id.restName);
        profileEmail = getView().findViewById(R.id.restEmail);
        profilePhone = getView().findViewById(R.id.restPhone);
        profileDesc = getView().findViewById(R.id.restDesc);

    }

    @Override
    public void onClick(View v) {
        if(v==updateBtn){
            Log.d("USER ","CLICK UPDATE PROFILE");
            updateProfile();
        }
        else if(v==logoutBtn){
            Log.d("USER ","CLICK LOGOUT");
            logout();
        }
        else if(v==updatePasswordBtn){
            Log.d("USER ","CLICK UPDATE PASSWORD");
            updatePassword();
        }
    }



    private void updatePassword() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rest_main_view, new UpdatePassword())
                .addToBackStack(null)
                .commit();

        Log.d("RESTAURANT ", "GO TO UPDATE PASSWORD");
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(myIntent);

        Log.d("USER ", "GO TO LOGIN");
    }

    private void updateProfile() {

        SharedPreferences.Editor prefs = getContext().getSharedPreferences("FoodLand",MODE_PRIVATE).edit();
        prefs.putString("rest_f_name", fname);
        prefs.putString("rest_l_name", lname);
        prefs.putString("rest_email", email);
        prefs.putString("rest_phone", phone);
        prefs.putString("rest_desc", desc);
        prefs.apply();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rest_main_view, new UpdateRestProfile())
                .addToBackStack(null)
                .commit();

        Log.d("USER ", "GO TO UPDATE PROFILE");
    }

    void delay(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("ระบบกำลังประมวลผล"); // Setting Title
        progressDialog.setMessage("กรุณารอสักครู่...");
        // Progress Dialog Style Horizontal
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Display Progress Dialog
        progressDialog.show();
        // Cannot Cancel Progress Dialog
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
