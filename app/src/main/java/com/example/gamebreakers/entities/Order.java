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
    boolean completed;

    public Order( int id, String foodname, String user, String stallname, String collectiontime, boolean completed) {
        this.id=id;
        this.foodname = foodname;
        this.user = user;
        this.stallname = stallname;
        this.collectiontime=collectiontime;
        this.completed = completed;
    }

    public String getFoodName() {
        return foodname;
    }

    public String getCollectiontime() {
        LocalDateTime localdate=LocalDateTime.parse(collectiontime);
        String time = ""+localdate.getHour()+ ":" +localdate.getMinute();
        return time;
    }

    public String getStallName() {return stallname;}

    public String getFullCollectiontime() {
        return collectiontime;
    }
    public boolean isCompleted() {return completed;}
    public void complete() {completed=true;}
}
