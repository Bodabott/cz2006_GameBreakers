package com.example.gamebreakers.login;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.DatabaseHelper;
import com.example.gamebreakers.entities.SQL;

/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_CreateOwnerAccount extends Activity {
    SQL myDb;
    EditText username,stallName,password,confirmPassword;
    TextView alreadyMember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createowneraccount);
        myDb = new SQL();

        username = findViewById(R.id.register_usernameText);
        stallName = findViewById(R.id.register_stallName);
        password = findViewById(R.id.register_passwordText);
        confirmPassword = findViewById(R.id.register_confirmPasswordText);
        alreadyMember = findViewById(R.id.already_member);

        alreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void registerOwnerAccount(final View v){
        // make sure username & stall_name & password fields are not empty
        if(isOwnerRegisterAcceptable(username.getText().toString(),stallName.getText().toString(),password.getText().toString(),confirmPassword.getText().toString())){
            // check if stall name already exists
            Cursor stall_name_res = myDb.isStallNameAcceptable(stallName.getText().toString());
            if(stall_name_res.getCount() == 1){
                stallName.setError("Stall Name Already Exists");
                return;
            }
            if(password.getText().toString().matches(confirmPassword.getText().toString())){
                Cursor user_res = myDb.checkUserLoginData(username.getText().toString(), password.getText().toString());
                Cursor owner_res = myDb.checkOwnerLoginData(username.getText().toString(), password.getText().toString());

                // make sure account doesn't exist in database
                if(user_res.getCount() == 1 || owner_res.getCount() == 1){
                    Toast.makeText(Activity_CreateOwnerAccount.this,"Account Already Exists",Toast.LENGTH_LONG).show();
                }else{
                    boolean isInserted = myDb.addOwnerAccount(username.getText().toString(),stallName.getText().toString(),password.getText().toString());

                    // on successful insert to database
                    if(isInserted){
                        Toast.makeText(Activity_CreateOwnerAccount.this,"Account Created",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(Activity_CreateOwnerAccount.this,"Account not Created",Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                confirmPassword.setError("Does not match your password");
            }
        }else{
            // username & password fields are empty
            Toast.makeText(Activity_CreateOwnerAccount.this,"Required Fields are Empty",Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOwnerRegisterAcceptable(String username,String stall_name, String password, String confirm_password){
        return !(username.isEmpty() || stall_name.isEmpty() || password.isEmpty() || confirm_password.isEmpty());
    }
}
