package com.android.encypher.justtrackme.nevigationdrawer;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.encypher.justtrackme.R;

public class NevigationDrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation_drawer);





        String str=getIntent().getExtras().getString("data");
        if(str.equals("activities")){
            try {
                getSupportActionBar().show();
                getSupportActionBar().setTitle("Activities");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }catch (NullPointerException e){
                Log.e("Nevigation Activity",e.toString());
            }
            FragmentManager fragmentManager = getFragmentManager();
            ActivitiesFragment loginFragment =  new ActivitiesFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_Nev, loginFragment).commit();

        }else if(str.equals("settings")){

            SettingsFragment loginFragment =  new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_Nev, loginFragment).commit();

        }
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
