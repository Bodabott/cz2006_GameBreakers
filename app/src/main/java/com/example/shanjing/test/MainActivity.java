package com.example.shanjing.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.loginButt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openOwnerSettings();
            }
        });
    }

    public void openOwnerSettings() {
        Intent intent = new Intent(this, OwnerSettings.class);
        startActivity(intent);
    }
}
