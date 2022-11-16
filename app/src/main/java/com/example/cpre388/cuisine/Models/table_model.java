package com.example.cpre388.cuisine.Models;


/**
 * App's Table Model:
 */
public class table_model {
    private int table_room;
    private int table_num;
    private int table_seats;
    private boolean table_free;
    private String table_reservationName;
    private int table_reservationTime;

    /**
     * Initializes a new table
     * @param room - room number at the restaurant
     * @param number - table number within the room
     * @param seats - table size
     */
    public void table_model(int room, int number, int seats){
        this.table_room = room;
        this.table_num = number;
        this.table_seats = seats;
        this.table_free = true;
        this.table_reservationName = "NULL";
        this.table_reservationTime = 0;
    }

    /**
     * Sets table reservation for individual table:
     * @param name - reservation name
     * @param time - reservation time
     */
    public void setTable_reservation(String name, int time){
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
