package com.example.cpre388.cuisine.Util;

import android.content.Context;

import com.example.cpre388.cuisine.Models.restaurant_model;
import com.example.cpre388.cuisine.R;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for Restaurants.
 */
public class RestaurantUtil {

    private static final String TAG = "RestaurantUtil";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static final String RESTAURANT_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";

    private static final int MAX_IMAGE_NUM = 22;

    private static final String[] NAME_FIRST_WORDS = {
            "Foo",
            "Bar",
            "Baz",
            "Qux",
            "Fire",
            "Sam's",
            "World Famous",
            "Google",
            "The Best",
    };

    private static final String[] NAME_SECOND_WORDS = {
            "Restaurant",
            "Cafe",
            "Spot",
            "Eatin' Place",
            "Eatery",
            "Drive Thru",
            "Diner",
    };


    /**
     * Create a random Restaurant POJO.
     */
    public static restaurant_model getRandom(Context context) {
        restaurant_model restaurant = new restaurant_model();
        Random random = new Random();

        // Cities (first element is 'Any')
        String[] cities = context.getResources().getStringArray(R.array.cities);
        cities = Arrays.copyOfRange(cities, 1, cities.length);

        // Categories (first element is 'Any')
        String[] categories = context.getResources().getStringArray(R.array.categories);
        categories = Arrays.copyOfRange(categories, 1, categories.length);

        int[] prices = new int[]{1, 2, 3};


        List<String> room_1 = Arrays.asList("0", "1", "1",
                                            "0", "1", "0",
                                            "1", "0", "1",
                                            "0", "1", "1");

        List<String> room_2 = Arrays.asList("0", "1", "1",
                "0", "1", "0",
                "1", "0", "1",
                "0", "1", "1");
        List<String> room_3 = Arrays.asList("0", "1", "1",
                "0", "1", "0",
                "1", "0", "1",
                "0", "1", "1");
        List<String> room_4 = Arrays.asList("0", "1", "1",
                "0", "1", "0",
                "1", "0", "1",
                "0", "1", "1");

        restaurant.setName(getRandomName(random));
        restaurant.setCity(getRandomString(cities, random));
        restaurant.setCategory(getRandomString(categories, random));
        restaurant.setPhoto(getRandomImageUrl(random));
        restaurant.setPrice(getRandomInt(prices, random));
        restaurant.setAvgRating(getRandomRating(random));
        restaurant.setNumRatings(random.nextInt(20));

        return restaurant;
    }


    /**
     * Get a random image.
     */
    private static String getRandomImageUrl(Random random) {
        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        int id = random.nextInt(MAX_IMAGE_NUM) + 1;

        return String.format(Locale.getDefault(), RESTAURANT_URL_FMT, id);
    }

    /**
     * Get price represented as dollar signs.
     */
    public static String getPriceString(restaurant_model restaurant) {
        return getPriceString(restaurant.getPrice());
    }

    /**
     * Get price represented as dollar signs.
     */
    public static String getPriceString(int priceInt) {
        switch (priceInt) {
            case 1:
                return "$";
            case 2:
                return "$$";
            case 3:
            default:
                return "$$$";
        }
    }

    private static double getRandomRating(Random random) {
        double min = 1.0;
        return min + (random.nextDouble() * 4.0);
    }

    private static String getRandomName(Random random) {
        return getRandomString(NAME_FIRST_WORDS, random) + " "
                + getRandomString(NAME_SECOND_WORDS, random);
    }

    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

    private static int getRandomInt(int[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

}
