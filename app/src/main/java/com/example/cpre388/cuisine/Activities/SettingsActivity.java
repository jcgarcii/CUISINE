package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private static final String NEW_SETTINGS = "com.example.cpre388.cuisine.activities.SETTINGS";

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;
    private AppCompatButton submit_btn;

    private LocalTime local;
    private int flag;

    //FireStore Documents:
    DocumentSnapshot document;
    DocumentReference userRef;

    //FireStore Document Fields:
    String user_type;
    String name;
    String phone;
    String favorite_restaurant;

    //Track Changes:
    String _m_fav_restaurant;
    String _m_type;
    String _m_name;
    String _m_phone;

    //Intent Array
    String[] _changes;

    //TextView and EditView Stuff:
    EditText _name_txt, _phone_txt;
    TextView _rest_pref_txt, _user_type_txt;

    //Spinner Stuff:
    private Spinner restaurant_spinner;
    private Spinner type_spinner;
    private String[] mAvailable_user_list;
    private String[] mRestaurant_list;
    private ArrayList<String> available_user_list;
    private ArrayList<String> restaurant_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //View Objects:
        _name_txt = findViewById(R.id.editTextTextPersonName);
        _phone_txt = findViewById(R.id.editTextPhone);
        _rest_pref_txt = findViewById(R.id.current_fav_rest);
        _user_type_txt = findViewById(R.id.current_user_type);
        submit_btn = findViewById(R.id.save_settings_btn);

        //Intent Array:
        _changes = new String[4];

        //While Loading:
        _name_txt.setText("Loading...");
        _phone_txt.setText("Loading...");

        //Prepare Spinner List for Table Selection:
        restaurant_spinner = (Spinner) this.findViewById(R.id.change_user_pref);
        type_spinner = (Spinner) this.findViewById(R.id.change_user_type_spinner);

        mAvailable_user_list = getResources().getStringArray(R.array.user_types);
        mRestaurant_list = getResources().getStringArray(R.array.categories);

        available_user_list = new ArrayList<String>(Arrays.asList(mAvailable_user_list));
        restaurant_list = new ArrayList<String>(Arrays.asList(mRestaurant_list));


        ArrayAdapter<String> user_adapter = new ArrayAdapter<String>(this, R.layout.table_reservation_spinner,available_user_list);
        type_spinner.setAdapter(user_adapter);

        ArrayAdapter<String> res_adapter = new ArrayAdapter<String>(this, R.layout.table_reservation_spinner,restaurant_list);
        restaurant_spinner.setAdapter(res_adapter);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = type_spinner.getSelectedItem().toString();
                _m_type = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                _m_type = "NULL";
                Log.d("WELL", "Nothing was Selected");
            }
        });

        restaurant_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = restaurant_spinner.getSelectedItem().toString();
                _m_fav_restaurant = selected;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                _m_fav_restaurant = "NULL";
                Log.d("WELL", "Nothing was Selected");
            }
        });

        submit_btn.setOnClickListener(this::on_submit);

        flag = 0;

        mFirestore = FirebaseUtil.getFirestore();
        local = LocalTime.now();
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
                            //Name Check:
                            name = document.getString("name");
                            if(name.isEmpty()){
                                _name_txt.setText("NULL");
                            }
                            else{
                                _name_txt.setText(name);
                            }

                            //Phone Check:
                            phone = document.getString("phone");
                            if(phone.isEmpty()){
                                _phone_txt.setText("NULL");
                            }
                            else{
                                _phone_txt.setText(phone);
                            }

                            //Restaurant Pref Check:
                            favorite_restaurant = document.getString("favorite_food");
                            if(favorite_restaurant.isEmpty()){
                                _rest_pref_txt.setText("NULL");
                            }
                            else{
                                _rest_pref_txt.setText(favorite_restaurant);
                            }

                            //User Type Check:
                            user_type = document.getString("type");
                            if(user_type.equals("1")){
                                _user_type_txt.setText("Restaurant Account");
                            }
                            else if(user_type.equals("0")){
                                _user_type_txt.setText("Customer Account");
                            }
                        }
                        else{
                            Log.d("FAILED", "no documents");

                        }
                        flag = 1;
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

    }


    public void on_submit(View view){
        Intent intent = new Intent(this, LoadingActivity.class);
        if(flag == 0) {
            return;
        }
        else
            {
                if(_m_type.equals(R.string.user_type_customer)){
                    _m_type = "0";
                }
                else if(_m_type.equals(R.string.user_type_owner)){
                    _m_type = "1";
                }
                else{
                    _m_type = "NULL";
                }

            _m_name = _name_txt.getText().toString();
            _m_phone = _phone_txt.getText().toString();

            _changes[0] = _m_name;
            _changes[1] = _m_phone;
            _changes[2] = _m_fav_restaurant;
            _changes[3] = _m_type;

            intent.putExtra(NEW_SETTINGS, _changes);

            startActivity(intent);
        }
    }

}