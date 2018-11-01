package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
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

    private String fName,lName,email,phone,restDesc,uid;
    private EditText fNameEdt,lNameEdt,phoneEdt,descEdt;
    private TextView  profileEmail;
    private Button saveBtn;
    private boolean checkNull = true;

    // Loading data dialog
    ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;


    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_update_view_rest,container,false);
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
        firestore.collection("UserProfile")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserProfile restProfile = documentSnapshot.toObject(UserProfile.class);
                        fName = restProfile.getfName();
                        lName = restProfile.getlName();
                        phone = restProfile.getPhone();
                        email = restProfile.getEmail();
                        restDesc = restProfile.getDesc();

                        fNameEdt.setText(fName);
                        lNameEdt.setText(lName);
                        profileEmail.setText(email);
                        phoneEdt.setText(phone);
                        descEdt.setText(restDesc);
                        Log.d("USER", "SHOW USER INFORMATION");

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

        fNameEdt = getView().findViewById(R.id.update_rest_fname);
        lNameEdt = getView().findViewById(R.id.update_rest_lname);
        profileEmail = getView().findViewById(R.id.restEmail);
        phoneEdt = getView().findViewById(R.id.update_rest_phone);
        descEdt = getView().findViewById(R.id.update_rest_desc);

    }


    @Override
    public void onClick(View v) {
        if(v == saveBtn){
            Log.d("USER ", "CLIECK SAVE");
            saveInformation();
        }
    }

    private void saveInformation() {

        getParameter();
        converseToString();

        if(checkNull == false){
            // Loading data dialog
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please waiting...");
            progressDialog.show();

            setParameter();
        }

    }

    private void converseToString() {
        fName = fNameEdt.getText().toString().toUpperCase();
        lName = lNameEdt.getText().toString().toUpperCase();
        phone = phoneEdt.getText().toString();
        restDesc = descEdt.getText().toString();
        email = profileEmail.getText().toString();

        //check parameter
        if(fName.isEmpty()||lName.isEmpty()||email.isEmpty()|| phone.isEmpty()){
            checkNull = true;
            Log.d("REGISTER", "PARAMETER IS EMPTY");
            Toast.makeText(getActivity(),"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
        }else{
            checkNull = false;
        }
    }

    private void setParameter() {

        if(restDesc.isEmpty()){
            restDesc = "...";
        }

        UserProfile userProfile = UserProfile.getRestProfileInstance();
        userProfile.setfName(fName);
        userProfile.setlName(lName);
        userProfile.setPhone(phone);
        userProfile.setEmail(email);
        userProfile.setDesc(restDesc);
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
                                .replace(R.id.main_view, new RestViewProfileFragment())
                                .addToBackStack(null)
                                .commit();
                        Log.d("USER", "GOTO RESTAURANT PROFILE");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("REGISTER", "ERROR =" + e.getMessage());
                        Toast.makeText(getContext(),"ERROR = "+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
