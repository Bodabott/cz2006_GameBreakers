package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.login.Activity_Main;

/**
 * Created by zNotAgain on 3/3/2018.
 */

public class Activity_Owner extends Activity {

    Button manageMenuButton,currentOrdersButton,transHistButton,settingsButton,logOutButton;
    TextView stallNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        stallNameTextView = findViewById(R.id.stall_Name);
        manageMenuButton = findViewById(R.id.manage_menu);
        currentOrdersButton = findViewById(R.id.current_orders);
        transHistButton = findViewById(R.id.transaction_history);
        settingsButton = findViewById(R.id.settings);
        logOutButton = findViewById(R.id.logout);

        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(v.getContext(),Activity_Main.class);
                startActivity(backIntent);
            }
        });

        manageMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(v.getContext(),Activity_Manage_Menu.class);
                goIntent.putExtra(Activity_Main.STALL_NAME,temp);
                startActivity(goIntent);
            }
        });

        currentOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(v.getContext(),Activity_Current_Orders.class);
                goIntent.putExtra(Activity_Main.STALL_NAME,temp);
                startActivity(goIntent);
            }
        });

        transHistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(v.getContext(),Activity_Transaction_History.class);
                goIntent.putExtra(Activity_Main.STALL_NAME,temp);
                startActivity(goIntent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(v.getContext(),Activity_Owner_Settings.class);
                goIntent.putExtra(Activity_Main.STALL_NAME,temp);
                startActivity(goIntent);
            }
        });
    }
}
