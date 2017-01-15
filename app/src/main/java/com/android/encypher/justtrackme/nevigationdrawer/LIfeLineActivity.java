package com.android.encypher.justtrackme.nevigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.encypher.justtrackme.home.ContactFragment;
import com.android.encypher.justtrackme.R;

public class LIfeLineActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_line);

        SharedPreferences sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);
        TextView tv1= (TextView) findViewById(R.id.cn1);
        TextView tv2= (TextView) findViewById(R.id.cn2);
        TextView tv3= (TextView) findViewById(R.id.cn3);

        tv1.setText(sharedPreferences.getString("cn1","Not Selected"));
        tv2.setText(sharedPreferences.getString("cn2","Not Selected"));
        tv3.setText(sharedPreferences.getString("cn3","Not Selected"));



        try {

            getSupportActionBar().show();
            getSupportActionBar().setTitle("Emergency Contacts");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        }catch (NullPointerException e){
            e.printStackTrace();
        }
        ImageView back= (ImageView) findViewById(R.id.back);
//        pr= (ProgressBar) findViewById(R.id.progressBar4);
//        pr.setVisibility(View.INVISIBLE);



//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // temperoray
//               startActivity( new Intent(LIfeLineActivity.this,HomeActivity.class));
//
//            }
//        });

        ImageView num1= (ImageView) findViewById(R.id.cont1);
        ImageView num2= (ImageView) findViewById(R.id.cont2);
        ImageView num3= (ImageView) findViewById(R.id.cont3);

        assert num1 != null;
        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactFragment s = new ContactFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_par, s);
                transaction.addToBackStack(null);
                transaction.commit();
                s.inten("1","1");



            }
        });
        assert num2 != null;
        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactFragment fragmentS1 = new ContactFragment();
                android.support.v4.app.FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_par, fragmentS1);
                transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_left);
                transaction.addToBackStack(null);
                transaction.commit();
                fragmentS1.inten("1","2");

            }
        });
        assert num3 != null;
        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactFragment fragmentS1 = new ContactFragment();
                android.support.v4.app.FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_par, fragmentS1);
                transaction.addToBackStack(null);
                transaction.commit();
                fragmentS1.inten("1","3");

            }
        });


//
//        if ((int) Build.VERSION.SDK_INT < 23)
//        {
//
//        }else if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_CONTACTS)
//                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_CONTACTS)
//                == PackageManager.PERMISSION_GRANTED) {
//
//
//
//        }else{
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_CONTACTS},
//                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//
//        }
//
//
//
//    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                } else {
////                    Snackbar.make(cl, "Replace with your own action", Snackbar.LENGTH_LONG)
////                            .setAction("Action", null).show();
//                    Intent in=new Intent(this,HomeActivity.class);
//                    startActivity(in);
//                }
//
//            }
//
//
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
