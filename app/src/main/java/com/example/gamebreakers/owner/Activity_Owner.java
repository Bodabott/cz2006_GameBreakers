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

    Button logOutButton;
    TextView stallNameTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        stallNameTextView = findViewById(R.id.stall_Name);

        logOutButton = findViewById(R.id.logout);

        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4)
            if(resultCode == Activity.RESULT_OK)
                finish();
    }

    public void manageMenu(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);
        Intent goIntent = new Intent(v.getContext(),Activity_Owner_Manage_Menu.class);
        goIntent.putExtra(Activity_Main.STALL_NAME,temp);
        startActivity(goIntent);
    }

    public void currentOrders(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);
        Intent goIntent = new Intent(v.getContext(),Activity_Owner_Orders.class);
        goIntent.putExtra(Activity_Main.STALL_NAME,temp);
        startActivity(goIntent);
    }

    public void transactionHistory(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);
        Intent goIntent = new Intent(v.getContext(),Activity_Owner_History.class);
        goIntent.putExtra(Activity_Main.STALL_NAME,temp);
        startActivity(goIntent);
    }

    public void owner_settings(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(temp != null)
            stallNameTextView.setText(temp);
        Intent goIntent = new Intent(v.getContext(),Activity_Owner_Settings.class);
        goIntent.putExtra(Activity_Main.STALL_NAME,temp);
        startActivityForResult(goIntent,4);
    }

}
