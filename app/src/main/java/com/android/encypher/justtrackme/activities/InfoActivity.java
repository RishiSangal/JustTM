package com.android.encypher.justtrackme.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            Log.e("Info",e.toString());
        }

        TextView tv= (TextView) findViewById(R.id.textView7);
        String str=getIntent().getExtras().getString("data");
        assert str != null;
        if(str.equals("home")){
            assert tv != null;
            tv.setText("Your group is created");

        }else {
            assert tv != null;
            tv.setText(str);

        }


    }



    @Override
    public void onBackPressed() {

        startActivity(new Intent(InfoActivity.this,HomeActivity.class));
        finish();
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
