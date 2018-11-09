package com.example.jugjig.foodland.customer;

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

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.UserProfile;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateCusProfile extends Fragment implements View.OnClickListener {

    private String fName, lName, email, phone, uid;
    private EditText fNameEdt, lNameEdt, phoneEdt;
    private TextView profileEmail;
    private Button saveBtn;

    // Loading data dialog
    ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_view_cus,container,false);
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
//
//        prefs.putString("cus_f_name", fname);
//        prefs.putString("cus_l_name", lname);
//        prefs.putString("cus_email", email);
//        prefs.putString("cus_phone", phone);

        fName = prefs.getString("cus_f_name", "none");
        lName = prefs.getString("cus_l_name", "none");
        phone = prefs.getString("cus_phone", "none");
        email = prefs.getString("cus_email", "none");

        fNameEdt.setText(fName);
        lNameEdt.setText(lName);
        profileEmail.setText(email);
        phoneEdt.setText(phone);
        prefs.edit().clear().commit();
        Log.d("USER", "SHOW USER INFORMATION");

    }


    private void getParameter() {

        fNameEdt = getView().findViewById(R.id.update_cus_FName);
        lNameEdt = getView().findViewById(R.id.update_cus_LName);
        profileEmail = getView().findViewById(R.id.update_cusEmail);
        phoneEdt = getView().findViewById(R.id.update_cusPhone);

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
        email = profileEmail.getText().toString();

        //check parameter
        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Log.d("REGISTER", "PARAMETER IS EMPTY");
            Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        } else {
            // Loading data dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please waiting...");
            progressDialog.show();

            setParameter();
        }
    }

    private void setParameter() {

        UserProfile userProfile = UserProfile.getRestProfileInstance();
        userProfile.setfName(fName);
        userProfile.setlName(lName);
        userProfile.setPhone(phone);
        userProfile.setEmail(email);
        userProfile.setRole("customer");

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
                                .replace(R.id.cus_main_view, new CusViewProfileFragment())
                                .addToBackStack(null)
                                .commit();
                        Log.d("USER", "GOTO CUSTOMER PROFILE");
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
