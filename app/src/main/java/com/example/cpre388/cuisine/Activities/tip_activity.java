package com.example.cpre388.cuisine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cpre388.cuisine.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class tip_activity extends AppCompatActivity {

    public static final String TAG = "receipt_act";
    public static final String KEY_ID = "id";
    String receiptId;

    TextView percentageLabel;   // Label above seekbar
    TextView mealPrice;         // Tipless meal total
    TextView resultPrice;       //  Tip value
    TextView finalPrice;        // Meal total + tip value

    FirebaseFirestore db;

    int price;
    int startingPercent = 15;
    int tipVal;
    int totalVal;
    int sliderNotTouched = 1;

    private FirebaseUser currUser;

    public Button ApplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        // Set up buttons
        ApplyButton = findViewById(R.id.buttonApply);

        ApplyButton.setOnClickListener(v -> applyClicked());

        // Get ID of the receipt that was clicked
        receiptId = getIntent().getExtras().getString(KEY_ID);
        if (receiptId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_ID);
        }

        db = FirebaseFirestore.getInstance();

        getBillTotal();

        // Set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(startingPercent);

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();

        // Set up textviews
        mealPrice = findViewById(R.id.orderTotal);
        resultPrice = findViewById(R.id.resultText);
        finalPrice = findViewById(R.id.finalTotal);

        percentageLabel = findViewById(R.id.tipPercentage);

        percentageLabel.setText(progress + "% Tip");

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set tipless cost and tip value textviews
        printValue(price, 0);
        printValue(calcTip(startingPercent), 1);

        // Also set initial tip + meal cost textview
        int total = price + calcTip(startingPercent);
        printValue(total, -1);

    }

    // Seekbar change listener
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the seekbar changes

            // A flag for if the user doesn't move the seekbar
            sliderNotTouched = 0;


            // Update and output values based on the seekbar
            tipVal = calcTip(progress);
            totalVal = tipVal + price;

            percentageLabel.setText(progress + "% Tip");

            printValue(tipVal, 1);
            printValue(totalVal, -1);

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    // Method for calculating the tip value
    public int calcTip(int percent) {

        int result = (int)((double)price * (percent / 100.0));

        return result;
    }

    // Method for formatting and setting output text
    public void printValue(int value, int totOrResult) {

        // String formatting
        String dollars = String.valueOf(value / 100);
        String cents = String.valueOf(value % 100);

        // Logic to make the dollar and cents fields function correctly
        if ((cents.length() < 2) && (((value % 100) > 9) || ((value % 100) == 0))){
            cents = cents + "0";

        } else if (cents.length() < 2){
            cents = "0" + cents;

        }


        // If totOrResult is 1, set the tip value textview
        // If totOrResult is -1, set the tip + meal cost textview
        // If totOrResult is anything else, set the meal cost textview

        if (totOrResult == 1) {
            resultPrice.setText("$" + dollars + "." + cents + " Tip");

        } else if (totOrResult == -1) {
            finalPrice.setText("$" + dollars + "." + cents + " Total");

        } else {
            mealPrice.setText("$" + dollars + "." + cents + " Meal");

        }
    }

    // Grabbing the total meal cost from the Firestore database
    public void getBillTotal(){

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Users")
                .document(currUser.getUid())
                .collection("Receipts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Finding the correct receipt document
                                if(receiptId.equals(document.getId())){

                                    int food = Integer.valueOf((String) document.get("food"));
                                    int drinks = Integer.valueOf((String) document.get("drinks"));
                                    int refills = Integer.valueOf((String) document.get("refills"));

                                    price = food + drinks + refills;

                                    // Making sure to set outputs once receipt data has been fetched
                                    printValue(price, 0);
                                    printValue(calcTip(startingPercent), 1);

                                    int total = price + calcTip(startingPercent);
                                    printValue(total, -1);
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    // Method that runs when the user hits the apply button
    public void applyClicked(){

        // Apply default tip if user hasn't changed seekbar
        if(sliderNotTouched == 1){
            tipVal = calcTip(startingPercent);
        }

        // Update tip value in the receipt Firestore document
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        String updatedTipVal = String.valueOf(tipVal);

        db.collection("Users")
                .document(currUser.getUid())
                .collection("Receipts")
                .document(receiptId)
                .update("tip", updatedTipVal);

        // And return to the receipt activity
        Intent intent = new Intent(this, CustomerRecieptsActivity.class);
        startActivity(intent);

    }

}