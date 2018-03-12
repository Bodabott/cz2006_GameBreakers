package com.example.gamebreakers.login;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.R;
import com.example.gamebreakers.user.Activity_UserMenu;

/**
 * Created by zNotAgain on 3/3/2018.
 */

public class Activity_CreateUserAccount extends Activity {

    DatabaseHelper myDb;
    EditText username,password,confirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuseraccount);
        myDb = new DatabaseHelper(this);
    }

    public void registerUser(View v){
        username = findViewById(R.id.register_usernameText);
        password = findViewById(R.id.register_passwordText);
        confirmPassword = findViewById(R.id.register_confirmPasswordText);

        // make sure username & password fields are not empty
        if(myDb.isUserRegisterAcceptable(username.getText().toString(),password.getText().toString(),confirmPassword.getText().toString())){
            if(password.getText().toString().matches(confirmPassword.getText().toString())){
                Cursor user_res = myDb.checkUserLoginData(username.getText().toString(), password.getText().toString());
                Cursor owner_res = myDb.checkOwnerLoginData(username.getText().toString(), password.getText().toString());

                // make sure account doesn't exist in database
                if(user_res.getCount() == 1 || owner_res.getCount() == 1){
                    Toast.makeText(Activity_CreateUserAccount.this,"Account Already Exists",Toast.LENGTH_LONG).show();
                }else{
                    boolean isInserted = myDb.createUserAccount(username.getText().toString(),password.getText().toString());

                    // on successful insert to database
                    if(isInserted){
                        Toast.makeText(Activity_CreateUserAccount.this,"Account Created",Toast.LENGTH_LONG).show();
                        Intent goIntent = new Intent(v.getContext(),Activity_UserMenu.class);
                        startActivity(goIntent);
                    }else{
                        Toast.makeText(Activity_CreateUserAccount.this,"Account not Created",Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                confirmPassword.setError("Does not match your password");
            }
        }else{
            // username & password fields are empty
            Toast.makeText(Activity_CreateUserAccount.this,"Required Fields are Empty",Toast.LENGTH_LONG).show();
        }
    }

    public void gobackMain(View view) {
        Intent backIntent = new Intent(view.getContext(),Activity_Main.class);
        startActivity(backIntent);
    }
}
