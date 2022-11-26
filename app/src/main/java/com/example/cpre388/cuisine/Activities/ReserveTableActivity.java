package com.example.cpre388.cuisine.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpre388.cuisine.R;

import java.util.ArrayList;
import java.util.List;

public class ReserveTableActivity extends AppCompatActivity {
    private Spinner spinner;
    private List<String> list;
    private ImageView tableView;

    //String Values for View purposes:
    private String one, two, three,
            four, five, six, seven, eight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_table);

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
                        tableView.setImageResource(R.drawable.table_for_one);
                        break;
                    case "Party of Two":
                        tableView.setImageResource(R.drawable.table_for_two);
                        break;
                    case "Party of Three":
                        tableView.setImageResource(R.drawable.table_for_three);
                        break;
                    case "Party of Four":
                        tableView.setImageResource(R.drawable.table_for_four);
                        break;
                    case "Party of Five":
                        tableView.setImageResource(R.drawable.table_for_five);
                        break;
                    case "Party of Six":
                        tableView.setImageResource(R.drawable.table_for_six);
                        break;
                    case "Party of Seven":
                        tableView.setImageResource(R.drawable.table_for_seven);
                        break;
                    case "Party of Eight":
                        tableView.setImageResource(R.drawable.table_for_eight);
                        break;
                    default:
                        tableView.setImageResource(R.drawable.table_for_default);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                tableView.setImageResource(R.drawable.table_for_default);
            }
        });
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
    }

}
