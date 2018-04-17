package com.example.gamebreakers.entities;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        return collectiontime;
    }
    public Calendar getCalendartime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
       try {
           cal.setTime(sdf.parse(collectiontime));
       } catch (Exception ParseException) {System.out.println("@@@@@@@@@@@@@@@@ PARSE FAILED @@@@@@@@");}

        return cal;
    }

    public String getStallName() {return stallname;}
    public boolean isCompleted() {return completed;}
    public void complete() {completed=true;}
}
