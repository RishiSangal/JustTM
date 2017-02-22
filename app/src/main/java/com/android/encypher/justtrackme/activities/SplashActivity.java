package com.android.encypher.justtrackme.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;

public class SplashActivity extends BaseActivity {

    SharedPreferences sharedPreferences;
    private static final long SPLASH_TIME_OUT =1000 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.scree_n));
//        View decorView = getWindow().getDecorView();
//        Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (sharedPreferences.getString("login", "0").equalsIgnoreCase("success")) {

                    Intent in = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(in);
                    finish();
                }else {
                    Intent in = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
