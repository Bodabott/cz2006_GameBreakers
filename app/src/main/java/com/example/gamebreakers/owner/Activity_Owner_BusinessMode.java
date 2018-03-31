package com.example.gamebreakers.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;


/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_BusinessMode extends AppCompatActivity implements Fragment_Owner_BusinessMode.OnOrderSelectedListener {

    String stallName;
    DatabaseHelper myDb;

    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_orders);
        //set fragment
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_BusinessMode())
                .commit();

        //initialisation
        myDb = new DatabaseHelper(this);
        //get args from intent
        Intent intent = getIntent();
        stallName = intent.getStringExtra(Activity_Main.STALL_NAME);

        //set stall name as title
        TextView stallnameTextView= findViewById(R.id.current_orders_stallName);
        stallnameTextView.setText(stallName);
    }

    //====================List Adaptor Methods=====================

    @Override
    public void onOrderSelected(String order) {
        String guide = "Swipe right to finish Orders \n" +
                "Swipe left to cancel Orders";
        Toast.makeText(this,guide,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void finishOrder(String order){
        myDb.addHistoryArrayData(order,myDb.getBuyerUsername(order,stallName),stallName);
        myDb.addUserHistoryArrayData(order,myDb.getBuyerUsername(order,stallName),stallName);
        Integer deletedRows = myDb.deleteOrderArrayData(order,stallName);
        if(deletedRows > 0){
            Toast.makeText(Activity_Owner_BusinessMode.this,"Order Completed",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(Activity_Owner_BusinessMode.this,"Order not Completed",Toast.LENGTH_LONG).show();

        // Resets the ListView
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_BusinessMode())
                .commit();
    }

    @Override
    public void cancelOrder(final String order){
        Log.v("PLACE",order);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_BusinessMode.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Canceling Order. \nAre you sure? ").setItems(R.array.Array_cancelOrder, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    // Cancel Order
                    Integer deletedRows = myDb.deleteOrderArrayData(order,stallName);
                    if(deletedRows > 0)
                        Toast.makeText(Activity_Owner_BusinessMode.this,"Order Canceled",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Activity_Owner_BusinessMode.this,"Order not Canceled",Toast.LENGTH_LONG).show();
                    // Resets the ListView
                    fragman.beginTransaction()
                            .replace(R.id.content_main, new Fragment_Owner_BusinessMode())
                            .commit();
                }
            }
        });
        mBuilder.show();
    }

    //================COMPLEX ON CLICK METHODS======================

    //================SIMPLE ON CLICK METHODS======================
    public void exit(View v) {
        finish();
    }
}
