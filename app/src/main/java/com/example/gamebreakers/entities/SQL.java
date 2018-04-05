package com.example.gamebreakers.entities;

<<<<<<< HEAD
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
=======
import android.os.StrictMode;
import android.util.Log;
>>>>>>> 2784b1e69b0fa63a51901a9cecee01c9092cdea9

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
<<<<<<< HEAD
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
=======
>>>>>>> 2784b1e69b0fa63a51901a9cecee01c9092cdea9

/**
 * Connection class
 * Created by chris on 3/4/2018.
<<<<<<< HEAD
 * Ensure ip is enabled on azure firewall before connecting
 */

public final class SQL {

    /////////////////////////////////////////// USER METHODS ///////////////////////////////////////////

    // Create connection with sql server
    static Connection connectionClass(){
=======
 */

public class SQL {

    public Connection ConnectToDatabase(){
        Connection con = connectionclass();
        return con;
    }

    Connection connectionclass(){
>>>>>>> 2784b1e69b0fa63a51901a9cecee01c9092cdea9
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
<<<<<<< HEAD

    // Add a new user account
    // Return true if successful, false otherwise
    public static boolean addUserAccount(String username, String password) {
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "INSERT INTO user_table (U_USERNAME,U_PASSWORD,U_BALANCE) " +
                        " VALUES ('" + username + "', '" + password + "', '0');";
                Statement stmt = con.createStatement();
                stmt.executeQuery(query);
                con.close();
                return true;
            }
            return false;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return true;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

    // Update user account balance
    // Return true if successful, false otherwise
    public static boolean saveUserAccount(User user){
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "UPDATE user_table " +
                        " SET U_BALANCE = " + user.balance +
                        " WHERE U_ID = " + user.id + " ";
                Statement stmt = con.createStatement();
                stmt.executeQuery(query);
                con.close();
                return true;
            }
            return false;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return true;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

    // Search the database for buyer username using food name and stall name
    // Return empty string if no result
    public static String getBuyerUsername(String food_name,String stall_name) {
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "SELECT * FROM all_orders_table WHERE " +
                        "  CONVERT(VARCHAR,FOOD_NAME) = '" + food_name +
                        "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                //To retrieve or display data, can be in a separate class/method
                final ArrayList<String> a = new ArrayList<String>();
                while (rs.next()) {
                    String username = rs.getString(3);
                    a.add(username);
                }

                //Check if there is any results
                if (a.size() == 0){
                    con.close();
                    return "";
                }

                String StringValues = "";
                for(int i = 0; i < a.size(); i++){
                    StringValues += a.get(i);
                    if (i < a.size()-1)
                        StringValues += " ";
                }
                con.close();
                return StringValues;
            }
            return "";
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return "";
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return "Error";
        }
    }

    // Original method returns Cursor
    // However, with SQL server, we only get back ResultSet
    // Return null if Error or Invalid Result
    public static ResultSet checkUserLoginData(String username, String password){

        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "SELECT * FROM user_table WHERE " +
                        "  CONVERT(VARCHAR,U_USERNAME) = '" + username +
                        "' AND CONVERT(VARCHAR,U_PASSWORD) = '" + password + "';";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                con.close();
                return rs;
            }
            return null;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return null;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return null;
        }
    }

    // Update the username of user account
    // Return true if successful, false otherwise
    public static boolean updateUserAccountUsername(String old_username,String new_username){
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "UPDATE user_table " +
                        " SET U_USERNAME = '" + new_username +
                        "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + old_username + "'; ";
                Statement stmt = con.createStatement();
                stmt.executeQuery(query);
                con.close();
                return true;
            }
            return false;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return true;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

    // Update the password of user account
    // Return true if successful, false otherwise
    public static boolean updateUserAccountPassword(String old_password,String new_password){
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "UPDATE user_table " +
                        " SET U_PASSWORD = '" + new_password +
                        "' WHERE CONVERT(VARCHAR,U_PASSWORD) = '" + old_password + "'; ";
                Statement stmt = con.createStatement();
                stmt.executeQuery(query);
                con.close();
                return true;
            }
            return false;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return true;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

    /////////////////////////////////////////// OWNER METHODS ///////////////////////////////////////////

    public static boolean addOwnerAccount(String username, String stall_name, String password){
        try {
            Connection con = connectionClass();
            if (con == null) {
                Log.i("Connection Error", "Check internet port or database firewall");
            } else {
                String query = "INSERT INTO owner_table (O_USERNAME,O_STALLNAME,O_PASSWORD) " +
                        " VALUES ('" + username + "', '" + stall_name + "', '" + password + "');";
                Statement stmt = con.createStatement();
                stmt.executeQuery(query);
                con.close();
                return true;
            }
            return false;
        }
        catch (SQLException se){
            // normal error: occurs when no values is returned
            return true;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

=======
>>>>>>> 2784b1e69b0fa63a51901a9cecee01c9092cdea9
}
