package com.example.cpre388.cuisine.Activities.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cpre388.cuisine.R;
import com.example.cpre388.cuisine.Util.FirebaseUtil;
import com.example.cpre388.cuisine.ViewModels.OwnerActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    private final static String SELECTION_DETAILS = "com.example.cpre388.cuisine.Activities.SelectTableActivity";
    public static final String KEY_RESTAURANT_ID = "key_restaurant_id";
    public static final String SELECTED_TIME = "key_time_selected";

    private String restaurant_id;
    private String selected_time;

    //time selection variables:
    private int mYear, mMonth, mDay, mHour, mMinute;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(String param1, String param2) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        OwnerActivityViewModel viewModel = new ViewModelProvider(requireActivity()).get(OwnerActivityViewModel.class);
        if(viewModel.ifReady()){
            restaurant_id = viewModel.getRestaurant_id();
        }
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //Hours Format:
        String hr = String.format("%d", mHour);
        if(hr.length() == 1){
            String i = "0" + hr;
            hr = i;
        }
        //Minute Format:
        String min = String.format("%d", mMinute);
        String _min = "";
        char tens = min.charAt(0);
        if(Integer.parseInt(String.valueOf(tens)) >= 3){
            _min = "30";
        }
        else {
            _min = "00";
        }

        selected_time = String.format("%s%s", hr, _min);

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

                            ready = true;

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





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        spinner = (Spinner) getView().findViewById(R.id.room_selector);
        spinner.setVisibility(View.INVISIBLE);
        list = new ArrayList<String>();
        setSpinner();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.table_reservation_spinner,list);
        spinner.setAdapter(adapter);

        //Table Matrix: Row 1
        mTable_1_1 = getView().findViewById(R.id.table_1_1);
        mTable_1_2 = getView().findViewById(R.id.table_1_2);
        mTable_1_3 = getView().findViewById(R.id.table_1_3);
        //Row 2:
        mTable_2_1 = getView().findViewById(R.id.table_2_1);
        mTable_2_2 = getView().findViewById(R.id.table_2_2);
        mTable_2_3 = getView().findViewById(R.id.table_2_3);
        //Row 3:
        mTable_3_1 = getView().findViewById(R.id.table_3_1);
        mTable_3_2 = getView().findViewById(R.id.table_3_2);
        mTable_3_3 = getView().findViewById(R.id.table_3_3);
        //Row 4:
        mTable_4_1 = getView().findViewById(R.id.table_4_1);
        mTable_4_2 = getView().findViewById(R.id.table_4_2);
        mTable_4_3 = getView().findViewById(R.id.table_4_3);

        init_tables(0);


        if(ready){
            init_tables(1);
            table_array_room_one = setArray(stable_array_room_one);
            table_array_room_two = setArray(stable_array_room_two);
            table_array_room_three = setArray(stable_array_room_three);
            table_array_room_four = setArray(stable_array_room_four);
            spinner.setVisibility(View.VISIBLE);

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
        }

        return inflater.inflate(R.layout.fragment_overview, container, false);
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