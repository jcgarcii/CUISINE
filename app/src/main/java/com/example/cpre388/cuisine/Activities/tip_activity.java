package com.example.cpre388.cuisine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    TextView percentageLabel;
    TextView mealPrice;
    TextView resultPrice;
    TextView finalPrice;

    FirebaseFirestore db;

    int price;
    int startingPercent = 15;
    int tipVal;
    int totalVal;

    private FirebaseUser currUser;

    public Button ApplyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);


        ApplyButton = findViewById(R.id.buttonApply);

        ApplyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                applyClicked();

            }
        });

        receiptId = getIntent().getExtras().getString(KEY_ID);
        if (receiptId == null) {
            throw new IllegalArgumentException("Must pass extra " + KEY_ID);
        }

        db = FirebaseFirestore.getInstance();

        getBillTotal();



        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(startingPercent);

        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();

        mealPrice = findViewById(R.id.orderTotal);
        resultPrice = findViewById(R.id.resultText);
        finalPrice = findViewById(R.id.finalTotal);

        percentageLabel = findViewById(R.id.tipPercentage);

        percentageLabel.setText(progress + "% Tip");

    }


    @Override
    protected void onStart() {
        super.onStart();

        printValue(price, 0, 0);
        printValue(calcTip(startingPercent), 1, startingPercent);

        int total = price + calcTip(startingPercent);
        printValue(total, -1, 0);

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb

            tipVal = calcTip(progress);
            totalVal = tipVal + price;

            percentageLabel.setText(progress + "% Tip");

            printValue(tipVal, 1, progress);
            printValue(totalVal, -1, 0);

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

    public int calcTip(int percent) {

        int result = (int)((double)price * (percent / 100.0));

        return result;
    }

    public void printValue(int value, int totOrResult, int tipPer) {

        String dollars = String.valueOf(value / 100);
        String cents = String.valueOf(value % 100);

        if ((cents.length() < 2) && (((value % 100) > 9) || ((value % 100) == 0))){
            cents = cents + "0";

        } else if (cents.length() < 2){
            cents = "0" + cents;

        }

        if (totOrResult == 1) {
            resultPrice.setText("$" + dollars + "." + cents + " Tip");

        } else if (totOrResult == -1) {
            finalPrice.setText("$" + dollars + "." + cents + " Total");

        } else {
            mealPrice.setText("$" + dollars + "." + cents + " Meal");

        }
    }

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

                                if(receiptId.equals(document.getId())){

                                    int food = Integer.valueOf((String) document.get("food"));
                                    int drinks = Integer.valueOf((String) document.get("drinks"));
                                    int refills = Integer.valueOf((String) document.get("refills"));

                                    price = food + drinks + refills;

                                    Log.d(TAG, String.valueOf(price) +" => hellohellohello " + document.getId());

                                    printValue(price, 0, 0);
                                    printValue(calcTip(startingPercent), 1, startingPercent);

                                    int total = price + calcTip(startingPercent);
                                    printValue(total, -1, 0);
                                }

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }



    public void applyClicked(){

        currUser = FirebaseAuth.getInstance().getCurrentUser();


        String updatedTipVal = String.valueOf(tipVal);

        db.collection("Users")
                .document(currUser.getUid())
                .collection("Receipts")
                .document(receiptId)
                .update("tip", updatedTipVal);


        Intent intent = new Intent(this, CustomerRecieptsActivity.class);
        startActivity(intent);

    }


}