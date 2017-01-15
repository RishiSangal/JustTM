package com.android.encypher.justtrackme.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.adapter.ContactAdapter;
import com.android.encypher.justtrackme.home.HomeActivity;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    CoordinatorLayout cl;

    ArrayList<String>  name;
    ArrayList<String> number;
    ListView lv;
    ContactAdapter cd;
    String str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        str=getIntent().getExtras().getString("lData","0");



        name=new ArrayList<>();
        number=new ArrayList<>();
       lv= (ListView) findViewById(R.id.list);


//        Button fab = (Button) findViewById(R.id.invite);

//        if(str.equals("1")){
//            assert fab != null;
//            fab.setVisibility(View.INVISIBLE);
//        }
        cl= (CoordinatorLayout) findViewById(R.id.contactLayout);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if ( Build.VERSION.SDK_INT < 23)
        {
            contactsRecover();
        }else if (ContextCompat.checkSelfPermission(ContactActivity.this,
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ContactActivity.this,
                Manifest.permission.WRITE_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            contactsRecover();


        }else{

            ActivityCompat.requestPermissions(ContactActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

             if(str.equals("1")){
                 // call the api


             }




            }
        });

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
    ArrayList<String> array=new ArrayList<>();

    public void saveData(String str){
        array.add(str);
        Log.e("check",str);


    }

    private void contactsRecover() {
        ProgressDialog pDialog = new ProgressDialog(ContactActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        ContentResolver cr = getContentResolver();
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
                    name.add(name1+" "+number1);
                    pCur.close();
                }
            }
        }
        pDialog.cancel();

        Log.e("Size",name.size()+"   "+number.size());
         cd=new ContactAdapter(ContactActivity.this, name);
        Log.e("Size",name.size()+"   "+number.size());

        assert lv != null;
        Log.e("piyush","going for adpter");
        lv.setAdapter(cd);
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
                    Intent in=new Intent(ContactActivity.this,HomeActivity.class);
                    startActivity(in);
                }

            }


        }
    }

    public void deleteData(String s) {
        array.remove(s);
    }
}
