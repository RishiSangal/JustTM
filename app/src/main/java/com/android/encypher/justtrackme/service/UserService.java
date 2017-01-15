package com.android.encypher.justtrackme.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by root on 20/6/16.
 */
public class UserService extends Service {


    private Looper mServiceLooper;
    private boolean isRunning = false;
    ServiceHandler mServiceHandler;
    private long clock;
    String userId;
    String places = "Not found";
    private String type;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {

        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Thread.MAX_PRIORITY);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        timeStatus();
        batteryStatus();
        androidId();
        sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("userId", "0");
        type = sharedPreferences.getString("type", "0");
        scheduleAlarm();

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    public void scheduleAlarm() {
        Log.e("AlarmService", "Alarm service started in on create method");

        Long time = new GregorianCalendar().getTimeInMillis() + 60 * 1000;

        // create an Intent and set the class which will execute when Alarm triggers, here we have
        // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
        // alarm triggers and
        //we will write the code to send SMS inside onRecieve() method pf Alarmreciever class
        Intent intentAlarm = new Intent(getBaseContext(), BootCompleteReceiver.class);

        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time,60000, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
        //  Toast.makeText(this, "Alarm Scheduled for Tommrrow", Toast.LENGTH_LONG).show();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        realResult=0;

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();
        Log.e("x===",realResult+" "+sharedPreferences.getString("gs","fffgg"));

        if ((!userId.equals("0")) && (realResult==0) &&  (sharedPreferences.getString("gs","").equals("public"))) {
           Log.e("service ","on start commond");
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(statusOfGPS){

                gpsLocations();
            }else if (isInternetPresent) {


                LocationManager lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                boolean netEnabled = lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                locationListener = new MyLocationListener();

                if (netEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    }

                    Log.e("gps data from network", "true");
                    lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300000, 10, locationListener);



                }
            }
        }else{
            Log.e("global sharing is off",realResult+"");
        }



        return START_STICKY;
    }

    MyLocationListener locationListener;

    private void gpsLocations() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10, locationListener);
    }

    double lat, lon;
    int i;
    String android_id;
    int battery;

    private void androidId() {
        android_id = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("android_id", android_id);

    }

    int realResult;
    private long timeStatus() {
        clock=0;
        clock = System.currentTimeMillis() / 1000L;
        Log.e("clock", "" + clock);
        return clock;

    }

    private int batteryStatus() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getBaseContext().registerReceiver(null, ifilter);
        assert batteryStatus != null;
        battery = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        Log.e("Battery", "" + battery);
        return batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

    }

    private class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

//
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.e("longitude", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.e("latitude", latitude);
            i = 1;


            if (lat == 0.0) {



//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        gpsLocations();
//                    }
//                }, 35000);
            } else {

                places = getPlaces(lat, lon);

                editor.putString("lat", "" + lat);
                editor.putString("lon", "" + lon);
                editor.putString("battery", "" + batteryStatus());
                editor.putString("clock", "" + timeStatus());
                editor.putString("places", places);
                editor.apply();

                if((sharedPreferences.getString("gs","").equals("public"))) {
                    saveUserInfo();
                    new ABC().execute();


                }
            }


        }



        @Override
        public void onProviderDisabled(String provider) {


//            showNotification();

            saveGpsInfo("off");


        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("piyush", "Enable");
            saveGpsInfo("on");

        }

        public void showNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setSmallIcon(R.drawable.logo);

            builder.setContentTitle("Your GPS Location in OFF");
            builder.setContentText("Please turn ON your GPS.");
            builder.setAutoCancel(true);
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());
//            notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;

        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void saveUserInfo() {
        realResult=1;
        MyDatabase db = new MyDatabase(getBaseContext(), "locationData", null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues gpsData = new ContentValues();
        gpsData.put("name", "normal");
        gpsData.put("latitude", lat);
        gpsData.put("longitude", lon);
        gpsData.put("userid", userId);
        gpsData.put("battery", "" + batteryStatus());
        gpsData.put("timestamp", "" + timeStatus());
        gpsData.put("place", places);
        gpsData.put("type", type);

        long result = sql.insert("myTable", "abc", gpsData);

        if (result > 0) {
            Log.e("Location Data", "User data is store in sqlite");
            realResult=1;
            sql.close();

        } else {
            Log.e("Location Data", "user Location is not store in sqlite, some error occur");

        }

    }

    public void saveGpsInfo(String status) {

        MyDatabase db = new MyDatabase(getBaseContext(), "locationData", null, 1);
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues gpsData = new ContentValues();
        gpsData.put("user_id", userId);
        gpsData.put("gpsStatus", status);
        gpsData.put("timestamp", timeStatus());

        long result = sql.insert("gpsTable", "abc", gpsData);
        if (result > 0) {
            Log.e("gps data", "realgpsdata is store in sqlite");
            sql.close();

        } else {
            Log.e("gps data", "Real data is not store in sqlite, some error occur");

        }
    }

    String urlParameters;
    String str;

    public class ABC extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            str = getResources().getString(R.string.url) + "addJsonLocation";


            Log.e("ABS", "going for network");



        }

        void sendData() {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest strReq = new StringRequest(Request.Method.POST, str
                    , new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e("response", response);


                    try {
                        if (new JSONObject(response).getString("success").equals("1")) {

                            Log.e("data status=", "deleted");
                            deleteDb("myTable");

                        } else {

                            Log.e("data status=", "not deleted");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: " + error.getMessage());

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new Hashtable<String, String>();
                    try {
                        params.put("user_id", userId);
                        params.put("json", jsonArray.toString());

                    } catch (Exception e) {
                        params.put("user_id", userId);
                        params.put("json", "");
                        e.printStackTrace();
                    }


                    return params;
                }
            };

            queue.add(strReq);
        }

        JSONArray jsonArray;

        private void retrieveData() throws JSONException {


            MyDatabase db1 = new MyDatabase(getBaseContext(), "locationData", null, 1);
            SQLiteDatabase sql1 = db1.getWritableDatabase();

            Cursor c = sql1.rawQuery("Select DISTINCT * from myTable WHERE id > -1;", null);
            if (c.getCount() > 0) {

                JSONObject gpsDat = new JSONObject();
                jsonArray = new JSONArray();

                while (c.moveToNext()) {

                    try {
                        gpsDat = new JSONObject();
                        gpsDat.put("latitude", c.getString(c.getColumnIndex("latitude")));
                        gpsDat.put("longitude", c.getString(c.getColumnIndex("longitude")));
                        gpsDat.put("timestamp", c.getString(c.getColumnIndex("timeStamp")));
                        gpsDat.put("userid", c.getString(c.getColumnIndex("userid")));
                        gpsDat.put("battery", c.getString(c.getColumnIndex("battery")));
                        gpsDat.put("place", c.getString(c.getColumnIndex("place")));
                        gpsDat.put("type", c.getString(c.getColumnIndex("type")));
                        gpsDat.put("name", c.getString(c.getColumnIndex("name")));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(gpsDat);
                }
                urlParameters = "user_id=" + userId + "&json=" + jsonArray.toString();
            }
            c.close();
            sql1.close();
            db1.close();
            Log.e("gps data url param", urlParameters);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                retrieveData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendData();
            sendGPSData(getResources().getString(R.string.url) + "savegpslogJson");
            sendNetData(getResources().getString(R.string.url)+"savenetstatjson");



            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    public String getPlaces(double lat, double lon) {

        Geocoder geocoder;
        String str;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            str = addresses.get(0).getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    + " " + addresses.get(0).getLocality()
                    + " " + addresses.get(0).getAdminArea()
                    + " " + addresses.get(0).getCountryName();
//           +addresses.get(0).getPostalCode();
            if (str.equals(" ")) {
                str = addresses.get(0).getFeatureName();
            }// Only if available else return NULL

        } catch (IOException e) {
            str = "Location Not Found";
            e.printStackTrace();
        }


        return str;
    }

    public String retrieveGps() {


        MyDatabase db1 = new MyDatabase(getBaseContext(), "locationData", null, 1);
        SQLiteDatabase sql1 = db1.getWritableDatabase();

        Cursor c = sql1.rawQuery("Select DISTINCT * from gpsTable WHERE id > -1;", null);
        if (c.getCount() > 0) {

            JSONObject gpsDat = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            while (c.moveToNext()) {
                try {
                    gpsDat = new JSONObject();
                    gpsDat.put("gpsstat", c.getString(c.getColumnIndex("gpsStatus")));
                    gpsDat.put("timestamp", c.getString(c.getColumnIndex("timestamp")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(gpsDat);
            }
            urlParameters = "user_id=" + userId + "&json=" + jsonArray.toString();
            Log.e("gpsParameter",urlParameters);
        }
        c.close();
        sql1.close();
        db1.close();
//        Log.e("gps data", urlParameters);

        return urlParameters;

    }
    public String retrieveNet() {


        MyDatabase db1 = new MyDatabase(getBaseContext(), "locationData", null, 1);
        SQLiteDatabase sql1 = db1.getWritableDatabase();

        Cursor c = sql1.rawQuery("Select DISTINCT * from netTable WHERE id > -1;", null);
        if (c.getCount() > 0) {

            JSONObject gpsDat = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            while (c.moveToNext()) {
                try {
                    gpsDat = new JSONObject();
                    gpsDat.put("netstat", c.getString(c.getColumnIndex("netStatus")));
                    gpsDat.put("timestamp", c.getString(c.getColumnIndex("timestamp")));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(gpsDat);
            }
            urlParameters = "user_id=" + userId + "&json=" + jsonArray.toString();
            Log.e("netStatus log",urlParameters);

        }
        c.close();
        sql1.close();
        db1.close();
//        Log.e("gps data", urlParameters);

        return urlParameters;

    }
    public void deleteDb(String tableName) {

        MyDatabase db2 = new MyDatabase(getBaseContext(), "locationData", null, 1);
        SQLiteDatabase sql2 = db2.getWritableDatabase();
        sql2.execSQL("delete FROM "+ tableName +" WHERE id > -1;");
        sql2.delete("myTable", null, null);
        urlParameters = "";
        sql2.close();
        db2.close();


    }
    void sendGPSData(String url) {

        final String json=retrieveGps();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.e("saveGPSURL",url);

        StringRequest strReq = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response in gps data", response);


                try {
                    if (new JSONObject(response).getString("success").equals("1")) {

                        Log.e("data status=", "deleted");
                        deleteDb("gpsTable");
                    } else {

                        Log.e("data status=", "not deleted");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                try {
                    params.put("user_id", userId);
                    params.put("json", json);

                } catch (Exception e) {
                    params.put("user_id", userId);
                    params.put("json", "");
                    e.printStackTrace();
                }


                return params;
            }
        };

        queue.add(strReq);
    }
    void sendNetData(String url) {

        final String json=retrieveNet();
        Log.e("saveNetURL",url);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response in net Data", response);


                try {
                    if (new JSONObject(response).getString("success").equals("1")) {

                        Log.e("data status=", "deleted");
                        deleteDb("netTable");
                    } else {

                        Log.e("data status=", "not deleted");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                try {
                    params.put("user_id", userId);
                    params.put("json", json);

                } catch (Exception e) {
                    params.put("user_id", userId);
                    params.put("json", "");
                    e.printStackTrace();
                }


                return params;
            }
        };

        queue.add(strReq);
    }

}
