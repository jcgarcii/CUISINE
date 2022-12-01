package com.example.cpre388.cuisine.Models;

import android.util.Log;

import com.example.cpre388.cuisine.R;

/**
 * Room model for the app:
 */
public class room_model {
    private static final float FULLY_BOOKED = -1;
    private String restaurant_id;
    private int reservations;
    private int capacity;
    private int room_number;
    private boolean isBooked;
    private int[][] table_layout;

    /**
     * Initializes a room object
     * @param res_id - restaurant name
     * @param number - room number
     * @param layout - stored table layout
     */
    public room_model(String res_id, int number, int[][] layout){
        this.restaurant_id = res_id;
        this.room_number = number;
        this.table_layout = layout;

        this.reservations = 0;
        this.isBooked = false;
    }
    /**
     * Returns capacity of the room:
     * @return - percentage full - unless it is full - returns error code FULLY_BOOKED
     */
    public float getCapacity(){
        int res = this.reservations;
        int cap = this.capacity;
        int check = cap - res;
        float percentage = (float) ((res * 1.0 )/cap);

        if(check <= 0){
            this.isBooked = true;
            return FULLY_BOOKED;
        } else{
            this.isBooked = false;
            return percentage;
        }
    }

    public int[][] getTable_layout(){return table_layout;}

    /**
     * Allows the restaurant owner to change table layout of their restaurants
     * @param current
     * @param type
     * @param new_position
     */
    public void change_layout(int[][] current, int type, String new_position){
        switch(new_position){
            case "1_1":
                if(type == 1)
                {
                    current[0][0] = 1;
                }
                else if(type == 0)
                {
                    current[0][0] = 0;
                }
                else if(type == 2)
                {
                    current[0][0] = 1;
                }
                break;
            case "1_2":
                if(type == 1)
                {
                    current[0][1] = 1;
                }
                else if(type == 0)
                {
                    current[0][1] = 0;
                }
                else if(type == 2)
                {
                    current[0][1] = 0;
                }
                break;
            case "1_3":
                if(type == 1)
                {
                    current[0][2] = 1;
                }
                else if(type == 0)
                {
                    current[0][2] = 0;
                }
                else if(type == 2)
                {
                    current[0][2] = 0;
                }
                break;
            case "2_1":
                if(type == 1)
                {
                    current[1][0] = 1;
                }
                else if(type == 0)
                {
                    current[1][0] = 0;
                }
                else if(type == 2)
                {
                    current[1][0] = 0;
                }
                break;
            case "2_2":
                if(type == 1)
                {
                    current[1][1] = 1;
                }
                else if(type == 0)
                {
                    current[1][1] = 0;
                }
                else if(type == 2)
                {
                    current[1][1] = 0;
                }
                break;
            case "2_3":
                if(type == 1)
                {
                    current[1][2] = 1;
                }
                else if(type == 0)
                {
                    current[1][2] = 0;
                }
                else if(type == 2)
                {
                    current[1][2] = 0;
                }
                break;
            case "3_1":
                if(type == 1)
                {
                    current[2][0] = 1;
                }
                else if(type == 0)
                {
                    current[2][0] = 0;
                }
                else if(type == 2)
                {
                    current[2][0] = 0;
                }
                break;
            case "3_2":
                if(type == 1)
                {
                    current[2][1] = 1;
                }
                else if(type == 0)
                {
                    current[2][1] = 0;
                }
                else if(type == 2)
                {
                    current[2][1] = 0;
                }
                break;
            case "3_3":
                if(type == 1)
                {
                    current[2][2] = 1;
                }
                else if(type == 0)
                {
                    current[2][2] = 0;
                }
                else if(type == 2)
                {
                    current[2][2] = 0;
                }
                break;
            case "4_1":
                if(type == 1)
                {
                    current[3][0] = 1;
                }
                else if(type == 0)
                {
                    current[3][0] = 0;
                }
                else if(type == 2)
                {
                    current[3][0] = 0;
                }
                break;
            case "4_2":
                if(type == 1)
                {
                    current[3][1] = 1;
                }
                else if(type == 0)
                {
                    current[3][1] = 0;
                }
                else if(type == 2)
                {
                    current[3][1] = 0;
                }
                break;
            case "4_3":
                if(type == 1)
                {
                    current[3][2] = 1;
                }
                else if(type == 0)
                {
                    current[3][2] = 0;
                }
                else if(type == 2)
                {
                    current[3][2] = 0;
                }
                break;
            case "default":
                Log.d("SELECTTABLEACTIVITY", "DEFAULT CASE");
                break;
        }
    }

    /**
     * Checks if room is fully booked
     * @return - returns boolean if room is booked
     */
    public boolean isBooked(){
        return this.isBooked;
    }
}
