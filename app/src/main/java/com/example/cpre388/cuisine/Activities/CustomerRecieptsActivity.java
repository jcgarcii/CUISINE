package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.APIs.BillAdapter;
import com.example.cpre388.cuisine.Models.restaurant_model;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.MainActivityViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class CustomerRecieptsActivity extends AppCompatActivity implements
        View.OnClickListener,
        FilterDialogFragment.FilterListener,
        BillAdapter.OnRestaurantSelectedListener {

    private static final String TAG = "RESTAURANT_SELECTION";


    private static final int LIMIT = 50;

    private RecyclerView mRestaurantsRecycler;
    private ViewGroup mEmptyView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private FilterDialogFragment mFilterDialog;
    private BillAdapter mAdapter;

    private MainActivityViewModel mViewModel;

    private FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reciepts);

        mRestaurantsRecycler = findViewById(R.id.recycler_restaurants);
        mEmptyView = findViewById(R.id.view_empty);


        // View model
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();


        currUser = FirebaseAuth.getInstance().getCurrentUser();


        mQuery = mFirestore.collection("Users")
                .document(currUser.getUid())
                .collection("Receipts")
                .limit(LIMIT);
        initRecyclerView();

        // Filter Dialog
        mFilterDialog = new FilterDialogFragment();

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

        // Apply filters
        onFilter(mViewModel.getFilters());

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


/*
    private void onAddItemsClicked() {
        // TODO(developer): Add random restaurants
        CollectionReference store = mFirestore.collection("restaurants");
        restaurant_model n;
        for(int i = 0; i <= 9; i++) {
            n = RestaurantUtil.getRandom(CustomerRecieptsActivity.this);
            store.add(n);
        }
    }

*/

    @Override
    public void onFilter(Filters filters) {
        // TODO(developer): Construct new query
        Query query = mFirestore.collection("restaurants");

        //cilter by category:
        if(filters.hasCategory()){
            query = query.whereEqualTo(restaurant_model.FIELD_CATEGORY, filters.getCategory());
        }
        //Filter by City:
        if(filters.hasCity()){
            query = query.whereEqualTo(restaurant_model.FIELD_CITY, filters.getCity());
        }
        //Filter by Price:
        if(filters.hasPrice()){
            query = query.whereEqualTo(restaurant_model.FIELD_PRICE, filters.getPrice());
        }
        //Sort by Specified Order (orderBy with Direction):
        if(filters.hasSortBy()){
            query = query.orderBy(filters.getSortBy(), filters.getSortDirection());
        }
        //Limit Items:
        query = query.limit(LIMIT);
        //Update the Query:



        //mQuery = query;
        //mAdapter.setQuery(query);


        // Save filters
        mViewModel.setFilters(filters);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_add_items:
                    onAddItemsClicked();
                    break;
                case R.id.menu_sign_out:
                    FirebaseUtil.getAuthUI().signOut(this);
                    //startSignIn();
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_bar:
                onFilterClicked();
                break;
            case R.id.button_clear_filter:
                onClearFilterClicked();
        }
    }

    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
        //startActivity(new Intent(MainActivity.this, activity_customer_main.class));
    }

    public void onClearFilterClicked() {
        mFilterDialog.resetFilters();

        onFilter(Filters.getDefault());
    }


    @Override
    public void onRestaurantSelected(DocumentSnapshot bill) {
        // Go to the details page for the selected restaurant


        Intent intent = new Intent(this, tip_activity.class);
        intent.putExtra(tip_activity.KEY_ID, bill.getId());

        startActivity(intent);
    }
}