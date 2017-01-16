package com.android.encypher.justtrackme.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;
import com.android.encypher.justtrackme.service.UserService;

public class SmartActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    int x=0,y=0;
    boolean statusOfGPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart);
        permissionCheck();
        Button btn= (Button) findViewById(R.id.smartContinue);
        assert btn != null;
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(x==1){
                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
                    boolean  statusOfGPS1 = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if(statusOfGPS1){

                        startActivity(new Intent(SmartActivity.this, HomeActivity.class));
                        finish();

                    }else{
                        Toast.makeText(SmartActivity.this,"Turn on the GPS",Toast.LENGTH_LONG).show();
                    }
                }else{
                    startActivity(new Intent(SmartActivity.this,SmartActivity.class));
                }

            }
        });


        if(statusOfGPS){
            y=1;
            permissionCheck();
        }else{
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

    }

    public void permissionCheck() {

        if ((int) Build.VERSION.SDK_INT > 22) {
            int permissionCheck1 = ContextCompat.checkSelfPermission(SmartActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            int permissionCheck2 = ContextCompat.checkSelfPermission(SmartActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 ==
                    PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(SmartActivity.this,
                    Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SmartActivity.this,
                    Manifest.permission.WRITE_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.e("hey", "permission ififi granted");
//                Intent in1 = new Intent(SmartActivity.this, UserService.class);
//                startService(in1);
                x=1;
                y=1;
//                if(y==1){
//                        startActivity(new Intent(SmartActivity.this, HomeActivity.class));
//                        finish();
//                    }else{
//                        Toast.makeText(SmartActivity.this,"Turn on the GPS",Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(SmartActivity.this,SmartActivity.class));
//                    }

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                                ,Manifest.permission.READ_CONTACTS,
                                Manifest.permission.WRITE_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            Log.e("hey", "permission  granted in smart");
        }else if(y==1){
            startActivity(new Intent(SmartActivity.this, HomeActivity.class));
            finish();

        }else{
            Toast.makeText(SmartActivity.this,"Turn on the GPS",Toast.LENGTH_LONG).show();
            startActivity(new Intent(SmartActivity.this,SmartActivity.class));
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("hey", "permission granted");
                    x=1;
                    Toast.makeText(SmartActivity.this, "Permission is Granted", Toast.LENGTH_SHORT).show();
                    Intent in1 = new Intent(SmartActivity.this, UserService.class);
                    startService(in1);
                } else {
                    Toast.makeText(SmartActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

}
