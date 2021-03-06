package com.example.gamebreakers.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zNotAgain on 2/3/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "zNotAgain.db";

    // Food-User Table
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_COL_1 = "U_ID";
    private static final String USER_COL_2 = "U_USERNAME";
    private static final String USER_COL_3 = "U_PASSWORD";
    private static final String USER_COL_4 = "U_BALANCE";

    // Stall-Owner Table
    private static final String OWNER_TABLE_NAME = "owner_table";
    private static final String OWNER_COL_1 = "O_ID";
    private static final String OWNER_COL_2 = "O_USERNAME";
    private static final String OWNER_COL_3 = "O_STALLNAME";
    private static final String OWNER_COL_4 = "O_PASSWORD";
    private static final String OWNER_COL_5 = "O_TYPE";
    private static final String OWNER_COL_6 = "O_QUEUENUM";
    private static final String OWNER_COL_7 = "O_AVERAGECOOKTIME";

    // Stall-Owner Menu Table
    private static final String OWNER_MENU_TABLE_NAME = "owner_menu_table";
    private static final String OWNER_MENU_COL_1 = "FOOD_NAME";
    private static final String OWNER_MENU_COL_2 = "O_STALLNAME";
    private static final String OWNER_MENU_COL_3 = "FOOD_PRICE";

    // Orders Table
    private static final String ORDERS_TABLE_NAME = "all_orders_table";
    private static final String ORDERS_COL_1 = "ORDER_ID";
    private static final String ORDERS_COL_2 = "FOOD_NAME";
    private static final String ORDERS_COL_3 = "U_USERNAME";
    private static final String ORDERS_COL_4 = "O_STALLNAME";
    private static final String ORDERS_COL_5 = "COLLECTION_TIME";
    private static final String ORDERS_COL_6 = "COMPLETED";


    // History Table
    private static final String OWNER_HISTORY_TABLE_NAME = "owner_history_table";
    private static final String USER_HISTORY_TABLE_NAME = "user_history_table";
    private static final String HISTORY_COL_1 = "ORDER_ID";
    private static final String HISTORY_COL_2 = "FOOD_NAME";
    private static final String HISTORY_COL_3 = "U_USERNAME";
    private static final String HISTORY_COL_4 = "O_STALLNAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); //create database and table
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_String = "CREATE TABLE " + USER_TABLE_NAME + "(" + USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_COL_2 + " TEXT," + USER_COL_3 + " TEXT," + USER_COL_4 +" INTEGER" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + OWNER_TABLE_NAME + "(" + OWNER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + OWNER_COL_2 + " TEXT," + OWNER_COL_3 + " TEXT,"
                + OWNER_COL_4 + " TEXT," + OWNER_COL_5 + " TEXT," +OWNER_COL_6 +" INTEGER, "+OWNER_COL_7 + " INTEGER" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + OWNER_MENU_TABLE_NAME + "(" + OWNER_MENU_COL_1 + " TEXT PRIMARY KEY," + OWNER_MENU_COL_2 + " TEXT,"+ OWNER_MENU_COL_3 + " INTEGER " + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + OWNER_HISTORY_TABLE_NAME + "(" + HISTORY_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + HISTORY_COL_2 + " TEXT," + HISTORY_COL_3 + " TEXT," + HISTORY_COL_4 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + USER_HISTORY_TABLE_NAME + "(" + HISTORY_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + HISTORY_COL_2 + " TEXT," + HISTORY_COL_3 + " TEXT," + HISTORY_COL_4 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + ORDERS_TABLE_NAME + "(" + ORDERS_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + ORDERS_COL_2 + " TEXT," + ORDERS_COL_3 + " TEXT,"
                + ORDERS_COL_4 + " TEXT," +ORDERS_COL_5 +" TEXT," + ORDERS_COL_6 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_MENU_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_HISTORY_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_HISTORY_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_MENU_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_HISTORY_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_HISTORY_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /////////////////////////////////////////// USER BALANCE ///////////////////////////////////////////

    public int getUserBalance(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + USER_COL_4 + " FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_2 + " = '" + username + "'",null);
        res.moveToNext();
        int bal = res.getInt(0);
        return bal;
    }

    public void updateUserBalance(String username, int new_amt) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("UPDATE " + USER_TABLE_NAME + " SET " + USER_COL_4 + " = '" + new_amt + "'" + " WHERE " + USER_COL_2 + " = '" + username + "'", null);
        res.moveToNext();
    }

    public int getFoodPrice(String foodname, String stallname) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + OWNER_MENU_COL_3 + " FROM " + OWNER_MENU_TABLE_NAME + " WHERE " + OWNER_MENU_COL_1 + " ='" + foodname + "' AND " + OWNER_MENU_COL_2 + " ='" + stallname + "'", null );
        res.moveToNext();
        int foodprice = res.getInt(0);
        return foodprice;
    }

    /////////////////////////////////////////// USER METHODS ///////////////////////////////////////////
    public boolean addUserAccount(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2,username);
        contentValues.put(USER_COL_3,password);
        contentValues.put(USER_COL_4, "0");
        long result = sqLiteDatabase.insert(USER_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public boolean saveUserAccount(User user){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_4, user.balance);
        return (sqLiteDatabase.update(USER_TABLE_NAME,contentValues,"U_ID = ?",new String[]{String.valueOf(user.id)}) > 0);
    }

    //check column index
    public String getBuyerUsername(String food_name,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + ORDERS_TABLE_NAME + " WHERE " + ORDERS_COL_2 + " = '" + food_name + "' AND " + ORDERS_COL_4 + " = '" + stall_name + "'",null);
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(2));
        }
        res.close();
        return buffer.toString();
    }

    public Cursor checkUserLoginData(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_2 + " = '" + username + "' AND " + USER_COL_3 + " = '" + password + "'",null);
        return res;
    }

    public boolean updateUserAccountUsername(String old_username,String new_username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2,new_username);
        return (sqLiteDatabase.update(USER_TABLE_NAME,contentValues,"U_USERNAME = ?",new String[]{old_username}) > 0);
    }

    public boolean updateUserAccountPassword(String old_password,String new_password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_3,new_password);
        return (sqLiteDatabase.update(USER_TABLE_NAME,contentValues,"U_PASSWORD = ?",new String[]{old_password}) > 0);
    }

    public Integer deleteUserAccount(String username,String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Integer temp;
        temp = sqLiteDatabase.delete(USER_TABLE_NAME,"U_USERNAME = ? AND U_PASSWORD = ?",new String[]{username,password});
        temp += sqLiteDatabase.delete(ORDERS_TABLE_NAME,"U_USERNAME = ?",new String[]{username});
        return temp;
    }

    /////////////////////////////////////////// OWNER METHODS ///////////////////////////////////////////
    public boolean addOwnerAccount(String username, String stall_name, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_2,username);
        contentValues.put(OWNER_COL_3,stall_name);
        contentValues.put(OWNER_COL_4,password);
        contentValues.put(OWNER_COL_6,"0");
        contentValues.put(OWNER_COL_7, "150");
        long result = sqLiteDatabase.insert(OWNER_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public String getOwnerUsername(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + OWNER_COL_2 + " FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_3 + " = '" + stall_name + "'",null);
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(0));
        }
        res.close();
        return buffer.toString();
    }

    public String getOwnerPassword(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + OWNER_COL_4 + " FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_3 + " = '" + stall_name + "'",null);
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(0));
        }
        res.close();
        return buffer.toString();
    }

    public String getStallName(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT " + OWNER_COL_3 + " FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_2 + " = '" + username + "' AND " + OWNER_COL_4 + " = '" + password + "'",null);
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(0));
        }
        res.close();
        return buffer.toString();
    }

    public boolean updateQueueNum(int queueNum,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_6,queueNum);
        return (sqLiteDatabase.update(OWNER_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{stall_name}) > 0);
    }

    public boolean updateOwnerAccountUsername(String username,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_2,username);
        return (sqLiteDatabase.update(OWNER_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{stall_name}) > 0);
    }

    public boolean updateOwnerAccountPassword(String password,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_4,password);
        return (sqLiteDatabase.update(OWNER_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{stall_name}) > 0);
    }

    public Integer updateOwnerAccountStallName(String old_stall_name,String new_stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_3,new_stall_name);
        Integer temp = sqLiteDatabase.update(OWNER_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{old_stall_name});
        temp += sqLiteDatabase.update(OWNER_MENU_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{old_stall_name});
        temp += sqLiteDatabase.update(ORDERS_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{old_stall_name});
        temp += sqLiteDatabase.update(OWNER_HISTORY_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{old_stall_name});
        temp += sqLiteDatabase.update(USER_HISTORY_TABLE_NAME,contentValues,"O_STALLNAME = ?",new String[]{old_stall_name});
        return temp;
    }

    public Integer deleteOwnerAccount(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Integer temp;
        temp = sqLiteDatabase.delete(OWNER_TABLE_NAME,"O_STALLNAME = ?",new String[]{stall_name});
        temp += sqLiteDatabase.delete(OWNER_MENU_TABLE_NAME,"O_STALLNAME = ?",new String[]{stall_name});
        temp += sqLiteDatabase.delete(ORDERS_TABLE_NAME,"O_STALLNAME = ?",new String[]{stall_name});
        temp += sqLiteDatabase.delete(OWNER_HISTORY_TABLE_NAME,"O_STALLNAME = ?",new String[]{stall_name});
        return temp;
    }

    public Cursor checkOwnerLoginData(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_2 + " = '" + username + "' AND " + OWNER_COL_4 + " = '" + password + "'",null);
        return res;
    }

    public Cursor isStallNameAcceptable(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_3 + " = '" + stall_name + "'",null);
        return res;
    }

    public Stall[] getArrayOfStall(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_TABLE_NAME,null);
        Stall[] stallList = new Stall[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            stallList[i] = new Stall(res.getInt(0),res.getString(2),res.getInt(5 ));
            i++;
        }
        res.close();
        return stallList;
    }

    public Food[] getStallMenu(String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_MENU_TABLE_NAME + " WHERE " + OWNER_MENU_COL_2 + " = '" + stallName + "'",null);
        Food[] foodList = new Food[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            foodList[i] = new Food(res.getString(0), res.getInt(2));
            i++;
        }
        res.close();
        return foodList;
    }

    public Integer deleteMenuArrayData(String food_name, String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(OWNER_MENU_TABLE_NAME,"FOOD_NAME = ? AND O_STALLNAME = ?",new String[]{food_name,stall_name});
    }

    public boolean addMenuArrayData(String foodName, String stallName, int price){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_MENU_COL_1,foodName);
        contentValues.put(OWNER_MENU_COL_2,stallName);
        contentValues.put(OWNER_MENU_COL_3, price);
        long result = sqLiteDatabase.insert(OWNER_MENU_TABLE_NAME,null,contentValues);
        return (result != -1);
    }


    /////////////////////////////////////////// ORDER METHODS ///////////////////////////////////////////
    public Order[] getUserArrayOfOrders(String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + ORDERS_TABLE_NAME
                        + " WHERE " + ORDERS_COL_3 + " = '" + username + "'"
                        +  " ORDER BY " + ORDERS_COL_1 + " ASC;",null);
        Order[] orderList = new Order[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            orderList[i] = new Order(res.getInt(0), res.getString(1),res.getString(2), res.getString(3),res.getString(4)
            , (res.getString(5).equals("y"))? true: false);
            i++;
        }
        res.close();
        return orderList;
    }

    public Order[] getArrayOfOrders(String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery(
                "SELECT * FROM " + ORDERS_TABLE_NAME
                    + " WHERE " + ORDERS_COL_4 + " = '" + stallName + "'"
                        + " ORDER BY " + ORDERS_COL_1 + " ASC;",null);
        Order[] orderList = new Order[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            orderList[i] = new Order(res.getInt(0), res.getString(1),res.getString(2), res.getString(3),res.getString(4)
                    , (res.getString(5).equals("y"))? true: false);
            i++;
        }
        res.close();
        return orderList;
    }

    public boolean addOrderArrayData(String foodName, String username,String stallName, String collectionTime){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COL_2,foodName);
        contentValues.put(ORDERS_COL_3,username);
        contentValues.put(ORDERS_COL_4,stallName);
        contentValues.put(ORDERS_COL_5,collectionTime);
        contentValues.put(ORDERS_COL_6, "n");
        long result = sqLiteDatabase.insert(ORDERS_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public boolean updateOrder(String food_name, String stall_name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ORDERS_COL_6, "y");
        long result = sqLiteDatabase.update(ORDERS_TABLE_NAME,contentValues,"FOOD_NAME = ? AND O_STALLNAME = ?",new String[]{food_name,stall_name});
        return !(result == -1);
    }

    public Integer deleteOrderArrayData(String food_name, String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(ORDERS_TABLE_NAME,"FOOD_NAME = ? AND O_STALLNAME = ?",new String[] {food_name,stall_name});
    }

    /////////////////////////////////////////// HISTORY METHODS ///////////////////////////////////////////
    public String[] getUserArrayOfHistory(String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_HISTORY_TABLE_NAME + " WHERE " + HISTORY_COL_3 + " = '" + username + "'" + " ORDER BY " + HISTORY_COL_1 + " ASC;",null);
        String[] stringList = new String[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            stringList[i] = res.getString(1);
            i++;
        }
        res.close();
        return stringList;
    }

    public String[] getArrayOfHistory(String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_HISTORY_TABLE_NAME + " WHERE " + HISTORY_COL_4 + " = '" + stallName + "'" + " ORDER BY " + HISTORY_COL_1 + " ASC;",null);
        String[] stringList = new String[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            stringList[i] = res.getString(1);
            i++;
        }
        res.close();
        return stringList;
    }

    public void addUserHistoryArrayData(String foodName, String username,String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COL_2,foodName);
        contentValues.put(HISTORY_COL_3,username);
        contentValues.put(HISTORY_COL_4,stallName);
        sqLiteDatabase.insert(USER_HISTORY_TABLE_NAME,null,contentValues);
    }

    public void addHistoryArrayData(String foodName, String username,String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HISTORY_COL_2,foodName);
        contentValues.put(HISTORY_COL_3,username);
        contentValues.put(HISTORY_COL_4,stallName);
        sqLiteDatabase.insert(OWNER_HISTORY_TABLE_NAME,null,contentValues);
    }

    public Integer deleteUserHistoryArrayData(String food_name, String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(USER_HISTORY_TABLE_NAME,"FOOD_NAME = ? AND U_USERNAME = ?",new String[] {food_name,username});
    }

    public Integer deleteAllUserHistoryData(String username){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(USER_HISTORY_TABLE_NAME,"U_USERNAME = ?",new String[]{username});
    }

    public Integer deleteHistoryArrayData(String food_name, String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(OWNER_HISTORY_TABLE_NAME,"FOOD_NAME = ? AND O_STALLNAME = ?",new String[] {food_name,stall_name});
    }

    public Integer deleteAllHistoryData(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(OWNER_HISTORY_TABLE_NAME,"O_STALLNAME = ?",new String[]{stall_name});
    }
}
