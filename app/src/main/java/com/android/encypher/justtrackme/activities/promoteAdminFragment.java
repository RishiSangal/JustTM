package com.android.encypher.justtrackme.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.encypher.justtrackme.adapter.PromoteListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class promoteAdminFragment extends Fragment {

    String userId,circle_id;
    ArrayList<String> memId=new ArrayList<>();

    ProgressBar pr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promote_admin, container, false);
    }



    ListView lv;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        pr= (ProgressBar) getActivity().findViewById(R.id.promoteProgress);

        lv= (ListView) getActivity().findViewById(R.id.promoteList);

        groupDetails(getResources().getString(R.string.url) + "circleDetails&user_id=" + userId + "&circle_id=" + circle_id);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               promotePopup(position);
            }
        });





    }

    private void promotePopup(final int position) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Exit");
        alertDialogBuilder.setMessage("Are you sure? ");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                promoteAdmin(getActivity().getResources().getString(R.string.url)+"promoteAdmin&admin_id="+userId+"&circle_id="+circle_id
                                        +"&user_id="+memId.get(position)
                                );

                                Intent in = new Intent(getActivity(), HomeActivity.class);
                                startActivity(in);
                                getActivity().finish();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    public void details(String userId, String circle_id) {
       this.userId=userId;
        this.circle_id=circle_id;


    }


    ArrayList<String> memName=new ArrayList<>();

    private void groupDetails(String s) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Log.e(" url promote admin", s);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("gropuDetailsResponse", response);


                try {


                    JSONArray jsonArray = new JSONArray(new JSONObject(response).getString("response"));


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        memName.add(jo.getString("fname") + " " + jo.getString("lname"));
                        memId.add(jo.getString("id"));

                    }

                    Log.e("memName",memName.toString());
                    lv.setAdapter(new PromoteListAdapter(getActivity(),memName));
                    pr.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {


                    Log.e("JSONArray", e.toString());
                    e.printStackTrace();
//                  new HomeActivity.NoNetworkfrag();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pr.setVisibility(View.INVISIBLE);
//                NoNetworkfrag();

            }

        });

        queue.add(strReq);


    }
    private void promoteAdmin(String s) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Log.e("promoteAdmin url", s);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("promoteAdminResponse", response);


                try {


                    JSONArray jsonArray = new JSONArray(new JSONObject(response).getString("response"));






                    pr.setVisibility(View.INVISIBLE);




                    ;



                } catch (JSONException e) {


                    Log.e("JSONArray", e.toString());
                    e.printStackTrace();
//                  new HomeActivity.NoNetworkfrag();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pr.setVisibility(View.INVISIBLE);
//                NoNetworkfrag();

            }

        });

        queue.add(strReq);


    }
}
