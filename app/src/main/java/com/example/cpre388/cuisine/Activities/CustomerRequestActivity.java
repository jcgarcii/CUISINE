package com.example.cpre388.cuisine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerRequestActivity extends AppCompatActivity {
    public final String REQ_RESERVATION = "request_edit_key";
    //Intent Data:
    private String[] data;
    private String mTable, mRoom, mREST_ID, mTime, mReservationID;

    private int[][] table_array_room_one;
    private int[][] table_array_room_two;
    private int[][] table_array_room_three;
    private int[][] table_array_room_four;
    private int[][] current_table_array;

    private String[][] stable_array_room_one;
    private String[][] stable_array_room_two;
    private String[][] stable_array_room_three;
    private String[][] stable_array_room_four;

    private FirebaseFirestore mFirestore;
    private DocumentReference userRef;
    private boolean ready;

    //Retrieve List<Integer> from cloud:
    private Map<String, Object> map;
    private List<String> room_1;
    private List<String> room_2;
    private List<String> room_3;
    private List<String> room_4;

    //View Buttons:
    private Button refill, check, waiter;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_request);

        check = findViewById(R.id.billRequestButton);
        refill = findViewById(R.id.waterRefillButton);
        waiter = findViewById(R.id.otherRequestButton);

        clicked = false;
        ready = false;

        Intent intent = getIntent();
        data = new String[5];
        data = intent.getExtras().getStringArray(REQ_RESERVATION);
        mTable = data[0];
        mRoom = data[1];
        mREST_ID = data[2];
        mTime = data[3];
        mReservationID = data[4];

        table_array_room_one = new int[4][3];
        table_array_room_two = new int[4][3];
        table_array_room_three = new int[4][3];
        table_array_room_four = new int[4][3];
        current_table_array = new int[4][3];

        stable_array_room_one = new String[4][3];
        stable_array_room_two = new String[4][3];
        stable_array_room_three = new String[4][3];
        stable_array_room_four = new String[4][3];

        mFirestore = FirebaseUtil.getFirestore();
        userRef = mFirestore.collection("restaurants").document(mREST_ID).collection("Layouts").document(mTime);

        refreshTables();
        refill.setOnClickListener(this::onRefill);
        check.setOnClickListener(this::onCheck);
        waiter.setOnClickListener(this::onWaiter);

    }

    private void refreshTables(){
        ready = false;
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

                        //init_tables(1);
                        table_array_room_one = setArray(stable_array_room_one);
                        table_array_room_two = setArray(stable_array_room_two);
                        table_array_room_three = setArray(stable_array_room_three);
                        table_array_room_four = setArray(stable_array_room_four);
                        set_active();

                        ready = true;
                        clicked = false;
                        //spinner.setVisibility(View.VISIBLE);
                        //spinner.setSelection(1);

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

    private void set_active(){
        switch (mRoom){
            case "One":
                current_table_array = table_array_room_one;
                break;
            case "Two":
                current_table_array = table_array_room_two;
                break;
            case "Three":
                current_table_array = table_array_room_three;
                break;
            case "Four":
                current_table_array = table_array_room_four;
                break;
        }

    }

    private void onRefill(View view){
        clicked = true;
        _update_layout(mRoom, mTable, current_table_array, 3);
        refreshTables();
    }
    private void onCheck(View view){
        clicked = true;
        _update_layout(mRoom, mTable, current_table_array, 4);
        refreshTables();
    }
    private void onWaiter(View view){
        clicked = true;
        _update_layout(mRoom, mTable, current_table_array, 5);
        refreshTables();
    }


    public void _update_layout(String roomSelection, String currSelection, int[][] array, int code){
        String currX = "";
        String currY = "";
        String val = "";

        //Update the Layout of the Current Selection:
        ArrayList<String> arrayList = new ArrayList<>();
        for (int x = 0; x < array.length; x++) {
            currX = String.format("%d", x + 1);
            for (int y = 0; y < array[x].length; y++) {
                currY = String.format("%d", y + 1);
                val = currX + "_" + currY;
                //String logged = String.format("Value: %d", array[x][y]);
                if(val.equals(currSelection)){
                    int mSelection = code;
                    arrayList.add(String.format("%d", mSelection));
                }
                else {
                    arrayList.add(String.format("%d", array[x][y]));
                }
            }
        }

        Map<String, Object> layout = new HashMap<>();

        //Update the Room:
        switch(roomSelection){
            case "One":
                layout.put("Room 1", arrayList);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                userRef.update(layout);
                break;
            case "Two":
                layout.put("Room 1", room_1);
                layout.put("Room 2", arrayList);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                userRef.update(layout);
                break;
            case "Three":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", arrayList);
                layout.put("Room 4", room_4);
                userRef.update(layout);
                break;
            case "Four":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", arrayList);
                userRef.update(layout);
                break;
        }
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
}