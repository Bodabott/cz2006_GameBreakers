package com.example.gamebreakers.entities;

/**
 * Created by Jiachin on 12-Mar-18.
 */

public class Owner extends User {
    Stall stall;

    Owner(String owner, String pass, Stall stall){
        super(owner, pass);
        this.stall = stall;
    }
}
