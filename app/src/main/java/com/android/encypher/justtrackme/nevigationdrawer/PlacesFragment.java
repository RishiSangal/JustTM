package com.android.encypher.justtrackme.nevigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;


public class PlacesFragment extends Fragment {

    int limit=20;
    int offset=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
        changeincircleshare(getResources().getString(R.string.url)+"mylocations&user_id="+sharedPreferences.getString("userId","o")
        +"&limit="+limit+"&offset="+offset);


    }

    private void changeincircleshare(String s) {

        final ProgressDialog pDialog=new ProgressDialog(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "response",response);


                pDialog.hide();



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

}
