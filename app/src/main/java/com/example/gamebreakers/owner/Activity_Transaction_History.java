package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.R;
import com.example.gamebreakers.login.Activity_Main;

/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Transaction_History extends Activity {
    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_Transaction_History mAdapterTransHist;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_hist);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.tran_hist_stallName);
        mListView = findViewById(R.id.tran_hist_ListView);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        strings = myDb.getArrayOfFood(stallNameMessage);
        mAdapterTransHist = new Adapter_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterTransHist);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Code Here not Completed
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Transaction_History.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("HA HA!!");
                mBuilder.setMessage("It's not done yet ^_^.\nThank you for your patience!");
                mBuilder.show();
            }
        });
        
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
