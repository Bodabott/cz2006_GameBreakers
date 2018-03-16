package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;


public class Activity_User_Payment extends AppCompatActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_payment);
        myDb = new DatabaseHelper(this);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String stallMessage = intent.getStringExtra(Activity_User_Stall.STALL_NAME);
        final String foodMessage = intent.getStringExtra(Activity_User_Food.FOOD_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView stallTextView = findViewById(R.id.stall_status);
        TextView foodTextView = findViewById(R.id.food_status);
        stallTextView.setText(stallMessage);
        foodTextView.setText(foodMessage);

    }

    public String foodNameConverter(String old_foodName, String stall_name, String user_name){
        // Initialise
        int ID = 0;
        int Max_ID = 1;
        String[] strings_orders = myDb.getArrayOfOrders(stall_name.substring(7));
        String[] strings_owner_history = myDb.getArrayOfHistory(stall_name.substring(7));
        String[] strings_user_history = myDb.getUserArrayOfHistory(user_name);

        // Initial Check, increment ID to 1
        for(String element : strings_orders)
            if(old_foodName.substring(6).matches(element))
                ID++;
        for(String element : strings_owner_history)
            if(old_foodName.substring(6).matches(element))
                ID++;
        for(String element : strings_user_history)
            if(old_foodName.substring(6).matches(element))
                ID++;

        // Check if there are duplicates in Order List
        for(String element : strings_orders)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.substring(6).matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in Owner History List
        for(String element : strings_owner_history)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.substring(6).matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in User History List
        for(String element : strings_user_history)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.substring(6).matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        if(ID != 0) { // If item already exists in Order/History List
            return  old_foodName.substring(6) + "-" + String.valueOf(Max_ID);
        }else{
            return old_foodName.substring(6);
        }
    }

    public void makePayment(View v){
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String stallMessage = intent.getStringExtra(Activity_User_Stall.STALL_NAME);
        final String foodMessage = intent.getStringExtra(Activity_User_Food.FOOD_NAME);
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);

        String newFoodMessage = foodNameConverter(foodMessage,stallMessage,usernameMessage);
        if(myDb.addOrderArrayData(newFoodMessage,usernameMessage,stallMessage.substring(7))){
            Toast.makeText(getApplicationContext(),"PAYMENT SUCCESSFUL",Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK);
            finish();
        }
        else
            Toast.makeText(getApplicationContext(),"PAYMENT NOT SUCCESSFUL",Toast.LENGTH_LONG).show();
    }

    public void backToFood(View v){
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String stallMessage = intent.getStringExtra(Activity_User_Stall.STALL_NAME);
        final String foodMessage = intent.getStringExtra(Activity_User_Food.FOOD_NAME);
        Intent goIntent = new Intent(v.getContext(),Activity_User_Food.class);
        goIntent.putExtra(Activity_User_Stall.STALL_NAME,stallMessage);
        goIntent.putExtra(Activity_User_Food.FOOD_NAME,foodMessage);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
