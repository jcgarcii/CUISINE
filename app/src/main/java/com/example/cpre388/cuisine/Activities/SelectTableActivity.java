package com.example.cpre388.cuisine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTableActivity extends AppCompatActivity {
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    public static final String SELECTED_TIME = "key_time_selected";

    private String restaurant_id;
    private String selected_time;
    private String formatted_time;

    //Tables in the Current Room Selection:
    private ImageView mTable_1_1, mTable_1_2, mTable_1_3,
            mTable_2_1, mTable_2_2, mTable_2_3,
            mTable_3_1, mTable_3_2, mTable_3_3,
            mTable_4_1, mTable_4_2, mTable_4_3;

    private Boolean ready;

    //Used to keep track of which tables are available for use:
    private int[][] table_array_room_one;
    private int[][] table_array_room_two;
    private int[][] table_array_room_three;
    private int[][] table_array_room_four;
    private int[][] current_table_array;

    private String[][] stable_array_room_one;
    private String[][] stable_array_room_two;
    private String[][] stable_array_room_three;
    private String[][] stable_array_room_four;

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

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    //Retrieve List<Integer> from cloud:
    private Map<String, Object> map;
    private List<String> room_1;
    private List<String> room_2;
    private List<String> room_3;
    private List<String> room_4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);

        Intent prev = getIntent();
        restaurant_id = prev.getExtras().getString(KEY_RESTAURANT_ID);
        selected_time = prev.getExtras().getString(SELECTED_TIME);

        ready = false;

        selected = false;
        currSelection = "CLEAR";
        confirmation_arr = new String[4];

        table_array_room_one = new int[4][3];
        table_array_room_two = new int[4][3];
        table_array_room_three = new int[4][3];
        table_array_room_four = new int[4][3];
        current_table_array = new int[4][3];

        stable_array_room_one = new String[4][3];
        stable_array_room_two = new String[4][3];
        stable_array_room_three = new String[4][3];
        stable_array_room_four = new String[4][3];

        spinner = (Spinner) this.findViewById(R.id.room_selector);
        spinner.setVisibility(View.INVISIBLE);
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

        init_tables(0);

        mFirestore = FirebaseUtil.getFirestore();

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            currUser = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference userRef = mFirestore.collection("restaurants").document(restaurant_id).collection("Layouts").document(selected_time);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            map = (Map<String, Object>) document.getData();
                            room_1 = (List<String>) map.getOrDefault("Room 1", "lol" );
                            room_2 = (List<String>) map.getOrDefault("Room 2", "lol" );
                            room_3 = (List<String>) map.getOrDefault("Room 3", "lol" );
                            room_4 = (List<String>) map.getOrDefault("Room 4", "lol" );

                            int _x_1 = 0;
                            int _y_1 = 0;
                            for(int i = 0; i < room_1.size(); i++){
                                stable_array_room_one[_y_1][_x_1] = room_1.get(i);

                                if(_x_1 == 2){
                                    _y_1++;
                                    _x_1 = 0;
                                }
                                else{
                                    _x_1++;
                                }
                            }

                            int _x_2 = 0;
                            int _y_2 = 0;
                            for(int i = 0; i < room_2.size(); i++){
                                stable_array_room_two[_y_2][_x_2] = room_2.get(i);

                                if(_x_2 == 2){
                                    _y_2++;
                                    _x_2 = 0;
                                }
                                else{
                                    _x_2++;
                                }
                            }

                            int _x_3 = 0;
                            int _y_3 = 0;
                            for(int i = 0; i < room_3.size(); i++){
                                stable_array_room_three[_y_3][_x_3] = room_3.get(i);

                                if(_x_3 == 2){
                                    _y_3++;
                                    _x_3 = 0;
                                }
                                else{
                                    _x_3++;
                                }
                            }

                            int _x_4 = 0;
                            int _y_4 = 0;
                            for(int i = 0; i < room_4.size(); i++){
                                stable_array_room_four[_y_4][_x_4] = room_4.get(i);

                                if(_x_4 == 2){
                                    _y_4++;
                                    _x_4 = 0;
                                }
                                else{
                                    _x_4++;
                                }
                            }

                            init_tables(1);
                            table_array_room_one = setArray(stable_array_room_one);
                            table_array_room_two = setArray(stable_array_room_two);
                            table_array_room_three = setArray(stable_array_room_three);
                            table_array_room_four = setArray(stable_array_room_four);

                            ready = true;
                            spinner.setVisibility(View.VISIBLE);

                            Log.d("Table Retrieval Success", "very nice, hopefully");
                        } else {
                            Log.d("Restaurant", "doesn't have layout");
                        }
                    } else {
                        Log.d("No Layout", "get failed with ", task.getException());
                    }
                }
            });
        }

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
               Log.d("TABLES", "no room was selected");
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

    private void init_tables(int i){
        int vis = 99;
        if(i == 1){
            vis = View.VISIBLE;
        }
        else{
            vis = View.INVISIBLE;
        }
        //Row 1 Visibility:
        mTable_1_1.setVisibility(vis);
        mTable_1_2.setVisibility(vis);
        mTable_1_3.setVisibility(vis);
        //Row 2 Visibility:
        mTable_2_1.setVisibility(vis);
        mTable_2_2.setVisibility(vis);
        mTable_2_3.setVisibility(vis);
        //Row 3 Visibility:
        mTable_3_1.setVisibility(vis);
        mTable_3_2.setVisibility(vis);
        mTable_3_3.setVisibility(vis);
        //Row 4 Visibililty:
        mTable_4_1.setVisibility(vis);
        mTable_4_2.setVisibility(vis);
        mTable_4_3.setVisibility(vis);
        btn.setVisibility(vis);

    }

    private void set_tables(int[][] table_array, int room_num){
        if(!ready) {return;}

        for (int x = 0; x < table_array.length; x++) {
            String currX = String.format("%d", x + 1);
            for (int y = 0; y < table_array[x].length; y++) {
                String currY = String.format("%d", y + 1);
                String val = currX + "_" + currY;
                String logged = String.format("Value: %d", table_array[x][y]);
                Log.d("CURR_VAL", val + logged);
                if (table_array[x][y] == 1) {
                    switch (val) {
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
                } else if (table_array[x][y] == 2) {
                    switch (val) {
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
                } else {
                    switch (val) {
                        case "1_1":
                            mTable_1_1.setImageResource(android.R.color.transparent);
                            break;
                        case "1_2":
                            mTable_1_2.setImageResource(android.R.color.transparent);
                            break;
                        case "1_3":
                            mTable_1_3.setImageResource(android.R.color.transparent);
                            break;
                        case "2_1":
                            mTable_2_1.setImageResource(android.R.color.transparent);
                            break;
                        case "2_2":
                            mTable_2_2.setImageResource(android.R.color.transparent);
                            break;
                        case "2_3":
                            mTable_2_3.setImageResource(android.R.color.transparent);
                            break;
                        case "3_1":
                            mTable_3_1.setImageResource(android.R.color.transparent);
                            break;
                        case "3_2":
                            mTable_3_2.setImageResource(android.R.color.transparent);
                            break;
                        case "3_3":
                            mTable_3_3.setImageResource(android.R.color.transparent);
                            break;
                        case "4_1":
                            mTable_4_1.setImageResource(android.R.color.transparent);
                            break;
                        case "4_2":
                            mTable_4_2.setImageResource(android.R.color.transparent);
                            break;
                        case "4_3":
                            mTable_4_3.setImageResource(android.R.color.transparent);
                            break;
                        default:
                            Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                            break;
                    }
                }
            }
        }
    }

    private String getTime(String time) {
        String ret = "";
        String t;
        if(!(time.length() == 4)){
            t = 0 + time;
            time = t;
        }
        String hrs = time.substring(0, 1);

        //retrieve minutes:
        String min = time.substring(2,3);
        String _min = "";
        //positional minutes:
        char tMin = min.charAt(0);

        switch(tMin){
            case '3':
                _min += "3";
                break;
            default:
                _min += "0";
                break;
        }
        _min += "0";
        ret = hrs + _min;

        return ret;
    }

    /**
     * Returns collected arrays from Firebase to matrix
     */
    private int[][] setArray(String[][] string_array){
        int[][] toReturn = new int[4][3];

        for(int i = 0; i < toReturn.length; i++){
            for(int j = 0; j < toReturn[i].length; j++){
                int k = Integer.parseInt(string_array[i][j]);
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

    public void _update_layout(String roomSelection, String currSelection){
        //Retrieve Selection:
        int row = Integer.parseInt(String.valueOf(currSelection.charAt(0)));
        int column = Integer.parseInt(String.valueOf(currSelection.charAt(2)));

        //Update the Layout of the Current Selection:
        ArrayList<String> arrayList = new ArrayList<>();
        for(int x = 0; x < current_table_array.length; x++) {
            for (int y = 0; y < current_table_array[x].length; y++) {
                if (row != x && column != y) {
                    arrayList.add(String.format("%d", current_table_array[x][y]));
                } else {
                    current_table_array[x][y] = 2;
                    arrayList.add(String.format("%d", current_table_array[x][y]));
                }
            }
        }

        //Correct Format:
        List<String> list = arrayList;

        Map<String, Object> layout = new HashMap<>();

        DocumentReference userRef = mFirestore.collection("restaurants").document(restaurant_id).collection("Layouts").document(selected_time);
        //Update the Room:
        switch(roomSelection){
            case "One":
                layout.put("Room 1", list);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                userRef.set(layout);
                break;
            case "Two":
                layout.put("Room 1", room_1);
                layout.put("Room 2", list);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                userRef.set(layout);
                break;
            case "Three":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", list);
                layout.put("Room 4", room_4);
                userRef.set(layout);
                break;
            case "Four":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", list);
                userRef.set(layout);
                break;
        }
    }

    public void onSubmit(View view){
        _update_layout(roomSelection, currSelection);

        confirmation_arr[0] = currSelection;
        confirmation_arr[1] = roomSelection;
        confirmation_arr[2] = restaurant_id;
        confirmation_arr[3] = selected_time;

        Intent intent = new Intent(this, ReserveTableActivity.class);
        //Passes the table selection and the room selection arr[0] arr[1] respectively
        //arr[3] contains the restaurant id for documentation purposes
        //arr[4] contains the selected time from the previous activity
        intent.putExtra(SELECTION_DETAILS, confirmation_arr);
        startActivity(intent);
    }
}
