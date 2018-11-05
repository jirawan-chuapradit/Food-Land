package com.example.jugjig.foodland.customer;

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
import com.example.jugjig.foodland.model.ProfileCustomer;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterCustomerFragment extends Fragment {

    private Button registerBtn;
    private String fName, lName, email, phone, password, rePassword, uid;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;

    // Loading data dialog
    ProgressDialog progressDialog;

    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        //
        return inflater.inflate(R.layout.fragment_register_customer, container, false);
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        register();
        //attaching listener


    }

    private void register() {

        registerBtn = getView().findViewById(R.id.regis_cus_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get parameter
                getParameter();

                //check parameter
                if (fName.isEmpty() || lName.isEmpty() || email.isEmpty()
                        || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    Log.d("REGISTER", "PARAMETER IS EMPTY");
                    Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(rePassword)) {
                    Log.d("REGISTER", "PASSWORD IS NOT EQUALS REPASSWORD");
                    Toast.makeText(getActivity(), "รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 5 || rePassword.length() <= 5) {
                    Log.d("REGISTER", "รหัสผ่านน้อยกว่า 6 ตัว");
                    Toast.makeText(getActivity(), "กรุณาระบุรหัสผ่านมากกว่า 5 ตัว", Toast.LENGTH_SHORT).show();
                } else {
                    // Loading data dialog following owner network speed
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Please waiting...");
                    progressDialog.show();
                    fbAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    uid = fbAuth.getCurrentUser().getUid();
                                    setParameter();
//                                    sendVerifiedEmail(authResult.getUser());

                                    fbAuth.getInstance().signOut();
                                    Log.d("LOGIN", "Send verify e-mail successful");
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .addToBackStack(null)
                                            .replace(R.id.main_view, new LoginFragment())
                                            .commit();
                                    Toast.makeText
                                            (getContext(), "Please Verify Your E-Mail", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });

                }
            }
        });

    }

    private void setParameter() {
        ProfileCustomer cusProfile = ProfileCustomer.getCusProfileInstance();
        cusProfile.setfName(fName);
        cusProfile.setlName(lName);
        cusProfile.setRole("customer");
        cusProfile.setPhone(phone);
        cusProfile.setEmail(email);

        firestore.collection("UserProfile")
                .document(uid)
                .set(cusProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");

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

    private void getParameter() {
        EditText fNameEdt = getView().findViewById(R.id.regis_Fname_cus);
        EditText lNameEdt = getView().findViewById(R.id.regis_Lname_cus);
        EditText emailEdt = getView().findViewById(R.id.regis_mail_cus);
        EditText phoneEdt = getView().findViewById(R.id.regis_phone_cus);
        EditText passwordEdt = getView().findViewById(R.id.regis_pass_cus);
        EditText repasswordEdt = getView().findViewById(R.id.regis_rePass_cus);

        fName = fNameEdt.getText().toString().toUpperCase();
        lName = lNameEdt.getText().toString().toUpperCase();
        email = emailEdt.getText().toString();
        phone = phoneEdt.getText().toString();
        password = passwordEdt.getText().toString();
        rePassword = repasswordEdt.getText().toString();

    }

    /****************************************************************
     * อาจจะต้องมีการ register หลายๆรอบ จนกว่าregister จะนิ่ง ยังไม่อยากให้เปิด verfiled mail*
     ****************************************************************/
//    void sendVerifiedEmail(FirebaseUser _user){
//        _user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//
//                fbAuth.getInstance().signOut();
//                Log.d("LOGIN", "Send verify e-mail successful");
//                getActivity().getSupportFragmentManager()
////                        .beginTransaction()
////                        .addToBackStack(null)
////                        .replace(R.id.main_view, new LoginFragment())
////                        .commit();
//                Toast.makeText
//                        (getContext(),"Please Verify Your E-Mail",Toast.LENGTH_SHORT)
//                        .show();
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(getActivity(),"ERROR = " + e.getMessage()
//                        ,Toast.LENGTH_SHORT)
//                        .show();
//
//                Log.d("LOGIN", "Send vefiry e-mail failure");
//
//            }
//        });
//    }


}
