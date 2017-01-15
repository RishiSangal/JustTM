package com.android.encypher.justtrackme.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.encypher.justtrackme.home.HomeActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceMapActivity extends AppCompatActivity
        implements OnMapReadyCallback
{

    String placeName;
    GoogleMap map;

    double lat;
    double lon;
    double latitude,longitude;
    String address;
    EditText edt;
    private static final int PLACE_PICKER_REQUEST = 1;

    ProgressBar pr;
    String userId;
    String url;
    String placeId;
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW ;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map);
        placeName=getIntent().getStringExtra("place");
        String lats=getIntent().getStringExtra("lat");
        String lons=getIntent().getStringExtra("lon");
        placeId=getIntent().getStringExtra("placeId");
        try {
            getSupportActionBar().setTitle(placeName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){
            Log.e("placeMApActivity",e.toString());
        }
        sharedPreferences=getSharedPreferences("piyush", Context.MODE_PRIVATE);
        userId=sharedPreferences.getString("userId","null");
        url=getResources().getString(R.string.url);
        pr= (ProgressBar) findViewById(R.id.progressBar3);
        pr.setVisibility(View.INVISIBLE);

        lat= Double.valueOf(lats);
        lon= Double.valueOf(lons);
        edt= (EditText) findViewById(R.id.editText2);

        BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(lat,lon), new LatLng(lat,lon));

        Button btn= (Button) findViewById(R.id.doneButton);

        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callingApi(url + "addlocation&user_id="
                        + userId + "&name="
                        + placeName + "&longitude="
                        + longitude + "&latitude="
                        + latitude +
                        "&battery=" + sharedPreferences.getString("battery", "NotFOund") +
                        "&place=" + edt.getText().toString().replaceAll("\\s+", "%20") +
                        "&timestamp=" + sharedPreferences.getString("timeStamp", "NotFound") +
                        "&type=myplace"+
                        "&location_id="+placeId
                );


            }
        });

        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
            Intent intent = intentBuilder.build(PlaceMapActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            latitude=place.getLatLng().latitude;
            longitude=place.getLatLng().longitude;
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            edt.setText(name+" "+address);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.realPlaceFragment);
        mapFragment.getMapAsync(PlaceMapActivity.this);
//            mAddress.setText(address);
//            mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;

        LatLng latLng1=new LatLng(latitude,longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng1).title(placeName).draggable(true));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("Your Address"));
                map.addCircle(new CircleOptions()
                        .center(new LatLng(lat,lon))
                        .radius(100)
                        .strokeColor(Color.parseColor("#80deea"))
                        .fillColor(Color.parseColor("#FFF9C4")));




            }
        });

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
    private void callingApi( String s) {

        pr.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(PlaceMapActivity.this);
        Log.e("place url",s);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response in place Amp", response);


                pr.setVisibility(View.INVISIBLE);


                try {
                    if (new JSONObject(response).getString("success").equals("1")) {

                        Toast.makeText(PlaceMapActivity.this,new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();

                        startActivity(new Intent(PlaceMapActivity.this,HomeActivity.class));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                Toast.makeText(PlaceMapActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                pr.setVisibility(View.INVISIBLE);


            }
        });

        queue.add(strReq);


    }


}
