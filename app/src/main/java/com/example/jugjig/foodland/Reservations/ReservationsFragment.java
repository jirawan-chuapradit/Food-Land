package com.example.jugjig.foodland.Reservations;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jugjig.foodland.R;
import com.example.jugjig.foodland.customer.HomeFragment;
import com.example.jugjig.foodland.model.Reservation;
import com.example.jugjig.foodland.model.Restaurant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationsFragment extends Fragment implements OnMapReadyCallback {
    private SQLiteDatabase myDB;
    private Bundle _bundle;
    private Reservation reservation;
    private Restaurant restaurant;
    private MapView mapView;
    private GoogleMap mMap;
    private ImageButton _addAmont, _subAmont, _addTime, _subtime;
    private String[] parts;
    private Integer amont_new;
    private TextView _amont, _time, _date, _date_select, _name, _phone, _confirmBtn;
    private String time_new;
    private final Integer DIFF_TIME = 15;
    private final SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d/M/y");
    private Format formatter = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));
    private final Calendar calendar = Calendar.getInstance();
    private Date setData;
    private String date, _uuid;
    private ImageView _backBtn;
    private FirebaseFirestore _firestore;
    private DocumentReference _documentReference;
    private EditText _comment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _firestore = FirebaseFirestore.getInstance();
        restaurant = new Restaurant();
        reservation = new Reservation();
        getRestaurant();
        getCustomerId();
        setInitValue();
        addAmont();
        subAmont();
        addTime();
        subTime();
        datePicker();
        backBtn();
        confirmBtn();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Init view
        _amont = getView().findViewById(R.id.reservation_amont);
        _addAmont = getView().findViewById(R.id.reservation_amont_add);
        _subAmont = getView().findViewById(R.id.reservation_amont_sub);
        _time = getView().findViewById(R.id.reservation_time);
        _addTime = getView().findViewById(R.id.reservation_time_add);
        _subtime = getView().findViewById(R.id.reservation_time_sub);
        _date = getView().findViewById(R.id.reservation_date);
        _date_select = getView().findViewById(R.id.reservation_date_select);
        _name = getView().findViewById(R.id.reservation_restaurant_name);
        _phone = getView().findViewById(R.id.reservation_restaurant_phone);
        _backBtn = getView().findViewById(R.id.reservation_back_btn);
        _confirmBtn = getView().findViewById(R.id.reservation_confirm);
        _comment = getView().findViewById(R.id.reservation_comment);

        mapView = view.findViewById(R.id.reservation_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        // Firebase
        _firestore = FirebaseFirestore.getInstance();
    }

    private void getRestaurant(){
        _bundle = getArguments();
        if( _bundle != null ) {
            restaurant.setRestaurantId(_bundle.getString("restaurantId"));
            restaurant.setName(_bundle.getString("restaurantName"));
            restaurant.setOpenTime(_bundle.getString("restaurantOpen"));
            restaurant.setCloseTime(_bundle.getString("restaurantClose"));
            restaurant.setLatitude(_bundle.getDouble("restaurantLat"));
            restaurant.setLongitude(_bundle.getDouble("restaurantLng"));
            restaurant.setTelephone(_bundle.getString("restaurantPhone"));

            reservation.setRestaurantId(_bundle.getString("restaurantId"));
        } else {
            // Error dialog
        }
    }

    private void getCustomerId(){
        myDB = getActivity().openOrCreateDatabase("foodland.db", Context.MODE_PRIVATE, null);
        Cursor myCursor = myDB.rawQuery("SELECT * FROM user", null);
        myCursor.moveToNext();
        reservation.setCustomerId(myCursor.getString(1));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void addAmont() {
        _addAmont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parts = _amont.getText().toString().split(" ");
                amont_new = Integer.parseInt(parts[0]) + 1;
                _amont.setText(amont_new.toString() + " " + parts[1]);
                if ( amont_new == 2 ) {
                    _subAmont.setEnabled(true);
                    _subAmont.setClickable(true);
                    _subAmont.setImageResource(R.drawable.sub);
                }
            }
        });
    }

    private void subAmont() {
        _subAmont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parts = _amont.getText().toString().split(" ");
                amont_new = Integer.parseInt(parts[0]) - 1;
                _amont.setText(amont_new.toString() + " " + parts[1]);
                if ( amont_new == 1 ) {
                    _subAmont.setEnabled(false);
                    _subAmont.setClickable(false);
                    _subAmont.setImageResource(R.drawable.sub_gray);
                }
            }
        });
    }

    private void addTime() {
        _addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parts = _time.getText().toString().split(" ");
                try {
                    Date d = df.parse(parts[0]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, DIFF_TIME);
                    time_new = df.format(cal.getTime());
                    _time.setText(time_new + " น");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void subTime() {
        _subtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parts = _time.getText().toString().split(" ");
                try {
                    Date d = df.parse(parts[0]);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    cal.add(Calendar.MINUTE, -DIFF_TIME);
                    time_new = df.format(cal.getTime());
                    _time.setText(time_new + " น");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void datePicker() {
        _date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
//                Log.d("RES", yy+ "--"+mm+"--"+dd);
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            monthOfYear += 1;
                            setData = simpleDateFormat.parse(String.format("%d/%d/%d", dayOfMonth, monthOfYear, year));
                            date = formatter.format(setData);
                            _date.setText(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
    }

    private void setInitValue() {
        _subAmont.setEnabled(false);
        _subAmont.setClickable(false);
//        _subtime.setEnabled(false);
//        _subtime.setClickable(false);

        _name.setText(restaurant.getName());
        _phone.setText(restaurant.getTelephone());

        try {
            _time.setText(restaurant.getOpenTime() + " น");
            _amont.setText("1 ที่นั่ง");
            setData = simpleDateFormat.parse(String.format("%d/%d/%d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR)));
            date = formatter.format(setData);
            _date.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void backBtn() {
        _backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.cus_main_view, new HomeFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void confirmBtn() {
        _confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _documentReference = _firestore.collection("Reservations").document();

                reservation.setAmount(Integer.parseInt(_amont.getText().toString().split(" ")[0]));
                reservation.setComment(_comment.getText().toString());
                reservation.setDate(_date.getText().toString());
                reservation.setStatus("pending");
                reservation.setTime(_time.getText().toString().split(" ")[0]);
                reservation.setReservationId(_documentReference.getId());

                _documentReference.set(reservation).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.cus_main_view, new HomeFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                });
            }
        });
    }
}
