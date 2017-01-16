package com.android.encypher.justtrackme.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.encypher.justtrackme.activities.BaseActivity;
import com.android.encypher.justtrackme.activities.InfoActivity;
import com.android.encypher.justtrackme.activities.MemberDetailsActivity;
import com.android.encypher.justtrackme.adapter.Communicator;
import com.android.encypher.justtrackme.adapter.GroupDetailAdapter;
import com.android.encypher.justtrackme.nevigationdrawer.GlobalShareFragment;
import com.android.encypher.justtrackme.nevigationdrawer.HelpFragment;
import com.android.encypher.justtrackme.nevigationdrawer.LIfeLineActivity;
import com.android.encypher.justtrackme.nevigationdrawer.MyLocationActivity;
import com.android.encypher.justtrackme.nevigationdrawer.NevigationDrawerActivity;
import com.android.encypher.justtrackme.nevigationdrawer.PremiumFragment;
import com.android.encypher.justtrackme.nevigationdrawer.RealPlacesFragment;
import com.android.encypher.justtrackme.nevigationdrawer.SahreFragment;
import com.android.encypher.justtrackme.service.UserService;
import com.android.encypher.justtrackme.utility.CustomVolleyRequest;
import com.android.encypher.justtrackme.utility.NoNetworkFragment;
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
import com.android.encypher.justtrackme.registration.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements OnMapReadyCallback, Communicator, AbsListView.OnScrollListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private int lastTopValue = 0;
    SupportMapFragment supportMapFragment;
    ImageView transparentImageView;
    ArrayList<String> circle_id = new ArrayList();
    int pos = 0;
    String userId;
    String groupName = "";
    //  LinkedList sharedStatus = new LinkedList();
    String TAG = this.getClass().getName();
    ArrayList<String> spinnerValue = new ArrayList<>();
    SharedPreferences sharedPreferences;
    Spinner sp;
    ListView listView;
    ArrayList<String> memName;
    ArrayList<String> memGs;
    ArrayList<String> memMobile;
    ArrayList<String> memImage;
    ArrayList<String> memTime;
    String status = "jcbjsdk";
    ArrayList<String> memLat;
    ArrayList<String> memLong;
    ArrayList<String> lastBattery;
    ArrayList<String> memberId;
    ArrayList<String> admin;
    Switch switchButton;
    View hView;
    GoogleMap mMap;
    ProgressBar pr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "0");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        permissionCheck();
        pr = (ProgressBar) findViewById(R.id.progressBar);
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        Log.e("Gps home", statusOfGPS + "");
        TextView gpsE = (TextView) findViewById(R.id.textView16);
        gpsE.setVisibility(View.INVISIBLE);


        if (!statusOfGPS) {
            gpsE.setVisibility(View.VISIBLE);
        } else {
            gpsE.setVisibility(View.INVISIBLE);
        }


        listView = (ListView) findViewById(R.id.groupList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabHome);
        assert fab != null;
        fab.setOnClickListener(fabClick);


        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        assert swipeRefreshLayout != null;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("swipe", "onRefresh called from SwipeRefreshLayout");
                startActivity(new Intent(HomeActivity.this, HomeActivity.class));

            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_listview_layout, listView, false);
        assert listView != null;
        listView.addHeaderView(header, null, false);

        transparentImageView = (ImageView) header.findViewById(R.id.transparent_image);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.headerListFragment);
        mapFragment.getMapAsync(this);
        Log.e("going for map", "map");

        transparentImageView.setOnTouchListener(transparentImageClick);


        listView.setOnScrollListener(HomeActivity.this);
        listView.setOnItemClickListener(listViewClick);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }

        switchButton = (Switch) findViewById(R.id.switch1);
        TextView checkIn = (TextView) findViewById(R.id.toolText);

        assert switchButton != null;
        assert checkIn != null;

        switchButton.setOnCheckedChangeListener(switchButtonClick);
        checkIn.setOnClickListener(checkInClick);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(navigationViewSelected);

        hView = navigationView.getHeaderView(0);

        sp = (Spinner) findViewById(R.id.toolSpinner);
        sp.setOnItemSelectedListener(spinnerClick);
        new Abc().execute();
    }

    private View.OnTouchListener transparentImageClick = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    listView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    listView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    listView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        }
    };

    private View.OnClickListener checkInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addCheckInName("1");
        }
    };

    private AdapterView.OnItemClickListener listViewClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String adm = "";
            Log.e("position in listView", "" + position + memLong.get(position - 1) + "dd" + userId);
            for (int i = 0; i < admin.size(); i++) {
                if (userId.equals(admin.get(i))) {
                    adm = "yes";
                }
            }

            Intent in = new Intent(HomeActivity.this, MemberDetailsActivity.class);
            in.putExtra("memImage", memImage.get(position - 1));
            in.putExtra("memName", memName.get(position - 1));
            in.putExtra("memLat", memLat.get(position - 1));
            in.putExtra("memTime", memTime.get(position - 1));
            in.putExtra("memLong", memLong.get(position - 1));
            in.putExtra("memGs", memGs.get(position - 1));
            in.putExtra("userId", userId);
            in.putExtra("admin", adm);
            in.putExtra("circle_id", circle_id.get(pos));
            in.putExtra("memId", memberId.get(position - 1));
            in.putExtra("lastBattery", lastBattery.get(position - 1));
            startActivity(in);
        }
    };

    private CompoundButton.OnCheckedChangeListener switchButtonClick = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                status = "public";
            } else {
                status = "private";
            }
            changeincircleshare(getResources().getString(R.string.url) + "changeincircleshare&" + "user_id=" + userId + "&circle_id=" + circle_id.get(pos) + "&shared_status=", status);
        }
    };

    private View.OnClickListener fabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FlaotingFragment s = new FlaotingFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.drawer_layout, s);
            transaction.addToBackStack(null);
            transaction.commit();
            s.data(userId, circle_id.get(pos));
        }
    };

    private AdapterView.OnItemSelectedListener spinnerClick = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            groupName = parent.getItemAtPosition(position).toString();
            Log.e("new groupsad", "going to add");
            if (groupName.equals("New Group")) {
                Log.e("new gropu", "going to add");
                addCheckInName("2");
            } else {
                groupDetails(getResources().getString(R.string.url) + "circleDetails&user_id=" + userId + "&circle_id=" + circle_id.get(position));
                Log.e("detail gropup", "going to add");
                pos = position;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private NavigationView.OnNavigationItemSelectedListener navigationViewSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.activities) {

                Intent in = new Intent(HomeActivity.this, NevigationDrawerActivity.class);
                in.putExtra("data", "activities");
                startActivity(in);


            } else if (id == R.id.lifeLine) {

                Intent in = new Intent(HomeActivity.this, LIfeLineActivity.class);

                startActivity(in);


            } else if (id == R.id.places) {

                RealPlacesFragment s = new RealPlacesFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();

            } else if (id == R.id.premium) {

                PremiumFragment s = new PremiumFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();


            } else if (id == R.id.setting) {
//
//            Intent in = new Intent(HomeActivity.this, NevigationDrawerActivity.class);
//            in.putExtra("data", "settings");
//            startActivity(in);

                z = 1;
                GlobalShareFragment s = new GlobalShareFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();


            } else if (id == R.id.logout) {
                logoutPopup();

            } else if (id == R.id.help) {


                HelpFragment s = new HelpFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();


            } else if (id == R.id.nav_share) {

                SahreFragment s = new SahreFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();


            } else if (id == R.id.myLocations) {
                Intent in = new Intent(HomeActivity.this, MyLocationActivity.class);
                in.putExtra("memberdata","home");
                startActivity(in);

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            assert drawer != null;
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    public void addSpinnerValue() {
        spinnerValue.clear();
        circle_id.clear();
        String str1 = sharedPreferences.getString("spinnerValue", "1");
        Log.e("piyushCircle", str1);
        if (!(str1.equals("1"))) {
            JSONArray ja = null;
            try {
                ja = new JSONArray(str1);
                Log.e("jsonArrayLength=", "" + ja.length());
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    spinnerValue.add(jo.getString("name"));
                    circle_id.add(jo.getString("id"));

                }
                sp = (Spinner) findViewById(R.id.toolSpinner);
                assert sp != null;
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, spinnerValue);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.notifyDataSetChanged();
                sp.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            spinnerValue.add("New Group");


        } else {
//
            Log.e("Activity restart", "spinner is not set");
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));


        }
    }

    private void groupDetails(String s) {
        memberId = new ArrayList<>();
        admin = new ArrayList<>();
        memName = new ArrayList<>();
        memGs = new ArrayList<>();
        memMobile = new ArrayList<>();
        memImage = new ArrayList<>();
        memTime = new ArrayList<>();
        memLat = new ArrayList<>();
        memLong = new ArrayList<>();
        lastBattery = new ArrayList<>();
        pr.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("groupDetails url", s);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("gropuDetailsResponse", response);
                try {
                    String owner = new JSONObject(response).getString("owners");
                    JSONArray ja = new JSONArray(owner);
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        admin.add(jo.getString("owner_id"));

                    }

                    JSONArray jsonArray = new JSONArray(new JSONObject(response).getString("response"));

                    memGs.clear();
                    memberId.clear();
                    memName.clear();
                    memImage.clear();
                    memTime.clear();
                    memLong.clear();
                    memLat.clear();
                    memMobile.clear();
                    lastBattery.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        memberId.add(jo.getString("id"));
                        if (memberId.get(i).equals(userId)) {
                            memLat.add(sharedPreferences.getString("lat", "28.113656"));
                            memLong.add(sharedPreferences.getString("lon", "77.123458"));
                            lastBattery.add(sharedPreferences.getString("battery", "not found"));
                            memTime.add(sharedPreferences.getString("clock", "not found"));
                            Log.e("Batter in home", "" + lastBattery.get(i));
                            String st = jo.getString("cs");
                            Log.e("user in cs", st);
                            if (st.equals("public")) {
                                switchButton.setChecked(true);
                            } else {
                                switchButton.setChecked(false);

                            }

                        } else {
                            Log.e("admin", "I am in not admin");
                            memLat.add(jo.getString("last_lat"));
                            memTime.add(jo.getString("last_time"));

                            memLong.add(jo.getString("last_long"));
                            lastBattery.add(jo.getString("last_battery"));
                        }

                        memName.add(jo.getString("fname") + " " + jo.getString("lname"));
                        memMobile.add(jo.getString("mobile"));
                        memGs.add(jo.getString("gs"));
                        Log.e("latSize=", "" + memLat.size());
                        memImage.add(jo.getString("image"));
                    }


                    GroupDetailAdapter ad = new GroupDetailAdapter(HomeActivity.this, memName, memMobile, memLat, memLong, memImage, memGs, lastBattery, memTime);
                    ad.notifyDataSetChanged();
                    pr.setVisibility(View.INVISIBLE);

                    listView.setAdapter(ad);
                    mMap.clear();

                    for (int i = 0; i < memLat.size(); i++) {
                        try {

                            LatLng latLng1 = new LatLng(Double.valueOf(memLat.get(i)), Double.valueOf(memLong.get(i)));
//                        LatLng latLng1 = new LatLng(28.132626,36.2265);
                            Log.e("LATLNG ", latLng1.toString());
                            final String str[] = memName.get(i).split(" ");
                            mMap.addMarker(new MarkerOptions().position(latLng1)).setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.chat, str[0])));
                            Log.e("map initialization", "Marker is added");
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    return true;
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Home Activity", e.toString());
                            Toast.makeText(HomeActivity.this, memName.get(i) + " Data is not found", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (JSONException e) {
                    Log.e("JSONArray", e.toString());
                    e.printStackTrace();
                    NoNetworkfrag();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pr.setVisibility(View.INVISIBLE);
                NoNetworkfrag();

            }

        });

        queue.add(strReq);


    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Rect rect = new Rect();

        transparentImageView.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            transparentImageView.setY((float) (rect.top / 2.0));
        }
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

    private void changeincircleshare(String s, final String status) {


        RequestQueue queue = Volley.newRequestQueue(this);
        pr.setVisibility(View.VISIBLE);
        Log.e("change in circle URl=", s + status);
//        Log.e("Bef shared_Status=",""+sharedStatus.get(pos)+pos);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                s + status
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("responseshar in circle", response);


                pr.setVisibility(View.INVISIBLE);

                try {
                    Toast.makeText(HomeActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_LONG).show();
//                    sharedStatus.add(pos,status);
//                    Log.e("change in sharedStatus=",""+sharedStatus.get(pos)+pos);


//                    if (sharedStatus.equals("public")) {
//                        switchButton.setChecked(false);
//                    }else{
//                        switchButton.setChecked(true);
//
//                    }

//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        if (sharedPreferences.getString("ges", "o").equals("private")) {
//                            editor.putString("gs", "public");
//                        } else if (sharedPreferences.getString("ges", "o").equals("public")) {
//                            editor.putString("gs", "private");
//
//
//                        editor.apply();

//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    NoNetworkfrag();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pr.setVisibility(View.INVISIBLE);
//                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                NoNetworkfrag();

            }
        });

        queue.add(strReq);
    }


    int z = 0;


    public void permissionCheck() {


        if ((int) Build.VERSION.SDK_INT > 22) {
//

            int permissionCheck1 = ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
            // Assume thisActivity is the current activity
            int permissionCheck2 = ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

                Log.e("hey", "permission ififi granted");
                Intent in1 = new Intent(HomeActivity.this, UserService.class);
                startService(in1);


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }

            Log.e("hey", "permission   tuut granted");


        } else {

            Intent in1 = new Intent(HomeActivity.this, UserService.class);
            startService(in1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.e("hey", "permission granted");

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.


                } else {

                    Toast.makeText(HomeActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void callingApi(String userId) {

        String url = getResources().getString(R.string.url);
        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("circleNames=", url + "mycircles&userid=" + userId);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url + "mycircles&userid=" + userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.e("response of circle name", response);

                try {

                    Log.e("Spinner value", new JSONObject(response).getString("circles"));
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("spinnerValue", new JSONObject(response).getString("circles"));
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("Spinner value", e.toString());

                    NoNetworkfrag();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                NoNetworkfrag();

            }
        });

        queue.add(strReq);


    }

    public void NoNetworkfrag() {
        NoNetworkFragment s = new NoNetworkFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.drawer_layout, s);
        transaction.commit();
    }

//    private static long back_pressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();


    }


    private void logoutPopup() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(HomeActivity.this);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Are you sure? ");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                SharedPreferences sharedPreferences = getSharedPreferences("piyush", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();
                                Intent in = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(in);
                                finish();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }




//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//
//    }

    @Override
    public void onDestroy() {
        Intent in = new Intent();
        in.setAction("StartkilledService");
        sendBroadcast(in);
        Log.e("debug", "Service Killed");

        super.onDestroy();
    }

    @Override
    public void response(String i) {

    }

    @Override
    public void emial(String i) {

    }

    @Override
    public void emial(String i, String j) {

    }

    @Override
    public void email(String email, String phone, String s, String s1, String s2, String s3, String country) {

    }


    @Override
    public void floatToConatct(String home, String circle_id) {


        ContactFragment s = new ContactFragment();
////                SplashFragment ws=new SplashFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.bottom_to_top,R.anim.bottom_to_top);
//
//// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.drawer_layout, s);

        transaction.addToBackStack(null);
////                 transaction.remove(ws);
        transaction.commit();
        s.check("home", circle_id);
    }

    String CheckInName;
    String newGroupName;

    private void addCheckInName(final String check) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Add Group Name");
        alertDialogBuilder.setMessage("Group Name Can Be Family,Friends...");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setHint("Enter Group Name");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!(userInput.getText().toString().isEmpty())) {

                                    if (check.equals("1")) {


                                        CheckInName = userInput.getText().toString();

//                                        callingApi(userId, (getResources().getString(R.string.url) + "checkin&name=" + CheckInName + "&user_id=").replaceAll("\\s+", "%20"));

                                    } else {
                                        newGroupName = userInput.getText().toString();

                                        addCircle(userId, newGroupName);


                                    }
                                } else {
                                    Toast.makeText(HomeActivity.this, "Enter Name First", Toast.LENGTH_SHORT).show();
                                }
                                //   Toast.makeText(HomeActivity.this,userInput.getText().toString(),Toast.LENGTH_LONG).show();
//                                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
//
//                                Boolean isInternetPresent = cd.isConnectingToInternet();
//
//                                if (isInternetPresent) {
//
//
//                                } else {
//                                    Toast.makeText(HomeActivity.this,
//                                            "You don't have internet connection.", Toast.LENGTH_LONG).show();
//                                }


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    //
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        try {

            Log.e("onMApReady", "I am in on mapReady");

            String lat = sharedPreferences.getString("lat", "0.0");
            String lon = sharedPreferences.getString("lon", "0.0");
            LatLng latLng1 = new LatLng(Double.valueOf(lat), Double.valueOf(lon));

//                       mMap.addMarker(new MarkerOptions().title(memName.get(i)));
//                        LatLng latLng1 = new LatLng(28.581056, 77.3175023);

            googleMap.addMarker(new MarkerOptions().position(latLng1)).setIcon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.chat, "you")));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } catch (NumberFormatException e) {
            Log.e("onMApReady", e.toString());
        }
    }
//    @Override
//    public void onBackPressed() {
//
//
//        if (back_pressed + 2000 > System.currentTimeMillis()) {
//            super.onBackPressed();
//        } else {
//            Toast.makeText(HomeActivity.this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
//            back_pressed = System.currentTimeMillis();
//        }
//
//
//    }

    private void addCircle(final String userId, String cName) {

        String url = getResources().getString(R.string.url);
        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("createCircleURL", url + "createcircle&userid=" + userId + "&cname=" + cName);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                (url + "createcircle&userid=" + userId + "&cname=" + cName).replaceAll("\\s+", "%20"), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response in add circle", response);


                try {
                    Toast.makeText(HomeActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_LONG).show();
                    callingApi(userId);
                    addSpinnerValue();

                    Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
                    intent.putExtra("data", "home");
                    startActivity(intent);
//                    finish();
//                    finish();
//                    overridePendingTransition( 0, 0);
//                    startActivity(getIntent());
//                    overridePendingTransition( 0, 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                NoNetworkfrag();
//                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);


    }

    private class Abc extends AsyncTask<Void, Void, Void> {
        NetworkImageView user;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pr.setVisibility(View.VISIBLE);

            user = (NetworkImageView) hView.findViewById(R.id.navImageView);
            TextView tv = (TextView) hView.findViewById(R.id.userId);
            tv.setText(sharedPreferences.getString("userName", "o"));

            assert user != null;
            ImageLoader imageLoader;

            String userImage = sharedPreferences.getString("image", "o");
            imageLoader = CustomVolleyRequest.getInstance(HomeActivity.this)
                    .getImageLoader();

            imageLoader.get(getResources().getString(R.string.imagesurl) + userImage, ImageLoader.getImageListener(user,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            user.setImageUrl(getResources().getString(R.string.imagesurl) + userImage, imageLoader);

            Log.e("bitmap", userImage);

        }

        @Override
        protected Void doInBackground(Void... params) {


            callingApi(userId);
            addSpinnerValue();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pr.setVisibility(View.INVISIBLE);

//            Log.e("shared_status",sharedStatus.get(pos));
//
//            if (sharedStatus.get(pos).equals("public")) {
//                switchButton.setChecked(true);
//            }else{
//                switchButton.setChecked(false);
//
//            }
        }

    }


}

