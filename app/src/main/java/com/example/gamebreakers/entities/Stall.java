package com.example.gamebreakers.entities;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Stall {

    int stall_ID;
    String stallName;

    public Stall() {}

    public Stall(int stall_ID, String stallName) {
        this.stall_ID = stall_ID;
        this.stallName = stallName;
    }

    public int getStall_ID() {
        return stall_ID;
    }

    public void setStall_ID(int stall_ID) {
        this.stall_ID = stall_ID;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

}
