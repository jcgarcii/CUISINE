package com.example.cpre388.cuisine.Models;

/**
 * User model - depricated
 */
public class user_model {
    private Float rating;
    private String uid;
    private String name;
    private String preference;
    private String type;

    public user_model(String uid){
        this.uid = uid;
    }

    public void set_name(String name){this.name = name; }
    public String get_name(){return name;}

    public void set_pref(String pref){this.preference = pref;}
    public String get_pref(){return preference;}

    public void set_type(String type){
        if(!type.equals(this.type)) {
            this.type = type;
        }
    }
    public String get_type(){return type; }

}
