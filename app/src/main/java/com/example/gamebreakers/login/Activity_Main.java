package com.example.gamebreakers.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.R;
import com.example.gamebreakers.owner.Activity_Owner;
import com.example.gamebreakers.user.Activity_UserMenu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Activity_Main extends Activity {

    public static final String STALL_NAME = "MY_STALL";
    DatabaseHelper myDb;
    EditText editUserName,editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
    }

    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + server + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        }catch(SQLException se){
            Log.e("error here 1 : ", se.getMessage());
        }catch(ClassNotFoundException e){
            Log.e("error here 2 : ", e.getMessage());
        }catch(Exception e){
            Log.e("error here 3 : ", e.getMessage());
        }

        return connection;
    }

    public void createAccount(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setCancelable(true);
        mBuilder.setTitle("Choose an Account Type: ")
                .setItems(R.array.Array_AccountType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent goIntent = new Intent(getBaseContext(),Activity_CreateUserAccount.class);
                            startActivity(goIntent);
                        }else{
                            Intent goIntent = new Intent(getBaseContext(),Activity_CreateOwnerAccount.class);
                            startActivity(goIntent);
                        }
                    }
                });
        mBuilder.show();
    }

    public void logIn(View view) {
        editUserName = findViewById(R.id.usernameText);
        editPassword = findViewById(R.id.passwordText);

        // make sure username & password fields are not empty
        if(myDb.isLoginAcceptable(editUserName.getText().toString(),editPassword.getText().toString())) {
            Cursor user_res = myDb.checkUserLoginData(editUserName.getText().toString(), editPassword.getText().toString());
            Cursor owner_res = myDb.checkOwnerLoginData(editUserName.getText().toString(), editPassword.getText().toString());

            // make sure account exists in database
            if (user_res.getCount() == 1) { // if user account
                Toast.makeText(Activity_Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent goIntent = new Intent(view.getContext(), Activity_UserMenu.class);
                startActivity(goIntent);
            }else if(owner_res.getCount() == 1){ // if owner account
                Toast.makeText(Activity_Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent goIntent = new Intent(view.getContext(), Activity_Owner.class);
                goIntent.putExtra(STALL_NAME,myDb.getStallName(editUserName.getText().toString(),editPassword.getText().toString()));
                startActivity(goIntent);
            }else{
                // show login fail message
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Main.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Error");
                mBuilder.setMessage("Account doesn't exists.");
                mBuilder.show();
            }
        }else{
            // username & password fields are empty
            Toast.makeText(Activity_Main.this,"Required Fields are Empty",Toast.LENGTH_LONG).show();
        }
    }
}
