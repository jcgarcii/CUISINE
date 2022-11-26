package com.example.cpre388.cuisine.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cpre388.cuisine.R;


public class tip_activity extends AppCompatActivity {

    TextView percentageLabel;
    TextView mealPrice;
    TextView resultPrice;
    TextView finalPrice;

    int price = 27940;
    int startingPercent = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);


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

            int tipVal = calcTip(progress);
            int totalVal = tipVal + price;

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
}