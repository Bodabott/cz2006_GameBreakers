package com.example.gamebreakers.entities;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Stall {

    int stall_ID, price, type, inQueue;
    String stallName;
    String averageTime;
    String postalCode;

    public Stall() {}

    public Stall(int stall_ID, String stallName, int inQueue) {
        this.stall_ID = stall_ID;
        this.stallName = stallName;
        this.inQueue = inQueue;
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

    public int getQueueNum() {
        return inQueue;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
