package com.example.cpre388.cuisine.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.example.cpre388.cuisine.APIs.RestaurantAdapter;
import com.example.cpre388.cuisine.Models.restaurant_model;
import com.example.cpre388.cuisine.Util.RestaurantUtil;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Collections;


/**
 * Main Activity (if new user, prompts users to log in and select which type of user they are)
 */
public class MainActivity extends AppCompatActivity {
    private static final String SPLASH_SCREEN = "com.example.cpre388.cuisine.Activities.MainActivity";

    private static final int RC_SIGN_IN = 9001;
    private MainActivityViewModel mViewModel;

    private LinearLayout linearLayout;
    private AnimationDrawable animationDrawable;
    private TextView greeting1, greeting2, greeting3;
    private VideoView videoview;
    private MediaPlayer lefestin;
    private MediaController.MediaPlayerControl le;
    private ImageView mainlogo;
    private AppCompatButton customer_btn, owner_btn;

    //Handlers for animations:
    private Handler first_animation, sec_animation, third_animation;

    /**
     * Prompts user login,
     * plays the backgroud view and the music
     *
     * @param savedInstanceState - view
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){startSignIn();}

            //Buttons:
        customer_btn = findViewById(R.id.CUSTOMER_btn);
        owner_btn = findViewById(R.id.RESTAURANT_btn);

        //Presentation:
        mainlogo = findViewById(R.id.welcome_activity_logo);
        greeting1 = findViewById(R.id.greeting_1);
        greeting1.setVisibility(View.INVISIBLE);
        greeting2 = findViewById(R.id.greeting_2);
        greeting2.setVisibility(View.INVISIBLE);
        linearLayout = findViewById(R.id.splash_header_bottom);

        //Background Music:
        lefestin = MediaPlayer.create(MainActivity.this,R.raw.jaz);

        //Background Video:
        videoview = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg);
        videoview.setVideoURI(uri);
        videoview.start();

        customer_btn.setOnClickListener(this::onCustomerPressed);
        owner_btn.setOnClickListener(this::onOwnerPressed);

        //Logo and Cool text fade in:
        mainlogo.setImageResource(R.drawable.logo);
        first_animation = new Handler();
        first_animation.postDelayed(first_greeting(), 2000);
        sec_animation = new Handler();
        sec_animation.postDelayed(second_greeting(), 4000);
        third_animation = new Handler();
        third_animation.postDelayed(third_greeting(), 6000);

        //Loop background video:
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    /**
     * Starts video and music
     */
    @Override
    protected void onResume(){
        super.onResume();
        videoview.start();
        lefestin.start();
    }

    /**
     * Text animation
     * @return - returns runnable
     */
    private Runnable first_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                greeting1.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    /**
     * Second text animation
     * @return - animation runnable
     */
    private Runnable second_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                greeting2.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    /**
     * Third text animation
     * @return - animation runnable
     */
    private Runnable third_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    /**
     * Sets new user as a customer, otherwise will send the user to the authentication activity to retrieve their user type
     * @param view
     */
    private void onCustomerPressed(View view){
        String userType = "0";
        Intent customer = new Intent(this, AuthenticationActivity.class);
        customer.putExtra(SPLASH_SCREEN, userType);
        startActivity(customer);
    }

    /**
     * Sets new user as a restaurant, otherwise wwill send the user to the authentication activity to retrieve their user type
     * @param view
     */
    private void onOwnerPressed(View view){
        String userType = "1";
        Intent owner = new Intent(this, AuthenticationActivity.class);
        owner.putExtra(SPLASH_SCREEN, userType);
        startActivity(owner);
    }

    /**
     * Pauses music and video onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        lefestin.stop();
        videoview.stopPlayback();
    }

    /**
     * Starts signing, if they make it to this activity they were not logged in.
     */
    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = FirebaseUtil.getAuthUI()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                //.setTheme(R.style.LoginTheme)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
    }
}
