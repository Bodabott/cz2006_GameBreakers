package com.example.gamebreakers;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.gamebreakers.login.Activity_Main;

public class user_ui extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui);
    }

    public void clickBrowse(View v)
    {
        FragmentManager fragman = getSupportFragmentManager();
        //fragman.beginTransaction().add(new )
    }
}
