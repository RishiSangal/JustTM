package com.android.encypher.justtrackme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;
import com.android.encypher.justtrackme.utility.CustomVolleyRequest;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by gipsy_danger on 24/6/16.
 */
public class GroupDetailAdapter extends ArrayAdapter {
    private ImageLoader imageLoader;
    ArrayList<String> name=new ArrayList<>();



    ArrayList<String> memGs=new ArrayList<>();
    ArrayList<String> memMobile=new ArrayList<>();
    ArrayList<String> memImage=new ArrayList<>();
    ArrayList<String> memLat=new ArrayList<>();
    ArrayList<String> memLong=new ArrayList<>();
    ArrayList<String> lastBattery=new ArrayList<>();
    ArrayList<String> memTime=new ArrayList<>();
    Context con;



    public GroupDetailAdapter(FragmentActivity activity, ArrayList<String> place) {
        super(activity, R.layout.layout_group_detail ,place);
        con=activity;
        name=place;


        Log.e("Adapter","Adapter");


    }



    public GroupDetailAdapter(HomeActivity homeActivity, ArrayList<String> memName, ArrayList<String> memMobile, ArrayList<String> memLat, ArrayList<String> memLong, ArrayList<String> memImage, ArrayList<String> memGs, ArrayList<String> lastBattery,ArrayList<String> memTime) {
        super(homeActivity, R.layout.layout_group_detail , memName);
        name=memName;
        this.memMobile=memMobile;
        this.memLat=memLat;
        this.memLong=memLong;
        this.memImage=memImage;
        this.memGs=memGs;
        this.lastBattery=lastBattery;
        this.memTime=memTime;
        con=homeActivity;


    }

    @Override
    public int getCount() {
//        Log.e("name",""+name.size());

        return name.size();
    }

    @Override
    public Object getItem(int position) {
//        Log.e("name",""+name.get(position));

        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
//        Log.e("name",""+position);

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.e("getView","getView");

        final ViewHolder h=new ViewHolder();


        final LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.layout_group_detail,null);
        h.tv= (TextView) convertView.findViewById(R.id.grpname);
        h.tv2= (TextView) convertView.findViewById(R.id.batterDeta);
        h.im= (NetworkImageView) convertView.findViewById(R.id.grpImage);

//        if(position==0){
//
//
//
////            h.im.setVisibility(View.INVISIBLE);
////            h.tv.setVisibility(View.INVISIBLE);
//
//        }else {

//        ImageView ib= (ImageView) convertView.findViewById(R.id.sp);

            h.tv.setText(name.get(position));
        String time,mainTime;
        Calendar mydate = Calendar.getInstance();
        try {
            mydate.setTimeInMillis(Long.parseLong(memTime.get(position)) * 1000);
            time= (mydate.get(Calendar.HOUR_OF_DAY)+":"+mydate.get(Calendar.MINUTE) + " [" + mydate.get(Calendar.DAY_OF_MONTH) + "." + mydate.get(Calendar.MONTH) + "." + mydate.get(Calendar.YEAR)+"]");
            mainTime=time;
        }catch (Exception e){
            mainTime="Not Found";
        }
            h.tv2.setText("Battery- "+lastBattery.get(position)+"%"+"\nTime-"+mainTime);


//             Bitmap b=   ImageLoder(con.getResources().getString(R.string.imagesurl)+memImage.get(position));
//             h.im.setImageBitmap(b);

        imageLoader = CustomVolleyRequest.getInstance(con)
                .getImageLoader();
        imageLoader.get(con.getResources().getString(R.string.imagesurl)+memImage.get(position), ImageLoader.getImageListener(h.im,
                R.drawable.hamburger, android.R.drawable
                        .ic_dialog_alert));
        h.im.setImageUrl(con.getResources().getString(R.string.imagesurl)+memImage.get(position), imageLoader);
//            h.im.setImageAlpha(image.get(position));
//        }
//        if(position==limit-1){
//            h.btn.setVisibility(View.VISIBLE);
//            h.btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    limit=+20;
//                    offset=+20;
//                    h.btn.setVisibility(View.INVISIBLE);
//                    h.pr.setVisibility(View.VISIBLE);
//                    new ActivitiesFragment().changeincircleshare(con.getResources().getString(R.string.url)+"mycheckins&user_id="+userId
//                            +"&limit="+limit+"&offset="+offset);
//                }
//            });
//        }



        return convertView;
    }

    static class ViewHolder{

        TextView tv;
        TextView tv2;
        NetworkImageView im;



    }
    Bitmap response1;


    public Bitmap ImageLoder(String url) {


        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
//                iv.setImageBitmap(response);
            response1 = response;

            }
        }, 0, 0, null, null);
        return response1;

    }
}