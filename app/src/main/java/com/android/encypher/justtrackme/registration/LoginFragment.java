package com.android.encypher.justtrackme.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.activities.SmartActivity;
import com.android.encypher.justtrackme.home.HomeActivity;
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



public class LoginFragment extends Fragment {

    EditText loginEmail,password;

    String usr;
   ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btn= (Button) getActivity().findViewById(R.id.login);
        loginEmail= (EditText) getActivity().findViewById(R.id.loginEmail);
        password= (EditText) getActivity().findViewById(R.id.loginPassword);

        TextView fPass= (TextView) getActivity().findViewById(R.id.fPassword);
        fPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmail();

            }
        });


//        Button sign= (Button) getActivity().findViewById(R.id.sign);
//        sign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ImageLoaderFragment s=new ImageLoaderFragment();
//                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack so the user can navigate back
//                transaction.replace(R.id.fragment_container, s);
//                transaction.addToBackStack(null);
//
//// Commit the transaction
//                transaction.commit();
//            }
//        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(getActivity());


                if(!(loginEmail.getText().toString().isEmpty() )){

                    if(!(password.getText().toString().isEmpty())){

                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        usr=loginEmail.getText().toString();

                        String url = getResources().getString(R.string.url)+"login&username="+usr
                                +"&password="+password.getText().toString();


                        RequestQueue queue = Volley.newRequestQueue(getActivity());

                        StringRequest strReq = new StringRequest(Request.Method.GET,
                                url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e( "response",response);

                                try {
                                    Toast.makeText(getActivity(),new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();


                                    if(new JSONObject(response).getString("success").equals("1")){

                                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("login","success");
                                        editor.putString("userId",new JSONObject(response).getString("user_id"));
                                        editor.putString("type",new JSONObject(response).getString("type"));
                                        String str;
                                        if("1".equals(new JSONObject(response).getString("global_share"))){
                                            str="public";
                                        }else{
                                            str="private";
                                        }
                                        editor.putString("gs",str);
                                        editor.putString("userName",usr);
                                        editor.putString("email",new JSONObject(response).getString("email"));
                                        editor.putString("phone",new JSONObject(response).getString("mobile"));
                                        editor.putString("image",new JSONObject(response).getString("image"));
                                        editor.putString("fname",new JSONObject(response).getString("fname"));
                                        editor.putString("lname",new JSONObject(response).getString("lname"));
                                        editor.apply();

                                        Intent in=new Intent(getActivity(), SmartActivity.class);
                                        startActivity(in);
                                        getActivity().finish();
                                    }else{
                                        pDialog.hide();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();

                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e( "Error in volley Login: " + error.getMessage());
                                pDialog.hide();
                            }
                        });

                        queue.add(strReq);


                    }else {
                        Toast.makeText(getActivity(),"Enter password",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getActivity(),"Enter user name",Toast.LENGTH_LONG).show();

                }


            }


        });
    }
    private void addEmail() {

        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setIcon(R.drawable.logo);
        alertDialogBuilder.setTitle("Enter Email");
//        alertDialogBuilder.setMessage("Group Name Can Be Family,Friends...");
        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        userInput.setHint("Enter Email");
        alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!(userInput.getText().toString().isEmpty())) {



                                        String CheckInName = userInput.getText().toString().trim();

                                    callingApi(getActivity().getResources().getString(R.string.url)+"forgetPassword$email="+CheckInName);


                                } else {
                                    Toast.makeText(getActivity(), "Enter Email First", Toast.LENGTH_SHORT).show();
                                    try {
                                        dialog.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
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


    private void callingApi(String userId) {

        String url = getResources().getString(R.string.url);
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        Log.e("fotgotPasswordURL", userId);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                 userId, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                Log.e("response of circle name", response);

                try {

                    Toast.makeText(getActivity(),new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();

                    Log.e("Spinner value", new JSONObject(response).getString("circles"));


                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.e("Spinner value", e.toString());

                    new HomeActivity().NoNetworkfrag();

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: " + error.getMessage());
               new HomeActivity().NoNetworkfrag();

            }
        });

        queue.add(strReq);


    }
}
