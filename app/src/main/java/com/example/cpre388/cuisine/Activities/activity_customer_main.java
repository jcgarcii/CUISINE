package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.cpre388.cuisine.R;
import com.google.android.material.navigation.NavigationView;


public class activity_customer_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    AppCompatButton reserve, request;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        //Assign XML Objects:
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar_customer_main);
        logo = findViewById(R.id.customer_main_activity_logo);
        request = findViewById(R.id.RequestButton);
        reserve = findViewById(R.id.ReserveButton);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_home);

        logo.setImageResource(R.drawable.logo);
        request.setOnClickListener(this::onRequestClicked);
        reserve.setOnClickListener(this::onReservedClicked);
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
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}