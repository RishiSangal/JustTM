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
 * Created by gipsy_danger on 30/7/16.
 */
public class TwoWayViewAdapter extends ArrayAdapter {

    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();

    private class ViewHolder {

        TextView img;

    }


    public TwoWayViewAdapter(Context context, ArrayList<String> location, ArrayList<String> users) {

        super(context, R.layout.simple_row, users);
        time = users;
        this.location=location;

    }


    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position// Check if an existing view is being reused, otherwise inflate the view

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.simple_row, parent, false);

            viewHolder.img = (TextView) convertView.findViewById(R.id.book);
//            Log.e("position==",position+"");




            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.img.setText(location.get(position)+"\n"+time.get(position));

        return convertView;

    }

}
