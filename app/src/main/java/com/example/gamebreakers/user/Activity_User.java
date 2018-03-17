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

import com.example.gamebreakers.R;
import com.example.gamebreakers.login.Activity_Main;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User extends AppCompatActivity implements Fragment_User_BrowseStall.OnStallNameSelectedListener{

    DrawerLayout mDrawerLayout;
    String stallName, food;

    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_MainMenu())
                .commit();

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
        final String username_message = intent.getStringExtra(Activity_Main.USER_NAME);
        final String password_message = intent.getStringExtra(Activity_Main.PASSWORD);
        // set item as selected to persist highlight
        item.setChecked(true);
        if(item.toString().matches("Current Orders")){
            // Go to User Current Orders. Item is recorded right after payment.
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_Orders.class);
            goIntent.putExtra(Activity_Main.USER_NAME,username_message);
            startActivityForResult(goIntent,1);
        }else if(item.toString().matches("History")){
            // Go to User Transaction History. Item is recorded only if Order is Successfully completed by Owner of that stall
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_History.class);
            goIntent.putExtra(Activity_Main.USER_NAME,username_message);
            startActivityForResult(goIntent,2);
        }else if(item.toString().matches("Settings")){
            Intent goIntent = new Intent(getApplicationContext(),Activity_User_Settings.class);
            goIntent.putExtra(Activity_Main.USER_NAME,username_message);
            goIntent.putExtra(Activity_Main.PASSWORD,password_message);
            startActivityForResult(goIntent,3);
        }else if(item.toString().matches("Log out")){
            finish();
        }
        // close drawer when item is tapped
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onStallNameSelected(String stallName) {
        this.stallName =  stallName;

    }
    //=======================ON CLICK METHODS======================
    public void orderFood(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_User_BrowseStall())
                .addToBackStack(null)
                .commit();
    }

    public void logout(View v) {
        finish();
    }
}
