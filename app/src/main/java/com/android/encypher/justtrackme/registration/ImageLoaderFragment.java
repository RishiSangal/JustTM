package com.android.encypher.justtrackme.registration;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.activities.SmartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

@Deprecated
public class ImageLoaderFragment extends Fragment {

    private static final int IMAGE = 2222;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ImageView image;
    Button camera, gallery, contin;
    private static final int CAMERA_REQUEST = 1888;
    ProgressBar pr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_loader, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        permissionCheck();


        if(x==1){
            permissionCheck();
        }


        image = (ImageView) getActivity().findViewById(R.id.ilImage);
        camera = (Button) getActivity().findViewById(R.id.ilCamera);
        gallery = (Button) getActivity().findViewById(R.id.ilGallery);
        contin = (Button) getActivity().findViewById(R.id.ilcontinue);
        pr= (ProgressBar) getActivity().findViewById(R.id.progressBar2);
        pr.setVisibility(View.INVISIBLE);
        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(photo!=null) {

                    sendDataToServer(getStringImage(photo));
                }else{
                    Toast.makeText(getActivity(),"Choose profile picture",Toast.LENGTH_LONG).show();

                }

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                pickImage();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != -1) {
            return;
        }
        if (requestCode == 1) {

            Log.e("userimage","kfdmklfd");
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Log.e("userimage","kfdmklfd");
                photo = extras.getParcelable("data");
                photo=getclip(photo);
                image.setImageBitmap(photo);

            }
        }
        if (requestCode == CAMERA_REQUEST   ) {
            photo = (Bitmap) data.getExtras().get("data");
            photo = getclip(photo);
            image.setImageBitmap(photo);

        }

    }
    public  Bitmap getclip(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private void sendDataToServer(final String stringImage) {

        pr.setVisibility(View.VISIBLE);


                        RequestQueue queue = Volley.newRequestQueue(getActivity());
                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                getResources().getString(R.string.url)+"signup"
                                , new Response.Listener<String>() {

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
                                        editor.putString("gs","public");
                                        editor.putString("type","normal");
                                        editor.putString("fname",fname);
                                        editor.putString("lname",lname);
                                        editor.putString("userName",userName);
                                        editor.putString("email",finEmail);
                                        editor.putString("phone",phone);
                                        editor.putString("image",new JSONObject(response).getString("image"));
                                        editor.putString("country",country);
                                        editor.apply();
                                        Intent in=new Intent(getActivity(), SmartActivity.class);
                                        startActivity(in);
                                        getActivity().finish();
                                        pr.setVisibility(View.INVISIBLE);
                                    }else{
                                        pr.setVisibility(View.INVISIBLE);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Log.e("ImageLoader",e.toString());
                                    pr.setVisibility(View.INVISIBLE);


                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e( "Error: " + error.getMessage());
                                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String,String> params = new Hashtable<String, String>();

                                params.put("email",finEmail);
                                params.put("mobile",phone);
                                params.put("password",password);
                                params.put("fname",fname);
                                params.put("lname",lname);
                                params.put("username",userName);
                                params.put("image",stringImage);
                                params.put("country",country);

                                return params;
                            }
                        };

                  queue.add(strReq);
    }

    Bitmap photo;
    String finEmail="",phone="",password="",fname="",lname="",userName="";

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }
    String country;

    public void change(String email, String phone, String s, String s1, String url, String image, String country) {
        finEmail=email;
        this.phone=phone;
        password=s;
        fname=s1;
        lname=url;
        userName=image;
        this.country= country;

    }

    int x=0;

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void permissionCheck() {


        if ((int) Build.VERSION.SDK_INT > 22) {
            int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {

       x=1;

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }

            Log.e("hey", "permission tuut granted");


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    Toast.makeText(getActivity(), "permission not granted", Toast.LENGTH_SHORT).show();
                    permissionCheck();
                }
                return;
            }

        }
    }

}
