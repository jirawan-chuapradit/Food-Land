package com.example.jugjig.foodland.Reservations;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.model.Reservation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ReservationsFragment extends Fragment implements OnMapReadyCallback {
    private SQLiteDatabase myDB;
    private Bundle _bundle;
    private Reservation reservation;
    private MapView mapView;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getRestaurantId();
//        getCustomerId();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.reservation_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void getRestaurantId(){
        _bundle = getArguments();
        if( _bundle != null ) {
            reservation.setRestaurantId(_bundle.getString("restaurantId"));
        } else {
            // Error dialog
        }
    }

    private void getCustomerId(){
        myDB = getActivity().openOrCreateDatabase("foodland.db", Context.MODE_PRIVATE, null);
        Cursor myCursor = myDB.rawQuery("SELECT * FROM user", null);
        myCursor.moveToNext();
        reservation.setCustomerId(myCursor.getString(0));
        //            Log.d("RESERVATION", "");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(13, 100);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
