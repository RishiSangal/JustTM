package com.android.encypher.justtrackme.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.encypher.justtrackme.adapter.Communicator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;

import org.json.JSONException;
import org.json.JSONObject;


public class FlaotingFragment extends Fragment {

    SharedPreferences sharedPreferences;
    ProgressBar pr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flaoting,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



//        View myView = getActivity().findViewById(R.id.relativeLayout);
//
//// get the center for the clipping circle
//        int cx = myView.getWidth() / 2;
//        int cy = myView.getHeight() / 2;
//
//// get the final radius for the clipping circle
//        float finalRadius = (float) Math.hypot(cx, cy);
//
//// create the animator for this view (the start radius is zero)
//        Animator anim = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//        }
//
//// make the view visible and start the animation
//        myView.setVisibility(View.VISIBLE);
//        anim.start();
        sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
        pr= (ProgressBar) getActivity().findViewById(R.id.floatProgressBar);
        pr.setVisibility(View.INVISIBLE);
        FloatingActionButton fab1= (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        FloatingActionButton fab2= (FloatingActionButton) getActivity().findViewById(R.id.fab3);
        FloatingActionButton fab3= (FloatingActionButton) getActivity().findViewById(R.id.fab2);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCheckInName("1");

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Communicator cm = (Communicator) getActivity();
                cm.floatToConatct("home",circle_id);

            }
        });
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }


    private void addCheckInName(final String check) {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Add Place Name");
//        alertDialogBuilder.setMessage("Group Name Can Be Family,Friends...");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setHint("Enter Place name");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!(userInput.getText().toString().isEmpty())) {

                                    if (check.equals("1")) {

                                        String CheckInName = userInput.getText().toString();
                                        callingApi(userId, (getResources().getString(R.string.url) + "checkin&name=" + CheckInName + "&user_id=").replaceAll("\\s+", "%20"));

                                    }
                                } else {
                                    Toast.makeText(getActivity(), "Enter Name First", Toast.LENGTH_SHORT).show();
                                }


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

    private void callingApi(String userId, String s) {

        pr.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity());


        Log.e("checkin url", s + userId + "&long=" + sharedPreferences.getString("lon", "0.0") + "&lat=" + sharedPreferences.getString("lat", "0.0")
                + "&battery=" + sharedPreferences.getString("battery", "0") + "&place=" + sharedPreferences.getString("places", "null").replaceAll("\\s+", "%20") +
                "&time=" + sharedPreferences.getString("clock", "0") + "&name=kmvkfvn");

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s +
                        userId +
                        "&long=" + sharedPreferences.getString("lon", "0.0") +
                        "&lat=" + sharedPreferences.getString("lat", "0.0")
                        + "&battery=" + sharedPreferences.getString("battery", "0") +
                        "&place=" + sharedPreferences.getString("places", "null").replaceAll("\\s+", "%20") +
                        "&time=" + sharedPreferences.getString("clock", "0")
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("response", response);


                pr.setVisibility(View.INVISIBLE);


                try {
                    Toast.makeText(getActivity(), new JSONObject(response).getString("message"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pr.setVisibility(View.INVISIBLE);


            }
        });

        queue.add(strReq);


    }

    String userId,circle_id;

    public void data(String userId, String s) {
        this.userId=userId;
        circle_id = s;
    }
}
