package com.example.cpre388.cuisine.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillingActivity extends AppCompatActivity {
    private String[] _reservation;
    private static final String RESERV_BILLING_KEY = "key_reservation_billing";

    //Intent Variables:
    private String name;
    private String uid;
    private String rest_id;
    private String reservation_id;
    private String fire_time;
    private String fire_room;
    private String fire_table;

    private FirebaseFirestore mFirestore;
    private FirebaseUser currUser;
    private DocumentReference receipt_ref;
    private DocumentReference reservationRef;
    private DocumentReference roomRef;


    //View Objects:
    private EditText _foodEntry, _bevEntry, _refillsEntry,
            _numFoodEntry, _numRefillsEntry, _numBevEntry;
    private TextView banner;
    private AppCompatButton submit_btn;

    private int ready;
    private int layout_ready;

    //Retrieve List<Integer> from cloud:
    private Map<String, Object> map;
    private List<String> room_1;
    private List<String> room_2;
    private List<String> room_3;
    private List<String> room_4;

    private int[][] table_array_room_one;
    private int[][] table_array_room_two;
    private int[][] table_array_room_three;
    private int[][] table_array_room_four;
    private int[][] current_table_array;

    private String[][] stable_array_room_one;
    private String[][] stable_array_room_two;
    private String[][] stable_array_room_three;
    private String[][] stable_array_room_four;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        _foodEntry = findViewById(R.id.billing_activity_food_entry);
        _bevEntry = findViewById(R.id.billing_activity_bev_entry);
        _refillsEntry = findViewById(R.id.billing_activity_refills_entry);
        _numFoodEntry = findViewById(R.id.billing_activity_num_food);
        _numBevEntry = findViewById(R.id.billing_activity_num_bev);
        _numRefillsEntry = findViewById(R.id.billing_activity_num_refills);
        banner = findViewById(R.id.billing_activity_banner);
        submit_btn = findViewById(R.id.billing_activity_submit_btn);

        table_array_room_one = new int[4][3];
        table_array_room_two = new int[4][3];
        table_array_room_three = new int[4][3];
        table_array_room_four = new int[4][3];
        current_table_array = new int[4][3];

        stable_array_room_one = new String[4][3];
        stable_array_room_two = new String[4][3];
        stable_array_room_three = new String[4][3];
        stable_array_room_four = new String[4][3];

        _reservation = new String[7];
        /*_reservation[0] = reservation.getString("reservation_for");
        _reservation[1] = reservation.getString("uid");
        _reservation[2] = reservation.getString("restaruant_id");
        _reservation[3] = reservation.getId();
        _reservation[4] = reservation.getString("reservation_time");
        _reservation[5] = reservation.getString("room_selected");
        _reservation[6] = reservation.getString("table_selected");*/

        Intent intent = getIntent();
        _reservation = intent.getExtras().getStringArray(RESERV_BILLING_KEY);
        name = _reservation[0];
        uid = _reservation[1];
        rest_id = _reservation[2];
        reservation_id = _reservation[3];
        fire_time = _reservation[4];
        fire_room = _reservation[5];
        fire_table = _reservation[6];

        banner.setText(String.format("Bill for: %s", name));

        ready = 0;
        layout_ready = 0;

        mFirestore = FirebaseUtil.getFirestore();
        String local = LocalTime.now().toString();
        receipt_ref = mFirestore.collection("Users").document(String.format("%s",uid)).collection("Receipts").document(rest_id + local);
        reservationRef = mFirestore.collection("Reservations").document(reservation_id);

        roomRef = mFirestore.collection("restaurants").document(rest_id).collection("Layouts").document(fire_time);

        roomRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        map = (Map<String, Object>) document.getData();
                        room_1 = (List<String>) map.getOrDefault("Room 1", "lol");
                        room_2 = (List<String>) map.getOrDefault("Room 2", "lol");
                        room_3 = (List<String>) map.getOrDefault("Room 3", "lol");
                        room_4 = (List<String>) map.getOrDefault("Room 4", "lol");

                        int _x_1 = 0;
                        int _y_1 = 0;
                        for (int i = 0; i < room_1.size(); i++) {
                            stable_array_room_one[_y_1][_x_1] = room_1.get(i);

                            if (_x_1 == 2) {
                                _y_1++;
                                _x_1 = 0;
                            } else {
                                _x_1++;
                            }
                        }

                        int _x_2 = 0;
                        int _y_2 = 0;
                        for (int i = 0; i < room_2.size(); i++) {
                            stable_array_room_two[_y_2][_x_2] = room_2.get(i);

                            if (_x_2 == 2) {
                                _y_2++;
                                _x_2 = 0;
                            } else {
                                _x_2++;
                            }
                        }

                        int _x_3 = 0;
                        int _y_3 = 0;
                        for (int i = 0; i < room_3.size(); i++) {
                            stable_array_room_three[_y_3][_x_3] = room_3.get(i);

                            if (_x_3 == 2) {
                                _y_3++;
                                _x_3 = 0;
                            } else {
                                _x_3++;
                            }
                        }

                        int _x_4 = 0;
                        int _y_4 = 0;
                        for (int i = 0; i < room_4.size(); i++) {
                            stable_array_room_four[_y_4][_x_4] = room_4.get(i);

                            if (_x_4 == 2) {
                                _y_4++;
                                _x_4 = 0;
                            } else {
                                _x_4++;
                            }
                        }

                        table_array_room_one = setArray(stable_array_room_one);
                        table_array_room_two = setArray(stable_array_room_two);
                        table_array_room_three = setArray(stable_array_room_three);
                        table_array_room_four = setArray(stable_array_room_four);

                        set_active();
                        layout_ready++;
                    }
                }


            }
        });
        submit_btn.setOnClickListener(this::onSubmit);
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


    private void set_active(){
        switch (fire_room){
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

    public void _update_layout(String roomSelection, String currSelection, int[][] array){
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
                    int mSelection = 1;
                    arrayList.add(String.format("%d", mSelection));
                }
                else {
                    arrayList.add(String.format("%d", array[x][y]));
                }
            }
        }

        //Correct Format:

        Map<String, Object> layout = new HashMap<>();

        DocumentReference new_layout = mFirestore.collection("restaurants").document(rest_id).collection("Layouts").document(fire_time);
        //Update the Room:
        switch(roomSelection){
            case "One":
                layout.put("Room 1", arrayList);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                new_layout.set(layout);
                break;
            case "Two":
                layout.put("Room 1", room_1);
                layout.put("Room 2", arrayList);
                layout.put("Room 3", room_3);
                layout.put("Room 4", room_4);
                new_layout.set(layout);
                break;
            case "Three":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", arrayList);
                layout.put("Room 4", room_4);
                new_layout.set(layout);
                break;
            case "Four":
                layout.put("Room 1", room_1);
                layout.put("Room 2", room_2);
                layout.put("Room 3", room_3);
                layout.put("Room 4", arrayList);
                new_layout.set(layout);
                break;
        }
    }

    private void delete_reservation(){
        reservationRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error deleting document", e);
                    }
                });
    }

    private void commit_receipt() {
        String foodEntry = _foodEntry.getText().toString();
        String drinkEntry = _bevEntry.getText().toString();
        String refillsEntry = _refillsEntry.getText().toString();

        String mFoodEntry = foodEntry.replaceAll("[$.]", "");
        String mDrinkEntry = drinkEntry.replaceAll("[$.]", "");
        String mRefillsEntry = refillsEntry.replaceAll("[$.]", "");

        String numFood = _numFoodEntry.getText().toString();
        String numDrink = _numBevEntry.getText().toString();
        String numRefills = _numRefillsEntry.getText().toString();

        Map<String, Object> receipt = new HashMap<>();
        receipt.put("name", name);
        receipt.put("uid", uid);
        receipt.put("rest_id", rest_id);
        receipt.put("food", mFoodEntry);
        receipt.put("drinks", mDrinkEntry);
        receipt.put("refills", mRefillsEntry);
        receipt.put("numFood", numFood);
        receipt.put("numDrinks", numDrink);
        receipt.put("numRefills", numRefills);
        receipt.put("tip", "0");

        receipt_ref.set(receipt);

        _update_layout(fire_room, fire_table, current_table_array);
        delete_reservation();

        ready++;
    }

    private void onSubmit(View view) {
        if(layout_ready > 0) {
            if (ready > 0) {
                Intent i = new Intent(this, OwnerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            } else {
                commit_receipt();
                Snackbar.make(findViewById(android.R.id.content),
                        "Tap Submit Again to Confirm", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}