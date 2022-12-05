package com.example.cpre388.cuisine.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.cpre388.cuisine.Activities.ui.main.SectionsPagerAdapter;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.OwnerActivityViewModel;
import com.example.cpre388.cuisine.databinding.ActivityOwnerBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnerActivity extends AppCompatActivity {
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    private String restaurant_id;
    private OwnerActivityViewModel ownerActivityViewModel;

    private ActivityOwnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ownerActivityViewModel = new ViewModelProvider(this).get(OwnerActivityViewModel.class);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setVisibility(View.INVISIBLE);

        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid()).collection("Restaurants").document("Ownership");

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            restaurant_id = document.getString("res_id");
                            ownerActivityViewModel.setRestaurant_id(restaurant_id);

                        } else {
                            //implement a new activity to create a new restaurant
                            //TODO
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}