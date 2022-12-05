package com.example.cpre388.cuisine.Activities.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.APIs.ReservationAdapter;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;

import com.example.cpre388.cuisine.databinding.FragmentOwnerBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReservationFragment extends Fragment implements
        View.OnClickListener,
        ReservationAdapter.OnReservationSelectedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int LIMIT = 50;

    private PageViewModel pageViewModel;
    private FragmentOwnerBinding binding;

    private FirebaseFirestore mFirestore;
    private Query mQuery;

    private ReservationAdapter mAdapter;

    private RecyclerView mReservationRecycler;
    private ViewGroup mEmptyView;

    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(getActivity()).get(PageViewModel.class);


        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();

        // Get the most recent resvations:
        mQuery = mFirestore.collection("Reservations")
                .orderBy("num_guests", Query.Direction.DESCENDING)
                .limit(LIMIT);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentOwnerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mReservationRecycler = root.findViewById(R.id.recycler_reservations);
        mEmptyView = root.findViewById(R.id.view_empty);

        initRecyclerView();
        return root;
    }

    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w("RESERVATION_FRAGMENT", "No query, not initializing RecyclerView");
        }

        mAdapter = new ReservationAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    mReservationRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mReservationRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(getView().findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mReservationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mReservationRecycler.setAdapter(mAdapter);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick(View view) {}

    @Override
    public void onReservationSelected(DocumentSnapshot reservation) {
        // Go to the details page for the selected restaurant
        //Intent intent = new Intent(this, RestaurantDetailActivity.class);
        //intent.putExtra(RestaurantDetailActivity.KEY_RESTAURANT_ID, restaurant.getId());

        //startActivity(intent);

    }
}