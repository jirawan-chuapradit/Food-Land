package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.jugjig.foodland.LoginFragment;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Restaurant;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class UpdateRestProfile extends Fragment implements View.OnClickListener {

    private String fName, lName, email, phone, desc, uid, generatedFilePath
            ,restName,restType,restLocation,restOpen,restClose;
    private EditText fNameEdt, lNameEdt, phoneEdt, descEdt
            ,restNameEdt,restTypeEdt,restOpenEdt,restCloseEdt,restLocationEdt;
    private TextView profileEmail;
    private Button saveBtn;
    private Uri filePath;
    ImageView userProfileImage;
    //a constant to track the file chooser intent
    private static int PICK_IMAGE = 123;

    // Loading data dialog
    ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth fbAuth;
    private FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    private StorageReference storageReference;


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
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //show information
        getParameter();
        showParameter();

        saveBtn = getView().findViewById(R.id.saveBtn);
        userProfileImage = getView().findViewById(R.id.updateProfileImage);


        saveBtn.setOnClickListener(this);
        userProfileImage.setOnClickListener(this);


    }

    private void getImagePic() {
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("restaurant_profile_image/"+uid+"/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if(getActivity() == null){
                    return;
                }

                Glide.with(getContext()).load(uri)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .placeholder(R.mipmap.ic_launcher_round)
                                .error(R.mipmap.ic_launcher_round)
                                .dontTransform()
                                .fitCenter()
                                .override(300,200)
                                .transform(new CircleCrop()))
                        .into(userProfileImage);
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

    private void showParameter() {
        delay();
        getImagePic();
        SharedPreferences prefs = getContext().getSharedPreferences("FoodLand", Context.MODE_PRIVATE);

        fName = prefs.getString("rest_f_name", "none");
        lName = prefs.getString("rest_l_name", "none");
        phone = prefs.getString("rest_phone", "none");
        email = prefs.getString("rest_email", "none");
        desc = prefs.getString("rest_desc", "none");
        restName = prefs.getString("restName", "none");
        restType = prefs.getString("restType", "none");
        restLocation = prefs.getString("restLocation", "none");
        restOpen = prefs.getString("restOpen", "none");
        restClose = prefs.getString("restClose","none");

        fNameEdt.setText(fName);
        lNameEdt.setText(lName);
        profileEmail.setText(email);
        phoneEdt.setText(phone);
        descEdt.setText(desc);
        restNameEdt.setText(restName);
        restLocationEdt.setText(restLocation);
        restTypeEdt.setText(restType);
        restOpenEdt.setText(restOpen);
        restCloseEdt.setText(restClose);
        prefs.edit().clear().commit();
        Log.d("USER", "SHOW USER INFORMATION");

    }


    private void getParameter() {

        userProfileImage = getView().findViewById(R.id.updateProfileImage);
        fNameEdt = getView().findViewById(R.id.update_rest_fname);
        lNameEdt = getView().findViewById(R.id.update_rest_lname);
        profileEmail = getView().findViewById(R.id.restEmail);
        phoneEdt = getView().findViewById(R.id.update_rest_phone);
        descEdt = getView().findViewById(R.id.update_rest_desc);

        restNameEdt = getView().findViewById(R.id.updateRestName);
        restLocationEdt = getView().findViewById(R.id.updateRestLocate);
        restOpenEdt = getView().findViewById(R.id.updateRestOpen);
        restCloseEdt = getView().findViewById(R.id.updateRestClose);
        restTypeEdt = getView().findViewById(R.id.updateRestType);

    }


    @Override
    public void onClick(View v) {
        if (v == saveBtn) {
            Log.d("USER ", "CLIECK SAVE");
            saveInformation();
        }
        else if(v==userProfileImage){
            Log.d("USER ", "CLIECK USER PROFILE IMAGE");
            showFileChooser();
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            filePath = data.getData();

            Glide.with(getContext()).load(filePath)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .dontTransform()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round)
                            .override(300,200)
                            .transform(new CircleCrop()))
                    .into(userProfileImage);

        }
        super.onActivityResult(requestCode, resultCode, data);
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

        restName = restNameEdt.getText().toString();
        restLocation = restLocationEdt.getText().toString();
        restOpen = restOpenEdt.getText().toString();
        restClose = restCloseEdt.getText().toString();
        restType = restTypeEdt.getText().toString();


        //check parameter
        if (fName.isEmpty() || lName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Log.d("REGISTER", "PARAMETER IS EMPTY");
            Toast.makeText(getActivity(), "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
        } else {
            // Loading data dialog
           delay();
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

        final Restaurant restaurant = new Restaurant();
        restaurant.setName(restName);
        restaurant.setLocation(restLocation);
        restaurant.setOpenTime(restOpen);
        restaurant.setCloseTime(restClose);
        restaurant.setDescription(desc);
        restaurant.setRestaurantId(uid);
        restaurant.setTelephone(phone);
        restaurant.setProfileImageURL(generatedFilePath);
        restaurant.setType(restType);

        firestore.collection("UserProfile")
                .document(uid)
                .set(userProfile)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.d("REGISTER", "VALUE HAS BEEN SAVED IN FIREBASE");

                        firestore.collection("Restaurant")
                                .document(uid)
                                .set(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.rest_main_view, new RestViewProfileFragment())
                                        .addToBackStack(null)
                                        .commit();
                                Log.d("USER", "GOTO RESTAURANT PROFILE");
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
}
