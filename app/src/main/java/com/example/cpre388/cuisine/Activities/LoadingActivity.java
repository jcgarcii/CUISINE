package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoadingActivity extends AppCompatActivity {
    private static final String NEW_SETTINGS = "com.example.cpre388.cuisine.activities.SETTINGS";

    private ConstraintLayout constraintLayout;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    private MainActivityViewModel mViewModel;

    //FireStore Documents:
    DocumentSnapshot document;
    DocumentReference userRef;

    //Firestore Fields:
    String mUser_type;
    String mName;
    String mNumber;
    String mFavorite_restaurant;


    //Intent Document Fields:
    String[] _changes;
    String user_type;
    String name;
    String number;
    String favorite_restaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        //Get Previous Array:
        /*
            _changes[0] = _m_name;
            _changes[1] = _m_phone;
            _changes[2] = _m_fav_restaurant;
            _changes[3] = _m_type;
         */

        _changes = new String[4];
        Intent intent = getIntent();
        _changes = intent.getExtras().getStringArray(NEW_SETTINGS);
        name = _changes[0];
        number = _changes[1];
        favorite_restaurant = _changes[2];
        user_type = _changes[3];

        constraintLayout = findViewById(R.id.auth_layout);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        //Animation Stuff:
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        mFirestore = FirebaseUtil.getFirestore();
        Map<String, Object> user_obj = new HashMap<>();


        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            userRef = mFirestore.collection("Users").document(currUser.getUid()).collection("Preferences").document("Settings");

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        document = task.getResult();
                        if (document.exists()) {
                            mName = document.getString("name");
                            if(!name.equals("NULL")){
                                user_obj.put("name", name);
                            }

                            mNumber = document.getString("phone");
                            if(!number.equals("NULL")){
                                user_obj.put("phone", number);
                            }


                            mFavorite_restaurant = document.getString("favorite_food");
                            if(!favorite_restaurant.equals("NULL")){
                                user_obj.put("favorite_food", favorite_restaurant);

                            }

                            mUser_type = document.getString("type");
                            if(!user_type.equals("NULL")){
                                user_obj.put("type", user_type);
                            }
                        }
                        else {
                            user_obj.putIfAbsent("name", name);
                            user_obj.putIfAbsent("phone", number);
                            user_obj.putIfAbsent("favorite_food", favorite_restaurant);
                            user_obj.putIfAbsent("type", user_type);
                        }
                        userRef.update(user_obj);
                        nextActivity();
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

    }

    private void nextActivity(){
        Intent intent = new Intent(this, activity_customer_main.class);
        startActivity(intent);
    }

}
