package com.android.encypher.justtrackme.service;

/**
 * Created by root on 20/6/16.
 */
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL("create table gpsTable (id integer primary key autoincrement, user_id Text, gpsStatus Text,timestamp Text);");
            db.execSQL("create table netTable (id integer primary key autoincrement, user_id Text, netStatus Text,timestamp Text);");

            db.execSQL("create table myTable (id integer primary key autoincrement, latitude Text, longitude Text,userid Text, battery Text,timeStamp Text,place Text,type Text,name Text);");
        }catch (SQLException e){
            e.printStackTrace();
            Log.e("creating table ", e.toString());
            // Toast.makeText()
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
