package com.example.gamebreakers.owner;

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
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_History extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_Owner_Transaction_History mAdapterTransHist;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_hist);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.tran_hist_stallName);
        mListView = findViewById(R.id.tran_hist_ListView);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        strings = myDb.getArrayOfHistory(stallNameMessage);
        mAdapterTransHist = new Adapter_Owner_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterTransHist);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_History.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Clear Data?");
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteHistoryArrayData(strings[position],stallNameMessage);
                        if(deletedRows > 0)
                            Toast.makeText(Activity_Owner_History.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Activity_Owner_History.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                        // Resets the ListView
                        strings = myDb.getArrayOfHistory(stallNameMessage);
                        mAdapterTransHist = new Adapter_Owner_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

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
        });
        
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void clearAllItems(View v){
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_History.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear All Data?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = myDb.deleteAllHistoryData(stallNameMessage);
                if(deletedRows > 0)
                    Toast.makeText(Activity_Owner_History.this,"All Data Deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Activity_Owner_History.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                // Resets the ListView
                strings = myDb.getArrayOfHistory(stallNameMessage);
                mAdapterTransHist = new Adapter_Owner_Transaction_History(getApplicationContext(),R.layout.row_transaction_history,strings);

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
