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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;
import org.json.JSONException;
import org.json.JSONObject;


public class GPSFragment extends Fragment {

    String url="";
    String number;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        url=getArguments().getString("pos");
        Log.e("url gps fragmentn ation",url);
        number=getArguments().getString("number");
    }

    ListView lv;
    TextView info;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_g, container, false);
        lv= (ListView) v.findViewById(R.id.gpsList);
        info= (TextView) v.findViewById(R.id.textView18);
        getDAta(url);
        return v;
    }

    public void getDAta(String s) {

        final ProgressDialog pDialog=new ProgressDialog(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        Log.e("urPGSGPSFragmentnjk",url);
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {

                Log.e("response in GPSlocation",response);
                 try {
                        Log.e("response gps ", new JSONObject(response).getString("message"));

                     if("0".equals( new JSONObject(response).getString("success"))){
                         info.setText( new JSONObject(response).getString("message"));
                     }else{

                     }

                        Toast.makeText(getActivity(),new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();



                        pDialog.hide();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("error in myLocation", e.toString());
                        pDialog.hide();
                        new HomeActivity().NoNetworkfrag();
                    }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(
                        getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);
    }

    public Fragment newInstance(String s, String i) {

        Bundle args = new Bundle();
        args.putString("pos", s);
        args.putString("number",i);

        LocationFragment fragment = new LocationFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
