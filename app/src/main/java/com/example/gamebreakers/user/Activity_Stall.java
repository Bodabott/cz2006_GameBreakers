package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.Stall;
import com.example.gamebreakers.entities.Stall_Details;

/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_Stall extends Activity {

    public static final String STALL_NAME = "Activity_Stall";

    Stall[] mStallArray = Stall_Details.mStallArray;

    TextView mStallTextView;
    ListView mListView;
    Adapter_Stall mAdapterStall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stall);

        mStallTextView = findViewById(R.id.stall_status);
        mListView = findViewById(R.id.myStallListView);

        Intent intent = getIntent();
        final String stallMessage = intent.getStringExtra(STALL_NAME);

        if(stallMessage != null){
            mStallTextView.setText(stallMessage);
        }

        mAdapterStall = new Adapter_Stall(getApplicationContext(),R.layout.row_stall,mStallArray);

        if(mListView != null){
            mListView.setAdapter(mAdapterStall);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("PLACE",mStallArray[position].getStallName());
                String temp = "Stall: " + mStallArray[position].getStallName();
                mStallTextView.setText(temp);
            }
        });
    }

    public void chooseCategory(View view) {
        Intent intent = new Intent(this, Activity_Category.class);
        TextView textView = findViewById(R.id.stall_status);
        String message = textView.getText().toString();
        if(message.matches("Stall: ")){
            Toast.makeText(view.getContext(),"Stall not chosen",Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra(STALL_NAME, message);
        startActivity(intent);
    }

    public void backtoUserMenu(View v) {
        Intent backIntent = new Intent(v.getContext(),Activity_UserMenu.class);
        startActivity(backIntent);
    }
}

