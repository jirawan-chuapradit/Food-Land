package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateRestProfile extends Fragment implements View.OnClickListener {

    private String fName, lName, email, phone, desc, uid;
    private EditText fNameEdt, lNameEdt, phoneEdt, descEdt;
    private TextView profileEmail;
    private Button saveBtn;

    // Loading data dialog
    ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;


    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_update_view_rest, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();

        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();

        //show information
        getParameter();
        showParameter();

        saveBtn = getView().findViewById(R.id.saveBtn);


        saveBtn.setOnClickListener(this);


    }


    private void showParameter() {

        SharedPreferences prefs = getContext().getSharedPreferences("FoodLand", Context.MODE_PRIVATE);

        fName = prefs.getString("rest_f_name", "none");
        lName = prefs.getString("rest_l_name", "none");
        phone = prefs.getString("rest_phone", "none");
        email = prefs.getString("rest_email", "none");
        desc = prefs.getString("rest_desc", "none");

        fNameEdt.setText(fName);
        lNameEdt.setText(lName);
        profileEmail.setText(email);
        phoneEdt.setText(phone);
        descEdt.setText(desc);
        prefs.edit().clear().commit();
        Log.d("USER", "SHOW USER INFORMATION");

    }


    private void getParameter() {

        fNameEdt = getView().findViewById(R.id.update_rest_fname);
        lNameEdt = getView().findViewById(R.id.update_rest_lname);
        profileEmail = getView().findViewById(R.id.restEmail);
        phoneEdt = getView().findViewById(R.id.update_rest_phone);
        descEdt = getView().findViewById(R.id.update_rest_desc);

    }


    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            Log.d("USER ", "CLIECK SAVE");
            saveInformation();
        }
    }

    private void saveInformation() {

        getParameter();
        converseToString();


    }

    private void converseToString() {
        fName = fNameEdt.getText().toString().toUpperCase();
        lName = lNameEdt.getText().toString().toUpperCase();
        phone = phoneEdt.getText().toString();
        desc = descEdt.getText().toString();
        email = profileEmail.getText().toString();

        //check parameter
        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Log.d("REGISTER", "PARAMETER IS EMPTY");
            Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        } else {
            // Loading data dialog
           delay();

            setParameter();
        }
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
    private void setParameter() {

        if (desc.isEmpty()) {
            desc = "...";
        }

        UserProfile userProfile = UserProfile.getRestProfileInstance();
        userProfile.setfName(fName);
        userProfile.setlName(lName);
        userProfile.setPhone(phone);
        userProfile.setEmail(email);
        userProfile.setDesc(desc);
        userProfile.setRole("restaurant");

        firestore.collection("UserProfile")
                .document(uid)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");


                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.rest_main_view, new RestViewProfileFragment())
                                .addToBackStack(null)
                                .commit();
                        Log.d("USER", "GOTO RESTAURANT PROFILE");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("REGISTER", "ERROR =" + e.getMessage());
                        Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
