package com.example.gamebreakers.user;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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

public class Activity_User_History extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_User_Transaction_History mAdapterTransHist;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user_transactions);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.tran_hist_stallName);
        mListView = findViewById(R.id.tran_hist_ListView);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);

        strings = myDb.getUserArrayOfHistory(usernameMessage);
        mAdapterTransHist = new Adapter_User_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterTransHist);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) { listItemClickListener(parent,view,position,id); }});

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void listItemClickListener(AdapterView<?> parent, View view, final int position, long id){
        Intent intent = getIntent();
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_User_History.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear Data?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = myDb.deleteUserHistoryArrayData(strings[position],usernameMessage);
                if(deletedRows > 0)
                    Toast.makeText(Activity_User_History.this,"Data Deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Activity_User_History.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                // Resets the ListView
                strings = myDb.getUserArrayOfHistory(usernameMessage);
                mAdapterTransHist = new Adapter_User_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

                if(mListView != null){
                    mListView.setAdapter(mAdapterTransHist);
                }
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.show();
    }

    public void clearAllItems(View v){
        Intent intent = getIntent();
        final String usernameMessage = intent.getStringExtra(Activity_Main.USER_NAME);
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_User_History.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear All Data?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = myDb.deleteAllUserHistoryData(usernameMessage);
                if(deletedRows > 0)
                    Toast.makeText(Activity_User_History.this,"All Data Deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Activity_User_History.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                // Resets the ListView
                strings = myDb.getUserArrayOfHistory(usernameMessage);
                mAdapterTransHist = new Adapter_User_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

                if(mListView != null){
                    mListView.setAdapter(mAdapterTransHist);
                }
            }
        });
        mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.show();
    }
}
