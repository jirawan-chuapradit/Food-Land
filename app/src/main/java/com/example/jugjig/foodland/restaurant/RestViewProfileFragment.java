package com.example.jugjig.foodland.restaurant;

import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.jugjig.foodland.MainActivity;
import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Restaurant;
import com.example.jugjig.foodland.model.UserProfile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    private TextView profileName, profilePhone, profileDesc, profileEmail
            ,profile_rName,profile_rLocation,profile_rOpen,profile_rClose,profile_rType;
    private String uid,fname,lname,phone,desc,email,restName, restLocation, restOpen, restClose, restType;
    Button updateBtn, logoutBtn,updatePasswordBtn;
    ProgressDialog progressDialog;
    ImageView profileImage;
    FirebaseStorage firebaseStorage;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Firebase
        firestore = FirebaseFirestore.getInstance();
        fbAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
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
                        .into(profileImage);
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

    private void setParmeter() {

        getImagePic();

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

        firestore.collection("Restaurant")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Restaurant restaurant = documentSnapshot.toObject(Restaurant.class);
                        restName = restaurant.getName();
                        restType = restaurant.getType();
                        restLocation = restaurant.getLocation();
                        restOpen = restaurant.getOpenTime();
                        restClose = restaurant.getCloseTime();

                        profile_rName.setText(restName);
                        profile_rType.setText(restType);
                        profile_rLocation.setText(restLocation);
                        profile_rOpen.setText(restOpen);
                        profile_rClose.setText(restClose);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void getParameter() {

        profileImage = getView().findViewById(R.id.restProfileImage);
        profileName = getView().findViewById(R.id.fName);
        profileEmail = getView().findViewById(R.id.restEmail);
        profilePhone = getView().findViewById(R.id.restPhone);
        profileDesc = getView().findViewById(R.id.restDesc);

        profile_rName = getView().findViewById(R.id.restName);
        profile_rType = getView().findViewById(R.id.restType);
        profile_rLocation = getView().findViewById(R.id.restLocate);
        profile_rOpen = getView().findViewById(R.id.restOpen);
        profile_rClose = getView().findViewById(R.id.restClose);

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
                .replace(R.id.rest_main_view, new RestUpdatePassword())
                .addToBackStack(null)
                .commit();

        Log.d("RESTAURANT ", "GO TO UPDATE PASSWORD");
    }

    private void logout() {
        SharedPreferences.Editor prefs = getContext().getSharedPreferences("FoodLand",MODE_PRIVATE).edit();
        prefs.clear().commit();
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

        prefs.putString("restName", restName);
        prefs.putString("restType", restType);
        prefs.putString("restOpen", restOpen);
        prefs.putString("restClose", restClose);
        prefs.putString("restLocation", restLocation);
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
