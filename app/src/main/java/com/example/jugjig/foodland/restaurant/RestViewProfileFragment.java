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
import android.widget.TextView;

import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private String uid,name,phone,desc,email;
    Button updateBtn;
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting...");
        progressDialog.show();

        getParameter();
        setParmeter();

        //getParameter from fragment
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
                        UserProfile restProfile = documentSnapshot.toObject(UserProfile.class);
                        name = restProfile.getfName()+"  "+restProfile.getlName();
                        phone = restProfile.getPhone();
                        email = restProfile.getEmail();
                        desc = restProfile.getDesc();

                        profileName.setText("ชื่อ : "+name);
                        profileEmail.setText("อีเมลล์ : "+email);
                        profilePhone.setText("เบอร์โทร : "+phone);
                        profileDesc.setText("รายละเอียดร้านค้า : "+desc);
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
    }

    private void updateProfile() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new UpdateRestProfile())
                .commit();

        Log.d("USER ", "GO TO UPDATE PROFILE");
    }
}
