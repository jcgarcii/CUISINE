package com.example.cpre388.cuisine.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditReservationActivity extends AppCompatActivity {
    private final static String SUCCESSFUL_EDIT_DETAILS = "com.example.cpre388.cuisine.Activities.EDITRESERVATION";
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";
    public final String EDIT_RESERVATION = "reservation_edit_key";

    //View Objects:
    private Spinner spinner;
    private List<String> list;
    private ImageView tableView;
    private EditText name;
    private EditText number;
    private AppCompatButton btn;

    //Previous Intent Values:
    private String[] selection;
    private String mRoom;
    private String mTable;
    private String mRestaurant_id;
    private String reservation_id;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;
    DocumentReference documentReference;


    //time selection variables:
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView display_time;
    private AppCompatButton set_time;
    private String final_time;
    private String given_time;

    //Confirmation Details: format -> name[0], details[1]
    private String[] confirmation;
    private int party_size;

    private int _document_ready;

    //String Values for View purposes:
    private String one, two, three,
            four, five, six, seven, eight, nine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_edit);

        //Intent Variables:
        selection = new String[5];
        //confirmation = new String[9];

        //Port in table selection and room (arr[0] and arr[1]):
        Intent intent = getIntent();
        selection = intent.getStringArrayExtra(EDIT_RESERVATION);
        mTable = selection[0];
        mRoom = selection[1];
        mRestaurant_id = selection[2];
        given_time = selection[3];
        reservation_id = selection[4];

        party_size = 0;

        //EditText Fields:
        name = findViewById(R.id.reservation_for_name);
        number = findViewById(R.id.reservation_for_phoneNumber);
        display_time = findViewById(R.id.display_time_view);

        display_time.setText(given_time);

        //Button:
        btn = findViewById(R.id.submit_reservation);

        //Table View (reflects the selected seating amount:
        tableView = findViewById(R.id.set_reservation_table_size_img);

        //Prepare Spinner List for Table Selection:
        spinner = (Spinner) this.findViewById(R.id.table_size_selector);
        list = new ArrayList<String>();
        setSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.table_reservation_spinner,list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner.getSelectedItem().toString();

                switch (selected){
                    case "Party of One":
                        party_size = 1;
                        tableView.setImageResource(R.drawable.table_for_one);
                        break;
                    case "Party of Two":
                        party_size = 2;
                        tableView.setImageResource(R.drawable.table_for_two);
                        break;
                    case "Party of Three":
                        party_size = 3;
                        tableView.setImageResource(R.drawable.table_for_three);
                        break;
                    case "Party of Four":
                        party_size = 4;
                        tableView.setImageResource(R.drawable.table_for_four);
                        break;
                    case "Party of Five":
                        party_size = 5;
                        tableView.setImageResource(R.drawable.table_for_five);
                        break;
                    case "Party of Six":
                        party_size = 6;
                        tableView.setImageResource(R.drawable.table_for_six);
                        break;
                    case "Party of Seven":
                        party_size = 7;
                        tableView.setImageResource(R.drawable.table_for_seven);
                        break;
                    case "Party of Eight":
                        party_size = 8;
                        tableView.setImageResource(R.drawable.table_for_eight);
                        break;
                    case "Life is a Party!":
                        party_size = 10;
                        tableView.setImageResource(R.drawable.part_for_large);
                    default:
                        party_size = 2;
                        tableView.setImageResource(R.drawable.table_for_default);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tableView.setImageResource(R.drawable.table_for_default);
            }
        });
        btn.setOnClickListener(this::onSubmit);
        display_time.setOnClickListener(this::onSetTime);

        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            documentReference = mFirestore.collection("Reservations").document(reservation_id);

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                        _document_ready++;
                        } else {
                           _document_ready = 0;
                        }
                    } else {
                        Log.d("FAILED TO UPDATE", "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    /**
     * Prepares the Spinner List for table selection
     */
    private void setSpinner(){
        one = getString(R.string.table_for_one);
        list.add(one);
        two = getString(R.string.table_for_two);
        list.add(two);
        three = getString(R.string.table_for_three);
        list.add(three);
        four = getString(R.string.table_for_four);
        list.add(four);
        five = getString(R.string.table_for_five);
        list.add(five);
        six = getString(R.string.table_for_six);
        list.add(six);
        seven = getString(R.string.table_for_seven);
        list.add(seven);
        eight = getString(R.string.table_for_eight);
        list.add(eight);
        nine =  getString(R.string.large_party);
        list.add(nine);
    }

    private void onSetTime(View view){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        //Hours Format:
                        String hr = String.format("%d", hourOfDay);
                        if(hr.length() == 1){
                            String i = "0" + hr;
                            hr = i;
                        }

                        //Minute Format:
                        String min = String.format("%d", minute);
                        String _min = "";
                        char tens = min.charAt(0);
                        if(Integer.parseInt(String.valueOf(tens)) >= 3){
                            _min = "30";
                        }
                        else {
                            _min = "00";
                        }

                        final_time = String.format("%s%s", hr, _min);
                        display_time.setText(hourOfDay + " : " + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
        _document_ready++;
    }


    private void onSubmit(View view){

        String confirmation_details = String.format("Your table for %d, will be ready soon!", party_size);
        String party_num = String.format("%d", party_size);
        String name_input = name.getText().toString();
        String contact_information = number.getText().toString();
/*
        confirmation[0] = String.format("Thank you, %s", name_input);
        confirmation[1] = confirmation_details;
        confirmation[2] = contact_information;
        confirmation[3] = mRoom;
        confirmation[4] = mTable;
        confirmation[5] = mRestaurant_id;
        confirmation[6] = party_num;
        confirmation[7] = given_time;
        confirmation[8] = name_input;
  */
        if(_document_ready > 0){
            documentReference = mFirestore.collection("Reservations").document(reservation_id);
            Map<String, Object> _n_reservation = new HashMap<>();

            //Write new Reservation Document
            _n_reservation.put("uid", currUser.getUid());
            _n_reservation.put("restaruant_id", mRestaurant_id);
            _n_reservation.put("reservation_for", name_input);
            _n_reservation.put("contact_information", contact_information);
            _n_reservation.put("room_selected", mRoom);
            _n_reservation.put("table_selected", mTable);
            _n_reservation.put("num_guests", party_size);
            _n_reservation.put("reservation_time", final_time);

            documentReference.update(_n_reservation);

            Intent confirmation_intent = new Intent(this, activity_customer_main.class);
            confirmation_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //confirmation_intent.putExtra(SUCCESSFUL_EDIT_DETAILS, "complete");
            startActivity(confirmation_intent);
        }
    }
}
