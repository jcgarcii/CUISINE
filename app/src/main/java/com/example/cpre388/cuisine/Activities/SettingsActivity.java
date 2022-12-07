package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

public class SettingsActivity extends AppCompatActivity {
    private static final String NEW_SETTINGS = "com.example.cpre388.cuisine.activities.SETTINGS";

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;
    private AppCompatButton submit_btn, delete_btn, discard_btn;
    private MainActivityViewModel mViewModel;

    private LocalTime local;
    private int dummy;
    private int flag;
    private int document_exists;
    private int changes;
    private Observer observer;

    //FireStore Documents:
    private DocumentSnapshot document;
    private DocumentReference userRef;

    //Track Changes:
    private String _m_fav_restaurant;
    private String _m_type;
    private String _m_name;
    private String _m_phone;

    //Intent Array
    private String[] _changes;

    //Firestore Stuff:
    private String fire_name;
    private String fire_type;
    private String fire_phone;
    private String fire_rest;

    //TextView and EditView Stuff:
    private EditText _name_txt, _phone_txt;
    private ImageView profile_pic;

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

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.set_settings(false);

        //View Objects:
        _name_txt = findViewById(R.id.editTextTextPersonName);
        _phone_txt = findViewById(R.id.editTextPhone);
        submit_btn = findViewById(R.id.save_settings_btn);
        discard_btn = findViewById(R.id.discard_changes_btn);
        delete_btn = findViewById(R.id.delete_account_btn);
        profile_pic = findViewById(R.id.profile_pic);

        profile_pic.setImageResource(R.drawable.profile);

        //Intent Array:
        _changes = new String[4];

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

        set_views(0);
        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference checkRef = mFirestore.collection("Users").document(currUser.getUid());

            checkRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            fire_name = document.getString("name");
                            fire_phone = document.getString("phone");
                            fire_rest = document.getString("favorite_food");
                            fire_type = document.getString("type");

                            //Set Fields with previously selected fields:
                            _name_txt.setHint(fire_name);
                            _phone_txt.setHint(fire_phone);
                            //set restaurant spinner to previous selected values:
                            int rest_post = res_adapter.getPosition(fire_rest);
                            restaurant_spinner.setSelection(rest_post);
                            //set type spinner to previous selected values:
                            String fire_type_l;
                            if (fire_type.equals("1")){
                                fire_type_l = "Owner";
                            }
                            else{
                                fire_type_l = "Customer";
                            }
                            int user_post = user_adapter.getPosition(fire_type_l);
                            type_spinner.setSelection(user_post);

                            document_exists = 1;
                            set_views(1);
                        } else {
                            set_views(1);
                            document_exists = 0;
                        }
                    } else {
                        Log.d("FAILURE", "get failed with ", task.getException());
                    }
                }
            });
        }

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
        discard_btn.setOnClickListener(this::on_discard);
        delete_btn.setOnClickListener(this::on_delete);

        flag = 0;
        changes = 0;
        dummy = 0;

        local = LocalTime.now();
    }

    private void set_views(int ready){
        int vis;
        if(ready == 0){
            vis = View.INVISIBLE;
        }
        else{
            vis = View.VISIBLE;
        }

        _name_txt.setVisibility(vis);
        _phone_txt.setVisibility(vis);
        submit_btn.setVisibility(vis);
        type_spinner.setVisibility(vis);
        restaurant_spinner.setVisibility(vis);

        if(document_exists > 0) {
            delete_btn.setVisibility(View.VISIBLE);
            discard_btn.setVisibility(View.VISIBLE);
        }else{
            delete_btn.setVisibility(View.INVISIBLE);
            discard_btn.setVisibility(View.INVISIBLE);
        }
    }

    public void _update(){
        if(changes == 1) {
            int _null_name = 0;
            int _null_phone = 0;
            Map<String, Object> user_obj = new HashMap<>();

            _m_name = _name_txt.getText().toString();
            _m_phone = _phone_txt.getText().toString();

            if(!_m_name.isEmpty()){
                _null_name ++;
            }
            if(!_m_phone.isEmpty()){
                _null_phone++;
            }

            switch (_m_type) {
                case "Customer":
                    _m_type = "0";
                    break;
                case "Owner":
                    _m_type = "1";
                    break;
                default:
                    _m_type = "0";
            }

            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                currUser = FirebaseAuth.getInstance().getCurrentUser();
                DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid());

                switch(document_exists){
                    case 1:
                        //uid - DO NOT TOUCH:
                        user_obj.put("uid", currUser.getUid());
                        //Name Check:
                        if(!_m_name.equals(fire_name) && _null_name > 0) {
                            user_obj.put("name", _m_name);
                        }
                        else{
                            user_obj.put("name", fire_name);
                        }
                        //Phone Check:
                        if(!_m_phone.equals(fire_phone) && _null_phone > 0) {
                            user_obj.put("phone", _m_phone);
                        }else{
                            user_obj.put("phone", fire_phone);
                        }
                        //Restaurant Pref Check:
                        if(!_m_fav_restaurant.equals(fire_rest)) {
                            user_obj.put("favorite_food", _m_fav_restaurant);
                        }
                        else{
                            user_obj.put("favorite_food", fire_rest);
                        }
                        //User Type:
                        if(!_m_type.equals(fire_type)) {
                            user_obj.put("type", _m_type);
                        }
                        else{
                            user_obj.put("type", fire_type);
                        }
                        //Send to database:
                        userRef.update(user_obj);
                        break;
                    case 0:
                        //uid:
                        user_obj.put("uid", currUser.getUid());
                        //Name Check:
                        user_obj.put("name", _m_name);
                        //Phone Check:
                        user_obj.put("phone", _m_phone);
                        //Restaurant Pref Check:
                        user_obj.put("favorite_food", _m_fav_restaurant);
                        //User Type:
                        user_obj.put("type", _m_type);
                        //Send to database:
                        userRef.set(user_obj);
                        break;
                }


                nextActivity(0);
            }

        }
        else{
            dummy++;
        }

    }

    /**
     * Selects the next activity for the user:
     * @param arg - if greater than 0, this means the user has discarded changes
     */
    public void nextActivity(int arg) {
        if(arg > 0){
            if (fire_type.equals("1")) {
                Intent i = new Intent(this, activity_owner_main.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Intent i = new Intent(this, activity_customer_main.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }else {
            if (_m_type.equals("1")) {
                Intent i = new Intent(this, activity_owner_main.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            } else {
                Intent i = new Intent(this, activity_customer_main.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        }
    }


    public void on_submit(View view){
         if(changes == 1){dummy++;}
         else {
             changes = 1;
             _update();
         }
/*
            _changes[0] = _m_name;
            _changes[1] = _m_phone;
            _changes[2] = _m_fav_restaurant;
            _changes[3] = _m_type;
            intent.putExtra(NEW_SETTINGS, _changes);
*/
    }


    public void on_discard(View view){
        if(document_exists > 0){
            nextActivity(99);
        }
    }

    public void on_delete(View view) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            currUser.delete();
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
