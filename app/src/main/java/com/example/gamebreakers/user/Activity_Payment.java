package com.example.gamebreakers.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;

public class Activity_Payment extends AppCompatActivity {

    String stallMessage;
    String categoryMessage;
    String foodMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        stallMessage = intent.getStringExtra(Activity_Stall.STALL_NAME);
        categoryMessage = intent.getStringExtra(Activity_Category.CATEGORY_NAME);
        foodMessage = intent.getStringExtra(Activity_Food.FOOD_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView stallTextView = findViewById(R.id.stall_status);
        TextView categoryTextView = findViewById(R.id.category_status);
        TextView foodTextView = findViewById(R.id.food_status);
        stallTextView.setText(stallMessage);
        categoryTextView.setText(categoryMessage);
        foodTextView.setText(foodMessage);
    }

    public void confirmPayment(View view) {
        Intent goIntent = new Intent(view.getContext(),Activity_UserMenu.class);
        Toast.makeText(getApplicationContext(),"PAYMENT SUCCESSFUL",Toast.LENGTH_LONG).show();
        startActivity(goIntent);
    }

    public void cancel(View view) {
        Intent goIntent = new Intent(view.getContext(),Activity_Food.class);

        goIntent.putExtra(Activity_Stall.STALL_NAME,stallMessage);
        goIntent.putExtra(Activity_Category.CATEGORY_NAME,categoryMessage);
        goIntent.putExtra(Activity_Food.FOOD_NAME,foodMessage);

        startActivity(goIntent);
    }
}
