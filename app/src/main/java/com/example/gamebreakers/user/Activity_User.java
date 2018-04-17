package com.example.gamebreakers.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Order;
import com.example.gamebreakers.entities.SQL;
import com.example.gamebreakers.entities.Stall;
import com.example.gamebreakers.entities.User;
import com.example.gamebreakers.login.Activity_Main;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.gamebreakers.login.Activity_Main.PASSWORD;
import static com.example.gamebreakers.login.Activity_Main.USER_NAME;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User extends AppCompatActivity
        implements Fragment_User_BrowseStall.OnStallNameSelectedListener, Fragment_User_BrowseFood.OnFoodSelectedListener
        ,Fragment_User_CurrentOrders.OnOrderSelectedListener, Fragment_User_Transactions.OnTransactionSelectedListener {

    DrawerLayout mDrawerLayout;
    String food, userName, passWord;
    Stall stall;
    User user;
    Dialog myDialog;
    Value val = new Value();
    android.support.v4.app.FragmentManager fragman = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        myDialog = new Dialog(this);

        //set initial fragment (Main Menu)
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
                .commit();
        //get arguments
        userName = getIntent().getStringExtra(USER_NAME);
        passWord = getIntent().getStringExtra(PASSWORD);
        //get user
        ArrayList user_res = SQL.checkUserLoginData(userName, passWord);
        System.out.println(user_res);
        HashMap row = (HashMap) user_res.get(0);
        user = new User(Integer.parseInt(row.get("U_ID").toString()), (String) row.get("U_USERNAME"), Integer.parseInt(row.get("U_BALANCE").toString()));


        //drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        ImageButton imageButton = headerLayout.findViewById(R.id.nav_back_arrow);

        TextView userNavTextView = headerLayout.findViewById(R.id.nav_header_username);
        userNavTextView.setText(userName);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return navigationItemListener(item);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String val_ue;
        int val;
        String un = user.getName();
        val = SQL.getUserBalance(un);
        val /= 100;
        val_ue = "$" + Integer.toString(val);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        MenuItem item = menu.findItem(R.id.value);
        Button itemButt = (Button) item.getActionView();
        if(itemButt != null) {
            itemButt.setText(val_ue);
            itemButt.setTextColor(Color.WHITE);
            itemButt.setBackgroundColor(Color.TRANSPARENT);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        String val_ue;
        int val;
        String un = user.getName();
        val = SQL.getUserBalance(un);
        val /= 100;
        val_ue = "$" + Integer.toString(val);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        MenuItem item = menu.findItem(R.id.value);
        Button itemButt = (Button) item.getActionView();
        if(itemButt != null) {
            itemButt.setText(val_ue);
            itemButt.setTextColor(Color.WHITE);
            itemButt.setBackgroundColor(Color.TRANSPARENT);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3) {
            if(resultCode == Activity.RESULT_OK)
                finish();
        }else if(requestCode == 5){
            if(resultCode == Activity.RESULT_OK){
                Stall[] stalls = SQL.getArrayOfStall();
                if(stalls != null) {
                    for (Stall tempStall : stalls) {
                        if (tempStall.getStallName().matches(data.getStringExtra("MAP"))) {
                            this.stall = tempStall;
                            break;
                        }
                    }
                }
                fragman.beginTransaction()
                        .replace(R.id.content_main,new Fragment_User_BrowseStall())
                        .addToBackStack(null)
                        .commit();
                fragman.beginTransaction()
                        .replace(R.id.content_main,new Fragment_User_BrowseFood())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean navigationItemListener(MenuItem item){
        // Clears all fragments from the stack
        for(int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
            getSupportFragmentManager().popBackStack();
        }
        // set item as selected to persist highlight
        item.setChecked(true);
        if(item.toString().matches("Current Orders")) {
            // Go to User Current Orders. Item is recorded right after payment.
            fragman.beginTransaction()
                    .replace(R.id.content_main, new Fragment_User_CurrentOrders())
                    .addToBackStack(null)
                    .commit();
        }else if(item.toString().matches("Top Up")) {
            // Go to Top Up Settings.
            fragman.beginTransaction()
                    .replace(R.id.content_main, new Fragment_User_Manage_Payment_Details())
                    .addToBackStack(null)
                    .commit();
        }else if(item.toString().matches("History")){
            // Go to User Transaction History. Item is recorded only if Order is Successfully completed by Owner of that stall
            fragman.beginTransaction()
                    .replace(R.id.content_main, new Fragment_User_Transactions())
                    .addToBackStack(null)
                    .commit();
        }else if(item.toString().matches("Settings")){
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_Settings.class);
            goIntent.putExtra(USER_NAME,userName);
            goIntent.putExtra(Activity_Main.PASSWORD,passWord);
            startActivityForResult(goIntent,3);
        }else if(item.toString().matches("Log out")){
            finish();
        }
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
        return true;
    }
    //====================Custom Methods=====================
    public String foodNameConverter(String old_foodName, String stall_name, String user_name){
        // Initialise
        int ID = 0;
        int Max_ID = 1;
        Order[] orders_array = SQL.getArrayOfOrders(stall_name);
        String[] strings_owner_history = SQL.getArrayOfHistory(stall_name);
        String[] strings_user_history = SQL.getUserArrayOfHistory(user_name);

        // Initial Check, increment ID to 1
        for(Order element : orders_array)
            if(old_foodName.matches(element.getFoodName()))
                ID++;
        for(String element : strings_owner_history)
            if(old_foodName.matches(element))
                ID++;
        for(String element : strings_user_history)
            if(old_foodName.matches(element))
                ID++;

        // Check if there are duplicates in Order List
        for(Order element : orders_array)
            for(int j=0;j<element.getFoodName().length();j++)
                if(element.getFoodName().charAt(j) == '-')
                    if(old_foodName.matches(element.getFoodName().substring(0,j)))
                        if((ID = Integer.valueOf(element.getFoodName().substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in Owner History List
        for(String element : strings_owner_history)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        // Check if there are duplicates in User History List
        for(String element : strings_user_history)
            for(int j=0;j<element.length();j++)
                if(element.charAt(j) == '-')
                    if(old_foodName.matches(element.substring(0,j)))
                        if((ID = Integer.valueOf(element.substring(j+1)) + 1) > Max_ID)
                            Max_ID = ID;

        if(ID != 0) { // If item already exists in Order/History List
            return  old_foodName + "-" + String.valueOf(Max_ID);
        }else{
            return old_foodName;
        }
    }

    public boolean afterEarliestOrderTime(String time) {
        LocalDateTime collectTime = LocalDateTime.parse(time);
        return collectTime.isAfter(getEarliestOrderTime());
    }

    public LocalDateTime getEarliestOrderTime() {
        LocalDateTime local = LocalDateTime.now();
        local.plusMinutes(stall.getQueueNum()*2);   //add 2 minutes per person in queue
        return local;
    }

    //====================List Adaptor Methods=====================
    @Override
    public void onFoodSelected(String food){
        this.food=food;

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_Payment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStallNameSelected(Stall stall) {
        this.stall=stall;

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_BrowseFood())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onOrderSelected(Order order) {
        if (order.isCompleted()) {

            SQL.addHistoryArrayData(order.getFoodName(), SQL.getBuyerUsername(order.getFoodName(), order.getStallName()), order.getStallName());
            SQL.addUserHistoryArrayData(order.getFoodName(), SQL.getBuyerUsername(order.getFoodName(), order.getStallName()), order.getStallName());
            Integer deletedRows = SQL.deleteOrderArrayData(order.getFoodName(), order.getStallName());

            if (deletedRows > 0) {
                Toast.makeText(Activity_User.this, "Order Collected", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(Activity_User.this, "Order not Collected", Toast.LENGTH_LONG).show();

            // Resets the ListView
            fragman.beginTransaction()
                    .replace(R.id.content_main, new Fragment_User_CurrentOrders())
                    .commit();
        }
        else Toast.makeText(Activity_User.this, "Order not Collected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionSelecteed(String item) {}

    //================COMPLEX ON CLICK METHODS======================

    public void ShowTopupPopup(View v) {
        Button buttonCancel;
        Button buttonConfirm;
        final EditText mEdit;

        myDialog.setContentView(R.layout.topup_popup);

        mEdit = (EditText) myDialog.findViewById(R.id.top_up_value);
        buttonCancel = (Button) myDialog.findViewById(R.id.cancel_button);
        buttonConfirm = (Button) myDialog.findViewById(R.id.confirm_button);

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String un = user.getName();
                String txt = mEdit.getText().toString();
                int bal = Integer.parseInt(txt);
                bal *= 100;
                int totalbal = SQL.getUserBalance(un) + bal;
                SQL.updateUserBalance(un, totalbal);
                invalidateOptionsMenu();
                myDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void updatePaymentDetails(View v){
        Toast.makeText(v.getContext(), "you are inside payment", Toast.LENGTH_SHORT).show();
    }

    public void makePayment(View v) {
        final String stallMessage = stall.getStallName();
        final String foodMessage = food;
        final String usernameMessage = user.getName();
        //get time
        String hour = ((Spinner) v.getRootView().findViewById(R.id.hourInput)).getSelectedItem().toString();
        String min = ((Spinner) v.getRootView().findViewById(R.id.minInput)).getSelectedItem().toString();
        String time = LocalDateTime.now().toString().substring(0, 11) + hour + ":" + min;

        String newFoodMessage = foodNameConverter(foodMessage, stallMessage, usernameMessage);
        if(afterEarliestOrderTime(time)) {
            if (SQL.addOrderArrayData(newFoodMessage, usernameMessage, stallMessage, time)) {
                int foodprice = SQL.getFoodPrice(foodMessage, stallMessage);
                int bal_left = SQL.getUserBalance(usernameMessage) - foodprice;
                SQL.updateUserBalance(usernameMessage, bal_left);
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "PAYMENT SUCCESSFUL", Toast.LENGTH_LONG).show();
                setResult(Activity.RESULT_OK);

                // Reset fragments
                for(int i=0;i<fragman.getBackStackEntryCount();++i)
                    fragman.popBackStack();
            }
        }
        else Toast.makeText(getApplicationContext(),"PAYMENT NOT SUCCESSFUL",Toast.LENGTH_LONG).show();
    }

    public void clearAllTransactions(View v){
        Intent intent = getIntent();
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear All Data?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = SQL.deleteAllUserHistoryData(usernameMessage);
                if(deletedRows > 0)
                    Toast.makeText(Activity_User.this,"All Data Deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Activity_User.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                // Resets the Fragment
                fragman.popBackStack();
                fragman.beginTransaction()
                        .replace(R.id.content_main, new Fragment_User_Transactions())
                        .addToBackStack(null)
                        .commit();
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.show();
    }

    public void searchStall(View v){
        Intent goIntent = new Intent(v.getContext(),MapsActivity.class);
        startActivityForResult(goIntent,5);
    }

    //================SIMPLE ON CLICK METHODS======================
    public void browseStall(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_BrowseStall())
                .addToBackStack(null)
                .commit();
    }

    public void browseFood(View v){
        if (stall==null) {
            Toast.makeText(v.getContext(),"Please choose a stall",Toast.LENGTH_LONG).show();
            return;
        }

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_BrowseFood())
                .addToBackStack(null)
                .commit();
    }
    public void goPayment(View v){
        if(food==null){
            Toast.makeText(v.getContext(),"Please choose a food",Toast.LENGTH_LONG).show();
            return;
        }

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_Payment())
                .addToBackStack(null)
                .commit();
    }

    public void checkOrders(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_CurrentOrders())
                .addToBackStack(null)
                .commit();
    }

    public void checkTransactions(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_Transactions())
                .addToBackStack(null)
                .commit();
    }

    public void backToMain(View v){
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    public void back(View v) {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.e("Fragment Count",String.valueOf(fragmentManager.getBackStackEntryCount()));
        if(fragmentManager.getBackStackEntryCount() == 0){
            super.onBackPressed();
        }else{
            fragmentManager.popBackStack();
        }
    }

    public void logout(View v) {
        finish();
    }
}
