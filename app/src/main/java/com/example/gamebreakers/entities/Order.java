package com.example.gamebreakers.entities;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Order {
    int id;
    String foodname;
    String user;
    String stallname;
    String ordertime;

    public Order( int id, String foodname, String user, String stallname) {
        this.id=id;
        this.foodname = foodname;
        this.user = user;
        this.stallname = stallname;
    }

    public String getFoodName() {
        return foodname;
    }
}
