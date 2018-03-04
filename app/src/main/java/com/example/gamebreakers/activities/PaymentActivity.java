package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.gamebreakers.R;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class PaymentActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    public void goReceiptActivity(View view) {
        Intent goIntent = new Intent(view.getContext(),ReceiptActivity.class);
        startActivity(goIntent);
    }

    public void goFoodActivity(View view) {

        Intent goIntent = new Intent(view.getContext(),FoodActivity.class);
        startActivity(goIntent);
    }
}
