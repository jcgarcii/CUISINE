package com.example.cpre388.cuisine.Models;

import androidx.lifecycle.ViewModel;

/**
 * App's Table Model:
 */
public class table_model extends ViewModel {
    private String id;
    private int table_room;
    private String table_num;
    private int table_seats;
    private boolean table_free;
    private String table_reservationName;
    private int table_reservationTime;

    //Table Exists:
    Boolean exists;

    /**
     * Initializes a new table
     * @param room - room number at the restaurant
     * @param number - table number within the room
     */
    public void table_model(String res_name, int room, String number, Boolean exists){
        this.id = res_name;
        this.table_room = room;
        this.table_num = number;
        this.table_free = true;
        this.exists = exists;
        this.table_reservationName = "NULL";
        this.table_reservationTime = 0;
    }

    public void setExists(Boolean check){
        this.exists = check;
    }

    public Boolean table_exists(){
        return exists;
    }

    /**
     * Changes the table's room location
     * @param room
     */
    public void change_room(int room){table_room = room;}

    /**
     * Sets table reservation for individual table:
     * @param name - reservation name
     * @param time - reservation time
     */
    public void setTable_reservation(String name, int time, int table_seats){
        this.table_reservationName = name;
        this.table_reservationTime = time;
        this.table_free = false;
    }

    /**
     * Checks if table is free, currently:
     * @return returns table_free boolean
     */
    public Boolean isFree(){
        return this.table_free;
    }

    /**
     * Releases Table:
     */
    public void setFree(){
        this.table_free = true;
        this.table_reservationName = "NULL";
        this.table_reservationTime = 0;
    }

}
