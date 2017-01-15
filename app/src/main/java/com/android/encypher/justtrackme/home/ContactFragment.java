package com.android.encypher.justtrackme.home;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.adapter.ContactAdapter;

import java.util.ArrayList;


public class ContactFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;


    ArrayList<String> name;
    ArrayList<String> name1=new ArrayList<>();

//    ArrayList<String> number;
    ListView lv;
//    RelativeLayout rl;
    ContactAdapter cd;
     EditText edt;
    ImageView imgC;
    String str="";
    int z=0;
    ProgressBar pr;
    ArrayList<String> newName=new ArrayList<>();
    String nameSearch="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
        userId=sharedPreferences.getString("userId","heh");
        pr= (ProgressBar) getActivity().findViewById(R.id.contactProgressBAr);

        pr.setVisibility(View.VISIBLE);
        Log.e("userId",userId);
        pr.setVisibility(View.VISIBLE);
        imgC= (ImageView) getActivity().findViewById(R.id.imageView7);


        name=new ArrayList<>();
//        number=new ArrayList<>();
        lv= (ListView) getActivity().findViewById(R.id.list1);
        edt= (EditText) getActivity().findViewById(R.id.ContectEditText);
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {


                String edtName=edt.getText().toString();

//                z=0;

                Log.e("charcter"+name.size(), String.valueOf(cs)+name.contains(edt.getText().toString()));

                   newName.clear();
                for(int i=0;i<name.size();i++){
                    Log.e("charcter12123", String.valueOf(cs)+name.get(i));

                    if(edtName.equalsIgnoreCase(name.get(i).substring(0,edtName.length()))){
                        newName.add(name.get(i));
//                        Log.e("new Name",newName.get(z));
//                        z++;
                    }

                }
//                newName.add(name.get(z));

//                cd.clear();
                cd = new ContactAdapter(getActivity(), newName, "life", cNumber, userId,circle_id);
                cd.notifyDataSetChanged();
                lv.setAdapter(cd);





            }

            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2,
                                          int arg3) {



            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });



        if ((int) Build.VERSION.SDK_INT < 23)
        {
            new Abc().execute();
        }else if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {

            new Abc().execute();


        }else{

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }





//
//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
//                                           int pos, long id) {
//                // TODO Auto-generated method stub
//
//                Log.e("long clicked","pos: " + pos);
////                arg1.setBackgroundColor(Color.parseColor("#222222"));
////                Log.e("Long Click",""+pos);
//
//                return true;
//            }
//        });
    }



//    private String jsonCon() {
//       JSONArray jsonArray =new JSONArray();
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
//            gpsDat.put("name",name);
//            gpsDat.put("mobile",mobile);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            jsonArray.put(gpsDat);
//        }
//
//        Log.e("contactJson=",""+jsonArray);
//
//        return  "&json="+jsonArray;
//
//    }
//
//    ArrayList<String> array=new ArrayList<>();
//
//    public void saveData(String str){
//        array.add(str);
//        Log.e("Array size hb=",array.size()+"");
//
//        Log.e("check",str);
//
//
//    }

    private void contactsRecover() {
      pr.setVisibility(View.VISIBLE);

        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);


        if (cur.getCount() > 0) {
            String number1="";
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name1=(cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME)));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        number1=(pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER)));
//                        int image=pCur.getInt(pCur.getColumnIndex(ContactsContract.DisplayPhoto.DISPLAY_MAX_DIM));
//                        Toast.makeText(ContactActivity.this, number.get(number.size()-1), Toast.LENGTH_SHORT).show();

                    }
                    name.add(name1+"~"+number1);
                    Log.e("contact",name1+"~"+number1);
                    pCur.close();
                }
            }
        }

        Log.e("in c Size",name.size()+"   ");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    contactsRecover();

                } else {
//                    Snackbar.make(cl, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                    Intent in=new Intent(getActivity(),HomeActivity.class);
                    startActivity(in);
                }

            }


        }
    }

    String cNumber;

    public void inten(String str, String cNumber) {
        this.str=str;
        this.cNumber=cNumber;
    }
    SharedPreferences sharedPreferences;
    String userId;
     String home="hey";
    String circle_id;
    public void check(String home, String s) {
        this.home=home;
        circle_id=s;
    }

    private class Abc extends AsyncTask<Void,Void,Void>{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pr.setVisibility(View.VISIBLE);
        Log.e("ContactFragment ","preExe");


    }

    @Override
    protected Void doInBackground(Void... params) {

        contactsRecover();

        return null;
    }

    @Override
    protected void onPostExecute(Void aoid) {
        super.onPostExecute(aoid);

        Log.e("ContactFragment ","sdfnkjdnf");

        pr.setVisibility(View.INVISIBLE);
        imgC.setVisibility(View.VISIBLE);
        edt.setVisibility(View.VISIBLE);

        if(home.equals("home")){
            cd = new ContactAdapter(getActivity(), name, "life", cNumber, userId,circle_id);

        }else {
            cd = new ContactAdapter(getActivity(), name, "lifeline", cNumber, userId);
        }
        Log.e("Size",""+name.size());

        assert lv != null;
        Log.e("piyush","going for adpter");
        lv.setAdapter(cd);



    }
}

}
