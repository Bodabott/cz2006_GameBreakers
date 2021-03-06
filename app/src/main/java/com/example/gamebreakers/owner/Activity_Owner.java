package com.example.gamebreakers.owner;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.gamebreakers.entities.SQL;
import com.example.gamebreakers.login.Activity_Main;

import java.io.IOException;
import java.util.List;

import static com.example.gamebreakers.login.Activity_Main.STALL_NAME;


/**
 * Created by zNotAgain on 3/3/2018.
 */

public class Activity_Owner extends AppCompatActivity
        implements Fragment_Owner_ManageMenu.OnMenuItemSelectedListener,Fragment_Owner_Transactions.OnTransactionSelectedListener {

    String stallName;

    android.support.v4.app.FragmentManager fragman = getSupportFragmentManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);


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

    public boolean isPostalCodeAcceptable(String postal_code){
        List<Address> addressList = null;
        String temp = "Singapore " + postal_code;

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try{
            addressList = geocoder.getFromLocationName(temp,1);
        }catch(IOException e){
            e.printStackTrace();
        }

        return addressList != null;
    }

    //====================List Adaptor Methods=====================
    @Override
    public void onMenuItemSelected(final String item) {
        Toast.makeText(this,item,Toast.LENGTH_SHORT).show();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Delete " + item + "?");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(SQL.deleteMenuArrayData(item,stallName) > 0)
                    Toast.makeText(Activity_Owner.this,"Data Deleted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Activity_Owner.this,"Data not Deleted",Toast.LENGTH_SHORT).show();
                    // Resets the ListView
                    fragman.popBackStack();
                    fragman.beginTransaction()
                            .replace(R.id.content_main, new Fragment_Owner_ManageMenu())
                            .addToBackStack(null)
                            .commit();
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
        final View addmenu = getLayoutInflater().inflate(R.layout.fragment_owner_addmenu,null);

        mBuilder.setView(addmenu);

        // Set up the buttons
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get inputs
                EditText stallname = addmenu.findViewById(R.id.addmenu_foodname);
                String m_Text=stallname.getText().toString();

                EditText dollars= addmenu.findViewById(R.id.addmenu_dollar_price);
                EditText cents= addmenu.findViewById(R.id.addmenu_cents_price);

                int price=0;
                if (!cents.getText().toString().equals("")) {
                    price += Integer.parseInt(cents.getText().toString());         //add cents
                }
                if (!dollars.getText().toString().equals("")) {
                    price += Integer.parseInt(dollars.getText().toString()) * 100; //add dollars
                }


                if(SQL.addMenuArrayData(m_Text,stallNameMessage, price))
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
                if(SQL.updateOwnerAccountUsername(m_Text,stallName)){
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
                if(SQL.updateOwnerAccountPassword(m_Text,stallName)){
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
                        if(SQL.updateOwnerAccountStallName(stallName,m_Text) > 0){
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

    public void editPostalCode(final View v){
        final String stallNameMessage = getIntent().getStringExtra(Activity_Main.STALL_NAME);
        android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Set new Postal Code:");
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

                        if(!isPostalCodeAcceptable(m_Text)) {
                            Toast.makeText(v.getContext(),"Invalid Postal Code",Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(SQL.updatePostalCode(stallNameMessage,Integer.parseInt(m_Text))){
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
                if(SQL.deleteOwnerAccount(stallName) > 0){
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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Clear All Data?");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer deletedRows = SQL.deleteAllHistoryData(stallName);
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
        mBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mBuilder.show();
    }


        //================SIMPLE ON CLICK METHODS======================
    public void currentOrders(View v){
        Intent intent = getIntent();
        final String temp = intent.getStringExtra(Activity_Main.STALL_NAME);
        Intent goIntent = new Intent(v.getContext(),Activity_Owner_BusinessMode.class);
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
