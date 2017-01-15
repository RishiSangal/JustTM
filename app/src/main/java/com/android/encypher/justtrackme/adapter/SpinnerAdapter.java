package com.android.encypher.justtrackme.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;

import java.util.ArrayList;

/**
 * Created by gipsy_danger on 14/6/16.
 */
public class SpinnerAdapter extends ArrayAdapter {

    ArrayList<String> place=new ArrayList<>();
    ArrayList<String> timeStamp=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    Context con;




    public SpinnerAdapter(FragmentActivity activity, ArrayList<String> place, ArrayList<String> timeStamp, ArrayList<String> name) {
        super(activity,R.layout.spinner_adapter_layout ,place);
        con=activity;
        this.place=place;
        this.timeStamp=timeStamp;
        this.name=name;

        Log.e("Adapter","Adapter");

    }

    public SpinnerAdapter(FragmentActivity activity, ArrayList<String> name, ArrayList<String> place) {
        super(activity,R.layout.spinner_adapter_layout ,place);
        con=activity;
        this.place=place;
        this.name=name;
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
        convertView =li.inflate(R.layout.spinner_adapter_layout,null);
        h.tv= (TextView) convertView.findViewById(R.id.spinnerAdapterText);
        h.tv2= (TextView) convertView.findViewById(R.id.textView17);

        h.im= (ImageView) convertView.findViewById(R.id.imageView2);

        h.tv.setText(name.get(position).toUpperCase());
        h.tv2.setText("Address-"+place.get(position)+"\nTime-"+timeStamp.get(position));
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

        TextView tv,tv2;
        ImageView im;



    }
}
