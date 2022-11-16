package com.example.cpre388.cuisine.Models;

/**
 * Room model for the app:
 */
public class room_model {
    private static final float FULLY_BOOKED = -1;
    private String restaurant;
    private int reservations;
    private int capacity;
    private int room_number;
    private int room_size;
    private boolean isBooked;

    /**
     * Initializes a room object
     * @param name - restaurant name
     * @param number - room number
     * @param size - number of tables supported in the restaurant
     * @param capacity - total number of seats
     */
    public void room_model(String name, int number, int size, int capacity){
        this.restaurant = name;
        this.room_number = number;
        this.room_size = size;
        this.capacity = capacity;
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

    /**
     * Checks if room is fully booked
     * @return - returns boolean if room is booked
     */
    public boolean isBooked(){
        return this.isBooked;
    }
}
