package com.example.cpre388.cuisine.Activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.cpre388.cuisine.Models.user_model;
import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    private user_model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
/*
        Map<String, Object> user_obj = new HashMap<>();
        mFirestore = FirebaseUtil.getFirestore();
        LocalTime local = LocalTime.now();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid_time = currUser.getUid() + local.toString();

            DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid()).collection("Preferences").document(uid_time);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                        } else {
                            model = new user_model(currUser.getUid());

                            user_obj.put("user_preferences", model);
                            userRef.set(user_obj);
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    */
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}