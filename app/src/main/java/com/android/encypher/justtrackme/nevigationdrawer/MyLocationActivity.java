package com.android.encypher.justtrackme.nevigationdrawer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.activities.LocationHistoryActivity;
import com.android.encypher.justtrackme.service.ConnectionDetector;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap googleMap;
    MyLocationListener locationListener;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    TextView gpsE;
    String url = "";
    double lat = 0.0, lon = 0.0;
    ArrayList<LatLng> points = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    FloatingActionButton extra;
   String data2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_my_location);

        data2 = getIntent().getExtras().getString("memberdata");
        Log.e("Data in myLocation",data2);
        extra = (FloatingActionButton) findViewById(R.id.extra);
        assert extra != null;
        extra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MyLocationActivity.this, LocationHistoryActivity.class);
                in.putExtra("Data", url);
                in.putExtra("timeStamp",""+l);
                if(data2.equals("home")){
                    data2=sharedPreferences.getString("userId",null);
                }
                in.putExtra("memid",data2);
                startActivity(in);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.Date);

        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.e("Gps home", statusOfGPS + "");
        gpsE = (TextView) findViewById(R.id.textView16);

        if (!statusOfGPS) {

            gpsE.setVisibility(View.VISIBLE);

        } else {
            gpsE.setVisibility(View.INVISIBLE);

        }

        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });

        sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        try {
            getSupportActionBar().show();
            getSupportActionBar().setTitle("Location");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        } catch (NullPointerException e) {
            Log.e("hey", e.toString());
        }

        progressDialog = new ProgressDialog(MyLocationActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myLocationfragment);
        supportMapFragment.getMapAsync(MyLocationActivity.this);
        lLocation();


    }

    private void gpsLocations() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 10, locationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }


    Date dat;
    private long timeStatus(String dat1) {
//        String str_date="07"+"-"+"06"+"-"+"2016";
        String str_date=dat1;
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            dat = formatter.parse(str_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            System.out.print(e.toString());
        }
        long output=dat.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        return timestamp;
    }

    private class MyLocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

//
            double lat=loc.getLatitude();
            double lon=loc.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
            points.add(latLng);
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            progressDialog.hide();
            lLocation();

        }

        @Override
        public void onProviderDisabled(String provider) {


            Log.e("piyush","Gps is disable");
//            showNotification();
//            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("piyush", "Enable");
            Toast.makeText(getBaseContext(), "Gps is turned on!!! ", Toast.LENGTH_SHORT).show();
        }
        public void showNotification(){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("Your GPS Location in OFF");
            builder.setContentText("Please turn ON your GPS Location settings");
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(0, builder.build());

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public void lLocation() {

        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        Boolean isInternetPresent = cd.isConnectingToInternet();

        Log.e("helllo","Heloo");

        if (isInternetPresent) {
            LocationManager lManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean netEnabled = lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            locationListener = new MyLocationListener();
            if (netEnabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 40000, 10, locationListener);

            }
        }else {
            gpsLocations();
        }
    }

    public void getDAta(String s) {

        final ProgressDialog pDialog=new ProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("myLocation url=",s);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                Log.e("response in my location",response);
                gpsE.setVisibility(View.INVISIBLE);


                String res = null;

                try {
                    res=new JSONObject(response).getString("message");
                    Log.e( "response in myLocations",new JSONObject(response).getString("message"));

                    PolylineOptions lineOptions = null;
                    MarkerOptions markerOptions = new MarkerOptions();
                    JSONArray ja=new JSONArray(new JSONObject(response).getString("checkins"));
                    googleMap.clear();

                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        lat=Double.parseDouble(jo.getString("latitude"));
                        lon=Double.parseDouble(jo.getString("longitude"));
                        time.add(jo.getString("timestamp"));
                       location.add(jo.getString("place"));
//                        time.add(jo.getString("timestamp"));
                        LatLng latLng = new LatLng(lat, lon);
//                        Log.e("LatLonin my location",latLng.toString());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(/*location.get(i)+"\n"+*/time.get(i)));
                        points.add(latLng);
                    }
                    lineOptions = new PolylineOptions();
                    lineOptions.addAll(points);
                    lineOptions.width(2);
                    lineOptions.color(Color.BLACK);
                    googleMap.addPolyline(lineOptions);
                    pDialog.hide();
                    points.clear();
                    extra.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error in myLocation",e.toString());
                    pDialog.hide();
                    gpsE.setVisibility(View.VISIBLE);
                    gpsE.setText(res);

                }





            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(MyLocationActivity
                .this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);
    }



    public void getDate(){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String str[]=date.split("-");
        year=Integer.parseInt(str[0]);
        month=Integer.parseInt(str[1])-1;
        day=Integer.parseInt(str[2]);

    }


    private int year=2016;
    private int month=06;
    private int day=06;
    static final int DATE_PICKER_ID = 1111;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                getDate();
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }
    long l;
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;
            StringBuilder builder=new StringBuilder().append(month + 1)
                    .append("-").append(day+1).append("-").append(year)
                    .append(" ");
             l=timeStatus(builder.toString())/1000;
            Log.e("timeStamp=",""+l);
            if(data2.equals("home")) {
                url = getResources().getString(R.string.url) + "myhistory&user_id=" + sharedPreferences.getString("userId", "null") + "&timestamp=" + "" + l + "&limit=1000&offset=0";
            }else if(!(sharedPreferences.getString("userId",null).equals(data2))){
                url = getResources().getString(R.string.url) + "memberhistory&user_id=" + sharedPreferences.getString("userId", "null") + "&timestamp=" + "" + l + "&limit=1000&offset=0&memberid="+data2;

            }else {
                url = getResources().getString(R.string.url) + "myhistory&user_id=" + sharedPreferences.getString("userId", "null") + "&timestamp=" + "" + l + "&limit=1000&offset=0";

            }
                getDAta(url);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if(id==R.id.home){
            super.onBackPressed();
             return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
