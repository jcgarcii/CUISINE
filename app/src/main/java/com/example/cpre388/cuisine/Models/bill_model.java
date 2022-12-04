package com.example.cpre388.cuisine.Models;

public class bill_model {

    /*
    public static final float FULLY_BOOKED = -1;
    //Restaurant private detils:
    private String owner_id;
    private String name;
    private int size;
    private int capacity;
    private int reservations;
    private boolean booked;

     */

    //Restaurant Public Details:
    private String food, drinks, refills, numFood, numDrinks, numRefills, tip;

    /**
     * Initializes a new bill object
     */
    public bill_model(String food, String drinks, String refills, String num_food, String num_drinks,
                      String num_refills, String tip){

        this.food = food;
        this.drinks = drinks;
        this.refills = refills;
        this.numFood = numFood;
        this.numDrinks = numDrinks;
        this.numRefills = numRefills;
        this.tip = tip;
    }

    public bill_model() {}



    public String getFood() {
        return food;
    }
    public String getDrinks() {
        return drinks;
    }
    public String getRefills() {
        return refills;
    }
    public String getNumFood() {
        return numFood;
    }
    public String getNumDrinks() {
        return numDrinks;
    }
    public String getNumRefills() {
        return numRefills;
    }
    public String getTip() {
        return tip;
    }




    /*
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

*/


}