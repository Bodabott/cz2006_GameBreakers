package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gamebreakers.R;
import com.example.gamebreakers.login.Activity_Main;

/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_Settings extends Activity {
    Button backButton;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_settings);

        textView = findViewById(R.id.owner_settings_stallName);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null)
            textView.setText(stallNameMessage);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(v.getContext(),Activity_Owner.class);
                backIntent.putExtra(Activity_Main.STALL_NAME,stallNameMessage);
                startActivity(backIntent);
            }
        });
    }
}
