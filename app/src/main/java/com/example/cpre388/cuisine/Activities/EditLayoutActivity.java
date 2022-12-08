package com.example.cpre388.cuisine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditLayoutActivity extends AppCompatActivity {
    private static final String EDIT_ROOM_ITERATION = "key_current_room_itr";
    private String restaurant_id;
    private String selected_time;

    //Tables in the Current Room Selection:
    private ImageView mTable_1_1, mTable_1_2, mTable_1_3,
            mTable_2_1, mTable_2_2, mTable_2_3,
            mTable_3_1, mTable_3_2, mTable_3_3,
            mTable_4_1, mTable_4_2, mTable_4_3;

    private TextView banner;

    private Boolean ready;
    private int current_iteration;

    //Used to keep track of which tables are available for use:
    private int[][] current_table_array;

    //Array Strings to Store
    private String[][] s_current_table_array;

    private int roomSelection;

    private Spinner spinner;
    private ArrayList<String> list;
    private String getRestaurant_id;

    //Time Arrays:
    private String[] times;

    private AppCompatButton btn;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;

    //Retrieve List<Integer> from cloud:
    private Map<String, Object> map;
    private List<String> current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_layout);

        mFirestore = FirebaseUtil.getFirestore();

        //Restaurant ID Ready:
        ready = false;
        Intent intent = getIntent();
        current_iteration = intent.getExtras().getInt(EDIT_ROOM_ITERATION);

        //Time Documents to Commit:
        times = getResources().getStringArray(R.array.times);

        //Init Matrices:

        current_table_array = new int[4][3];
        s_current_table_array = new String[4][3];

        btn = findViewById(R.id.confirm_table_layout_btn);
        banner = findViewById(R.id.edit_activity_banner);
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

        init_tables();

        banner.setText(String.format("Changing Room: %d", current_iteration));
        btn.setOnClickListener(this::onSubmit);
    }


    private void init_tables() {
        int[][] toSet = new int[4][3];

        for (int x = 0; x < toSet.length; x++) {
            for (int y = 0; y < toSet[x].length; y++) {
                toSet[x][y] = 1;
            }
        }
        current_table_array = toSet;
        _default_tables();
    }

    private void _default_tables(){
        mTable_1_1.setImageResource(R.drawable.table_for_default);
        mTable_1_2.setImageResource(R.drawable.table_for_default);
        mTable_1_3.setImageResource(R.drawable.table_for_default);

        mTable_2_1.setImageResource(R.drawable.table_for_default);
        mTable_2_2.setImageResource(R.drawable.table_for_default);
        mTable_2_3.setImageResource(R.drawable.table_for_default);

        mTable_3_1.setImageResource(R.drawable.table_for_default);
        mTable_3_2.setImageResource(R.drawable.table_for_default);
        mTable_3_3.setImageResource(R.drawable.table_for_default);

        mTable_4_1.setImageResource(R.drawable.table_for_default);
        mTable_4_2.setImageResource(R.drawable.table_for_default);
        mTable_4_3.setImageResource(R.drawable.table_for_default);
    }

    /**
     * Returns int matrices in String format for Firestore
     */
    private String[][] setArray(int[][] int_array){
        String[][] toReturn = new String[4][3];

        for(int i = 0; i < toReturn.length; i++){
            for(int j = 0; j < toReturn[i].length; j++){
                String k = String.valueOf(int_array[i][j]);
                toReturn[i][j] = k;
            }
        }
        return toReturn;
    }

    /**
     * Prepares the Spinner List for room selection
     */
    private void setSpinner(){
        //String Values for View purposes:
        String one = "Room One";
        list.add(one);
        String two = "Room Two";
        list.add(two);
        String three = "Room Three";
        list.add(three);
        String four = "Room Four";
        list.add(four);
    }

    public void on_1_1_clicked(View view){
        if(current_table_array[0][0] == 1) {
            current_table_array[0][0] = 0;
            mTable_1_1.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[0][0] = 1;
            mTable_1_1.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_1_2_clicked(View view) {
        if (current_table_array[0][1] == 1) {
            current_table_array[0][1] = 0;
            mTable_1_2.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[0][1] = 1;
            mTable_1_2.setImageResource(R.drawable.table_for_default);
        };

    }

    public void on_1_3_clicked(View view){
        if (current_table_array[0][2] == 1) {
            current_table_array[0][2] = 0;
            mTable_1_3.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[0][2] = 1;
            mTable_1_3.setImageResource(R.drawable.table_for_default);
        }

    }
    public void on_2_1_clicked(View view){
        if (current_table_array[1][0] == 1) {
            current_table_array[1][0] = 0;
            mTable_2_1.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[1][0] = 1;
            mTable_2_1.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_2_2_clicked(View view){
        if (current_table_array[1][1] == 1) {
            current_table_array[1][1] = 0;
            mTable_2_2.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[1][1] = 1;
            mTable_2_2.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_2_3_clicked(View view){

        if (current_table_array[1][2] == 1) {
            current_table_array[1][2] = 0;
            mTable_2_3.setImageResource(android.R.color.transparent);

        }else{
            current_table_array[1][2] = 1;
            mTable_2_3.setImageResource(R.drawable.table_for_default);
        }

    }
    public void on_3_1_clicked(View view) {
        if (current_table_array[2][0] == 1) {
            current_table_array[2][0] = 0;
            mTable_3_1.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[2][0] = 1;
            mTable_3_1.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_3_2_clicked(View view){
         if (current_table_array[2][1] == 1) {
             current_table_array[2][1] = 0;
             mTable_3_2.setImageResource(android.R.color.transparent);
         }else{
             current_table_array[2][1] = 1;
             mTable_3_2.setImageResource(R.drawable.table_for_default);
         }

    }
    public void on_3_3_clicked(View view){
        if (current_table_array[2][2] == 1) {
            current_table_array[2][2] = 0;
            mTable_3_3.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[2][2] = 1;
            mTable_3_3.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_4_1_clicked(View view){
        if (current_table_array[3][0] == 1) {
            current_table_array[3][0] = 0;
            mTable_4_1.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[3][0] = 1;
            mTable_4_1.setImageResource(R.drawable.table_for_default);
        }
    }
    public void on_4_2_clicked(View view){
        if (current_table_array[3][1] == 1) {
            current_table_array[3][1] = 0;
            mTable_4_2.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[3][1] = 1;
            mTable_4_2.setImageResource(R.drawable.table_for_default);
        }
    }

    public void on_4_3_clicked(View view){
        if (current_table_array[3][2] == 1) {
            current_table_array[3][2] = 0;
            mTable_4_3.setImageResource(android.R.color.transparent);
        }else{
            current_table_array[3][2] = 1;
            mTable_4_3.setImageResource(R.drawable.table_for_default);
        }

    }

    public ArrayList<String> _update_room(String[][] room_array){

        //Update the Layout of the Current Selection:
        ArrayList<String> arrayList = new ArrayList<>();
        for (int x = 0; x < room_array.length; x++) {
            for (int y = 0; y < room_array[x].length; y++) {
                arrayList.add(room_array[x][y]);
            }
        }
        return arrayList;
    }

    public void commitLayout(String time){
        //Correct Format
        current = _update_room(s_current_table_array);

        Map<String, Object> layout = new HashMap<>();
        DocumentReference userRef = mFirestore.collection("restaurants").document("test").collection("Layouts").document(time);

        String room_to_set;
        //Update the Rooms:
        switch(current_iteration){
            case 1:
                layout.put("Room 1", current);
                break;
            case 2:
                layout.put("Room 2", current);
                break;
            case 3:
                layout.put("Room 3", current);
                break;
            case 4:
                layout.put("Room 4", current);
                break;
        }

        userRef.update(layout);
    }


    private void onSubmit(View view){
        s_current_table_array = setArray(current_table_array);

        for(int i = 0; i < times.length; i++){
            commitLayout(times[i]);
        }

        switch (current_iteration){
            case 1:
                Intent i = new Intent(this, EditLayoutActivity.class);
                i.putExtra(EDIT_ROOM_ITERATION, 2);
                startActivity(i);
                break;
            case 2:
                Intent y = new Intent(this, EditLayoutActivity.class);
                y.putExtra(EDIT_ROOM_ITERATION, 3);
                startActivity(y);
                break;
            case 3:
                Intent k = new Intent(this, EditLayoutActivity.class);
                k.putExtra(EDIT_ROOM_ITERATION, 4);
                startActivity(k);
                break;
            case 4:
                Intent main = new Intent(this, activity_owner_main.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                break;
        }
    }
}