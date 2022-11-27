package com.example.cpre388.cuisine.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpre388.cuisine.R;

import java.util.ArrayList;
import java.util.Random;

public class SelectTableActivity extends AppCompatActivity {

    //Tables in the Current Room Selection:
    private ImageView mTable_1_1, mTable_1_2, mTable_1_3,
            mTable_2_1, mTable_2_2, mTable_2_3,
            mTable_3_1, mTable_3_2, mTable_3_3,
            mTable_4_1, mTable_4_2, mTable_4_3;

    private int[][] table_array_room_one;
    private int[][] table_array_room_two;
    private int[][] table_array_room_three;
    private int[][] table_array_room_four;


    private Spinner spinner;
    private ArrayList<String> list;
    //String Values for View purposes:
    private String one, two, three, four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);

        table_array_room_one = new int[3][4];
        table_array_room_two = new int[3][4];
        table_array_room_three = new int[3][4];
        table_array_room_four = new int[3][4];

        spinner = (Spinner) this.findViewById(R.id.room_selector);
        list = new ArrayList<String>();
        setSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.table_reservation_spinner,list);
        spinner.setAdapter(adapter);

        //Table Matrix: Row 1
        mTable_1_1 = findViewById(R.id.table_1_1);
        mTable_1_2 = findViewById(R.id.table_1_2);
        mTable_1_3 = findViewById(R.id.table_1_3);
        //Row 2:
        mTable_2_1 = findViewById(R.id.table_2_1);
        mTable_2_2 = findViewById(R.id.table_2_2);
        mTable_2_3 = findViewById(R.id.table_2_3);
        //Row 3:
        mTable_3_1 = findViewById(R.id.table_3_1);
        mTable_3_2 = findViewById(R.id.table_3_2);
        mTable_3_3 = findViewById(R.id.table_3_3);
        //Row 4:
        mTable_4_1 = findViewById(R.id.table_4_1);
        mTable_4_2 = findViewById(R.id.table_4_2);
        mTable_4_3 = findViewById(R.id.table_4_3);

        table_array_room_one = dummy_array();
        table_array_room_two = dummy_array();
        table_array_room_three = dummy_array();
        table_array_room_four = dummy_array();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spinner.getSelectedItem().toString();

                switch (selected){
                    case "Room One":
                        init_tables();
                        set_tables(table_array_room_one, 1);
                        break;
                    case "Room Two":
                        init_tables();
                        set_tables(table_array_room_two, 2);
                        break;
                    case "Room Three":
                        init_tables();
                        set_tables(table_array_room_three, 3);
                        break;
                    case "Room Four":
                        init_tables();
                        set_tables(table_array_room_four, 4);
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                init_tables();

            }
        });

    }

    /**
     * Prepares the Spinner List for room selection
     */
    private void setSpinner(){
        one = "Room One";
        list.add(one);
        two = "Room Two";
        list.add(two);
        three = "Room Three";
        list.add(three);
        four = "Room Four";
        list.add(four);
    }

    private void init_tables(){
        for(int x = 0; x < 3; x++){
            String currX = String.format("%d", x + 1);
            for(int y = 0; y < 4; y++){
                String currY = String.format("%d",y + 1);
                String val = currX + "_" + currY;

                switch(val){
                    case "1_1":
                        mTable_1_1.setImageResource(R.drawable.table_for_default);
                        break;
                    case "1_2":
                        mTable_1_2.setImageResource(R.drawable.table_for_default);
                        break;
                    case "1_3":
                        mTable_1_3.setImageResource(R.drawable.table_for_default);
                        break;
                    case "2_1":
                        mTable_2_1.setImageResource(R.drawable.table_for_default);
                        break;
                    case "2_2":
                        mTable_2_2.setImageResource(R.drawable.table_for_default);
                        break;
                    case "2_3":
                        mTable_2_3.setImageResource(R.drawable.table_for_default);
                        break;
                    case "3_1":
                        mTable_3_1.setImageResource(R.drawable.table_for_default);
                        break;
                    case "3_2":
                        mTable_3_2.setImageResource(R.drawable.table_for_default);
                        break;
                    case "3_3":
                        mTable_3_3.setImageResource(R.drawable.table_for_default);
                        break;
                    case "default":
                        Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                        break;
                }
            }
        }
    }

    private void set_tables(int[][] table_array, int room_num){
        for(int x = 0; x < 3; x++){
            String currX = String.format("%d", x + 1);
            for(int y = 0; y < 4; y++){
                String currY = String.format("%d",y + 1);
                String val = currY + "_" + currX;
                String logged = String.format("Value: %d", table_array[x][y]);
                Log.d("CURR_VAL", val + logged);
                if(table_array[x][y] > 0){
                    switch(val){
                        case "1_1":
                            mTable_1_1.setImageResource(R.drawable.table_for_default);
                            break;
                        case "1_2":
                            mTable_1_2.setImageResource(R.drawable.table_for_default);
                            break;
                        case "1_3":
                            mTable_1_3.setImageResource(R.drawable.table_for_default);
                            break;
                        case "2_1":
                            mTable_2_1.setImageResource(R.drawable.table_for_default);
                            break;
                        case "2_2":
                            mTable_2_2.setImageResource(R.drawable.table_for_default);
                            break;
                        case "2_3":
                            mTable_2_3.setImageResource(R.drawable.table_for_default);
                            break;
                        case "3_1":
                            mTable_3_1.setImageResource(R.drawable.table_for_default);
                            break;
                        case "3_2":
                            mTable_3_2.setImageResource(R.drawable.table_for_default);
                            break;
                        case "3_3":
                            mTable_3_3.setImageResource(R.drawable.table_for_default);
                            break;
                        case "default":
                            Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                            break;
                    }
                }
                else{
                    switch(val){
                        case "1_1":
                            mTable_1_1.setImageResource(R.drawable.empty_space);
                            break;
                        case "1_2":
                            mTable_1_2.setImageResource(R.drawable.empty_space);
                            break;
                        case "1_3":
                            mTable_1_3.setImageResource(R.drawable.empty_space);
                            break;
                        case "2_1":
                            mTable_2_1.setImageResource(R.drawable.empty_space);
                            break;
                        case "2_2":
                            mTable_2_2.setImageResource(R.drawable.empty_space);
                            break;
                        case "2_3":
                            mTable_2_3.setImageResource(R.drawable.empty_space);
                            break;
                        case "3_1":
                            mTable_3_1.setImageResource(R.drawable.empty_space);
                            break;
                        case "3_2":
                            mTable_3_2.setImageResource(R.drawable.empty_space);
                            break;
                        case "3_3":
                            mTable_3_3.setImageResource(R.drawable.empty_space);
                            break;
                        default:
                            Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                            break;
                    }
                }
            }
        }
    }


    /**
     * Fills Array with dummy values for testing purposes;
     */
    private int[][] dummy_array(){
        int[][] toReturn = new int[3][4];
        Random rand = new Random();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                int k = rand.nextInt(8) - 5;
                toReturn[i][j] = k;
            }
        }
        return toReturn;
    }

}
