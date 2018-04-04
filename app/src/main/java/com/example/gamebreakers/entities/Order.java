package com.example.gamebreakers.entities;

import java.time.LocalDateTime;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Order {
    int id;
    String foodname;
    String user;
    String stallname;
    String collectiontime;

    public Order( int id, String foodname, String user, String stallname, String collectiontime) {
        this.id=id;
        this.foodname = foodname;
        this.user = user;
        this.stallname = stallname;
        this.collectiontime=collectiontime;
    }

    public String getFoodName() {
        return foodname;
    }

    public String getCollectiontime() {
        LocalDateTime localdate=LocalDateTime.parse(collectiontime);
        String time = ""+localdate.getHour()+ ":" +localdate.getMinute();
        return time;
    }
}
