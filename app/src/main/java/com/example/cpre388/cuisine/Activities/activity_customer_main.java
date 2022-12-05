package com.example.cpre388.cuisine.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.APIs.ReservationAdapter;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Calendar;


public class activity_customer_main extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ReservationAdapter.OnReservationSelectedListener {

    public final String EDIT_RESERVATION = "reservation_edit_key";
    private final static String SUCCESSFUL_EDIT_DETAILS = "com.example.cpre388.cuisine.Activities.EDITRESERVATION";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    AppCompatButton reserve, request;
    ImageView logo;

    //time selection variables:
    private int mYear, mMonth, mDay, mHour, mMinute;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private FirebaseUser currUser;

    private ReservationAdapter mAdapter;

    private RecyclerView mReservationRecycler;
    private ViewGroup mEmptyView;

    private static final int LIMIT = 1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        //Get Current Time:
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //Hours Format:
        String hr = String.format("%d", mHour);
        if (hr.length() == 1) {
            String i = "0" + hr;
            hr = i;
        }
        //Minute Format:
        String min = String.format("%d", mMinute);
        String _min = "";
        char tens = min.charAt(0);
        if (Integer.parseInt(String.valueOf(tens)) >= 3) {
            _min = "30";
        } else {
            _min = "00";
        }

        String selected_time = String.format("%s%s", hr, _min);

        //Assign XML Objects:
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar_customer_main);
        logo = findViewById(R.id.customer_main_activity_logo);
        request = findViewById(R.id.RequestButton);
        reserve = findViewById(R.id.ReserveButton);

        mReservationRecycler = findViewById(R.id.recycler_reservations_cust);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            // Get the most recent resvations:
            mQuery = mFirestore.collection("Reservations")
                    .whereEqualTo("uid", currUser.getUid())
                    .orderBy("reservation_time", Query.Direction.DESCENDING)
                    .limit(LIMIT);
        }

        logo.setImageResource(R.drawable.logo);
        request.setOnClickListener(this::onRequestClicked);
        reserve.setOnClickListener(this::onReservedClicked);
        initRecyclerView();
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("RESERVATION_FRAGMENT", "No query, not initializing RecyclerView");
        }

        mAdapter = new ReservationAdapter(mQuery, (ReservationAdapter.OnReservationSelectedListener) this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mReservationRecycler.setVisibility(View.GONE);
                    //mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mReservationRecycler.setVisibility(View.VISIBLE);
                    //mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mReservationRecycler.setLayoutManager(new LinearLayoutManager(this));
        mReservationRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void onReservedClicked(View view){
        Intent reservation_start = new Intent(this, RestaurantSelectionActivity.class);
        startActivity(reservation_start);
    }

    private void onRequestClicked(View view){
        startActivity(new Intent(activity_customer_main.this, CustomerRequestActivity.class));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_reciept:
                Intent intent = new Intent(this, CustomerRecieptsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_review:
                break;
            case R.id.nav_calculator:
                Intent calcIntent = new Intent(this, tip_activity.class);
                startActivity(calcIntent);
                break;
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onReservationSelected(DocumentSnapshot reservation) {
        String[] edit = new String[5];

        edit[0] = reservation.getString("table_selected");
        edit[1] = reservation.getString("room_selected");
        edit[2] = reservation.getString("restaruant_id");
        edit[3] = reservation.getString("reservation_time");
        edit[4] = reservation.getId().toString();

        Intent intent = new Intent(this, EditReservationActivity.class);
        //Passes the table selection and the room selection arr[0] arr[1] respectively
        //arr[3] contains the restaurant id for documentation purposes
        //arr[4] contains the selected time from the previous activity
        intent.putExtra(EDIT_RESERVATION, edit);
        startActivity(intent);


    }
}