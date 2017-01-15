package com.android.encypher.justtrackme.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.utility.CustomVolleyRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;
import com.android.encypher.justtrackme.nevigationdrawer.MyLocationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MemberDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    LatLng latLng1;
    String memImage;
    String memTime;
    String memName;
    String memLat;
    String memLong;
    String memGs;
    String lastBattery;
    String id;
    String admin;
    String userId;
    String circle_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        NetworkImageView im = (NetworkImageView) findViewById(R.id.memberImage);
        memName = getIntent().getStringExtra("memName");

        try {
            getSupportActionBar().show();
            getSupportActionBar().setTitle(memName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            Log.e("MemberDeta",e.toString());
        }

//        ImageView img= (ImageView) findViewById(R.id.back);
//        assert img != null;
//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        id = getIntent().getStringExtra("memId");
        userId = getIntent().getStringExtra("userId");
        admin = getIntent().getStringExtra("admin");
        circle_id=getIntent().getStringExtra("circle_id");
        memTime=getIntent().getStringExtra("memTime");


        Button rButton = (Button) findViewById(R.id.removeButton);

        Log.e("admin", admin);

        if (admin.equals("yes") && !(userId.equals(id))) {
            assert rButton != null;
            rButton.setText("REMOVE");
        } else if( (userId.equals(id))) {
            assert rButton != null;
            rButton.setText("Exit From Group");

        }else{
            assert rButton != null;
            rButton.setVisibility(View.INVISIBLE);
        }

        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userId.equals(id)){

                    removeFromGroup(getResources().getString(R.string.url)+"removeMember&admin_id="+userId+"&circle_id="+circle_id+"&member_id="+id);

                }else{

                   removeFromGroup(getResources().getString(R.string.url)+"removeMe&user_id="+userId+"&circle_id="+circle_id);

                }
            }
        });

        memImage = getIntent().getStringExtra("memImage");
        memLat = getIntent().getStringExtra("memLat");
        memLong = getIntent().getStringExtra("memLong");
        memGs = getIntent().getStringExtra("memGs");
        lastBattery = getIntent().getStringExtra("lastBattery");

//        TextView tv= (TextView) findViewById(R.id.memName);
//        assert tv != null;
//        tv.setText(memName);

        String time,mainTime;
        Calendar mydate = Calendar.getInstance();
        try {
            mydate.setTimeInMillis(Long.parseLong(memTime) * 1000);
            time= (mydate.get(Calendar.HOUR_OF_DAY)+":"+mydate.get(Calendar.MINUTE) + " [" + mydate.get(Calendar.DAY_OF_MONTH) + "." + mydate.get(Calendar.MONTH) + "." + mydate.get(Calendar.YEAR)+"]");
            mainTime=time;
        }catch (Exception e){
            mainTime="Not Found";
        }


        ImageLoader imageLoader = CustomVolleyRequest.getInstance(MemberDetailsActivity.this)
                .getImageLoader();
        imageLoader.get(getResources().getString(R.string.imagesurl) + memImage, ImageLoader.getImageListener(im,
                R.drawable.hamburger, android.R.drawable.ic_dialog_alert));
        assert im != null;
        im.setImageUrl(getResources().getString(R.string.imagesurl) + memImage, imageLoader);

        TextView tv1 = (TextView) findViewById(R.id.memberDetial);
        assert tv1 != null;
        tv1.setText(memName );
        TextView tv2= (TextView) findViewById(R.id.textBatt);
        assert tv2 != null;
        tv2.setText( "Battery-" + lastBattery+"%\nSharing-" + memGs+"\nTime-"+mainTime);


//                       mMap.addMarker(new MarkerOptions().title(memName.get(i)));
//                        LatLng latLng1 = new LatLng(28.581056, 77.3175023);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.memeberDetailFragment);
        mapFragment.getMapAsync(this);


//        mMap.addMarker(new MarkerOptions().position(latLng1)).setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.chat,str[0])));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }

    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return (int) ((nDP * conversionScale) + 0.5f);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        try {
            latLng1 = new LatLng(Double.valueOf(memLat), Double.valueOf(memLong));
            addMarker(latLng1, memName);


        } catch (Exception e) {
            Toast.makeText(MemberDetailsActivity.this, "No Location found", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MemberDetailsActivity.this, HomeActivity.class));
        }


    }

    public void addMarker(LatLng sydney, String s) {


        mMap.addMarker(new MarkerOptions().position(latLng1)).setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.chat, memName)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onBackPressed() {

//        startActivity(new Intent(MemberDetailsActivity.this,HomeActivity.class));
//        finish();
        super.onBackPressed();
    }


    private void removeFromGroup(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("removeMeurl=", url);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("removeGroup response",response);

                try {
                    if("0".equals( new JSONObject(response).getString("success")) && "You cannot be removed from default circle".equals( new JSONObject(response).getString("message")) ){
                        Toast.makeText(MemberDetailsActivity.this,new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(MemberDetailsActivity.this,HomeActivity.class));
//                        finish();
                        onBackPressed();

                    } else if ("004".equals( new JSONObject(response).getString("code"))) {

                        Toast.makeText(MemberDetailsActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        promoteAdminFragment s = new promoteAdminFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.memberActivity, s);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        s.details(userId, circle_id);

                    } else {

                        Toast.makeText(MemberDetailsActivity.this,new JSONObject(response).getString("message"),Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MemberDetailsActivity.this, InfoActivity.class);
                        intent.putExtra("data",new JSONObject(response).getString("message"));
                        startActivity(intent);
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
        });

        queue.add(strReq);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//         Handle action bar item clicks here. The action bar will
//         automatically handle clicks on the Home/Up button, so long
//         as you specify a parent activity in AndroidManifest.xml.
        int id1 = item.getItemId();

        if (id1 == R.id.premium_detail) {



         getAccess(getResources().getString(R.string.url)+"canAccess&user_id="+userId+"&member_id="+id);



        }else if(id1==R.id.pay){

        }else if(id1==R.id.home){
          super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getAccess(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("getAcces=", url);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("getAccess response",response);

                try {
                    if("1".equals( new JSONObject(response).getString("success"))) {
//                        Toast.makeText(MemberDetailsActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(MemberDetailsActivity.this, MyLocationActivity.class);
                        in.putExtra("memberdata", id);
                        startActivity(in);
                    }else {
                        Toast.makeText(MemberDetailsActivity.this, "This is a premium feature. You must pay for this user", Toast.LENGTH_SHORT).show();

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
        });

        queue.add(strReq);


    }
}
