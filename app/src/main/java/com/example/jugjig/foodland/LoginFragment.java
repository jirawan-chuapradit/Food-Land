package com.example.jugjig.foodland;

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

import com.example.jugjig.foodland.customer.CusViewProfileFragment;
import com.example.jugjig.foodland.customer.RegisterCustomerFragment;
import com.example.jugjig.foodland.model.UserProfile;
import com.example.jugjig.foodland.restaurant.RegisterRestFragment;
import com.example.jugjig.foodland.restaurant.RestViewProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFragment extends Fragment {


    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private String uid, role;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated
            (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();


//        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
//
//
//            Log.d("USER", "USER ALREADY LOG IN");
//            Log.d("USER", "GOTO Menu");
//            getActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.main_view,new RegisterCustomerFragment())
//                    .commit();
//
//            Toast.makeText(
//                    getActivity(),
//                    "Login Success , Go to menu",
//                    Toast.LENGTH_SHORT
//            ).show();
//
//            return;
//        }

        initLoginBtn();
        initRegisterBtn();

    }

    void initLoginBtn() {

        Button _loginBtn = getView().findViewById(R.id.login_btn);
        _loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText _userId = (EditText) getView().findViewById(R.id.login_userid);
                EditText _password = (EditText) getView().findViewById(R.id.login_password);
                String _userIdStr = _userId.getText().toString();
                String _passwordStr = _password.getText().toString();


                if (_userIdStr.isEmpty() || _passwordStr.isEmpty()) {
                    Toast.makeText(
                            getActivity(),
                            "กรุณาระบุ user or password",
                            Toast.LENGTH_SHORT
                    ).show();
                    Log.d("USER", "USER OR PASSWORD IS EMPTY");

                } else {


                    FirebaseAuth.getInstance().signInWithEmailAndPassword(_userIdStr, _passwordStr)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    chkIsVeriFied(authResult.getUser());

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            FirebaseAuth.getInstance().signOut();
                            Log.d("USER", "INVALID USER OR PASSWORD");
                            Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
//
                }
            }
        });
    }

    private void GotoMenu() {

        if (role.equals("restaurant")) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new RestViewProfileFragment())
                    .commit();
            Log.d("USER", "GOTO Restaurant Menu");
        } else if (role.equals("customer")) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_view, new CusViewProfileFragment())
                    .commit();


            Log.d("USER", "GOTO Customer Menu");
        }
    }

    private void getRole() {
        firestore.collection("UserProfile")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserProfile userProfile = documentSnapshot.toObject(UserProfile.class);
                        role = userProfile.getRole();
                        GotoMenu();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("USER", "ERROR = " + e.getMessage());
                    }
                });
    }


    void chkIsVeriFied(final FirebaseUser _user) {

        //USER CONFIRM EMAIL
        if(_user.isEmailVerified()){

            uid = fbAuth.getCurrentUser().getUid();

            getRole();
            Toast.makeText
                    (getContext(),"EMAIL IS VERIFIED , GO TO PROFILE",Toast.LENGTH_SHORT)
                    .show();
        }else{
            FirebaseAuth.getInstance().signOut();
            Log.d("USER", "EMAIL IS NOT VERIFIED");
            Toast.makeText
                    (getContext(),"EMAIL IS NOT VERIFIED",Toast.LENGTH_SHORT)
                    .show();

        }


    }

    void initRegisterBtn() {
        TextView _registerBtn = (TextView) getView().findViewById(R.id.login_register_Btn);
        _registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOGIN", "go to register");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view, new SelectRegisterFragment())
                        .commit();
                Toast.makeText(
                        getActivity(),
                        "go to register",
                        Toast.LENGTH_SHORT
                ).show();

            }
        });
    }



}
