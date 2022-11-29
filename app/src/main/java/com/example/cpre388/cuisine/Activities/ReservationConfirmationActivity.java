package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.R;

public class ReservationConfirmationActivity extends AppCompatActivity {
    private final static String CONFIRMATION_DETAILS = "com.example.cpre388.cuisine.Activities.ReserveTableActivity";
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";

    private TextView name, details;
    private String[] confirmation;
    private AppCompatButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmation);

        name = findViewById(R.id.reservation_for_name_confirm);
        details = findViewById(R.id.reservation_details);
        btn = findViewById(R.id.to_home_menu);

        //Contains name [0], confirmation details [1], contact information [2], table/room information [3]
        confirmation = new String[4];

        Intent intent = getIntent();
        confirmation = intent.getStringArrayExtra(CONFIRMATION_DETAILS);

        name.setText(confirmation[0]);
        details.setText(confirmation[1]);
        btn.setOnClickListener(this::onHomePressed);
    }

    private void onHomePressed(View view){
        //Submit to database asynchronously, pass information to display on home page

        Intent i=new Intent(this, activity_customer_main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }


}
