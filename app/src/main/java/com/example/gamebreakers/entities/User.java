package com.example.gamebreakers.entities;

/**
 * Created by Jiachin on 13-Mar-18.
 */

public class User {
    int id;
    String name;
    int balance;

    public User (int id, String user, int bal) {
        this.id = id;
        name = user;
        balance=bal;
    }

    public String getName() {return name;}
    public int getBalance() {return balance;}
}