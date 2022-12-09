package com.example.cpre388.cuisine.Activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.MainActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * App's Splash Screen
 *
 * If returning user, launch authentication activity
 * If new user, launch main actvity (the one with music) and allows user to select details
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static final String SPLASH_SCREEN = "com.example.cpre388.cuisine.Activities.MainActivity";
    private MainActivityViewModel mViewModel;

    /**
     * onCreate() method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    //if user exists, launch authentication activity to pull their data
    private void onUser(){
        Intent intent;
        intent = new Intent(this, AuthenticationActivity.class);
        //Default is customer, however it'll pull their own type from firestore
        intent.putExtra(SPLASH_SCREEN, "0");
        startActivity(intent);
    }
    //if new user, launch main activity and start new user process
    private void onNewUser(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Checks if a user is logged in, starts previous methods based on the status
     */
    @Override
    public void onStart() {
        super.onStart();
        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }
        onUser();
    }

    /**
     * Checks if there is a user
     * @return - true if user needs to login
     */
    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseUtil.getAuth().getCurrentUser() == null);
    }

    /**
     * Calls new user method for a new user
     */
    private void startSignIn(){
        onNewUser();
    }

}
