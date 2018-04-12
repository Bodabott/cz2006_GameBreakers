package com.example.gamebreakers.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.owner.Activity_Owner;
import com.example.gamebreakers.user.Activity_User;

import static com.example.gamebreakers.entities.SQL.testConnection;

public class Activity_Main extends AppCompatActivity {

    public static final String STALL_NAME = "MY_STALL";
    public static final String USER_NAME = "MY_USERNAME";
    public static final String PASSWORD = "MY_PASSWORD";

    DatabaseHelper myDb;
    EditText editUserName,editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
    }

    public void create(View v){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Main.this);
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

    public void login(View v) {
        editUserName = findViewById(R.id.usernameText);
        editPassword = findViewById(R.id.passwordText);
        // make sure username & password fields are not empty
        if (isLoginAcceptable(editUserName.getText().toString(), editPassword.getText().toString())) {
            Cursor user_res = myDb.checkUserLoginData(editUserName.getText().toString(), editPassword.getText().toString());
            Cursor owner_res = myDb.checkOwnerLoginData(editUserName.getText().toString(), editPassword.getText().toString());

            // make sure account exists in database
            if (user_res.getCount() == 1) { // if user account
                Toast.makeText(Activity_Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent goIntent = new Intent(v.getContext(), Activity_User.class);

                goIntent.putExtra(USER_NAME, editUserName.getText().toString());
                goIntent.putExtra(PASSWORD, editPassword.getText().toString());
                startActivity(goIntent);
            } else if (owner_res.getCount() == 1) { // if owner account
                Toast.makeText(Activity_Main.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent goIntent = new Intent(v.getContext(), Activity_Owner.class);
                goIntent.putExtra(STALL_NAME, myDb.getStallName(editUserName.getText().toString(), editPassword.getText().toString()));
                startActivity(goIntent);
            } else {
                // show login fail message
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Activity_Main.this);
                mBuilder.setCancelable(true);
                mBuilder.setTitle("Error");
                mBuilder.setMessage("Account doesn't exists.");
                mBuilder.show();
            }
        } else {
            // username & password fields are empty
            Toast.makeText(Activity_Main.this, "Required Fields are Empty", Toast.LENGTH_LONG).show();
        }
    }
    public boolean isLoginAcceptable(String username, String password){
        return !(username.isEmpty() || password.isEmpty());
    }
}
// Check valid email code
// !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()