package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_Orders extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_Owner_Current_Orders mAdapterCurrentOrders;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_orders);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.current_orders_stallName);
        mListView = findViewById(R.id.pendingOrdersListView);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        strings = myDb.getArrayOfOrders(stallNameMessage);
        mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getApplicationContext(),R.layout.row_current_orders,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterCurrentOrders);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Log.v("PLACE",strings[position]);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_Orders.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Choose an Action: ").setItems(R.array.Array_currentOrder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            // Finish Order
                            myDb.addHistoryArrayData(strings[position],myDb.getBuyerUsername(strings[position],stallNameMessage),stallNameMessage);
                            myDb.addUserHistoryArrayData(strings[position],myDb.getBuyerUsername(strings[position],stallNameMessage),stallNameMessage);
                            Integer deletedRows = myDb.deleteOrderArrayData(strings[position],stallNameMessage);
                            if(deletedRows > 0){
                                Toast.makeText(Activity_Owner_Orders.this,"Order Completed",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(Activity_Owner_Orders.this,"Order not Completed",Toast.LENGTH_LONG).show();

                            // Resets the ListView
                            strings = myDb.getArrayOfOrders(stallNameMessage);
                            mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getApplicationContext(),R.layout.row_current_orders,strings);

                            if(mListView != null){
                                mListView.setAdapter(mAdapterCurrentOrders);
                            }
                        }else{
                            // Cancel Order
                            Integer deletedRows = myDb.deleteOrderArrayData(strings[position],stallNameMessage);
                            if(deletedRows > 0)
                                Toast.makeText(Activity_Owner_Orders.this,"Order Canceled",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(Activity_Owner_Orders.this,"Order not Canceled",Toast.LENGTH_LONG).show();
                             // Resets the ListView
                            strings = myDb.getArrayOfOrders(stallNameMessage);
                            mAdapterCurrentOrders = new Adapter_Owner_Current_Orders(getApplicationContext(),R.layout.row_current_orders,strings);

                            if(mListView != null){
                                mListView.setAdapter(mAdapterCurrentOrders);
                            }
                        }
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
}
