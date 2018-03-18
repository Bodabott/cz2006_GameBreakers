package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;

import static com.example.gamebreakers.login.Activity_Main.STALL_NAME;


/**
 * Created by zNotAgain on 3/3/2018.
 */

public class Activity_Owner extends AppCompatActivity
        implements Fragment_Owner_ManageMenu.OnMenuItemSelectedListener,Fragment_Owner_Transactions.OnTransactionSelectedListener {

    String stallName;
    DatabaseHelper myDb;

    android.support.v4.app.FragmentManager fragman= getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        myDb = new DatabaseHelper(this);

        //set initial fragment (Main Menu)
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_MainMenu())
                .commit();
        //get arguments
        stallName = getIntent().getStringExtra(STALL_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4)
            if(resultCode == Activity.RESULT_OK)
                finish();
    }

    //====================List Adaptor Methods=====================
    @Override
    public void onMenuItemSelected(String item) {

    }

    @Override
    public void onTransactionSelected(String item) {

    }
    //================COMPLEX ON CLICK METHODS======================
    public void addMenuItem(final View v){
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set Food Name: ");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if(myDb.addMenuArrayData(m_Text,stallNameMessage))
                    Toast.makeText(Activity_Owner.this,"Data Added",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Activity_Owner.this,"Data not Added",Toast.LENGTH_SHORT).show();
                // Resets the ListView
                fragman.popBackStack();
                fragman.beginTransaction()
                        .replace(R.id.content_main, new Fragment_Owner_ManageMenu())
                        .addToBackStack(null)
                        .commit();
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

    public void editUsername(final View v){
        if(stallName != null)
            Log.e("START",stallName);

        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set new Username:");
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
                if(myDb.updateOwnerAccountUsername(m_Text,stallName)){
                    Toast.makeText(Activity_Owner.this,"Edit Successful",Toast.LENGTH_SHORT).show();

                    fragman.popBackStack();
                    fragman.beginTransaction()
                            .replace(R.id.content_main, new Fragment_Owner_Settings())
                            .addToBackStack(null)
                            .commit();
                }
                else
                    Toast.makeText(Activity_Owner.this,"Edit Failed",Toast.LENGTH_SHORT).show();
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

    public void editPassword(final View v){
        if(stallName != null){
            Log.e("START",stallName);
        }
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set new Password:");
        // Set up the input
        final EditText input = new EditText(v.getContext());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString();
                if(myDb.updateOwnerAccountPassword(m_Text,stallName)){
                    Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();

                    fragman.popBackStack();
                    fragman.beginTransaction()
                            .replace(R.id.content_main, new Fragment_Owner_Settings())
                            .addToBackStack(null)
                            .commit();
                }
                else
                    Toast.makeText(v.getContext(),"Edit Failed",Toast.LENGTH_SHORT).show();
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

    public void editStallName(final View v){
        if(stallName != null){
            Log.e("START",stallName);
        }
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set new Stall Name:");
        // Set up the input
        final EditText input = new EditText(v.getContext());
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        mBuilder.setView(input);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.app.AlertDialog.Builder tempBuilder = new android.app.AlertDialog.Builder(v.getContext());
                tempBuilder.setCancelable(false);
                tempBuilder.setTitle("RESET REQUIRED:");
                tempBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if(myDb.updateOwnerAccountStallName(stallName,m_Text) > 0){
                            Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();

                            stallName=m_Text;

                            fragman.popBackStack();
                            fragman.beginTransaction()
                                    .replace(R.id.content_main, new Fragment_Owner_Settings())
                                    .addToBackStack(null)
                                    .commit();
                        }
                        else
                            Toast.makeText(v.getContext(),"Edit Failed",Toast.LENGTH_SHORT).show();
                    }
                });
                tempBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                tempBuilder.show();
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

    public void deleteAccount(final View v){
        if(stallName != null){
            Log.e("START",stallName);
        }
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Confirm?");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(myDb.deleteOwnerAccount(stallName) > 0){
                    Toast.makeText(v.getContext(),"Account Successfully Deleted",Toast.LENGTH_LONG).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }else
                    Toast.makeText(v.getContext(),"Account Delete Failed",Toast.LENGTH_LONG).show();
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

    public void clearAllItems(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear All Data?");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = myDb.deleteAllHistoryData(stallName);
                if(deletedRows > 0)
                    Toast.makeText(Activity_Owner.this,"All Data Deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(Activity_Owner.this,"Data not Deleted",Toast.LENGTH_LONG).show();

                // Resets the ListView
                fragman.popBackStack();
                fragman.beginTransaction()
                        .replace(R.id.content_main, new Fragment_Owner_Transactions())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


        //================SIMPLE ON CLICK METHODS======================
    public void currentOrders(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);

        Intent goIntent = new Intent(v.getContext(),Activity_Owner_Business_Mode.class);
        goIntent.putExtra(Activity_Main.STALL_NAME,temp);
        startActivity(goIntent);
    }

    public void manageMenu(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_ManageMenu())
                .addToBackStack(null)
                .commit();
    }

    public void transactionHistory(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_Transactions())
                .addToBackStack(null)
                .commit();
    }

    public void owner_settings(View v){
        fragman.beginTransaction()
                .replace(R.id.content_main, new Fragment_Owner_Settings())
                .addToBackStack(null)
                .commit();
    }


    public void back(View v) {
        this.onBackPressed();
    }

    public void logout(View v) {
        finish();
    }
}
