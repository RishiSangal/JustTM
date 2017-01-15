package com.android.encypher.justtrackme.locations_fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.encypher.justtrackme.adapter.TwoWayViewAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationFragment extends Fragment {
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> gps = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();

    String url = "";
    String number;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url = getArguments().getString("pos");
        Log.e("url fragment Location", url);
        number = getArguments().getString("number");
    }

    ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location, container, false);
        lv = (ListView) v.findViewById(R.id.locationList);
//        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush", Context.MODE_PRIVATE);

        getDAta(url);


        return v;
    }

    public void getDAta(String s) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.e("urlationHistoryFragment", url);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                Log.e("response in my location", response);

                String res = null;

                if (number.equals("0")) {

                    try {
                        Log.e("respons LocationFragmnt", new JSONObject(response).getString("message"));

                        JSONArray ja = new JSONArray(new JSONObject(response).getString("checkins"));
                        location.clear();
                        time.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            time.add(jo.getString("timestamp"));
                            location.add(jo.getString("place"));
//                            Log.e("location=", location.get(i) + "\n" + time.get(i));


                        }


                        pDialog.hide();
                        TwoWayViewAdapter adapter = new TwoWayViewAdapter(getActivity(), location, time);
                        lv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error in myLocation", e.toString());
                        pDialog.hide();


                    }
//                } else if (number.equals("1")) {
//
//
//                    try {
//                        res = new JSONObject(response).getString("message");
//                        Log.e("response gps", new JSONObject(response).getString("message"));
//
//                        JSONArray ja = new JSONArray(new JSONObject(response).getString("checkins"));
//
//                        for (int i = 0; i < ja.length(); i++) {
//                            JSONObject jo = ja.getJSONObject(i);
////                            time.add(jo.getString("timestamp"));
////                            location.add(jo.getString("place"));
////                            Log.e("location=", location.get(i) + "\n" + time.get(i));
//                        }
//
//
//                        pDialog.hide();
////                        TwoWayViewAdapter adapter = new TwoWayViewAdapter(getActivity(), location, time);
////                        lv.setAdapter(adapter);
////                        adapter.notifyDataSetChanged();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Log.e("error in myLocation", e.toString());
//                        pDialog.hide();
//
//
//                    }
//
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(
                        getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);
    }

    public Fragment newInstance(String s, String i) {

        Bundle args = new Bundle();
        args.putString("pos", s);
        args.putString("number", i);

        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
