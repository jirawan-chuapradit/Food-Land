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
import android.widget.Toast;

import com.example.jugjig.foodland.customer.RegisterCustomerFragment;
import com.example.jugjig.foodland.restaurant.RegisterRestFragment;


public class SelectRegisterFragment extends Fragment implements View.OnClickListener {

    private Button cus,rest;

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

        //get parameter
        cus = getView().findViewById(R.id.customer_regis);
        rest = getView().findViewById(R.id.rest_regis);

        //attaching listener
        cus.setOnClickListener(this);
        rest.setOnClickListener(this);
    }

    private void selectRestaurant() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_view,new RegisterRestFragment())
                .commit();
        Log.d("REST", "GOTO Restaurant Register");
    }

    private void selectCustomer(){
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_view,new RegisterCustomerFragment())
                        .commit();
                Log.d("CUSTOMER", "GOTO Customer Register");
    }


    @Override
    public void onClick(View v) {
        if(v == cus){
            Log.d("ROLE =", "CHOOSE CUSTOMER");
            selectCustomer();
        }else if(v == rest){
            Log.d("ROLE =", "CHOOSE RESTAURANT");
            selectRestaurant();
        }
    }
}
