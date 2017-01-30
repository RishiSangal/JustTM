package com.android.encypher.justtrackme.activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EmergencyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e("Emergency Contact", e.toString());
        }
        logoutPopup();
    }

    private void logoutPopup() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Send Help");
        alertDialogBuilder.setMessage("Are you sure? ");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
                                String lat = sharedPreferences.getString("lat", "Location did not fatched");
                                String lon = sharedPreferences.getString("lon", "Location did not fatched");
                                String user = sharedPreferences.getString("userId", "not found");
                                getDAta(getResources().getString(R.string.url) + "contactEmergency&user_id=" + user + "&long=" + lon + "lat=" + lat);
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                EmergencyActivity.this.finishAffinity();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void getDAta(String s) {

        final ProgressDialog pDialog = new ProgressDialog(this);

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("Emergency Contact=", s);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.e("response in Emergency", response);
                try {
                    Log.e("response in emergency ", new JSONObject(response).getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error in myLocation", e.toString());
                }
                pDialog.hide();
                EmergencyActivity.this.finishAffinity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(EmergencyActivity
                        .this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(strReq);
    }
}
