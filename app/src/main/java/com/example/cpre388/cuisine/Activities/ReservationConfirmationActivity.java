package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class ReservationConfirmationActivity extends AppCompatActivity {
    private final static String CONFIRMATION_DETAILS = "com.example.cpre388.cuisine.Activities.ReserveTableActivity";

    private TextView name, details;
    private String[] confirmation;
    private AppCompatButton btn;

    //Reservation Details:
    private String contact_information, table_selected, rest_id, room_number, party_size, time;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirmation);

        name = findViewById(R.id.reservation_for_name_confirm);
        details = findViewById(R.id.reservation_details);
        btn = findViewById(R.id.to_home_menu);

        /*
        confirmation[0] = String.format("Thank you, %s", name_input);
        confirmation[1] = confirmation_details;
        confirmation[2] = contact_information;
        confirmation[3] = mRoom;
        confirmation[4] = mTable;
        confirmation[5] = mRestaurant_id;
        confirmation[6] = number of guests
         */
        confirmation = new String[8];

        Intent intent = getIntent();
        confirmation = intent.getStringArrayExtra(CONFIRMATION_DETAILS);
        contact_information = confirmation[2];
        room_number = confirmation[3];
        table_selected = confirmation[4];
        rest_id = confirmation[5];
        party_size = confirmation[6];
        time = confirmation[7];


        name.setText(confirmation[0]);
        details.setText(confirmation[1]);
        btn.setOnClickListener(this::onHomePressed);

        //Store User Object onto Firestore:
        Map<String, Object> reservation = new HashMap<>();
        mFirestore = FirebaseUtil.getFirestore();
        LocalTime local = LocalTime.now();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            String uid_time = currUser.getUid() + local.toString();
            DocumentReference userRef = mFirestore.collection("Users").document(currUser.getUid()).collection("Reservations").document(uid_time);

            //Write new Reservation Document
            currUser = FirebaseAuth.getInstance().getCurrentUser();
            reservation.put("uid", currUser.getUid());
            reservation.put("restaruant_id", rest_id);
            reservation.put("contact_information", contact_information);
            reservation.put("room_selected", room_number);
            reservation.put("table_selected", table_selected);
            reservation.put("num_guests", party_size);
            reservation.put("reservation_time", time);
            userRef.set(reservation);
        }
    }

    private void onHomePressed(View view){
        Intent i=new Intent(this, activity_customer_main.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }
}
