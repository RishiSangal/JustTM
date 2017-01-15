package com.android.encypher.justtrackme.service;

/**
 * Created by root on 20/6/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.encypher.justtrackme.utility.Utility;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent service = new Intent(context, UserService.class);
            context.startService(service);
            Log.e("after boot=", "Service started");

        } else if ("StartkilledService".equals(intent.getAction())) {

            Intent service = new Intent(context, UserService.class);
            context.startService(service);
            Log.e("after destroy=", "Service started");

        } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {

            boolean b = new ConnectionDetector(context).isConnectingToInternet();
            if ("public".equals(context.getSharedPreferences("piyush", Context.MODE_PRIVATE).getString("gs", "0"))) {
                Log.e("after boot=", "netStatus" + b);
                if (b) {

                    new Utility(context, "on");
                } else {
                    new Utility(context, "off");

                }
            }


        } else if ("android.net.wifi.WIFI_STATE_CHANGED".equals(intent.getAction())) {
            boolean b = new ConnectionDetector(context).isConnectingToInternet();
            Log.e("after boot=", "wifiStatus" + b);
//
//            if(b) {
//
//                new Utility(context, "on");
//            }else{
//                new Utility(context, "off");
//
//            }


//            Toast.makeText(context, status, Toast.LENGTH_LONG).show();

        } else {
            Log.e("AlarmService=", "alarm Service started from boot");
            Intent service = new Intent(context, UserService.class);
            context.startService(service);

        }

    }

}