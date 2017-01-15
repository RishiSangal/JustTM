package com.android.encypher.justtrackme.utility;

/**
 * Created by root on 17/6/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.encypher.justtrackme.service.MyDatabase;

public class Utility {

    public Utility(Context con, String status) {

        SharedPreferences sharedPreferences = con.getSharedPreferences("piyush", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");

      if("public".equals(sharedPreferences.getString("gs","0"))) {
          MyDatabase db = new MyDatabase(con, "locationData", null, 1);
          SQLiteDatabase sql = db.getWritableDatabase();

          ContentValues gpsData = new ContentValues();
          gpsData.put("user_id", userId);
          gpsData.put("netStatus", status);
          gpsData.put("timestamp", timeStatus() + "");
          long result = sql.insert("netTable", "abc", gpsData);

          if (result > 0) {
              Log.e("net data", "data is store in sqlite");
              sql.close();

          } else {
              Log.e("Net data", "Location is not store in sqlite, some error occur");

          }
      }


    }

    public long timeStatus() {

        return System.currentTimeMillis() / 1000L;

    }
}
