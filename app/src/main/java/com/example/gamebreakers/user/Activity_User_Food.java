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

public class Activity_User_Food extends Activity {

    public static final String FOOD_NAME = "Activity_User_Food";

    // Initialise
    DatabaseHelper myDb;
    ListView mListView;
    Adapter_User_Food mAdapterFood;
    TextView mStallTextView,mFoodTextView;
    Button nextButton;
    String[] strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food);
        myDb = new DatabaseHelper(this);

        // Connect to respective items in layout
        mListView = findViewById(R.id.myFoodListView);
        mStallTextView = findViewById(R.id.stall_status);
        mFoodTextView = findViewById(R.id.food_status);
        nextButton = findViewById(R.id.next);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String stallMessage = intent.getStringExtra(Activity_User_Stall.STALL_NAME);
        String foodMessage = intent.getStringExtra(FOOD_NAME);
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);

        if(stallMessage != null){
            mStallTextView.setText(stallMessage);
            if(foodMessage != null){
                mFoodTextView.setText(foodMessage);
            }
        }

        strings = myDb.getStallMenu(stallMessage.substring(7));

        mAdapterFood = new Adapter_User_Food(getApplicationContext(),R.layout.row_stall,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterFood);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {listItemClickListener(parent,view,position,id); }});

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v,usernameMessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

    public void listItemClickListener(AdapterView<?> parent, View view, int position, long id){
        Log.v("PLACE",strings[position]);
        String temp = "Food: " + strings[position];
        mFoodTextView.setText(temp);
    }

    public void backToUserPage(View v){
        setResult(Activity.RESULT_OK); // To close Activity_User_Stall
        finish();
    }

    public void backToStall(View v){
        Intent backIntent = new Intent(v.getContext(),Activity_User_Stall.class);
        TextView stallNameTextView = findViewById(R.id.stall_status);
        String stallNameMessage = stallNameTextView.getText().toString();
        backIntent.putExtra(Activity_User_Stall.STALL_NAME,stallNameMessage);
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void sendMessage(View view,String username) {
        Intent intent = new Intent(this, Activity_User_Payment.class);

        TextView stallNameTextView = findViewById(R.id.stall_status);
        TextView foodTextView = findViewById(R.id.food_status);

        String stallNameMessage = stallNameTextView.getText().toString();
        String foodMessage = foodTextView.getText().toString();

        if(foodMessage.matches("Food: ")){
            Toast.makeText(view.getContext(),"Food not chosen",Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra(Activity_User_Stall.STALL_NAME, stallNameMessage);
        intent.putExtra(FOOD_NAME, foodMessage);
        intent.putExtra(Activity_Main.USER_NAME,username);

        startActivityForResult(intent,2);
    }
}
