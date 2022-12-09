package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.cpre388.cuisine.Models.restaurant_model;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.Util.RestaurantUtil;
import com.example.cpre388.cuisine.ViewModels.OwnerActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.acl.Owner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Owner Main Hub Activity.
 *
 * Allows Owner to:
 * 1. Manage their restaurant
 * 2. Edit their Settings
 * 3. Edit their layout
 */
public class activity_owner_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String KEY_RESTAURANT_ID_OWNER = "key_restaurant_id_owner";
    private static final String EDIT_ROOM_ITERATION = "key_current_room_itr";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    AppCompatButton supervise;
    ImageView logo;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    private String restaurant_id;
    //private OwnerActivityViewModel ownerActivityViewModel;
    private int ready;
    //Checks if user has a restaurant setup:
    private boolean nullRestaurant;

    /**
     * Activity's onCreate() method, initializes view models
     *
     * Retrieves the user's restuaurant ID for which they own, otherwise it'll create a new restaurant
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_main);

        //Assign XML Objects:
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar_owner_main);
        logo = findViewById(R.id.customer_main_activity_logo);
        supervise = findViewById(R.id.supervise_restaurant);

        ready = 0;

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);

        mFirestore = FirebaseUtil.getFirestore();

        //Retrieves Restaurant ID, if new owner - creates new restaurant:
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid()).collection("Restaurants").document("Ownership");

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            restaurant_id = document.getString("res_id");
                            //System.out.println(restaurant_id);
                            ready++;

                        } else {
                            restaurant_id = "res" + currUser.getUid();
                            //create a new restaurant for the user:
                            restaurant_model n = RestaurantUtil.getRandom(activity_owner_main.this);
                            DocumentReference newRest = mFirestore.collection("restaurants").document(restaurant_id);
                            newRest.set(n);
                            //set the restaurant as theirs:
                            Map<String, Object> ownership = new HashMap<>();
                            ownership.put("res_id", restaurant_id);
                            userRef.set(ownership);
                            //give random layout:
                            setRandomLayout(restaurant_id);

                            ready++;
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });


        }

        logo.setImageResource(R.drawable.logo);
        supervise.setOnClickListener(this::onSuperviseClicked);
    }

    /**
     * Menu Drawer Controls
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
     * If supervise is clicked, it'll launch the management portion of the program if data is ready
     * @param view
     */
    private void onSuperviseClicked(View view){
        if(!(ready >0)) {return;}
        else{
            Intent reservation_start = new Intent(this, OwnerActivity.class);
            reservation_start.putExtra(KEY_RESTAURANT_ID_OWNER, restaurant_id);
            startActivity(reservation_start);
        }
    }

    /**
     * Menu navigation controls
     * @param item - option selected by user
     * @return - true on success
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_restaurants:
                Intent rest = new Intent(this, EditLayoutActivity.class);
                rest.putExtra(KEY_RESTAURANT_ID_OWNER, restaurant_id);
                rest.putExtra(EDIT_ROOM_ITERATION, 1);
                startActivity(rest);
                break;
            case R.id.settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Sets a random layout for the restaurant's rooms if new owner
     * @param res_id - restaurant id for the newly created restaurant
     */
    public void setRandomLayout(String res_id){
        String[] times = getResources().getStringArray(R.array.times);
        for(int ti = 0; ti < times.length; ti++){
            setTimes(times[ti]);
        }
    }

    /**
     * Sets up all times for the new restaurant based on random layout
     * @param curr - current time
     */
    private void setTimes(String curr){
        List<String> _room_1 = Arrays.asList("1", "1", "1",
                "0", "0", "0",
                "1", "1", "1",
                "0", "0", "0");
        List<String> _room_2 = Arrays.asList("1", "1", "1",
                "0", "0", "0",
                "1", "1", "1",
                "0", "0", "0");
        List<String> _room_3 = Arrays.asList("1", "1", "1",
                "0", "0", "0",
                "1", "1", "1",
                "0", "0", "0");
        List<String> _room_4 = Arrays.asList("1", "1", "1",
                "0", "0", "0",
                "1", "1", "1",
                "0", "0", "0");

        Map<String, Object> layout = new HashMap<>();

        DocumentReference layoutRef = mFirestore.collection("restaurants").document(restaurant_id).collection("Layouts").document(curr);

        layout.put("Room 1", _room_1);
        layout.put("Room 2", _room_2);
        layout.put("Room 3", _room_3);
        layout.put("Room 4", _room_4);
        layoutRef.set(layout);
    }
}