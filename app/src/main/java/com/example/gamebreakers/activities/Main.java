package com.example.gamebreakers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import com.example.gamebreakers.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Activity {

    String un,pass,db,ip;
    Connection mConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = "14.0.1000.169";
        db = "LAPTOP-A6RP57A1";
        un = "LAPTOP-A6RP57A1\\zNotAgain";
        pass = "";
        mConnection = connectionClass(un,pass,db,ip);

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

    public void goMainMenu(View view) {
        Intent loginIntent = new Intent(view.getContext(),MainMenuActivity.class);
        loginIntent.putExtra("KeyForSending", "login successful");
        startActivity(loginIntent);
    }
}
