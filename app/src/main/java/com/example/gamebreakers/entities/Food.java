package com.example.gamebreakers.entities;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Food {
    String foodName;
    String[] Reviews;
    int totalscore;
    int totalvotes;

    public Food(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getRating(){return ((float) totalscore)/totalvotes;}
}
