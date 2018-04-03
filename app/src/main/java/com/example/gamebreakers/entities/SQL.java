package com.example.gamebreakers.entities;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connection class
 * Created by chris on 3/4/2018.
 */

public class SQL {

    public Connection ConnectToDatabase(){
        Connection con = connectionclass();
        return con;
    }

    Connection connectionclass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //ConnectionURL = "jdbc:jtds:sqlserver://" + server + ";User=" + user + ";password=" + password + ";";
            ConnectionURL = "jdbc:jtds:sqlserver://sqlexpress5.database.windows.net:1433;" +
                    "DatabaseName=myDatabase;user=sqlexpress5@sqlexpress5;password=SQLexpre$$5;encrypt=true;" +
                    "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("Error 1:", se.getMessage());
        } catch (ClassNotFoundException ce) {
            Log.e("Error 2:", ce.getMessage());
        } catch (Exception e) {
            Log.e("Error 3:", e.getMessage());
        }
        return connection;
    }
}
