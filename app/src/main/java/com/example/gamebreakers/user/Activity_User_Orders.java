package com.example.gamebreakers.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by zNotAgain on 9/3/2018.
 */

public class Activity_User_Orders extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_User_Current_Orders mAdapterCurrentOrders;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.current_orders);
        mListView = findViewById(R.id.pendingOrdersListView);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
        strings = myDb.getUserArrayOfOrders(usernameMessage);

        mAdapterCurrentOrders = new Adapter_User_Current_Orders(getApplicationContext(),R.layout.row_current_orders,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterCurrentOrders);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) { Toast.makeText(view.getContext(),strings[position],Toast.LENGTH_LONG).show(); }});

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
