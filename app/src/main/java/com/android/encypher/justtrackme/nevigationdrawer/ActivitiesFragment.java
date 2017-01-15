package com.android.encypher.justtrackme.nevigationdrawer;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.adapter.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ActivitiesFragment extends Fragment {

    ArrayList<String>  place=new ArrayList<>();
    ArrayList<String>  timeStamp=new ArrayList<>();
    ArrayList<String>  name=new ArrayList<>();


    ListView lv;

    int limit=20;
    int offset=0;
    int maxSize;
    String userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activities,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);

        userId=sharedPreferences.getString("userId","1");

//        ImageView back= (ImageView) getActivity().findViewById(R.id.back);
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                getActivity().startActivity( new Intent(getActivity(),HomeActivity.class));
//
//            }
//        });

        lv= (ListView) getActivity().findViewById(R.id.activityList);
        Button btnLoadMore = new Button(getActivity());
        btnLoadMore.setText("Load More");
        lv.addFooterView(btnLoadMore);

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeincircleshare(getResources().getString(R.string.url)+"mycheckins&user_id="+userId
                        +"&limit="+limit+"&offset="+offset);

            }
        });


        changeincircleshare(getResources().getString(R.string.url)+"mycheckins&user_id="+userId
        +"&limit="+limit+"&offset="+offset);


    }

    public void changeincircleshare(String s) {

        final ProgressDialog pDialog=new ProgressDialog(getActivity());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Log.e("checkin url",s);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                Log.e( "Activity -response",new JSONObject(response).getString("checkins"));


                    JSONArray ja=new JSONArray(new JSONObject(response).getString("checkins"));

                    for(int i=0;i<ja.length();i++){
                        JSONObject jo=ja.getJSONObject(i);
                        place.add(jo.getString("place"));
                        Log.e("log in acti",jo.getString("place"));
                        name.add(jo.getString("name"));


                        timeStamp.add(jo.getString("timestamp"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pDialog.hide();

                //gfhjkghjk check null or not

                lv.setAdapter(new SpinnerAdapter(getActivity(),place,timeStamp,name));
                
                offset=offset+19;



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);
    }

   String  conversion(String str){
       long unixSeconds = Long.parseLong(str);
       Date date = new Date(unixSeconds*1000L); // *1000 is to convert seconds to milliseconds
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss z"); // the format of your date
//       sdf.setTimeZone(TimeZone.getTimeZone("GMT-5:30")); // give a timezone reference for formating (see comment at the bottom
       return sdf.format(date);
   }

}
