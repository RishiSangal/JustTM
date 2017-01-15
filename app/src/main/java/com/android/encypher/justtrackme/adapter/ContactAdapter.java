package com.android.encypher.justtrackme.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.encypher.justtrackme.nevigationdrawer.LIfeLineActivity;
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

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by root on 16/6/16.
 */
public class ContactAdapter extends ArrayAdapter {
  public   ArrayList<String> name=new ArrayList<>();
//    ArrayList<String> number=new ArrayList<>();
private int selectedIndex;
    Context con;
    String Life="hello";
    String circle_id;
    String cNumber;
    String userId;

    public ContactAdapter(FragmentActivity activity, ArrayList<String> name, String lifeline, String cNumber, String userId) {
        super(activity, R.layout.contacts_list_item,name);
        con=activity;
        Collections.sort(name);
        this.name=name;
        Life=lifeline;
        this.cNumber=cNumber;
        this.userId=userId;
    }

    public ContactAdapter(FragmentActivity activity, ArrayList<String> name, String life, String cNumber, String userId, String circle_id) {
        super(activity, R.layout.contacts_list_item,name);
        con=activity;
        Collections.sort(name);
        this.name=name;
        Life=life;
        this.cNumber=cNumber;
        this.userId=userId;
        this.circle_id=circle_id;
    }


//    public void setSelectedIndex(int ind) {
//        selectedIndex = ind;
//        notifyDataSetChanged();
//    }

    public ContactAdapter(FragmentActivity activity, ArrayList<String> name) {

        super(activity, R.layout.contacts_list_item,name);
        con=activity;
        this.name=name;
        Collections.sort(name);

    }


    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {

        return name.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    String str1;
    String phn;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater li= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView =li.inflate(R.layout.contacts_list_item,null);
        final ViewHolder h=new ViewHolder();
         h.tv= (TextView) convertView.findViewById(R.id.text1);
        h.tv1= (TextView) convertView.findViewById(R.id.text2);
        h.im= (CheckBox) convertView.findViewById(R.id.checkBox);
        h.tv2= (TextView) convertView.findViewById(R.id.text3);
        h.fab= (ImageButton) convertView.findViewById(R.id.invite);
        h.im.setVisibility(View.VISIBLE);
        final String str=name.get(position);
        int x=str.lastIndexOf("~");
        str1=str.substring(x+1);
        phn=str.substring(0,x);
        h.tv.setText(phn);
        try {
            h.tv2.setText(str1);
        }catch (Exception e){
            e.printStackTrace();
        }
        h.tv1.setText(String.valueOf(name.get(position).charAt(0)));
//        h.tv1.setTextColor();

//        if (position == 0) {
//            h.tv1.setBackgroundColor(color);
//        }
//        else if (position % 2 == 1) {
//            h.tv1.setBackgroundColor(color);
//        }
//        else if (position % 2 == 0) {
//            h.tv1.setBackgroundColor(color);
//        }
       h.im.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(h.im.isChecked()) {
                 saveData(name.get(position));
               }else{
                deleteData(name.get(position));

               }
           }
       });
        if(Life.equals("lifeline")) {
            h.im.setVisibility(View.INVISIBLE);
            h.fab.setVisibility(View.VISIBLE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  String str=name.get(position);
                    int x=str.lastIndexOf("~");
                    str1=str.substring(x+1);
                    phn=str.substring(0,x);

                    phn=phn.replaceAll("\\s","");

                    String mob="";
                    try {
                        str1 = str1.replaceAll("\\s", "");
                        int a = str1.length();
                        mob = str1.substring(a - 10, a);
                        Log.e("mobile" + mob, " " + mob.length());
                    }catch (ArrayIndexOutOfBoundsException e){
                        Log.e("Array out",e.toString());
                        Toast.makeText(con,"Check your mobile number",Toast.LENGTH_LONG).show();
                    }

                    showAlertDialog("Emergency Contact","Add \""+phn+"\" to the Emergency Contact?",mob,phn);
                }
            });
            h.fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String str=name.get(position);
                    int x=str.lastIndexOf("~");
                    str1=str.substring(x+1);
                    String mob="";

                    try {
                        str1 = str1.replaceAll("\\s", "");
                        int a = str1.length();

                        mob = str1.substring(a - 10, a);
                        Log.e("mobile" + mob, " " + mob.length());
                    }catch (ArrayIndexOutOfBoundsException e){
                        Log.e("Array out",e.toString());
                        Toast.makeText(con,"Check your mobile number",Toast.LENGTH_LONG).show();
                    }
                    phn=str.substring(0,x);
                    phn=phn.replaceAll("\\s","");
                    showAlertDialog("Emergency Contact","Add \""+phn+"\" to the Emergency Contact?",mob,phn);
                }
            });
        }else {
            h.im.setVisibility(View.VISIBLE);

                h.fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String str=name.get(position);
                        int x=str.lastIndexOf("~");
                        str1=str.substring(x+1);
                        String mob = null;
                        SharedPreferences sharedPreferences=con.getSharedPreferences("piyush",Context.MODE_PRIVATE);
                        String country=sharedPreferences.getString("country","%2B91");

                        try {
                            str1 = str1.replaceAll("\\s", "");
                            int a = str1.length();

                            mob = str1.substring(a - 10, a);
                            Log.e("mobile" + mob, " " + mob.length());
                        }catch (ArrayIndexOutOfBoundsException e){
                            Log.e("Array out",e.toString());
                            Toast.makeText(con,"Check your mobile number",Toast.LENGTH_LONG).show();
                        }

                        callingAPI(con.getResources().getString(R.string.url) + "addMember&user_id=" + userId + "&circle_id=" + circle_id + "&mobile=" + mob + "&country=%2B91" );
//                        Intent sendIntent = new Intent();
//                        sendIntent.setAction(Intent.ACTION_SEND);
//                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is gps tracker.");
//                        sendIntent.setType("text/plain");
//                        con.startActivity(sendIntent);
                    }
                });
            }

        return convertView;
    }
    static class ViewHolder{

        TextView tv,tv1,tv2;
        CheckBox im;
        ImageButton fab;




}
    public void callingApi(String phone, final String name) {

        SharedPreferences sharedPreferences=con.getSharedPreferences("piyush",Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=sharedPreferences.edit();

     Log.e("Emergency",con.getResources().getString(R.string.url)+"AddEmergencyContact&user_id="+
             userId+"&name="+
             name.replaceAll("//s+","%20")+"&mobile="+phone.replaceAll("//s+","%20")+"&number="+cNumber+"&country=%2B91");
        RequestQueue queue = Volley.newRequestQueue(con);


        StringRequest strReq = new StringRequest(Request.Method.GET,con.getResources().getString(R.string.url)+"" +
                "AddEmergencyContact&user_id="+
                userId+"&name="+
                name.replaceAll("//s+","%20")+
                "&mobile="+phone.replaceAll("//s+","%20")+
                "&number="+cNumber+"&country=%2B91"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "response",response);


                try {
                    Toast.makeText(con,new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();
                    if(new JSONObject(response).getString("success").equals("1")){
//                        Log.e("Spinner value",new JSONObject(response).getString("circles"));


                        Intent in = new Intent(con, LIfeLineActivity.class);
                        editor.putString("cn"+cNumber,name);
                        editor.apply();

                        con.startActivity(in);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("emergency",e.toString());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
            }
        });

        queue.add(strReq);



    }

//    private String jsonCon() {
//        JSONArray jsonArray =new JSONArray();
//        String name,mobile;
//
//        JSONObject gpsDat=new JSONObject();
//
//
//        Log.e("Array size=",array.size()+"");
//
//        for(int i=0;i<array.size();i++) {
//
//            String str = array.get(i);
//            int x = str.lastIndexOf(" ");
//            mobile = str.substring(x);
//            name= str.substring(0, x);
//            Log.e("contactname=",""+name+mobile+""+x);
//
//            gpsDat=new JSONObject();
//            try {
//                gpsDat.put("name",name);
//                gpsDat.put("mobile",mobile);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            jsonArray.put(gpsDat);
//        }
//
//        Log.e("contactJson=",""+jsonArray);
//
//        return  ""+jsonArray;
//
//    }

    ArrayList<String> array=new ArrayList<>();

    public void saveData(String str){
        array.add(str);
        Log.e("Array size hb=",array.size()+"");

        Log.e("check",str);


    }
    public void deleteData(String s) {
        array.remove(s);

    }

    private void callingAPI(String string) {

        RequestQueue queue = Volley.newRequestQueue(con);
        Log.e("url===",string);


        StringRequest strReq = new StringRequest(Request.Method.GET,string
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "response",response);


                try {
                    Toast.makeText(con,new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();
                    Intent in=new Intent(con,HomeActivity.class);
                    con.startActivity(in);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                Toast.makeText(con,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        queue.add(strReq);


    }


    public void showAlertDialog(String title, String message, final String str, final String phn) {
        final AlertDialog alertDialog = new AlertDialog.Builder(con).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon(R.drawable.logo);


        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


//                Toast.makeText(con, "Your r/equested has been sent", Toast.LENGTH_LONG).show();

                callingApi(str,phn);







            }
        });


        alertDialog.show();
    }


}
