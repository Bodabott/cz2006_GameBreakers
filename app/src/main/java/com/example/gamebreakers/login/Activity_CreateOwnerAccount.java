package com.example.gamebreakers.login;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gamebreakers.R;
import com.example.gamebreakers.entities.SQL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zNotAgain on 5/3/2018.
 */

public class Activity_CreateOwnerAccount extends Activity {

    EditText username,stallName,password,confirmPassword,postalCode;
    TextView alreadyMember;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createowneraccount);

        username = findViewById(R.id.register_usernameText);
        stallName = findViewById(R.id.register_stallName);
        password = findViewById(R.id.register_passwordText);
        confirmPassword = findViewById(R.id.register_confirmPasswordText);
        postalCode = findViewById(R.id.register_postalCodeText);
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
        if(isOwnerRegisterAcceptable(username.getText().toString(),stallName.getText().toString(),password.getText().toString(),confirmPassword.getText().toString(),postalCode.getText().toString())){
            // check if stall name already exists
            ArrayList stall_name_res = SQL.isStallNameAcceptable(stallName.getText().toString());
            if(stall_name_res.size() == 1){
                stallName.setError("Stall Name Already Exists");
                return;
            }
            // check if postal code is valid
            if(!isPostalCodeAcceptable(postalCode.getText().toString())){
                postalCode.setError("Invalid Postal Code");
                return;
            }
            if(password.getText().toString().matches(confirmPassword.getText().toString())){
                ArrayList user_res = SQL.checkUserLoginData(username.getText().toString(), password.getText().toString());
                ArrayList owner_res = SQL.checkOwnerLoginData(username.getText().toString(), password.getText().toString());
                ArrayList postal_res = SQL.checkOwnerPostalCode(postalCode.getText().toString());

                // make sure account doesn't exist in database
                if(user_res.size() == 1 || owner_res.size() == 1 || postal_res.size() == 1){
                    Toast.makeText(Activity_CreateOwnerAccount.this,"Account Already Exists",Toast.LENGTH_LONG).show();
                }else{
                    boolean isInserted = SQL.addOwnerAccount(username.getText().toString(),stallName.getText().toString(),password.getText().toString(),Integer.parseInt(postalCode.getText().toString()));

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

    public boolean isOwnerRegisterAcceptable(String username,String stall_name, String password, String confirm_password, String postal_code){
        return !(username.isEmpty() || stall_name.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || postal_code.isEmpty());
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
}
