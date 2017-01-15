package com.android.encypher.justtrackme.nevigationdrawer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.encypher.justtrackme.utility.CustomVolleyRequest;
import com.android.volley.AuthFailureError;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SettingsFragment extends Fragment {
    
    EditText signupFirstName,SignUpLastName,email,phone;
    SharedPreferences sharedPreferences;
    String usr,fname,lname,emai,phn,image;
    NetworkImageView img;
    Bitmap photo;
    String userId;

    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toast.makeText(getActivity(),"click on image to change profile picture",Toast.LENGTH_SHORT).show();
//        userName= (EditText) getActivity().findViewById(R.id.username);
        signupFirstName= (EditText) getActivity().findViewById(R.id.signUpFirstName);
        SignUpLastName= (EditText) getActivity().findViewById(R.id.signUpLastName);
        phone= (EditText) getActivity().findViewById(R.id.pass1);
        btn= (Button) getActivity().findViewById(R.id.signupButton);
        email= (EditText) getActivity().findViewById(R.id.Emailorphone);
        sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
        usr=sharedPreferences.getString("userName","00");
        fname=sharedPreferences.getString("fname","00");
        lname=sharedPreferences.getString("lname","00");
        emai=sharedPreferences.getString("email","000");
        phn=sharedPreferences.getString("phone","00");
        userId=sharedPreferences.getString("userId","00");
        image=sharedPreferences.getString("image","00");
//        userName.setText(usr);
        signupFirstName.setText(fname);
        SignUpLastName.setText(lname);
        phone.setText(phn);
        email.setText(emai);
        img= (NetworkImageView) getActivity().findViewById(R.id.imageOfUser);

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity())
                .getImageLoader();
        imageLoader.get(getResources().getString(R.string.imagesurl)+image, ImageLoader.getImageListener(img,
                R.drawable.hamburger, android.R.drawable
                        .ic_dialog_alert));
        img.setImageUrl(getResources().getString(R.string.imagesurl)+image, imageLoader);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(photo!=null) {
                    sendDataToServer(getStringImage(photo));
                }else{
                    sendDataToServer("");
                }

            }
        });



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickImage();

            }
        });
        
        


    }

    private void sendDataToServer(final String stringImage) {

        final ProgressDialog pDialog = new ProgressDialog(getActivity());


        pDialog.setMessage("Loading...");
        pDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                getResources().getString(R.string.url)+"editProfile"
                , new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e( "response",response);

                try {

                    Toast.makeText(getActivity(),new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();

                    if(new JSONObject(response).getString("success").equals("1")){

                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("fname",fname);
                        editor.putString("lname",lname);
                        editor.putString("email",email.getText().toString());
                        editor.putString("phone",phone.getText().toString());
                        editor.putString("image",new JSONObject(response).getString("image"));
                        editor.apply();
                        Intent in=new Intent(getActivity(), HomeActivity.class);
                        startActivity(in);
                        pDialog.hide();
                    }

                } catch (JSONException e) {

                    Log.e("edit profile",e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e( "Error: " + error.getMessage());
                pDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new Hashtable<>();
                params.put("email",email.getText().toString());
                params.put("mobile",phone.getText().toString());
                params.put("fname",signupFirstName.getText().toString());
                params.put("lname",SignUpLastName.getText().toString());
                params.put("user_id",userId);
                params.put("image",stringImage);

                return params;
            }
        };

        queue.add(strReq);
    }

    public void pickImage() {
        if (getActivity() == null && isGooglePhotosInstalled(getActivity())) {
            Log.e("Settings ","jsd");
            // get available share intents
            List<Intent> targets = new ArrayList<>();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.putExtra("return-data", true);
            List<ResolveInfo> candidates = getActivity().getPackageManager().queryIntentActivities(intent, 0);

            for (ResolveInfo candidate : candidates) {
                String packageName = candidate.activityInfo.packageName;
                if (packageName.equals("com.google.android.apps.photos") || packageName.equals("com.google.android.apps.plus")) {
                    Intent iWantThis = new Intent();
                    Log.e("Settingsyry ","jsd");
                    iWantThis.setType("image/*");
                    iWantThis.setAction(Intent.ACTION_PICK);
                    iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    iWantThis.setPackage(packageName);
                    targets.add(iWantThis);
                }
            }
            Intent chooser = Intent.createChooser(targets.get(0), "Select Picture");
            chooser.putExtra(Intent.ACTION_PICK, targets.toArray(new Parcelable[targets.size()]));
            startActivityForResult(chooser, 1);
        }
    else{


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
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
    }
    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
    public static boolean isGooglePhotosInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(GOOGLE_PHOTOS_PACKAGE_NAME, PackageManager.GET_ACTIVITIES) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        Log.e("ImageString",encodedImage);
        return encodedImage;
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != -1) {
            return;
        }
        if (requestCode == 1) {
            Log.e("photo","kdfnkj");

            final Bundle extras = data.getExtras();
            Log.e("photo","kdfnkj"+extras+"jdfn");

            if (extras != null) {
                photo = extras.getParcelable("data");
                Log.e("photo",photo.toString());
                photo=getclip(photo);
                img.setImageBitmap(photo);
            }
        }


    }
}
