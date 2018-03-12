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

    // Stall-Owner Table
    private static final String OWNER_TABLE_NAME = "owner_table";
    private static final String OWNER_COL_1 = "O_ID";
    private static final String OWNER_COL_2 = "O_USERNAME";
    private static final String OWNER_COL_3 = "O_STALLNAME";
    private static final String OWNER_COL_4 = "O_PASSWORD";

    // Stall-Owner Menu Table
    private static final String OWNER_MENU_TABLE_NAME = "owner_menu_table";
    private static final String OWNER_MENU_COL_1 = "FOOD_NAME";
    private static final String OWNER_MENU_COL_2 = "O_STALLNAME";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase(); //create database and table
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_String = "CREATE TABLE " + USER_TABLE_NAME + "(" + USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_COL_2 + " TEXT," + USER_COL_3 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + OWNER_TABLE_NAME + "(" + OWNER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + OWNER_COL_2 + " TEXT," + OWNER_COL_3 + " TEXT," + OWNER_COL_4 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
        SQL_String = "CREATE TABLE " + OWNER_MENU_TABLE_NAME + "(" + OWNER_MENU_COL_1 + "TEXT PRIMARY KEY," + OWNER_MENU_COL_2 + " TEXT" + ")";
        sqLiteDatabase.execSQL(SQL_String);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OWNER_MENU_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean createUserAccount(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2,username);
        contentValues.put(USER_COL_3,password);
        long result = sqLiteDatabase.insert(USER_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public boolean createOwnerAccount(String username,String stall_name, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_COL_2,username);
        contentValues.put(OWNER_COL_3,stall_name);
        contentValues.put(OWNER_COL_4,password);
        long result = sqLiteDatabase.insert(OWNER_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public boolean createOwnerMenu(String food_name,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_MENU_COL_1,food_name);
        contentValues.put(OWNER_MENU_COL_2,stall_name);
        long result = sqLiteDatabase.insert(OWNER_MENU_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }

    public Cursor checkUserLoginData(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + USER_COL_2 + " = '" + username + "' AND " + USER_COL_3 + " = '" + password + "'",null);
        return res;
    }

    public Cursor checkOwnerLoginData(String username, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_2 + " = '" + username + "' AND " + OWNER_COL_4 + " = '" + password + "'",null);
        return res;
    }

    public boolean isLoginAcceptable(String username, String password){
        return !(username.isEmpty() || password.isEmpty());
    }

    public boolean isUserRegisterAcceptable(String username, String password, String confirm_password){
        return !(username.isEmpty() || password.isEmpty() || confirm_password.isEmpty());
    }

    public boolean isOwnerRegisterAcceptable(String username,String stall_name, String password, String confirm_password){
        return !(username.isEmpty() || stall_name.isEmpty() || password.isEmpty() || confirm_password.isEmpty());
    }

    public Cursor isStallNameAcceptable(String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_TABLE_NAME + " WHERE " + OWNER_COL_3 + " = '" + stall_name + "'",null);
        return res;
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

    public String[] getArrayOfFood(String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM " + OWNER_MENU_TABLE_NAME + " WHERE " + OWNER_MENU_COL_2 + " = '" + stallName + "'",null);
        String[] stringList = new String[res.getCount()];
        int i = 0;
        while(res.moveToNext()){
            stringList[i] = res.getString(0);
            i++;
        }
        res.close();
        return stringList;
    }

    public Integer deleteArrayData(String food_name,String stall_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(OWNER_MENU_TABLE_NAME,"FOOD_NAME = ? AND O_STALLNAME = ?",new String[]{food_name,stall_name});
    }

    public boolean addArrayData(String foodName, String stallName){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OWNER_MENU_COL_1,foodName);
        contentValues.put(OWNER_MENU_COL_2,stallName);
        long result = sqLiteDatabase.insert(OWNER_MENU_TABLE_NAME,null,contentValues);
        return !(result == -1);
    }
}
