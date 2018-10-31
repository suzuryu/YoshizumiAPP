package com.example.yoshizumiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class SQLiteManager {
    private Context context;
    private SubOpenHelper subHelper;
    private static final String dbName = "townData.db";
    private static final String tableName = "cityTable";

    SQLiteManager(Context c) {
        this.context = c;
        this.subHelper = new SubOpenHelper(context,  dbName, 7);
    }

    private  SQLiteDatabase getWRDatabase(){
        return subHelper.getWritableDatabase();
    }

    private SQLiteDatabase getRDatabase(){
        try{
            subHelper.getReadableDatabase();
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        return subHelper.getReadableDatabase();
    }

    private TownData townDataFromCursor(Cursor c){
        TownData td = new TownData();
        if(c.moveToFirst()){
            td.setCityName(c.getString(c.getColumnIndex("cityName")));
            td.setPrefecture(c.getString(c.getColumnIndex("prefName")));
            td.setPopulation(c.getInt(c.getColumnIndex("population")));
            td.setSchoolCount(c.getInt(c.getColumnIndex("schoolCount")));
            td.setStationCount(c.getInt(c.getColumnIndex("stationCount")));
            td.setCrimePer(c.getDouble(c.getColumnIndex("crimePer")));
            td.setX(c.getDouble(c.getColumnIndex("x")));
            td.setY(c.getDouble(c.getColumnIndex("y")));
        }
        c.close();

        return td;
    }

    public ArrayList<TownData> townDataFromCursor30(Cursor c){

        ArrayList<TownData> datas = new ArrayList<>();
        c.moveToFirst();
        for(int i=0;i < 30;i++){
            TownData td = new TownData();
            td.setCityName(c.getString(c.getColumnIndex("cityName")));
            td.setPrefecture(c.getString(c.getColumnIndex("prefName")));
            td.setPopulation(c.getInt(c.getColumnIndex("population")));
            td.setSchoolCount(c.getInt(c.getColumnIndex("schoolCount")));
            td.setStationCount(c.getInt(c.getColumnIndex("stationCount")));
            td.setCrimePer(c.getDouble(c.getColumnIndex("crimePer")));
            td.setX(c.getDouble(c.getColumnIndex("x")));
            td.setY(c.getDouble(c.getColumnIndex("y")));

            datas.add(td);
            c.moveToNext();
        }

        c.close();

        return datas;
    }

    public TownData queryByCityName(String cityName){
        String[] selectionArgs = {cityName};
        try {
            Cursor c = this.getRDatabase().query(tableName, null, "cityName = ?", selectionArgs, null, null, null);
            return townDataFromCursor(c);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return new TownData();
    }

    public ArrayList<TownData> queryByHighOrLowTop30(String target,String highOrLow){
        /*
            target <= population | schoolCount | stationCount | crimePer
         */
        String orderBy = target + " ";

        if(highOrLow == "high"){
            orderBy += "DESC";
        }else if(highOrLow == "low"){
            orderBy += "ASC";
        }
        try {
            Cursor c = this.getRDatabase().query(tableName, null, null, null, null, null, orderBy);
            return townDataFromCursor30(c);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return new ArrayList<TownData>();
    }

    public ArrayList<TownData> queryByHighOrLowTop30(){
        Cursor c = this.getRDatabase().query(tableName, null, null, null, null, null, null);
        return townDataFromCursor30(c);
    }

    public void insertData(ContentValues cv){
        long id = this.getWRDatabase().insert(tableName,  null, cv);
        if(id < 0){
            Log.d("SQL","could not insert data");
        }
    }
}
