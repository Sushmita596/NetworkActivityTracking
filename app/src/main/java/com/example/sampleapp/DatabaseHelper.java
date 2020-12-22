package com.example.sampleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NetworkActivity.DB";
    public static final String TABLE_NAME = "AppUsageTable";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "creationTime";
    public static final String COL_3 = "updatedTime";
    public static final String COL_4  = "events";


    //-------------------------------DataBase Table Creation Declaration----------------------------------------------------------------------------------
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID TEXT PRIMARY KEY,creationTime INTEGER,updatedTime INTEGER,events TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //----------------DataBase Create or Upload Method-------------------------------------------------------------------------------------------------
    public boolean insertData(String id, long creationtime, long updatetime, String events){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,creationtime);
        contentValues.put(COL_3,updatetime);
        contentValues.put(COL_4,events);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    //-----------------DataBase Update Method-----------------------------------------------------------------------------------------------------
    public boolean updateData(String id, long creationtime, long updatetime, String events){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,creationtime);
        contentValues.put(COL_3,updatetime);
        contentValues.put(COL_4,events);
        db.update(TABLE_NAME,contentValues,"ID = ?",new String[] { id });
        return true;
    }

    //----------------------------Getting Data from DataBase Table----------------------------------
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
}

