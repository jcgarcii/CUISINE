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

public class MainActivity extends AppCompatActivity {
    private static final String SPLASH_SCREEN = "com.example.cpre388.cuisine.Activities.MainActivity";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    @Override
    protected void onResume(){
        super.onResume();
        videoview.start();
        lefestin.start();
    }

    private Runnable first_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                greeting1.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    private Runnable second_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                greeting2.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    private Runnable third_greeting(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
            }
        };
        return runnable;
    }

    private void onCustomerPressed(View view){
        Boolean userType = false;
        Intent customer = new Intent(this, AuthenticationActivity.class);
        customer.putExtra(SPLASH_SCREEN, userType);
        startActivity(customer);
    }

    private void onOwnerPressed(View view){
        Boolean userType = true;
        Intent owner = new Intent(this, AuthenticationActivity.class);
        owner.putExtra(SPLASH_SCREEN, userType);
        startActivity(owner);
    }


    @Override
    protected void onPause() {
        super.onPause();
        lefestin.stop();
        videoview.stopPlayback();
    }
}
