package com.example.gamebreakers.entities;

/**
 * Created by Jiachin on 13-Mar-18.
 */

public class Owner extends User {
    Stall stall;

    Owner(int id, String owner, int balance, Stall stall){
        super(id, owner, balance);
        this.stall = stall;
    }
}