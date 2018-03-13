package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;


/**
 * Created by zNotAgain on 1/3/2018.
 */

public class Activity_User_Stall extends Activity {

    public static final String STALL_NAME = "Activity_User_Stall";

    DatabaseHelper myDb;
    TextView mStallTextView;
    Button exitButton,nextButton;
    ListView mListView;
    Adapter_User_Stall mAdapterStall;
    String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stall);
        myDb = new DatabaseHelper(this);
        strings = myDb.getArrayOfStall();

        mStallTextView = findViewById(R.id.stall_status);
        mListView = findViewById(R.id.myStallListView);
        exitButton = findViewById(R.id.exit);
        nextButton = findViewById(R.id.next);

        Intent intent = getIntent();
        final String stallMessage = intent.getStringExtra(STALL_NAME);
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);

        if(stallMessage != null){
            mStallTextView.setText(stallMessage);
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v,usernameMessage);
            }
        });

        mAdapterStall = new Adapter_User_Stall(getApplicationContext(),R.layout.row_stall,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterStall);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { listItemClickListener(parent,view,position,id); }});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
           if(resultCode == Activity.RESULT_OK)
               finish();
    }

    public void listItemClickListener(AdapterView<?> parent, View view, int position, long id){
        Log.v("PLACE",strings[position]);
        String temp = "Stall: " + strings[position];
        mStallTextView.setText(temp);
    }

    public void sendMessage(View view,String username) {
        Intent intent = new Intent(this, Activity_User_Food.class);
        TextView textView = findViewById(R.id.stall_status);
        String message = textView.getText().toString();
        if(message.matches("Stall: ")){
            Toast.makeText(view.getContext(),"Stall not chosen",Toast.LENGTH_LONG).show();
            return;
        }
        intent.putExtra(STALL_NAME, message);
        intent.putExtra(Activity_Main.USER_NAME,username);
        startActivityForResult(intent,1);
    }
}

