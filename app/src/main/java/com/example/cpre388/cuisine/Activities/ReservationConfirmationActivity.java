package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpre388.cuisine.R;

public class ReservationConfirmationActivity extends AppCompatActivity {
    private TextView name, details;
    private String[] confirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmation);

        name = findViewById(R.id.reservation_for_name_confirm);
        details = findViewById(R.id.reservation_details);
        //Contains name [0] and details [1]
        confirmation = new String[2];

        Intent intent = getIntent();
        confirmation = intent.getStringArrayExtra("CONFIRMATION_DETAILS");

        name.setText(confirmation[0]);
        details.setText(confirmation[1]);
    }
}
