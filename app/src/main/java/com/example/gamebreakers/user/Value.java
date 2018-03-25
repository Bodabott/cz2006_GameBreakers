package com.example.gamebreakers.user;

/**
 * Created by Shan Jing on 19/3/2018.
 */

public class Value {

    static private int result = 0;
    static private String value = "0";

    Value(){}

    public void setValue(String v) {

        Integer int_value = Integer.valueOf(v);
        result = int_value + result;

        String str_value = String.valueOf(result);

        value = str_value;
    }

    public String getValue() {
        return "$" + value;
    }

}