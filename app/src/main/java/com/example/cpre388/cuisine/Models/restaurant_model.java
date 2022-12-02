package com.example.cpre388.cuisine.Models;

public class restaurant_model {
    public static final String FIELD_OWNER = "owner";
    public static final String FIELD_CITY = "city";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_POPULARITY = "numRatings";
    public static final String FIELD_AVG_RATING = "avgRating";
    public static final float FULLY_BOOKED = -1;
    //Restaurant private detils:
    private String owner_id;
    private String name;
    private int size;
    private int capacity;
    private int reservations;
    private boolean booked;
    //Restaurant Public Details:
    private String city;
    private String category;
    private String photo;
    private int price;
    private int numRatings;
    private double avgRating;
    /**
     * Initializes a new restaurant object
     * @param owner - restaurant owner (user_id)
     * @param name - restaurant name
     * @param size - restaurant size (rooms)
     */
    public restaurant_model(String owner, String name, int size, int capacity
        , String city, String category, String photo, int price, int numRatings, double avgRating){
        this.owner_id = owner;
        this.name = name;
        this.size = size;
        this.capacity = capacity;
        this.booked = false;
        this.reservations = 0;
        this.city = city;
        this.category = category;
        this.photo = photo;
        this.price = price;
        this.numRatings = numRatings;
        this.avgRating = avgRating;
    }

    public restaurant_model() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(int numRatings) {
        this.numRatings = numRatings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
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
