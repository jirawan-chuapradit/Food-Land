package com.example.jugjig.foodland;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;

public class UpdatePassword extends Fragment {

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private Button saveBtn;
    private EditText newPassword, reNewPassword,oldPassword;
    private FirebaseUser firebaseUser;
    private String userPasswordNew, userRePasswordNew,uid,userOldPassword;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.update_rc_password,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();


        //GET VALUDE FROM FIREBASE
        uid = fbAuth.getCurrentUser().getUid();
        saveBtn = getView().findViewById(R.id.saveBtn);
        oldPassword = getView().findViewById(R.id.update_old_password);
        newPassword = getView().findViewById(R.id.update_new_password);
        reNewPassword = getView().findViewById(R.id.update_re_new_password);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userOldPassword = oldPassword.getText().toString();
                userPasswordNew = newPassword.getText().toString();
                userRePasswordNew = reNewPassword.getText().toString();


                if (userPasswordNew.length() <= 5 || userRePasswordNew.length() <= 5){
                    Log.d("UPDATE", "PASSWORD LESS THAN 6");
                    Toast.makeText(getActivity(),"กรุณาระบุรหัสผ่านมากกว่า 5 ตัว",Toast.LENGTH_SHORT).show();
                }else if(userPasswordNew.isEmpty() || userRePasswordNew.isEmpty()){
                    Log.d("UPDATE", "VALUE IS EMPTY");
                    Toast.makeText(getActivity(),"กรุณาระบุข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                }else if(!userPasswordNew.equals(userRePasswordNew)){
                    Log.d("UPDATE", "PASSWORD NOT EQUALS RE PASSWORD");
                    Toast.makeText(getActivity(),"รหัสผ่านไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
                }
                else {
                    delay();
                    SharedPreferences prefs = getContext().getSharedPreferences("FoodLand", Context.MODE_PRIVATE);
                    String email = prefs.getString("_userId", "empty email");
                    Log.d("USER ID: ", email);

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,userOldPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            updatePassword(userPasswordNew);
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Log.d("UPDATE", "OLD PASSWORD WAS WRONG");
                                    Toast.makeText(
                                            getActivity(),
                                            "รหัสผ่านไม่ถูกต้อง",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });

                }

            }
        });
    }
    private void updatePassword(String userPasswordNew) {

        firebaseUser.updatePassword(userPasswordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SharedPreferences.Editor prefs = getContext().getSharedPreferences("FoodLand",MODE_PRIVATE).edit();
                    prefs.clear().commit();
                    //FORCE USER SIGGOUT
                    FirebaseAuth.getInstance().signOut();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(myIntent);

                    Log.d("USER ", "GO TO LOGIN");
                    Toast.makeText(
                            getActivity(),
                            "Password has been Changed",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UPDATEPASSWORD : ", e.getMessage());
                        Toast.makeText(
                                getActivity(),
                                "Password Update Failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                });
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
