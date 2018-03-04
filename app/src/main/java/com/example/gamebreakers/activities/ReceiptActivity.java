package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class ReceiptActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

    }

    public void goMainMenu(View view) {
        Intent loginIntent = new Intent(view.getContext(),MainMenuActivity.class);
        startActivity(loginIntent);
    }
}
