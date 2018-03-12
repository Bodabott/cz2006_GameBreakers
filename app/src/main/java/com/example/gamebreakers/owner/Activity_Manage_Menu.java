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

import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.R;
import com.example.gamebreakers.login.Activity_Main;


/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Manage_Menu extends Activity {

    DatabaseHelper myDb;
    TextView mTextView;
    ListView mListView;
    Adapter_Manage_Menu mAdapterManageMenu;
    Button backButton,addButton;
    String[] strings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu);
        myDb = new DatabaseHelper(this);

        mTextView = findViewById(R.id.stall_name_holder);
        mListView = findViewById(R.id.manage_menu_category_list_view);
        backButton = findViewById(R.id.back);
        addButton = findViewById(R.id.add_item_icon);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            mTextView.setText(stallNameMessage);
        }

        strings = myDb.getArrayOfFood(stallNameMessage);
        mAdapterManageMenu = new Adapter_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

        if(mListView != null){
            mListView.setAdapter(mAdapterManageMenu);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Log.v("PLACE",strings[position]);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Manage_Menu.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Choose an Action: ")
                        .setItems(R.array.Array_manageMenu, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which == 0){
                                    // Code Here not Completed
                                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Manage_Menu.this);
                                    mBuilder.setCancelable(true);
                                    mBuilder.setTitle("HA HA!!");
                                    mBuilder.setMessage("It's not done yet ^_^.\nThank you for your patience!");
                                    mBuilder.show();
                                }else{
                                    Integer deletedRows = myDb.deleteArrayData(strings[position],stallNameMessage);
                                    if(deletedRows > 0)
                                        Toast.makeText(Activity_Manage_Menu.this,"Data Deleted",Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(Activity_Manage_Menu.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                                    // Resets the ListView
                                    strings = myDb.getArrayOfFood(stallNameMessage);
                                    mAdapterManageMenu = new Adapter_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

                                    if(mListView != null){
                                        mListView.setAdapter(mAdapterManageMenu);
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
                Intent backIntent = new Intent(v.getContext(),Activity_Owner.class);
                backIntent.putExtra(Activity_Main.STALL_NAME,stallNameMessage);
                startActivity(backIntent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Manage_Menu.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Set Food Name: ");

                // Set up the input
                final EditText input = new EditText(v.getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                mBuilder.setView(input);

                // Set up the buttons
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        myDb.addArrayData(m_Text,stallNameMessage);
                        // Resets the ListView
                        strings = myDb.getArrayOfFood(stallNameMessage);
                        mAdapterManageMenu = new Adapter_Manage_Menu(getApplicationContext(),R.layout.row_manage_menu,strings);

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
        });
    }
}
