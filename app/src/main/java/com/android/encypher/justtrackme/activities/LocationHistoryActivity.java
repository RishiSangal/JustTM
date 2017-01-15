package com.android.encypher.justtrackme.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.encypher.justtrackme.locations_fragment.GPSFragment;
import com.android.encypher.justtrackme.locations_fragment.NetStatFragment;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.locations_fragment.LocationFragment;

import android.support.v4.app.FragmentPagerAdapter;

public class LocationHistoryActivity extends AppCompatActivity {


    String url="";
    String userId;
    String timeStamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);

        try{

            getSupportActionBar().show();
            getSupportActionBar().setTitle("Location Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }catch (NullPointerException e){
            Log.e(getClass().getName(),e.toString());
        }

        url=  getIntent().getExtras().getString("Data");
        userId=getIntent().getExtras().getString("memid");
        timeStamp=getIntent().getExtras().getString("timeStamp");
        Log.e("url in LocationHistory",url);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        tabLayout.addTab(tabLayout.newTab().setText("Locations"));
        tabLayout.addTab(tabLayout.newTab().setText("GPS Status"));
        tabLayout.addTab(tabLayout.newTab().setText("NET Status"));

        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.camera));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(),3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                try{
                    viewPager.setCurrentItem(tab.getPosition());}catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }





   public long timeStatus() {

        return System.currentTimeMillis() / 1000L;
    }

    class PagerAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
//            Fragment fragment = null;



            switch (position) {
                case 0:
                    return new LocationFragment().newInstance(url,"0");
//                    break;


                case 1:
//                    GPSFragment tab2 = new GPSFragment();
//                    return tab2;
                   return new GPSFragment().newInstance(getResources().getString(R.string.url)+"gpslog&user_id="+userId+"&timestamp="+timeStamp+"&limit=1000&offset=0","1");
//                    break;

//                    return new GPSFragment().newInstance(getResources().getString(R.string.url)+"gpslog&user_id="+userId+"&timestamp="+timeStatus()+"&limit=1000&offset=0","1");

                case 2:
                    return new NetStatFragment().newInstance(getResources().getString(R.string.url)+"netstat&user_id="+userId+"&timestamp="+timeStamp+"&limit=1000&offset=0","1");
//                    break;
//                    TabFragment3 tab3 = new TabFragment3();
//                    return tab3;
                default:

                    return null;
            }
//            return fragment;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
