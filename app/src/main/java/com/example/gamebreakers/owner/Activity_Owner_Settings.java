package com.example.gamebreakers.owner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.login.Activity_Main;

/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_Owner_Settings extends Activity {

    DatabaseHelper myDb;
    Button backButton;
    TextView textView,userName,passWord,stallName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_owner_settings);
        myDb = new DatabaseHelper(this);

        textView = findViewById(R.id.owner_settings_stallName);
        backButton = findViewById(R.id.back);
        userName = findViewById(R.id.settings_username);
        passWord = findViewById(R.id.settings_password);
        stallName = findViewById(R.id.settings_stallName);

        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(v.getContext(),Activity_Owner.class);
                backIntent.putExtra(Activity_Main.STALL_NAME,stallName.getText().toString());
                finish();
            }
        });
    }

    public void resetOnStallNameChange(final View v, final String stall_name){
        Intent resetIntent = new Intent(v.getContext(),Activity_Owner.class);
        resetIntent.putExtra(Activity_Main.STALL_NAME,stall_name);
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void editUsername(final View v){
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
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
                if(myDb.updateOwnerAccountUsername(m_Text,stallNameMessage)){
                    Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                    userName.setText(m_Text);
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

    public void editPassword(final View v){
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
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
                if(myDb.updateOwnerAccountPassword(m_Text,stallNameMessage)){
                    Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                    passWord.setText(m_Text);
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
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
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
                AlertDialog.Builder tempBuilder = new AlertDialog.Builder(v.getContext());
                tempBuilder.setCancelable(false);
                tempBuilder.setTitle("RESET REQUIRED:");
                tempBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if(myDb.updateOwnerAccountStallName(stallNameMessage,m_Text) > 0){
                            Toast.makeText(v.getContext(),"Edit Successful",Toast.LENGTH_SHORT).show();
                            stallName.setText(m_Text);
                            textView.setText(m_Text);
                            resetOnStallNameChange(v,m_Text);
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
        Intent intent = getIntent();
        final String stallNameMessage = intent.getStringExtra(Activity_Main.STALL_NAME);
        if(stallNameMessage != null){
            textView.setText(stallNameMessage);
            userName.setText(myDb.getOwnerUsername(stallNameMessage));
            passWord.setText(myDb.getOwnerPassword(stallNameMessage));
            stallName.setText(stallNameMessage);
            Log.e("START",stallNameMessage);
        }
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(v.getContext());
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Confirm?");
        mBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(myDb.deleteOwnerAccount(stallNameMessage) > 0){
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
}
