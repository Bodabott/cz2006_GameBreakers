package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;


/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_Manage_Menu extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_Owner_Manage_Menu mAdapterManageMenu;
    Button backButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_manage_menu);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.stall_name_holder);
        mListView = findViewById(R.id.manage_menu_category_list_view);
        backButton = findViewById(R.id.back);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        strings = myDb.getStallMenu(stallNameMessage);
        mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterManageMenu);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Log.v("PLACE",strings[position]);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_Manage_Menu.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Remove item?");
                mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteMenuArrayData(strings[position],stallNameMessage);
                        if(deletedRows > 0)
                            Toast.makeText(Activity_Owner_Manage_Menu.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Activity_Owner_Manage_Menu.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                        // Resets the ListView
                        strings = myDb.getStallMenu(stallNameMessage);
                        mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

                        if(mListView != null){
                            mListView.setAdapter(mAdapterManageMenu);
                        }
                    }
                });
                mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

    public void addMenuItem(final View v){
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Owner_Manage_Menu.this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set Food Name: ");

        // Set up the input
        final EditText input = new EditText(v.getContext());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if(myDb.addMenuArrayData(m_Text,stallNameMessage))
                    Toast.makeText(v.getContext(),"Data Added",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(v.getContext(),"Data not Added",Toast.LENGTH_SHORT).show();
                // Resets the ListView
                strings = myDb.getStallMenu(stallNameMessage);
                mAdapterManageMenu = new Adapter_Owner_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

                if(mListView != null){
                    mListView.setAdapter(mAdapterManageMenu);
                }
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        mBuilder.show();
    }
}
