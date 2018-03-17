package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;

import static com.example.gamebreakers.login.Activity_Main.STALL_NAME;
import static com.example.gamebreakers.login.Activity_Main.USER_NAME;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User extends AppCompatActivity implements Fragment_User_BrowseStall.OnStallNameSelectedListener, Fragment_User_BrowseFood.OnListFragmentInteractionListener{

    DrawerLayout mDrawerLayout;
    String username, stallName, food;
    DatabaseHelper myDb;

    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        myDb = new DatabaseHelper(this);

        //set initial fragment (Main Menu)
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
                .commit();
        //get arguments
        username = getIntent().getStringExtra(USER_NAME);
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
        if(item.toString().matches("Current Orders")){
            // Go to User Current Orders. Item is recorded right after payment.
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_Orders.class);
            goIntent.putExtra(USER_NAME,username_message);
            startActivityForResult(goIntent,1);
        }else if(item.toString().matches("History")){
            // Go to User Transaction History. Item is recorded only if Order is Successfully completed by Owner of that stall
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_History.class);
            goIntent.putExtra(USER_NAME,username_message);
            startActivityForResult(goIntent,2);
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
    //====================List Adaptor Methods=====================
    @Override
    public void onListFragmentInteraction(String food){
        this.food=food;
        TextView v = findViewById(R.id.selected_food);
        v.setText("Food: "+food);
    }

    @Override
    public void onStallNameSelected(String stallName) {
        this.stallName =  stallName;
        TextView v = findViewById(R.id.selected_stall);
        v.setText("Stall: "+stallName);
    }
    //=======================ON CLICK METHODS======================
    public void browseStall(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_BrowseStall())
                .addToBackStack(null)
                .commit();
    }

    public void browseFood(View v){
        if (stallName==null) {
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

    public void makePayment(View v){
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        final String stallMessage = stallName;
        final String foodMessage = food;
        final String usernameMessage = username;

        String newFoodMessage = foodNameConverter(foodMessage,stallMessage,usernameMessage);
        if(myDb.addOrderArrayData(newFoodMessage,usernameMessage,stallMessage.substring(7))){
            Toast.makeText(getApplicationContext(),"PAYMENT SUCCESSFUL",Toast.LENGTH_LONG).show();
            setResult(Activity.RESULT_OK);
        }
        else Toast.makeText(getApplicationContext(),"PAYMENT NOT SUCCESSFUL",Toast.LENGTH_LONG).show();

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
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
