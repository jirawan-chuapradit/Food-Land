package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.SelectRegisterFragment;
import com.example.jugjig.foodland.model.Restaurant;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class RegisterRestFragment extends Fragment implements View.OnClickListener {

    private Button registerBtn;
    private String fName, lName, email, phone, restDesc, password, rePassword
            , uid, resName, resLocation, resOpen, resClose, resType
    ,generatedFilePath;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    private Button back;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;




    // Loading data dialog
    ProgressDialog progressDialog;

    //ImageView
    private ImageView userProfileImage;
    //a constant to track the file chooser intent
    private static int PICK_IMAGE = 123;
    //a Uri object to store file path
    private Uri filePath;


    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register_rest, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        fbAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //get parameter
        registerBtn = getView().findViewById(R.id.registerBtn);
        back = getView().findViewById(R.id.back_btn);
        userProfileImage = getView().findViewById(R.id.restProfileImage);

        //attaching listener
        registerBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        userProfileImage.setOnClickListener(this);

    }


    private void register() {
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
            delay();

            if (restDesc.isEmpty()) {
                restDesc = "...";
            }
            fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    uid = fbAuth.getCurrentUser().getUid();
                    System.out.println("User id: "+uid);
                    //save image to storage
                    StorageReference imageReference = storageReference.child("restaurant_profile_image").child(uid).child("Profile Pic");//restaurant_profile_image/user id/Profile Pic.jpg
                    UploadTask uploadTask = imageReference.putFile(filePath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(
                                    getActivity(),
                                    "Upload failed!",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(
                                    getActivity(),
                                    "Upload successful!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            getProfileImageURL();
                        }
                    });



                }
            }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("USER", "not found UID");
                            Toast.makeText(
                                    getActivity(),
                                    "Please check your network!",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

        }
    }

    private void getProfileImageURL() {
        storageReference.child("restaurant_profile_image/"+uid+"/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Uri downloadUri = uri;
                generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
                //setParameter
                setParameter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Log.d("REGISTER", "ERROR =" + e.getMessage());
                Toast.makeText(getContext(), "ERROR = " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    void delay() {
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
        UserProfile restProfile = UserProfile.getRestProfileInstance();
        restProfile.setfName(fName);
        restProfile.setlName(lName);
        restProfile.setRole("restaurant");
        restProfile.setPhone(phone);
        restProfile.setEmail(email);
        restProfile.setDesc(restDesc);

        final Restaurant restaurant = new Restaurant();
        restaurant.setName(resName);
        restaurant.setLocation(resLocation);
        restaurant.setOpenTime(resOpen);
        restaurant.setCloseTime(resClose);
        restaurant.setDescription(restDesc);
        restaurant.setRestaurantId(uid);
        restaurant.setTelephone(phone);
        restaurant.setProfileImageURL(generatedFilePath);
        restaurant.setType(resType);

        firestore.collection("UserProfile")
                .document(uid)
                .set(restProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");

                        //USER SIGGOUT

                        firestore.collection("Restaurant")
                                .document(uid)
                                .set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();

                                FirebaseAuth.getInstance().signOut();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_view, new LoginFragment())
                                        .addToBackStack(null)
                                        .commit();
                                Log.d("USER", "GOTO LOGIN");
                            }
                        });
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
        EditText fNameEdt = getView().findViewById(R.id.registerFname);
        EditText lNameEdt = getView().findViewById(R.id.registerLname);
        EditText emailEdt = getView().findViewById(R.id.registerEmail);
        EditText phoneEdt = getView().findViewById(R.id.registerPhone);
        EditText descEdt = getView().findViewById(R.id.registerRestDesc);
        EditText passwordEdt = getView().findViewById(R.id.registerPassword);
        EditText repasswordEdt = getView().findViewById(R.id.registerRePassword);

        EditText resNameEdt = getView().findViewById(R.id.registeRestName);
        EditText resLocationEdt = getView().findViewById(R.id.registeRestLocate);
        EditText resOpenEdt = getView().findViewById(R.id.registeRestOpen);
        EditText resCloseEdt = getView().findViewById(R.id.registeRestClose);
        EditText resTypeEdt = getView().findViewById(R.id.registeRestType);

        fName = fNameEdt.getText().toString().toUpperCase();
        lName = lNameEdt.getText().toString().toUpperCase();
        email = emailEdt.getText().toString();
        phone = phoneEdt.getText().toString();
        restDesc = descEdt.getText().toString();
        password = passwordEdt.getText().toString();
        rePassword = repasswordEdt.getText().toString();

        resName = resNameEdt.getText().toString();
        resLocation = resLocationEdt.getText().toString();
        resOpen = resOpenEdt.getText().toString();
        resClose = resCloseEdt.getText().toString();
        resType = resTypeEdt.getText().toString();

    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            filePath = data.getData();

            Glide.with(getContext()).load(filePath)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .dontTransform()
                    .override(300,200)
                    .transform(new CircleCrop()))
                   .into(userProfileImage);

/*
            Glide.with(getContext()).load(filePath).centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .transform((Transformation) new PicassoCircleTransformation())
                    .into(userProfileImage);
                    */
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v == registerBtn) {
            Log.d("REST", "REGISTER");
            register();
        } else if (v == back) {
            Log.d("CKICK: ", "BACK");
            back();
        }else if(v == userProfileImage){
            //open file chooser
            Log.d("REGISTER", "CLICK = USER_PROFIRE_IMAGE");
            showFileChooser();

        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void back() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view, new SelectRegisterFragment())
                .addToBackStack(null)
                .commit();
        Log.d("CUSTOMER", "GOTO Select Register");
    }


}
