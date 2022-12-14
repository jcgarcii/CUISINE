package com.example.cpre388.cuisine.Models;


/**
 * Receipt model. -Adapted from the Firebase lab restaurant model
 */
public class bill_model {

    private String food, drinks, refills, numFood, numDrinks, numRefills, tip;


    //Initializes a new bill object
    public bill_model() {}

    //For grabbing bill values
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

}