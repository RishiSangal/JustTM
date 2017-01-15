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
 * Created by gipsy_danger on 5/8/16.
 */
public class NetAndGpsAdapter  extends ArrayAdapter {

    ArrayList<String> time=new ArrayList<>();
    ArrayList<String> status=new ArrayList<>();
    Context con;




    public NetAndGpsAdapter(FragmentActivity activity, ArrayList<String> time, ArrayList<String> status) {
        super(activity, R.layout.gps_net_adapter_layout ,time);
        con=activity;
        this.time=time;
        this.status=status;

        Log.e("Adapter","Adapter");

    }


    @Override
    public int getCount() {
//        Log.e("status",""+status.size());

        return time.size();
    }

    @Override
    public Object getItem(int position) {
//        Log.e("status",""+status.get(position));

        return time.get(position);
    }

    @Override
    public long getItemId(int position) {
//        Log.e("status",""+position);

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.e("getView","getView");

        final ViewHolder h=new ViewHolder();


        final LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.gps_net_adapter_layout,null);
        h.id= (TextView) convertView.findViewById(R.id.numId);
        h.time1= (TextView) convertView.findViewById(R.id.time);
        h.status1= (TextView) convertView.findViewById(R.id.status);


        h.id.setText(""+position);
        h.status1.setText(status.get(position));
        h.time1.setText(time.get(position));




        return convertView;
    }

    static class ViewHolder{

        TextView id,time1,status1;
//        ImageView im;



    }
}
