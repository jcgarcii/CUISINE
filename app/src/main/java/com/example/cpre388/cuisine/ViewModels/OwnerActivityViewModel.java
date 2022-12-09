package com.example.cpre388.cuisine.ViewModels;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel for {@link com.example.cpre388.cuisine.Activities.OwnerActivity}.
 */

public class OwnerActivityViewModel extends ViewModel {

    private String restaurant_id;
    private boolean ready;

    public OwnerActivityViewModel(){ready = false; }

    public void setRestaurant_id(String id){
        ready = true;
        this.restaurant_id = id;
    }

    public boolean ifReady(){return ready;}

    public String getRestaurant_id(){
        return restaurant_id;
    }
}
