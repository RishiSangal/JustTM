package com.android.encypher.justtrackme.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;

import java.util.ArrayList;

/**
 * Created by gipsy_danger on 11/7/16.
 */
public class PlaceAdapter extends ArrayAdapter {

    ArrayList<String> place=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    Context con;




    public PlaceAdapter(FragmentActivity activity, ArrayList<String> place, ArrayList<String> name) {
        super(activity, R.layout.place_layout ,place);
        con=activity;
        this.place=place;
        this.name=name;

        Log.e("Adapter","Adapter");

    }


    @Override
    public int getCount() {
//        Log.e("name",""+name.size());

        return place.size();
    }

    @Override
    public Object getItem(int position) {
//        Log.e("name",""+name.get(position));

        return place.get(position);
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
        convertView =li.inflate(R.layout.place_layout,null);
        h.tv= (TextView) convertView.findViewById(R.id.placeName);


        h.tv.setText(place.get(position));




        return convertView;
    }

    static class ViewHolder{

        TextView tv;
//        ImageView im;



    }
}
