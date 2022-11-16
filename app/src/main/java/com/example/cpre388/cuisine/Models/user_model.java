package com.example.cpre388.cuisine.Models;

public class user_model {
    private Float rating;
    private String name;
    private Boolean isOwner;

    public void user_model(String name, Boolean owner, Float rating ){
        this.rating = rating;
        this.name = name;
        this.isOwner = owner;
    }
}
