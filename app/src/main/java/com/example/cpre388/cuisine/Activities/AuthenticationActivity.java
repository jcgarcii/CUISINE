package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String SPLASH_SCREEN = "com.example.cpre388.cuisine.Activities.MainActivity";
    private static final String TAG = "AuthenticationActivity";
    private static final String USER_TYPE = "type";

    private static final int RC_SIGN_IN = 9001;

    private ConstraintLayout constraintLayout;
    private String type_selected;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Intent intent = getIntent();
        type_selected = intent.getExtras().getString(SPLASH_SCREEN);
        constraintLayout = findViewById(R.id.auth_layout);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //Animation Stuff:
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //Store User Object onto Firestore:
        Map<String, Object> user = new HashMap<>();
        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid());

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            type_selected = document.getString("type");
                            nextActivity(type_selected);
                        } else {
                            user.put("uid", currUser.getUid());
                            user.put("type", type_selected);
                            user.put("name", "none");
                            //Phone Check:
                            user.put("phone", "none");
                            //Restaurant Pref Check:
                            user.put("favorite_food", "none");
                            userRef.set(user);
                            nextActivity("NEW");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else{
            if(shouldStartSignIn()) {
                startSignIn();
            }
            else{
                currUser = FirebaseAuth.getInstance().getCurrentUser();
                currUser.delete();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }
    }

    private void nextActivity(String t){
        if(t.equals("1")){
            Intent owner = new Intent(this, activity_owner_main.class);
            startActivity(owner);
        }
        else if(t.equals("0")){
            Intent customer = new Intent(this, activity_customer_main.class);
            startActivity(customer);
        }
        else {
            Intent setup = new Intent(this, SettingsActivity.class);
            startActivity(setup);
        }
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseUtil.getAuth().getCurrentUser() == null);
    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = FirebaseUtil.getAuthUI()
                .createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

}
