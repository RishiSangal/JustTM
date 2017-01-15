package com.android.encypher.justtrackme.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;

import java.util.ArrayList;

/**
 * Created by gipsy_danger on 5/7/16.
 */
public class MainSpinerAdapter extends ArrayAdapter {


    ArrayList<String> name=new ArrayList<>();
    Context con;



    public MainSpinerAdapter(Context applicationContext, ArrayList<String> spinnerValue) {
        super(applicationContext,R.layout.spinner_layout ,spinnerValue);
        con=applicationContext;
        name=spinnerValue;

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
        convertView =li.inflate(R.layout.spinner_layout,null);
        h.tv= (TextView) convertView.findViewById(R.id.spTExt);

//        ImageView ib= (ImageView) convertView.findViewById(R.id.sp);

        h.tv.setText("Name=="+name.get(position));



        return convertView;
    }

    static class ViewHolder{

        TextView tv;



    }
}
