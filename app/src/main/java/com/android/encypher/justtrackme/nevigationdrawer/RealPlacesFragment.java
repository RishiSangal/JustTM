package com.android.encypher.justtrackme.nevigationdrawer;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.encypher.justtrackme.activities.PlaceMapActivity;
import com.android.encypher.justtrackme.adapter.PlaceAdapter;
import com.android.encypher.justtrackme.home.HomeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RealPlacesFragment extends Fragment {

    ListView lv;

    TextView tv;
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_real_places,container,false);

    }
    int x=0;
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> place=new ArrayList<>();
    ArrayList<String> latitude=new ArrayList<>();
    ArrayList<String> longitude=new ArrayList<>();
    ArrayList<String> placeId=new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences=getActivity().getSharedPreferences("piyush", Context.MODE_PRIVATE);

        ImageView back= (ImageView) getActivity().findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);



            }
        });

        tv= (TextView) getActivity().findViewById(R.id.textView8);
        lv= (ListView) getActivity().findViewById(R.id.placesList);

        callingApi(sharedPreferences.getString("userId","null"));



        fab= (FloatingActionButton) getActivity().findViewById(R.id.placesAdd);
        x=0;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlaceName();
            }
        });

      

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(getActivity(), PlaceMapActivity.class);
                in.putExtra("place",name.get(position));
                in.putExtra("lat",latitude.get(position));
                in.putExtra("lon",longitude.get(position));
                in.putExtra("placeId",placeId.get(position));
                getActivity().startActivity(in);
            }
        });


    }



    private void callingApi( String userId) {

        String url=getResources().getString(R.string.url);
        final ProgressDialog pDialog=new ProgressDialog(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url+"mylocations&user_id="+userId+"&limit=20&offset="+x, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "response in RealPlace",response);
                try {


                    if(new JSONObject(response).getString("success").equals("1")){

                        JSONArray ja=new JSONArray(new JSONObject(response).getString("checkins"));


                        if(ja.length()>19){
                            Button btnLoadMore = new Button(getActivity());
                            btnLoadMore.setText("Load More");
                            lv.addFooterView(btnLoadMore);
                        }



                        for(int i=0;i<ja.length();i++){
                            JSONObject jo=ja.getJSONObject(i);
                            name.add(jo.getString("name"));
                            place.add(jo.getString("place"));
                            latitude.add(jo.getString("latitude"));
                            longitude.add(jo.getString("longitude"));
                            placeId.add(jo.getString("id"));
                            Log.e("shared status",jo.getString("name"));

                        }



                        lv.setAdapter(new PlaceAdapter(getActivity(),name,place));// make new adapter for this
                        pDialog.hide();

                    }else {
                        pDialog.hide();
                        tv.setVisibility(View.VISIBLE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.hide();
                    tv.setVisibility(View.VISIBLE);


                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                pDialog.hide();
                tv.setVisibility(View.VISIBLE);
                tv.setText("Check your Internet Settings.");

            }
        });

        queue.add(strReq);




    }

    private void addPlaceName() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setTitle("Add Place Name");
        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setMessage("Add Places Like Office, Home etc.");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setHint("Enter Place name");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!(userInput.getText().toString().isEmpty())) {



                                        String CheckInName = userInput.getText().toString();
                                    Intent in =new Intent(getActivity(), PlaceMapActivity.class);
                                    in.putExtra("place",CheckInName);
                                    in.putExtra("lat",sharedPreferences.getString("lat","0.0"));
                                    in.putExtra("lon",sharedPreferences.getString("lon","0.0"));
                                    in.putExtra("placeId","0");
                                    getActivity().startActivity(in);



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

}
