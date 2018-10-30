package com.example.jugjig.foodland;

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
import android.widget.Toast;



public class SelectRegisterFragment extends Fragment {

    public View onCreateView
            (@NonNull LayoutInflater inflater,
             @Nullable ViewGroup container,
             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_register, container, false);
    }

    @Override
    public void onActivityCreated
            (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectCustomer();
        selectRestaurant();
    }

    void selectCustomer(){

        Button cus = getView().findViewById(R.id.customer_regis);
        cus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new RegisterCustomerFragment())
                        .commit();
                Log.d("USER", "GOTO Register");
                Toast.makeText
                        (getContext(),"GO TO Register",Toast.LENGTH_SHORT)
                        .show();
            }
        });


    }

    void selectRestaurant(){

    }
}
