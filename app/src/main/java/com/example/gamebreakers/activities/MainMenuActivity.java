package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 28/2/2018.
 */

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            String detailValue = extras.getString("KeyForSending");
            if(detailValue != null){
                Toast.makeText(this,detailValue,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goStallOrder(View view) {
        Intent stallOrderIntent = new Intent(view.getContext(),StallOrderActivity.class);
        startActivity(stallOrderIntent);
    }

    public void gobackMain(View view) {
        Intent backIntent = new Intent(view.getContext(),Main.class);
        startActivity(backIntent);
    }
}
