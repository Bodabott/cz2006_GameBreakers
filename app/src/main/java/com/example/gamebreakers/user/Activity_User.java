package com.example.gamebreakers.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.entities.Order;
import com.example.gamebreakers.entities.Stall;
import com.example.gamebreakers.entities.User;
import com.example.gamebreakers.login.Activity_Main;

import java.time.LocalDateTime;

import static com.example.gamebreakers.login.Activity_Main.PASSWORD;
import static com.example.gamebreakers.login.Activity_Main.USER_NAME;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User extends AppCompatActivity
        implements Fragment_User_BrowseStall.OnStallNameSelectedListener, Fragment_User_BrowseFood.OnFoodSelectedListener
        ,Fragment_User_CurrentOrders.OnOrderSelectedListener, Fragment_User_Transactions.OnTransactionSelectedListener {

    DrawerLayout mDrawerLayout;
    String food;
    Stall stall;
    User user;
    DatabaseHelper myDb;
    Dialog myDialog;
    Value val = new Value();
    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        myDb = new DatabaseHelper(this);

        myDialog = new Dialog(this);

        //set initial fragment (Main Menu)
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
                .commit();
        //get arguments
        String username = getIntent().getStringExtra(USER_NAME);
        String password = getIntent().getStringExtra(PASSWORD);
        //get user
        Cursor user_res = myDb.checkUserLoginData(username, password);
        user_res.moveToNext();
        user = new User(user_res.getInt(0),user_res.getString(1),user_res.getInt(3));


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
                int totalbal = myDb.getUserBalance(un) + bal;
                myDb.updateUserBalance(un, totalbal);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        String val_ue;
        int val;
        String un = user.getName();
        val = myDb.getUserBalance(un);
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
        val = myDb.getUserBalance(un);
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
        if(requestCode == 3)
            if(resultCode == Activity.RESULT_OK)
                finish();
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
        Intent intent = getIntent();
        final String username_message = intent.getStringExtra(USER_NAME);
        final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);
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
            goIntent.putExtra(USER_NAME,username_message);
            goIntent.putExtra(Activity_Main.PASSWORD,password_message);
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
        Order[] orders_array = myDb.getArrayOfOrders(stall_name);
        String[] strings_owner_history = myDb.getArrayOfHistory(stall_name);
        String[] strings_user_history = myDb.getUserArrayOfHistory(user_name);

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
    public void onOrderSelected(String item) {

    }

    @Override
    public void onTransactionSelecteed(String item) {

    }

    //================COMPLEX ON CLICK METHODS======================
    public void makePayment(View v){
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String stallMessage = stall.getStallName();
        final String foodMessage = food;
        final String usernameMessage = user.getName();
        //get time
        String hour =((Spinner) v.getRootView().findViewById(R.id.hourInput)).getSelectedItem().toString();
        String min =((Spinner) v.getRootView().findViewById(R.id.minInput)).getSelectedItem().toString();
        String time= LocalDateTime.now().toString().substring(0,11) + hour + ":" + min ;

        String newFoodMessage = foodNameConverter(foodMessage,stallMessage,usernameMessage);
        if(myDb.addOrderArrayData(newFoodMessage,usernameMessage,stallMessage, time) && afterEarliestOrderTime(time)){
            int foodprice = myDb.getFoodPrice(foodMessage, stallMessage);
            int bal_left = myDb.getUserBalance(usernameMessage) - foodprice;
            myDb.updateUserBalance(usernameMessage, bal_left);
            invalidateOptionsMenu();
            Toast.makeText(getApplicationContext(),"PAYMENT SUCCESSFUL",Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK);

            fragman.popBackStack();
            fragman.beginTransaction()
                    .replace(R.id.content_main, new Fragment_User_MainMenu())
                    .commit();
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
                Integer deletedRows = myDb.deleteAllUserHistoryData(usernameMessage);
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
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_Payment())
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

    public void backtoMain(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
                .commit();
    }

    public void back(View v) {
        this.onBackPressed();
    }

    public void logout(View v) {
        finish();
    }
}
