package com.example.gamebreakers.entities;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Connection class
 * Created by chris on 3/4/2018.
 */


public final class SQL {

    // Create connection with sql server
    static Connection connectionClass(){
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

    /**
     * Send update query
     * Return true on successful submission of query, return false otherwise
     * Note: No error is returned if the query did not update table
     * Return false if violate table or key constraint
     */
    static boolean sendUpdate(String query){
        try{
            Connection con = connectionClass();
            if (con == null){
                Log.i("Connection Error", "Check internet port or database firewall");
            }else{
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
                con.close();
                return true;
            }
            con.close();
            return false;
        }
        catch(SQLException se){
            Log.e("Access Error ","SQLException",se);
            return false;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return false;
        }
    }

    /**
     * Send query
     * Return ArrayList from query
     * Each element in ArrayList is a row on the database, where each row is a Hashmap,
     * where each Hashmap is a mapping from column name(String) to record data (String)
     * Return null if there is no results
     */
    static ArrayList sendQuery(String query){
        try{
            Connection con = connectionClass();
            if (con == null){
                Log.i("Connection Error", "Check internet port or database firewall");
            }else{
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                ArrayList a = new ArrayList();
                ResultSetMetaData md = rs.getMetaData();
                int col = md.getColumnCount();
                while(rs.next()){
                    HashMap row = new HashMap(col);
                    for(int i = 1; i <= col; ++i)
                        row.put(md.getColumnName(i),rs.getString(i));
                    a.add(row);
                }
                con.close();
                return a;
            }
            return null;
        }
        catch(SQLException se){
            Log.e("Access Error ","SQLException",se);
            return null;
        }
        catch (Exception e){
            Log.e("Error ","exception",e);
            return null;
        }
    }

    // Append ArrayList results to a string
    static String concatArrayListToString(ArrayList a){
        if (a.size() == 0)
            return "";

        // Appending results in ArrayList to a String
        String StringValues = "";
        for (int i = 0; i < a.size(); i++) {
            StringValues += a.get(i);
            if (i < a.size() - 1)
                StringValues += " ";
        }
        return StringValues;
    }

    // Test Connection, returns true if successful
    public static boolean testConnection(){
        return (connectionClass() != null);
    }

    /////////////////////////////////////////// USER BALANCE ///////////////////////////////////////////


    // Search the database for buyer username using food name and stall name
    // Return 0 if no result or 0 balance, good to check username exist first
    public static int getUserBalance(String username) {
        String query = "SELECT * FROM user_table WHERE " +
                "  CONVERT(VARCHAR,U_USERNAME) = '" + username + "';";
        ArrayList <HashMap> a = sendQuery(query);
        if (a == null || a.size() == 0)
            return 0;

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("U_BALANCE"));
        }
        return Integer.parseInt(StringList.get(0));
    }


    // Return true if update to user table is successful, false otherwise
    // Note: Assumption is new balance not equals to old balance
    public static boolean updateUserBalance(String username, int new_amt){
        String query = "UPDATE user_table " +
                " SET U_BALANCE = '" + new_amt +
                "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + username + "';";

        String query2 = "SELECT * FROM user_table WHERE " +
                "CONVERT(VARCHAR,U_USERNAME) = '" + username +
                "' AND CONVERT(VARCHAR,U_BALANCE) = '" + new_amt + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);
        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }


    // Search the database for food price using food name and stall name
    // Return 0 if no result or 0 price, good to check stall name exist first
    public static int getFoodPrice(String food_name, String stall_name) {
        String query = "SELECT * FROM owner_menu_table WHERE " +
                "  CONVERT(VARCHAR,FOOD_NAME) = '" + food_name +
                "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        ArrayList <HashMap> a = sendQuery(query);
        if (a == null || a.size() == 0)
            return 0;

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("FOOD_PRICE"));
        }
        return Integer.parseInt(StringList.get(0));
    }

    /////////////////////////////////////////// USER METHODS ///////////////////////////////////////////

    // Add a new user account
    // Return true if successful, false otherwise
    public static boolean addUserAccount(String username, String password) {
        String query = "INSERT INTO user_table (U_USERNAME,U_PASSWORD,U_BALANCE) " +
                " VALUES ('" + username + "', '" + password + "', '0');";

        String query2 = "SELECT * FROM user_table WHERE " +
                "CONVERT(VARCHAR,U_USERNAME) = '" + username +
                "' AND CONVERT(VARCHAR,U_PASSWORD) = '" + password + "';";

        // Make sure account does not already exist
        ArrayList a = sendQuery(query2);
        if (a == null || a.size()>0)
            return false;

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size()>0)
            return true;
        else
            return false;
    }


    // Update user account balance
    // Return true if successful, false otherwise
    public static boolean saveUserAccount(User user){
        String query =  "UPDATE user_table "
                + " SET U_BALANCE = " + user.balance
                + " WHERE U_ID = " + user.id + " ";
        sendUpdate(query);

        String query2 = "SELECT * FROM user_table WHERE " +
                "U_ID = " + user.id + " AND U_BALANCE = " + user.balance + ";";

        ArrayList a = sendQuery(query2);
        if (a == null || a.size() < 1)
            return false;
        else
            return true;
    }


    // Search the database for buyer username using food name and stall name
    // Return empty string if no result
    public static String getBuyerUsername(String food_name,String stall_name) {
        String query = "SELECT * FROM all_orders_table WHERE " +
                "  CONVERT(VARCHAR,FOOD_NAME) = '" + food_name +
                "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return "";

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("U_USERNAME"));
        }
        return concatArrayListToString(StringList);
    }


    /**
     * Original method returns Cursor
     * However, with SQL server, we only get back ResultSet which will be converted to ArrayList
     * Each element in ArrayList is a row on the database, where each row is a Hashmap,
     * where each Hashmap is a mapping from column name(String) to record data (String)
     *
     * * Returns ArrayList of rows, with user column hashmap to values
     * Return null if Error or Invalid Result
     */
    public static ArrayList checkUserLoginData(String username, String password){
        String query = "SELECT * FROM user_table WHERE " +
                " CONVERT(VARCHAR,U_USERNAME) = '" + username +
                "' AND CONVERT(VARCHAR,U_PASSWORD) = '" + password + "';";
        return sendQuery(query);
    }


    // Update the username of user account, also updates all orders, order history username
    // Return true if update to user table is successful, false otherwise
    // Note: Assumption is all users have different username and new username does not exist previously
    public static boolean updateUserAccountUsername(String old_username,String new_username){
        String query = "UPDATE user_table " +
                " SET U_USERNAME = '" + new_username +
                "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + old_username + "'; ";

        String query2 = "UPDATE all_orders_table " +
                " SET U_USERNAME = '" + new_username +
                "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + old_username + "'; ";

        String query3 = "UPDATE user_history_table " +
                " SET U_USERNAME = '" + new_username +
                "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + old_username + "'; ";

        String query4 = "UPDATE owner_history_table " +
                " SET U_USERNAME = '" + new_username +
                "' WHERE CONVERT(VARCHAR,U_USERNAME) = '" + old_username + "'; ";

        String query5 = "SELECT * FROM user_table WHERE " +
                "CONVERT(VARCHAR,U_USERNAME) = '" + new_username + "';";


        ArrayList a = sendQuery(query5);
        sendUpdate(query);
        sendUpdate(query2);
        sendUpdate(query3);
        sendUpdate(query4);
        ArrayList a2 = sendQuery(query5);
        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }

    // Update the password of user account
    // Return true if successful, false otherwise
    // Note: Assumption is users have different password and new password does not exist previously
    public static boolean updateUserAccountPassword(String old_password,String new_password){
        String query = "UPDATE user_table " +
                " SET U_PASSWORD = '" + new_password +
                "' WHERE CONVERT(VARCHAR,U_PASSWORD) = '" + old_password + "'; ";

        String query2 = "SELECT * FROM user_table WHERE " +
                " CONVERT(VARCHAR,U_PASSWORD) = '" + new_password + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);
        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }


    // Delete user account from user table (username is still in orders history table)
    // Return 1 if successful, 0 otherwise
    public static Integer deleteUserAccount(String username, String password){
        String query = "DELETE FROM user_table WHERE CONVERT (VARCHAR,U_USERNAME) = '" +
                username + "' AND CONVERT(VARCHAR,U_PASSWORD) = '" + password + "';";

        String query2 = "DELETE FROM user_history_table WHERE CONVERT (VARCHAR,U_USERNAME) = '" +
                username + "';";

        String query3 = "SELECT * FROM user_table WHERE "+
                " CONVERT(VARCHAR,U_USERNAME) = '" + username +
                "' AND CONVERT(VARCHAR,U_PASSWORD) = '" + password + "'; ";

        ArrayList a = sendQuery(query3);
        sendUpdate(query);
        sendUpdate(query2);
        ArrayList a2 = sendQuery(query3);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }

    /////////////////////////////////////////// OWNER METHODS ///////////////////////////////////////////

    // Add a new owner account
    // Return true if successful, false otherwise
    public static boolean addOwnerAccount(String username, String stall_name, String password){
        String query = "INSERT INTO owner_table (O_USERNAME,O_STALLNAME,O_PASSWORD) " +
                " VALUES ('" + username + "', '" + stall_name + "', '" + password + "');";

        String query2 = "SELECT * FROM owner_table WHERE CONVERT(VARCHAR,O_USERNAME) = '" +
                username + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "' AND CONVERT(VARCHAR,O_PASSWORD) = '" +
                password + "';";

        // Make sure account does not already exist
        ArrayList a = sendQuery(query2);
        if (a == null || a.size()>0)
            return false;

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size()>0)
            return true;
        else
            return false;
    }


    // Search the database for owner username using stall name
    // Return empty string if no result
    public static String getOwnerUsername(String stall_name){
        String query = "SELECT O_USERNAME FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return "";

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("O_USERNAME"));
        }
        return concatArrayListToString(StringList);
    }


    // Search the database for owner password using stall name
    // Return empty string if no result
    public static String getOwnerPassword(String stall_name){
        String query = "SELECT O_PASSWORD FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return "";

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("O_PASSWORD"));
        }
        return concatArrayListToString(StringList);
    }


    // Search the database for stall name using username and password
    // Return empty string if no result
    public static String getStallName(String username, String password){
        String query = "SELECT O_STALLNAME FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_USERNAME) = '" + username
                + "' AND CONVERT(VARCHAR,O_PASSWORD) = '" + password + "';";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return "";

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("O_STALLNAME"));
        }
        return concatArrayListToString(StringList);
    }


    // Update the queue number in the owner table
    // Return true if successful, false otherwise
    public static boolean updateQueueNum(int queueNum,String stall_name){
        String query = "UPDATE owner_table " +
                " SET O_QUEUENUM = '" + queueNum +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "'; ";

        String query2 = "SELECT * FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +
                "' AND CONVERT(VARCHAR,O_QUEUENUM) = '" + queueNum + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }

    // Update the owner username in the database
    // Return true if successful, false otherwise
    public static boolean updateOwnerAccountUsername(String username,String stall_name){
        String query = "UPDATE owner_table " +
                " SET O_USERNAME = '" + username +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "'; ";

        String query2 = "SELECT * FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_USERNAME) = '" + username + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }


    // Update owner account password in database
    // Return true if successful, false otherwise
    public static boolean updateOwnerAccountPassword(String password,String stall_name){

        String query = "UPDATE owner_table " +
                " SET O_PASSWORD = '" + password +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "'; ";

        String query2 = "SELECT * FROM owner_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "' AND CONVERT(VARCHAR,O_PASSWORD) = '" + password + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }


    // Update stall name in database
    // Returns 1 if successful, 0 otherwise
    public static Integer updateOwnerAccountStallName(String old_stall_name, String stall_name){
        String query = "UPDATE owner_table " +
                " SET O_STALLNAME = '" + stall_name +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + old_stall_name + "'; ";

        String query2 = "UPDATE owner_menu_table " +
                " SET O_STALLNAME = '" + stall_name +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + old_stall_name + "'; ";

        String query3 = "UPDATE all_orders_table " +
                " SET O_STALLNAME = '" + stall_name +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + old_stall_name + "'; ";

        String query4 = "UPDATE owner_history_table " +
                " SET O_STALLNAME = '" + stall_name +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + old_stall_name + "'; ";

        String query5 = "UPDATE user_history_table " +
                " SET O_STALLNAME = '" + stall_name +
                "' WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + old_stall_name + "'; ";

        String query6 = "SELECT * FROM owner_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '"
                + stall_name + "';";

        ArrayList a = sendQuery(query6);
        sendUpdate(query);
        sendUpdate(query2);
        sendUpdate(query3);
        sendUpdate(query4);
        sendUpdate(query5);
        ArrayList a2 = sendQuery(query6);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()<1 && a2.size()>0)
            return 1;
        else
            return 0;
    }


    // Delete owner account in owner table, orders table, owner manu and history table
    // return 1 if successful, 0 otherwise
    public static Integer deleteOwnerAccount(String stall_name){
        String query = "DELETE FROM owner_table WHERE CONVERT (VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        String query2 = "DELETE FROM owner_menu_table WHERE CONVERT (VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        String query3 = "DELETE FROM owner_history_table WHERE CONVERT (VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        String query4 = "DELETE FROM all_orders_table WHERE CONVERT (VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        String query5 = "SELECT * FROM owner_table WHERE "+
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "'; ";

        ArrayList a = sendQuery(query5);
        sendUpdate(query);
        sendUpdate(query2);
        sendUpdate(query3);
        sendUpdate(query4);
        ArrayList a2 = sendQuery(query5);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }

    /**
     * Original method returns Cursor
     * However, with SQL server, we only get back ResultSet which will be converted to ArrayList
     * Each element in ArrayList is a row on the database, where each row is a Hashmap,
     * where each Hashmap is a mapping from column name(String) to record data (String)
     *
     * Returns ArrayList of rows, with owner column hashmap to values
     * Return null if Error or Invalid Result
     */
    public static ArrayList checkOwnerLoginData(String username, String password){
        String query = "SELECT * FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_USERNAME) = '" + username +
                "' AND CONVERT(VARCHAR,O_PASSWORD) = '" + password + "';";
        return sendQuery(query);
    }

    /**
     * Original method returns Cursor
     * However, with SQL server, we only get back ResultSet which will be converted to ArrayList
     * Each element in ArrayList is a row on the database, where each row is a Hashmap,
     * where each Hashmap is a mapping from column name(String) to record data (String)
     *
     * Returns ArrayList of rows, with column O_STALLNAME hashmap to stall_name
     * Return null if Error or Invalid Result
     */
    public static ArrayList isStallNameAcceptable(String stall_name){
        String query = "SELECT * FROM owner_table WHERE " +
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        return sendQuery(query);
    }


    //Returns an array stalls
    //Return null if Error or Invalid Result
    public static Stall[] getArrayOfStall(){
        String query = "SELECT * FROM owner_table;";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        Stall[] stallList = new Stall[a.size()];
        int i = 0;
        for (int j = 0; j < a.size(); j++){
            HashMap row = a.get(j);
            stallList[i] = new Stall(
                    Integer.parseInt((String)row.get("O_ID")),
                    (String)row.get("O_STALLNAME"),
                    0
            );
            i++;
        }
        return stallList;
    }


    //Return Food Array
    // Return null if Error or Invalid Result
    public static Food[] getStallMenu(String stall_name){
        String query = "SELECT * FROM owner_menu_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "';";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        Food[] foodList = new Food[a.size()];
        int i = 0;
        for (int j = 0; j < a.size(); j++){
            HashMap row = a.get(j);
            foodList[i] = new Food(
                    (String)row.get("FOOD_NAME"),
                    Integer.parseInt((String)row.get("FOOD_PRICE"))
            );
            i++;
        }
        return foodList;
    }


    // Delete food from owner menu table
    // return 1 if successful, 0 otherwise
    public static Integer deleteMenuArrayData(String food_name, String stall_name){
        String query = "DELETE FROM owner_menu_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +  "';";

        String query2 = "SELECT * FROM owner_menu_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +  "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }


    // Add a new food to owner menu table
    // Return true if successful, false otherwise
    public static boolean addMenuArrayData(String food_name, String stall_name, int price){
        String query = "INSERT INTO owner_menu_table (FOOD_NAME,O_STALLNAME,FOOD_PRICE) " +
                " VALUES ('" + food_name + "', '" + stall_name + "', '" + price + "');";

        String query2 = "SELECT * FROM owner_menu_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "' AND CONVERT(VARCHAR,FOOD_PRICE) = '" +
                price + "';";

        // Make sure does not already exist
        ArrayList a = sendQuery(query2);
        if (a == null || a.size()>0)
            return false;

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size()>0)
            return true;
        else
            return false;
    }


    /////////////////////////////////////////// ORDER METHODS ///////////////////////////////////////////

    //Returns user order array
    //Return null if Error or Invalid Result
    public static Order[] getUserArrayOfOrders(String username){
        String query = "SELECT * FROM all_orders_table WHERE CONVERT(VARCHAR,U_USERNAME) = '"
                + username +  "' ORDER BY ORDER_ID ASC;";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        Order[] orderList = new Order[a.size()];
        int i = 0;
        for (int j = 0; j < a.size(); j++){
            HashMap row = a.get(j);
            orderList[i] = new Order(
                    Integer.parseInt((String)row.get("ORDER_ID")),
                    (String)row.get("FOOD_NAME"),
                    (String)row.get("U_USERNAME"),
                    (String)row.get("O_STALLNAME"),
                    (String)row.get("COLLECTION_TIME"),
                    (((String)row.get("COMPLETED")).equals("y"))
            );
            i++;
        }
        return orderList;
    }


    //Returns order array with stall name
    //Return null if Error or Invalid Result
    public static Order[] getArrayOfOrders(String stall_name){
        String query = "SELECT * FROM all_orders_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '"
                + stall_name +  "' ORDER BY ORDER_ID ASC;";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        Order[] orderList = new Order[a.size()];
        int i = 0;
        for (int j = 0; j < a.size(); j++){
            HashMap row = a.get(j);
            orderList[i] = new Order(
                    Integer.parseInt((String)row.get("ORDER_ID")),
                    (String)row.get("FOOD_NAME"),
                    (String)row.get("U_USERNAME"),
                    (String)row.get("O_STALLNAME"),
                    (String)row.get("COLLECTION_TIME"),
                    (((String)row.get("COMPLETED")).equals("y"))
            );
            i++;
        }
        return orderList;
    }


    // Add a new order to orders table
    // Return true if successful, false otherwise
    // Note: Orders are non-unique, identifier will be order id.
    // No limit imposed on adding new orders
    public static boolean addOrderArrayData(String food_name, String username,String stall_name, String collection_time){
        String query = "INSERT INTO all_orders_table (FOOD_NAME,U_USERNAME,O_STALLNAME,COLLECTION_TIME,COMPLETED) " +
                " VALUES ('" + food_name + "', '" + username + "', '" + stall_name + "', '" + collection_time + "', 'n');";

        String query2 = "SELECT * FROM all_orders_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,U_USERNAME) = '" +
                username + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "' AND CONVERT(VARCHAR,COLLECTION_TIME) = '" +
                collection_time + "';";

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size()>0)
            return true;
        else
            return false;
    }

    // Update orders from all orders table to completed
    // Return true if successful, false otherwise
    // Note: Orders with the same food, stall and collection time will be updated together as completed
    public static boolean updateOrder(String food_name, String stall_name){
        String query = "UPDATE all_orders_table " +
                " SET COMPLETED = 'y'"  +
                " WHERE CONVERT(VARCHAR,FOOD_NAME) = '" + food_name +
                "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        String query2 = "SELECT * FROM all_orders_table WHERE " +
                " CONVERT(VARCHAR,COMPLETED) = 'y' " +
                " AND CONVERT(VARCHAR,FOOD_NAME) = '" + food_name +
                "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return false;
        else if (a.size()<1 && a2.size()>0)
            return true;
        else
            return false;
    }

    // Delete orders from all orders table
    // return 1 if successful, 0 otherwise
    public static Integer deleteOrderArrayData(String food_name, String stall_name){
        String query = "DELETE FROM all_orders_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +  "';";

        String query2 = "SELECT * FROM all_orders_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +  "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }

    /////////////////////////////////////////// HISTORY METHODS ///////////////////////////////////////////


    // Search the database for user previous order for food
    // Return String array of food
    // Return null if no result
    public static String[] getUserArrayOfHistory(String username){
        String query = "SELECT * FROM user_history_table WHERE " +
                " CONVERT(VARCHAR,U_USERNAME) = '" + username
                + "' ORDER BY ORDER_ID ASC;";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("FOOD_NAME"));
        }
        return StringList.toArray(new String[StringList.size()]);
    }

    // Search the database for owner previous order for food
    // Return String array of food
    // Return null if no result
    public static String[] getArrayOfHistory(String stall_name){
        String query = "SELECT * FROM owner_history_table WHERE " +
                " CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        ArrayList <HashMap> a = sendQuery(query);
        if (a == null)
            return null;

        ArrayList<String> StringList = new ArrayList<String>();
        for (int i = 0; i < a.size(); i++){
            HashMap row = a.get(i);
            StringList.add((String)row.get("FOOD_NAME"));
        }
        return StringList.toArray(new String[StringList.size()]);
    }


    // Orginal method is void, this returns boolean
    // Add a new order to user order history table
    // Return true if successful, false otherwise
    public static boolean addUserHistoryArrayData(String food_name, String username,String stall_name){
        String query = "INSERT INTO user_history_table (FOOD_NAME,U_USERNAME,O_STALLNAME) " +
                " VALUES ('" + food_name + "', '" + username + "', '" + stall_name + "');";

        String query2 = "SELECT * FROM user_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,U_USERNAME) = '" +
                username + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "';";

        // Make sure does not already exist
        ArrayList a = sendQuery(query2);
        if (a == null || a.size()>0)
            return false;

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size()>0)
            return true;
        else
            return false;
    }


    // Orginal method is void, this returns boolean
    // Add a new order to owner order history table
    // Return true if successful, false otherwise
    public static boolean addHistoryArrayData(String food_name, String username,String stall_name) {
        String query = "INSERT INTO owner_history_table (FOOD_NAME,U_USERNAME,O_STALLNAME) " +
                " VALUES ('" + food_name + "', '" + username + "', '" + stall_name + "');";

        String query2 = "SELECT * FROM owner_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,U_USERNAME) = '" +
                username + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" +
                stall_name + "';";

        // Make sure does not already exist
        ArrayList a = sendQuery(query2);
        if (a == null || a.size() > 0)
            return false;

        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        // Check successful update
        if (a2 == null)
            return false;
        else if (a2.size() > 0)
            return true;
        else
            return false;
    }


    // Delete orders from user order history table
    // return 1 if successful, 0 otherwise
    public static Integer deleteUserHistoryArrayData(String food_name, String username){
        String query = "DELETE FROM user_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,U_USERNAME) = '" + username + "';";

        String query2 = "SELECT * FROM user_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,U_USERNAME) = '" + username +  "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }


    // Delete all user data from user order history table
    // return 1 if successful, 0 otherwise
    public static Integer deleteAllUserHistoryData(String username){
        String query = "DELETE FROM user_history_table WHERE CONVERT(VARCHAR,USERNAME) = '" + username + "';";
        String query2 = "SELECT * FROM user_history_table WHERE CONVERT(VARCHAR,USERNAME) = '" + username + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }


    // Delete orders from owner order history table
    // return 1 if successful, 0 otherwise
    public static Integer deleteHistoryArrayData(String food_name, String stall_name){
        String query = "DELETE FROM order_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        String query2 = "SELECT * FROM order_history_table WHERE CONVERT(VARCHAR,FOOD_NAME) = '" +
                food_name + "' AND CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name +  "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }


    // Delete all owner data from owner order history table
    // return 1 if successful, 0 otherwise
    public static Integer deleteAllHistoryData(String stall_name){
        String query = "DELETE FROM owner_history_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";
        String query2 = "SELECT * FROM owner_history_table WHERE CONVERT(VARCHAR,O_STALLNAME) = '" + stall_name + "';";

        ArrayList a = sendQuery(query2);
        sendUpdate(query);
        ArrayList a2 = sendQuery(query2);

        if (a == null || a2 == null)
            return 0;
        else if (a.size()>0 && a2.size()<1)
            return 1;
        else
            return 0;
    }

}