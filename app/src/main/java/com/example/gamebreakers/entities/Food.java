package com.example.gamebreakers.entities;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Food {
    String foodName;
    int price;
    String[] Reviews;
    int totalscore;
    int totalvotes;

    public Food(String foodName, int price) {
        this.foodName = foodName;
        this.price = price;
    }

    public String getFoodName() {
        return foodName;
    }
    public int getPrice() {return price;}

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public float getRating(){return ((float) totalscore)/totalvotes;}
}
