package com.example.cpre388.cuisine.Models;

public class restaurant_model {

    private static final float FULLY_BOOKED = -1;
    private String owner;
    private String name;
    private int size;
    private int capacity;
    private int reservations;
    private boolean booked;


    /**
     * Initializes a new restaurant object
     * @param owner - restaurant owner
     * @param name - restaurant name
     * @param size - restaurant size (rooms)
     */
    public void restaurant_model(String owner, String name, int size, int capacity){
        this.owner = owner;
        this.name = name;
        this.size = size;
        this.capacity = capacity;
        this.booked = false;
        this.reservations = 0;
    }

    /**
     * Returns capacity of the restuarant:
     * @return - percentage full - unless it is full - returns error code FULLY_BOOKED
     */
    public float getCapacity(){
        int res = this.reservations;
        int cap = this.capacity;
        int check = cap - res;
        float percentage = (float) ((res * 1.0 )/cap);

        if(check <= 0){
            this.booked = true;
            return FULLY_BOOKED;
        } else{
            this.booked = false;
            return percentage;
        }
    }

    /**
     * Checks if restaurant is fully booked
     * @return - returns boolean if restaurant is booked
     */
    public boolean isBooked(){
        return this.booked;
    }

}
