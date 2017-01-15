package com.android.encypher.justtrackme.nevigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.utility.CustomVolleyRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class GlobalShareFragment extends Fragment {

    NetworkImageView img;
    TextView tv;
    Switch aSwitch;
    int flag=0;
    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_global_share,container,false);



    }
    String url,imageUrl;
    String usr;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ImageView back= (ImageView) getActivity().findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                // temperoray
                getActivity().startActivity( new Intent(getActivity(),HomeActivity.class));

            }
        });

        img= (NetworkImageView) getActivity().findViewById(R.id.gsUserImage);
        tv= (TextView) getActivity().findViewById(R.id.gsUserName);
        aSwitch= (Switch) getActivity().findViewById(R.id.gsSwitch1);
        sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        usr=sharedPreferences.getString("userId","sdh");
        imageUrl=sharedPreferences.getString("image","");
        TextView fname= (TextView) getActivity().findViewById(R.id.fNmae1);
        fname.setText(sharedPreferences.getString("fname","Not Found"));
        TextView lname= (TextView) getActivity().findViewById(R.id.lname);
        lname.setText(sharedPreferences.getString("lname","Not Found"));
        TextView mobile= (TextView) getActivity().findViewById(R.id.mob);
        String str=sharedPreferences.getString("phone","Not Found");
        mobile.setText( Html.fromHtml("<b>"+str+"</b>"));
        TextView email= (TextView) getActivity().findViewById(R.id.emai);
        email.setText(sharedPreferences.getString("email","Not Found"));
        Button btn= (Button) getActivity().findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment s = new SettingsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });
        String gs=sharedPreferences.getString("gs","sdh");

        Log.e("globalShare",gs);


       url=getResources().getString(R.string.url);

       ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity())
                .getImageLoader();

        imageLoader.get(getResources().getString(R.string.imagesurl) + imageUrl, ImageLoader.getImageListener(img,
                R.drawable.hamburger, android.R.drawable
                        .ic_dialog_alert));
        img.setImageUrl(getResources().getString(R.string.imagesurl) + imageUrl, imageLoader);

        tv.setText(sharedPreferences.getString("userName","sdh"));

        if(gs.equals("public")){
            Log.e("sharedPref",gs);
            aSwitch.setText("ON");
            aSwitch.setChecked(true);
            flag=1;
        }else{
            Log.e("sharedPrefELSE",gs);
            aSwitch.setText("OFF");
            aSwitch.setChecked(false);
            flag=1;

        }
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    changeincircleshare(url + "changeglobalshare&user_id=" + usr + "&status=" , "public");
                    aSwitch.setText("ON");

                } else{
                    changeincircleshare(url + "changeglobalshare&user_id=" + usr + "&status=" ,"private");
                    aSwitch.setText("OFF");


                }
            }
        });






    }




    private void changeincircleshare(final String s, final String status) {

        Log.e("url=",s);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Log.e("GS URL+",s+status);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                s+status
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("gloabal share response", response);


                pDialog.hide();
                String str;

                if(status.equals("public")){
                    str="public";
                    Toast.makeText(getActivity(),"Master Sharing ON",Toast.LENGTH_SHORT).show();

                }else{
                    str="private";
                    Toast.makeText(getActivity(),"Master Sharing OFF",Toast.LENGTH_SHORT).show();

                }

                try {
                    if(new JSONObject(response).getString("message").equals("1"))
                    Toast.makeText(getActivity(),"Your Global Location Sharing is "+str, Toast.LENGTH_LONG).show();
                    editor.putString("gs",str);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.hide();

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
                pDialog.hide();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);
    }
}
