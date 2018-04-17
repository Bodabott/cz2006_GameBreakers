package com.example.gamebreakers.owner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Order;
import com.example.gamebreakers.entities.SQL;
import com.example.gamebreakers.login.Activity_Main;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_BusinessMode extends AppCompatActivity implements Fragment_Owner_BusinessMode.OnOrderSelectedListener {

    String stallName;
    List<Order> orders = new LinkedList<>();
    Handler mHandler;
    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_orders);

        //get args from intent
        Intent intent = getIntent();
        stallName = intent.getStringExtra(Activity_Main.STALL_NAME);

        Order[] ordersArray = SQL.getArrayOfOrders(this.stallName);
        orders.addAll(Arrays.asList(ordersArray));
        removeCompletedOrders();

        //set stall name as title
        TextView stallnameTextView= findViewById(R.id.current_orders_stallName);
        stallnameTextView.setText(stallName);

        //set fragment
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_BusinessMode())
                .commit();

        //set handler, for periodic refreshing
        this.mHandler = new Handler();

        //update queue Num
        updateQueueNum();
    }
    protected void onResume(@Nullable Bundle savedInstanceState) {
        this.mHandler.postDelayed(refreshArray,5000);
    }

    protected void onPause(@Nullable Bundle savedInstanceState) {
        mHandler.removeCallbacksAndMessages(null);
    }


    //====================List Adaptor Methods=====================

    @Override
    public void onOrderSelected(String order) {
        String guide = "Swipe right to finish Orders \n" +
                "Swipe left to cancel Orders";
        Toast.makeText(this,guide,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean finishOrder(Order order){
        order.complete();
        if(SQL.updateOrder(order.getFoodName(),order.getStallName())){
            Toast.makeText(Activity_Owner_BusinessMode.this,"Order Completed",Toast.LENGTH_LONG).show();
            removeCompletedOrders();
            // Resets the ListView
            Fragment currentFragment = fragman.findFragmentById(R.id.content_main);
            if (currentFragment instanceof Fragment_Owner_BusinessMode) {
                fragman.beginTransaction()
                        .detach(currentFragment)
                        .attach(currentFragment)
                        .commit();
            }
            updateQueueNum();
            return true;
        }
        else{
            Toast.makeText(Activity_Owner_BusinessMode.this,"Order not Completed",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public boolean cancelOrder(final String order){
        Integer deletedRows = SQL.deleteOrderArrayData(order,stallName);
        if(deletedRows > 0){
            Toast.makeText(Activity_Owner_BusinessMode.this,"Order Canceled",Toast.LENGTH_LONG).show();
            updateQueueNum();
            return true;
        }
        Toast.makeText(Activity_Owner_BusinessMode.this,"Order not Canceled",Toast.LENGTH_LONG).show();
        return false;
    }
    //====================Custom Methods=====================

    public void removeCompletedOrders() {

        for (Iterator<Order> iterator = orders.iterator(); iterator.hasNext();) {
            Order o = iterator.next();
            if (o.isCompleted()) iterator.remove();
        }
    }

    public void updateQueueNum() {
        int i=0;
        Calendar latest = Calendar.getInstance();   //get 30 minutes from now
        latest.add(Calendar.MINUTE, 30);

        for (Order o : orders) {
            Calendar collectTime = o.getCalendartime();

            if (collectTime.before(latest))i++;
        }
        SQL.updateQueueNum(i, stallName);
    }

    private final Runnable refreshArray = new Runnable()
    {
        public void run()

        {
            Order[] ordersArray = SQL.getArrayOfOrders(stallName);
            orders.addAll(Arrays.asList(ordersArray));
            removeCompletedOrders();

            Fragment currentFragment = fragman.findFragmentById(R.id.content_main);
            if (currentFragment instanceof Fragment_Owner_BusinessMode) {
                fragman.beginTransaction()
                        .detach(currentFragment)
                        .attach(currentFragment)
                        .commit();
            }

            mHandler.postDelayed(refreshArray, 5000);
        }

    };//runnable
    //================COMPLEX ON CLICK METHODS======================

    //================SIMPLE ON CLICK METHODS======================
    public void exit(View v) {
        finish();
    }
}
