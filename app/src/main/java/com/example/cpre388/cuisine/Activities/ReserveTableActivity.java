package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Reservation Activity - prompts users to enter their details after selecting a restaurant and their table
 */
public class ReserveTableActivity extends AppCompatActivity {
    private final static String CONFIRMATION_DETAILS = "com.example.cpre388.cuisine.Activities.ReserveTableActivity";
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";

    //View Objects:
    private Spinner spinner;
    private List<String> list;
    private ImageView tableView;
    private EditText name;
    private EditText number;

    private String mRoom;
    private String mTable;
    private String mRestaurant_id;

    //time selection variables:
    private String given_time;

    //Confirmation Details: format -> name[0], details[1]
    private String[] confirmation;
    private int party_size;

    /**
     * Sets view objects
     *
     * And spinner objects
     * @param savedInstanceState - bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_table);

        //Intent Variables:
        //Previous Intent Values:
        String[] selection = new String[4];
        confirmation = new String[9];

        //Port in table selection and room (arr[0] and arr[1]):
        Intent intent = getIntent();
        selection = intent.getStringArrayExtra(SELECTION_DETAILS);
        mTable = selection[0];
        mRoom = selection[1];
        mRestaurant_id = selection[2];
        given_time = selection[3];

        party_size = 0;

        //EditText Fields:
        name = findViewById(R.id.reservation_for_name);
        number = findViewById(R.id.reservation_for_phoneNumber);
        TextView display_time = findViewById(R.id.display_time_view);

        display_time.setText(given_time);

        //Button:
        AppCompatButton btn = findViewById(R.id.submit_reservation);

        //Table View (reflects the selected seating amount:
        tableView = findViewById(R.id.set_reservation_table_size_img);

        //Prepare Spinner List for Table Selection:
        spinner = (Spinner) this.findViewById(R.id.table_size_selector);
        list = new ArrayList<>();
        setSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.table_reservation_spinner, list);
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
                    case "Life is a Party":
                        party_size = 15;
                        tableView.setImageResource(R.drawable.part_for_large);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tableView.setImageResource(R.drawable.table_for_default);
            }
        });
        btn.setOnClickListener(this::onSubmit);
    }

    /**
     * Prepares the Spinner List for table selection
     */
    private void setSpinner(){
        //String Values for View purposes:
        String one = getString(R.string.table_for_one);
        list.add(one);
        String two = getString(R.string.table_for_two);
        list.add(two);
        String three = getString(R.string.table_for_three);
        list.add(three);
        String four = getString(R.string.table_for_four);
        list.add(four);
        String five = getString(R.string.table_for_five);
        list.add(five);
        String six = getString(R.string.table_for_six);
        list.add(six);
        String seven = getString(R.string.table_for_seven);
        list.add(seven);
        String eight = getString(R.string.table_for_eight);
        list.add(eight);
        String nine = getString(R.string.large_party);
        list.add(nine);
    }

    /**
     * onSubmit(): sends details to the confirmation to be submitted
     * @param view - view
     */
    private void onSubmit(View view){
        String confirmation_details = String.format("Your table for %d, will be ready soon!", party_size);
        String party_num = String.format("%d", party_size);
        String name_input = name.getText().toString();
        String contact_information = number.getText().toString();

        confirmation[0] = String.format("Thank you, %s", name_input);
        confirmation[1] = confirmation_details;
        confirmation[2] = contact_information;
        confirmation[3] = mRoom;
        confirmation[4] = mTable;
        confirmation[5] = mRestaurant_id;
        confirmation[6] = party_num;
        confirmation[7] = given_time;
        confirmation[8] = name_input;

        Intent confirmation_intent = new Intent(this, ReservationConfirmationActivity.class);
        confirmation_intent.putExtra(CONFIRMATION_DETAILS, confirmation);
        startActivity(confirmation_intent);
    }
}
