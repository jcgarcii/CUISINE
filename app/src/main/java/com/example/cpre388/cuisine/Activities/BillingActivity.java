package com.example.cpre388.cuisine.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class BillingActivity extends AppCompatActivity {
    private String[] _reservation;
    private static final String RESERV_BILLING_KEY = "key_reservation_billing";

    //Intent Variables:
    private String name;
    private String uid;
    private String rest_id;
    private String reservation_id;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;
    DocumentReference userRef;

    //View Objects:
    private EditText _foodEntry, _bevEntry, _refillsEntry,
            _numFoodEntry, _numRefillsEntry, _numBevEntry;
    private TextView banner;
    private AppCompatButton submit_btn;

    private int ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        _foodEntry = findViewById(R.id.billing_activity_food_entry);
        _bevEntry = findViewById(R.id.billing_activity_bev_entry);
        _refillsEntry = findViewById(R.id.billing_activity_refills_entry);
        _numFoodEntry = findViewById(R.id.billing_activity_num_food);
        _numBevEntry = findViewById(R.id.billing_activity_num_bev);
        _numRefillsEntry = findViewById(R.id.billing_activity_num_refills);
        banner = findViewById(R.id.billing_activity_banner);
        submit_btn = findViewById(R.id.billing_activity_submit_btn);

        _reservation = new String[4];
        /*_reservation[0] = reservation.getString("reservation_for");
        _reservation[1] = reservation.getString("uid");
        _reservation[2] = reservation.getString("restaruant_id");
        _reservation[3] = reservation.getId();*/

        Intent intent = getIntent();
        _reservation = intent.getExtras().getStringArray(RESERV_BILLING_KEY);
        name = _reservation[0];
        uid = _reservation[1];
        rest_id = _reservation[2];
        reservation_id = _reservation[3];

        banner.setText(String.format("Bill for: %s", name));

        ready = 0;

        mFirestore = FirebaseUtil.getFirestore();
        String local = LocalTime.now().toString();
        userRef = mFirestore.collection("Users").document(String.format("%s",uid)).collection("Receipts").document(rest_id + local);

        submit_btn.setOnClickListener(this::onSubmit);
    }

    private void commit_receipt() {
        String foodEntry = _foodEntry.getText().toString();
        String drinkEntry = _bevEntry.getText().toString();
        String refillsEntry = _refillsEntry.getText().toString();

        String mFoodEntry = foodEntry.replaceAll("[$.]", "");
        String mDrinkEntry = drinkEntry.replaceAll("[$.]", "");
        String mRefillsEntry = refillsEntry.replaceAll("[$.]", "");

        String numFood = _numFoodEntry.getText().toString();
        String numDrink = _numBevEntry.getText().toString();
        String numRefills = _numRefillsEntry.getText().toString();

        Map<String, Object> receipt = new HashMap<>();
        receipt.put("name", name);
        receipt.put("uid", uid);
        receipt.put("rest_id", rest_id);
        receipt.put("reserv_id", reservation_id);
        receipt.put("food", mFoodEntry);
        receipt.put("drinks", mDrinkEntry);
        receipt.put("refills", mRefillsEntry);
        receipt.put("numFood", numFood);
        receipt.put("numDrinks", numDrink);
        receipt.put("numRefills", numRefills);
        receipt.put("tip", "0");

        userRef.set(receipt);

        ready++;
    }

    private void onSubmit(View view) {
        if (ready > 0) {
            Intent i = new Intent(this, OwnerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        } else {
            commit_receipt();
            Snackbar.make(findViewById(android.R.id.content),
                    "Tap Submit Again to Confirm", Snackbar.LENGTH_LONG).show();
        }
    }
}