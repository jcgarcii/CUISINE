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

public class SplashScreenActivity extends AppCompatActivity {
    private static final String SPLASH_SCREEN = "com.example.cpre388.cuisine.Activities.MainActivity";
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private void onUser(){
        Intent intent;
        intent = new Intent(this, AuthenticationActivity.class);
        //Default is customer, however it'll pull their own type from firestore
        intent.putExtra(SPLASH_SCREEN, "0");
        startActivity(intent);
    }

    private void onNewUser(){
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

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

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseUtil.getAuth().getCurrentUser() == null);
    }
    private void startSignIn(){
        onNewUser();
    }

}
