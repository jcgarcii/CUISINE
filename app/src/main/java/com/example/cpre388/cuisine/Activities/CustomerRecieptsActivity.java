package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.APIs.BillAdapter;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class CustomerRecieptsActivity extends AppCompatActivity implements
        BillAdapter.OnRestaurantSelectedListener {

    private static final String TAG = "RESTAURANT_SELECTION";


    private static final int LIMIT = 50;

    private RecyclerView mRestaurantsRecycler;
    private ViewGroup mEmptyView;

    private Query mQuery;

    private BillAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reciepts);

        mRestaurantsRecycler = findViewById(R.id.recycler_restaurants);
        mEmptyView = findViewById(R.id.view_empty);


        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        FirebaseFirestore mFirestore = FirebaseUtil.getFirestore();


        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currUser != null) {
            mQuery = mFirestore.collection("Users")
                    .document(currUser.getUid())
                    .collection("Receipts")
                    .limit(LIMIT);
        }

        initRecyclerView();


    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new BillAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mRestaurantsRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mRestaurantsRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mRestaurantsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsRecycler.setAdapter(mAdapter);
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
    public void onBackPressed(){
        super.onBackPressed();
        Intent customer_main = new Intent(this, activity_customer_main.class);
        startActivity(customer_main);

    }

    @Override
    public void onRestaurantSelected(DocumentSnapshot bill) {
        // Go to the details page for the selected restaurant


        Intent intent = new Intent(this, tip_activity.class);
        intent.putExtra(tip_activity.KEY_ID, bill.getId());

        startActivity(intent);
    }
}