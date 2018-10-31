package com.example.yoshizumiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SubOpenHelper extends SQLiteOpenHelper {
    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE cityTable(_id INTEGER PRIMARY KEY, cityName TEXT, prefName TEXT, population INTEGER,"
            +"schoolCount INTEGER, stationCount INTEGER, crimePer DOUBLE, x DOUBLE, y DOUBLE);";

    public SubOpenHelper(Context c, String dbName, int version){
        super(c, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ENTRIES);
            Log.d("SQL", "create");
        }catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS cityTable");
                db.execSQL(SQL_CREATE_ENTRIES);
                Log.d("SQL", "update");
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }
    }

}
