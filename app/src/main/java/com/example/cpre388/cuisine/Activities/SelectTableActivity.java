package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.Models.room_model;
import com.example.cpre388.cuisine.Models.table_model;
import com.example.cpre388.cuisine.R;

import java.util.ArrayList;
import java.util.Random;

public class SelectTableActivity extends AppCompatActivity {
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";

    private String restaurant_id;
    //Tables in the Current Room Selection:
    private ImageView mTable_1_1, mTable_1_2, mTable_1_3,
            mTable_2_1, mTable_2_2, mTable_2_3,
            mTable_3_1, mTable_3_2, mTable_3_3,
            mTable_4_1, mTable_4_2, mTable_4_3;

    //Used to keep track of which tables are available for use:
    private int[][] table_array_room_one;
    private int[][] table_array_room_two;
    private int[][] table_array_room_three;
    private int[][] table_array_room_four;
    private int[][] current_table_array;


    private Boolean selected;
    private String currSelection;
    private String roomSelection;

    private Spinner spinner;
    private ArrayList<String> list;
    //String Values for View purposes:
    private String one, two, three, four;

    //String Value for the Selected Table:
    private String[] confirmation_arr;

    private AppCompatButton btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);

        Intent prev = getIntent();
        restaurant_id = prev.getExtras().getString(KEY_RESTAURANT_ID);

        selected = false;
        currSelection = "CLEAR";
        confirmation_arr = new String[3];

        table_array_room_one = new int[4][3];
        table_array_room_two = new int[4][3];
        table_array_room_three = new int[4][3];
        table_array_room_four = new int[4][3];
        current_table_array = new int[4][3];

        spinner = (Spinner) this.findViewById(R.id.room_selector);
        list = new ArrayList<String>();
        setSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.table_reservation_spinner,list);
        spinner.setAdapter(adapter);

        btn = findViewById(R.id.confirm_table_selection_btn);

        //Table Matrix: Row 1
        mTable_1_1 = findViewById(R.id.table_1_1);
        mTable_1_1.setOnClickListener(this::on_1_1_clicked);
        mTable_1_2 = findViewById(R.id.table_1_2);
        mTable_1_2.setOnClickListener(this::on_1_2_clicked);
        mTable_1_3 = findViewById(R.id.table_1_3);
        mTable_1_3.setOnClickListener(this::on_1_3_clicked);
        //Row 2:
        mTable_2_1 = findViewById(R.id.table_2_1);
        mTable_2_1.setOnClickListener(this::on_2_1_clicked);
        mTable_2_2 = findViewById(R.id.table_2_2);
        mTable_2_2.setOnClickListener(this::on_2_2_clicked);
        mTable_2_3 = findViewById(R.id.table_2_3);
        mTable_2_3.setOnClickListener(this::on_2_3_clicked);
        //Row 3:
        mTable_3_1 = findViewById(R.id.table_3_1);
        mTable_3_1.setOnClickListener(this::on_3_1_clicked);
        mTable_3_2 = findViewById(R.id.table_3_2);
        mTable_3_2.setOnClickListener(this::on_3_2_clicked);
        mTable_3_3 = findViewById(R.id.table_3_3);
        mTable_3_3.setOnClickListener(this::on_3_3_clicked);
        //Row 4:
        mTable_4_1 = findViewById(R.id.table_4_1);
        mTable_4_1.setOnClickListener(this::on_4_1_clicked);
        mTable_4_2 = findViewById(R.id.table_4_2);
        mTable_4_2.setOnClickListener(this::on_4_2_clicked);
        mTable_4_3 = findViewById(R.id.table_4_3);
        mTable_4_3.setOnClickListener(this::on_4_3_clicked);

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
                        roomSelection = "One";
                        current_table_array = table_array_room_one;
                        set_tables(table_array_room_one, 1);
                        break;
                    case "Room Two":
                        roomSelection = "Two";
                        current_table_array = table_array_room_two;
                        set_tables(table_array_room_two, 2);
                        break;
                    case "Room Three":
                        roomSelection = "Three";
                        current_table_array = table_array_room_three;
                        set_tables(table_array_room_three, 3);
                        break;
                    case "Room Four":
                        roomSelection = "Four";
                        current_table_array = table_array_room_four;
                        set_tables(table_array_room_four, 4);
                        break;
                    default:
                        //One is the default is there is only one room at a restaurant
                        roomSelection = "One";
                        current_table_array = table_array_room_one;
                        set_tables(table_array_room_one, 0);
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                init_tables();
            }
        });
        btn.setOnClickListener(this::onSubmit);
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
        for(int x = 0; x < table_array_room_one.length; x++){
            String currX = String.format("%d", x + 1);
            for(int y = 0; y < table_array_room_one[x].length; y++){
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
                    case "4_1":
                        mTable_4_1.setImageResource(R.drawable.table_for_default);
                        break;
                    case "4_2":
                        mTable_4_2.setImageResource(R.drawable.table_for_default);
                        break;
                    case "4_3":
                        mTable_4_3.setImageResource(R.drawable.table_for_default);
                        break;
                    case "default":
                        Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                        break;
                }
            }
        }
    }

    private void set_tables(int[][] table_array, int room_num){
        for(int x = 0; x < table_array.length ; x++){
            String currX = String.format("%d", x + 1);
            for(int y = 0; y < table_array[x].length ; y++){
                String currY = String.format("%d",y + 1);
                String val = currX + "_" + currY;
                String logged = String.format("Value: %d", table_array[x][y]);
                Log.d("CURR_VAL", val + logged);
                if(table_array[x][y] > 1){
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
                        case "4_1":
                            mTable_4_1.setImageResource(R.drawable.table_for_default);
                            break;
                        case "4_2":
                            mTable_4_2.setImageResource(R.drawable.table_for_default);
                            break;
                        case "4_3":
                            mTable_4_3.setImageResource(R.drawable.table_for_default);
                            break;
                        case "default":
                            Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                            break;
                    }
                }
                else {
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
                        case "4_1":
                            mTable_4_1.setImageResource(R.drawable.empty_space);
                            break;
                        case "4_2":
                            mTable_4_2.setImageResource(R.drawable.empty_space);
                            break;
                        case "4_3":
                            mTable_4_3.setImageResource(R.drawable.empty_space);
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
        int[][] toReturn = new int[4][3];
        Random rand = new Random();
        for(int i = 0; i < toReturn.length; i++){
            for(int j = 0; j < toReturn[i].length; j++){
                int k = rand.nextInt(8) - 5;
                toReturn[i][j] = k;
            }
        }
        return toReturn;
    }

    public void on_1_1_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "1_1"){
            if(current_table_array[0][0] > 0) {
                mTable_1_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "1_1";
            }
        }
        else if(selected == true && currSelection == "1_1"){
            if(current_table_array[0][0] > 0) {
                mTable_1_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if(current_table_array[0][0] > 0) {
                mTable_1_1.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "1_1";
            }
        }
    }
    public void on_1_2_clicked(View view) {
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "1_2"){
            if (current_table_array[0][1] > 0) {
                mTable_1_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "1_2";
            }
        }
        else if(selected == true && currSelection == "1_2"){
            if (current_table_array[0][1] > 0) {
                mTable_1_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[0][1] > 0) {
                mTable_1_2.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "1_2";
            }
        }
    }

    public void on_1_3_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "1_3"){
            if (current_table_array[0][2] > 0) {
                mTable_1_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "1_3";
            }
        }
        else if(selected == true && currSelection == "1_3"){
            if (current_table_array[0][2] > 0) {
                mTable_1_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[0][2] > 0) {
                mTable_1_3.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "1_3";
            }
        }
    }
    public void on_2_1_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "2_1"){
            if (current_table_array[1][0] > 0) {
                mTable_2_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "2_1";
            }
        }
        else if(selected == true && currSelection == "2_1"){
            if (current_table_array[1][0] > 0) {
                mTable_2_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[1][0] > 0) {
                mTable_2_1.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "2_1";
            }
        }
    }
    public void on_2_2_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "2_2"){
            if (current_table_array[1][1] > 0) {
                mTable_2_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "2_2";
            }
        }
        else if(selected == true && currSelection == "2_2"){
            if (current_table_array[1][1] > 0) {
                mTable_2_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[1][1] > 0) {
                mTable_2_2.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "2_2";
            }
        }
    }
    public void on_2_3_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "2_3"){
            if (current_table_array[1][2] >= 0) {
                mTable_2_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "2_3";
            }
        }
        else if(selected == true && currSelection == "2_3"){
            if (current_table_array[1][2] >= 0) {
                mTable_2_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[1][2] >= 0) {
                mTable_2_3.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "2_3";
            }
        }
    }
    public void on_3_1_clicked(View view) {
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if (selected == true && currSelection != "3_1") {
            if (current_table_array[2][0] > 0) {
                mTable_3_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "3_1";
            }
        } else if (selected == true && currSelection == "3_1") {
            if (current_table_array[2][0] > 0) {
                mTable_3_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        } else {
            if (current_table_array[2][0] > 0) {
                mTable_3_1.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "3_1";
            }
        }
    }
    public void on_3_2_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "3_2"){
            if (current_table_array[2][1] > 0) {
                mTable_3_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "3_2";
            }
        }
        else if(selected == true && currSelection == "3_2"){
            if (current_table_array[2][1] > 0) {
                mTable_3_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[2][1] > 0) {
                mTable_3_2.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "3_2";
            }
        }
    }
    public void on_3_3_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "3_3"){
            if (current_table_array[2][2] > 0) {
                mTable_3_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "3_3";
            }
        }
        else if(selected == true && currSelection == "3_3"){
            if (current_table_array[2][2] > 0) {
                mTable_3_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[2][2] > 0) {
                mTable_3_3.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "3_3";
            }
        }
    }
    public void on_4_1_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "4_1"){
            if (current_table_array[3][0] > 0) {
                mTable_4_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "4_1";
            }
        }
        else if(selected == true && currSelection == "4_1"){
            if (current_table_array[3][0] > 0) {
                mTable_4_1.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[3][0] > 0) {
                mTable_4_1.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "4_1";
            }
        }
    }
    public void on_4_2_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "4_2"){
            if (current_table_array[3][1] > 0) {
                mTable_4_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "4_2";
            }
        }
        else if(selected == true && currSelection == "4_2"){
            if (current_table_array[3][1] > 0) {
                mTable_4_2.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[3][1] > 0) {
                mTable_4_2.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "4_2";
            }
        }
    }
    public void on_4_3_clicked(View view){
        /*
        if(EMPTY){}
        elseif(RESERVED){}
        else{}
         */
        if(selected == true && currSelection != "4_3"){
            if (current_table_array[3][2] > 0) {
                mTable_4_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = true;
                currSelection = "4_3";
            }
        }
        else if(selected == true && currSelection == "4_3"){
            if (current_table_array[3][2] > 0) {
                mTable_4_3.setImageResource(R.drawable.current_table_selection);
                set_tables(current_table_array, 1);
                selected = false;
                currSelection = "CLEAR";
            }
        }
        else{
            if (current_table_array[3][2] > 0) {
                mTable_4_3.setImageResource(R.drawable.current_table_selection);
                selected = true;
                currSelection = "4_3";
            }
        }
    }

    public void onSubmit(View view){
        confirmation_arr[0] = currSelection;
        confirmation_arr[1] = roomSelection;
        confirmation_arr[2] = restaurant_id;

        Intent intent = new Intent(this, ReserveTableActivity.class);
        //Passes the table selection and the room selection arr[0] arr[1] respectively
        //arr[3] contains the restaurant id for documentation purposes
        intent.putExtra(SELECTION_DETAILS, confirmation_arr);
        startActivity(intent);
    }
}
