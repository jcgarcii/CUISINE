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

/**
 * Customer's Main Hub Activity:
 *
 * Allows users to make reservations, view their current active reservation, edit their reservation, or make requests - and make changes to their account
 */
public class activity_customer_main extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ReservationAdapter.OnReservationSelectedListener {

    public final String EDIT_RESERVATION = "reservation_edit_key";
    public final String REQ_RESERVATION = "request_edit_key";
    private final static String SUCCESSFUL_EDIT_DETAILS = "com.example.cpre388.cuisine.Activities.EDITRESERVATION";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    AppCompatButton reserve;
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

    /**
     * Activity's onCreate() method, initializes view objects and retrieves the current reservation from Firestore
     * @param savedInstanceState - bundle
     */
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

        //Retrieve most recent reservation
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            // Get the most recent resvations:
            mQuery = mFirestore.collection("Reservations")
                    .whereEqualTo("uid", currUser.getUid())
                    //.orderBy("reservation_time", Query.Direction.DESCENDING)
                    .limit(LIMIT);
        }

        logo.setImageResource(R.drawable.logo);
        reserve.setOnClickListener(this::onReservedClicked);
        initRecyclerView();
    }

    /**
     * Initializes the recycler view, shows only one reservation - the active one
     */
    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("RESERVATION_FRAGMENT", "No query, not initializing RecyclerView");
        }

        mAdapter = new ReservationAdapter(mQuery, (ReservationAdapter.OnReservationSelectedListener) this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    //mReservationRecycler.setVisibility(View.GONE);
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

    /**
     * Drawer Menu controls
     */
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    /**
     * Initializes Reservation Activities to allow user to make resrvations
     * @param view - View
     */
    private void onReservedClicked(View view){
        Intent reservation_start = new Intent(this, RestaurantSelectionActivity.class);
        startActivity(reservation_start);
    }

    /**
     * Drawer Menu Options
     * @param item - selected item
     * @return - returns on success
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_reciept:
                Intent intent = new Intent(this, CustomerRecieptsActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Starts reservatoin listner for updates
     */
    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    /**
     * Stops reservation adapter
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    /**
     * Decides whtat to do with the reservation if active:
     * 1: Allows the User to make updates to their reservation if reservation is greather than 30 minutes
     * 2: Allows user to make requests once reservation has started
     * @param reservation - current reservation document
     */
    @Override
    public void onReservationSelected(DocumentSnapshot reservation) {
        String[] edit = new String[5];
        String res_time = reservation.getString("reservation_time");
        //Intent Variables:
        edit[0] = reservation.getString("table_selected");
        edit[1] = reservation.getString("room_selected");
        edit[2] = reservation.getString("restaruant_id");
        edit[3] = res_time;
        edit[4] = reservation.getId().toString();

        //Retrieves current time
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
        String current_time = String.format("%s%s", hr, _min);

        int sel_time = Integer.valueOf(res_time);
        int curr_time = Integer.valueOf(current_time);
        int difference = sel_time - curr_time;
        /*
            Time logic, launches activities - or lets user know that their reservation is no longer active (if restaurant hasn't billed them)
        */
        if(difference > 30){
            Intent intent = new Intent(this, EditReservationActivity.class);
            //Passes the table selection and the room selection arr[0] arr[1] respectively
            //arr[3] contains the restaurant id for documentation purposes
            //arr[4] contains the selected time from the previous activity
            intent.putExtra(EDIT_RESERVATION, edit);
            //System.out.println(String.format("Difference: %d", difference));
            startActivity(intent);
        }else if((difference <= 30) && (difference > -30)){
            Intent reqIntent = new Intent(this, CustomerRequestActivity.class);
            reqIntent.putExtra(REQ_RESERVATION, edit);
            //System.out.println(String.format("Difference: %d", difference));
            startActivity(reqIntent);
        }else{
            Snackbar.make(findViewById(android.R.id.content),
                    "Reservation too Early or inactive, call restaurant to confirm", Snackbar.LENGTH_LONG).show();
        }
    }
}