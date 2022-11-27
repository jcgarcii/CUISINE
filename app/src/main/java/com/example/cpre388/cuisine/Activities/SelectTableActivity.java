package com.example.cpre388.cuisine.Activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cpre388.cuisine.R;

public class SelectTableActivity extends AppCompatActivity {

    //Tables in the Current Room Selection:
    private ImageView mTable_1_1, mTable_1_2, mTable_1_3,
            mTable_2_1, mTable_2_2, mTable_2_3,
            mTable_3_1, mTable_3_2, mTable_3_3,
            mTable_4_1, mTable_4_2, mTable_4_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_table);

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
    }

    private void initialize_tables(){ 

    }
}
