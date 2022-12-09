package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
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

/**
 * Fragment Parent for the Fragment Tabs
 */
public class OwnerActivity extends AppCompatActivity {
    public static final String KEY_RESTAURANT_ID_OWNER = "key_restaurant_id_owner";
    private ActivityOwnerBinding binding;
    private OwnerActivityViewModel ownerActivityViewModel;
    private String getKeyRestaurantId;

    /**
     * onCreate() method
     *
     * retrieves the user's restaurant id of which they own
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownerActivityViewModel = new ViewModelProvider(this).get(OwnerActivityViewModel.class);

        Bundle getExtras = getIntent().getExtras();
        if(getExtras != null) {
            getKeyRestaurantId = getExtras.getString(KEY_RESTAURANT_ID_OWNER);
        }else{
            Log.d("OwnerActivity", "No Extras were Pulled");
        }
        ownerActivityViewModel.setRestaurant_id(getKeyRestaurantId);

        binding = ActivityOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setVisibility(View.INVISIBLE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}