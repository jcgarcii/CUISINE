package com.example.cpre388.cuisine.Activities.ui.main;

import android.content.Intent;
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
import com.example.cpre388.cuisine.Activities.BillingActivity;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;

import com.example.cpre388.cuisine.databinding.FragmentOwnerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.Billing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


/**
 * Reservation Fragment shows the Restaurant all upcoming reservations for their restaurant
 */
public class ReservationFragment extends Fragment implements
        View.OnClickListener,
        ReservationAdapter.OnReservationSelectedListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int LIMIT = 50;
    private static final String RESERV_BILLING_KEY = "key_reservation_billing";

    private PageViewModel pageViewModel;
    private boolean ready;
    private FragmentOwnerBinding binding;
    private String rest_id;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private FirebaseUser currUser;

    private ReservationAdapter mAdapter;

    private RecyclerView mReservationRecycler;
    private ViewGroup mEmptyView;

    /**
     * Empty Public Constructor
     */
    public ReservationFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate() for the fragment, starts query for the restaurant's reservations
     * @param savedInstanceState - bundle items
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(getActivity()).get(PageViewModel.class);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        ready = false;

        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the most recent reservations:
        mQuery = mFirestore.collection("Reservations")
                .orderBy("reservation_time")
                .limit(LIMIT);

    }

    /**
     * onCreateView() for the fragment, sets view objects
     * @param inflater -
     * @param container -
     * @param savedInstanceState -
     * @return - view root
     */
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

    /**
     * Initializes the reservation recycler, showcases all of the reservations for the restaurant
     */
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

    /**
     * onStart() - starts reservation adapter
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
     * Stops listening on the reservation adapter
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    /**
     * onDestroyView() method
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick(View view) {}

    /**
     * Starts Billing Activity to generate user's receipt
     * @param reservation - selected reservation
     */
    @Override
    public void onReservationSelected(DocumentSnapshot reservation) {
        String[] _reservation = new String[7];
        _reservation[0] = reservation.getString("reservation_for");
        _reservation[1] = reservation.getString("uid");
        _reservation[2] = reservation.getString("restaruant_id");
        _reservation[3] = reservation.getId();
        _reservation[4] = reservation.getString("reservation_time");
        _reservation[5] = reservation.getString("room_selected");
        _reservation[6] = reservation.getString("table_selected");

        //Bill the Customer
        Intent intent = new Intent(getActivity(), BillingActivity.class);
        intent.putExtra(RESERV_BILLING_KEY, _reservation);

        startActivity(intent);

    }
}