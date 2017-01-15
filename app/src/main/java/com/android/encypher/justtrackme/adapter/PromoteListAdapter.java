package com.android.encypher.justtrackme.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;

import java.util.ArrayList;

/**
 * Created by gipsy_danger on 18/7/16.
 */
public class PromoteListAdapter extends ArrayAdapter {

    Context con;

    ArrayList<String> memName=new ArrayList<>();
    public PromoteListAdapter(FragmentActivity activity, ArrayList<String> memName) {
        super(activity, R.layout.romote_list,memName);

        con=activity;
        this.memName=memName;
    }


    @Override
    public int getCount() {
//        Log.e("name",""+name.size());

        return memName.size();
    }

    @Override
    public Object getItem(int position) {
//        Log.e("name",""+name.get(position));

        return memName.get(position);
    }

    @Override
    public long getItemId(int position) {
//        Log.e("name",""+position);

        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {





        final ViewHolder h=new ViewHolder();
        final LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.romote_list,null);
        h.tv= (TextView) convertView.findViewById(R.id.prmoteText);


        h.tv.setText(memName.get(position));







        return convertView;

    }
    static class ViewHolder{

        TextView tv;




    }
}
