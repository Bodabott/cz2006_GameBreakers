package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.gamebreakers.login.Activity_Main;
import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_UserMenu extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
    }
    public void goStallOrder(View view) {
        Intent stallOrderIntent = new Intent(view.getContext(),Activity_Stall.class);
        startActivity(stallOrderIntent);
    }

    public void gobackMain(View view) {
        Intent backIntent = new Intent(view.getContext(),Activity_Main.class);
        startActivity(backIntent);
    }
}
