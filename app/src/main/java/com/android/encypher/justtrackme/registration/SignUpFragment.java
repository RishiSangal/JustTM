package com.android.encypher.justtrackme.registration;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.encypher.justtrackme.adapter.Communicator;
import com.android.encypher.justtrackme.R;

import java.io.ByteArrayOutputStream;


public class SignUpFragment extends Fragment {

    EditText pass1,pass2,edEmail,fname,lname;
    String email,phone;
//    TextView tv;
    String j="";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        email= (EditText) getActivity().findViewById(R.id.signUpEmail);
        pass1= (EditText) getActivity().findViewById(R.id.pass1);
        pass2= (EditText) getActivity().findViewById(R.id.username);
//        tv= (TextView) getActivity().findViewById(R.id.textView111);
        edEmail= (EditText) getActivity().findViewById(R.id.Emailorphone);
        fname= (EditText) getActivity().findViewById(R.id.signUpFirstName);
        lname= (EditText) getActivity().findViewById(R.id.signUpLastName);

//        tv.setText(email);
        if(j.equals("Email")){
//            edEmail.setHint("Phone");
            edEmail.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);


        }else{
//            edEmail.setHint("Email");
            edEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        }

        Button btn= (Button) getActivity().findViewById(R.id.signupButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if( pass1.getText().toString().isEmpty() || pass2.getText().toString().isEmpty() || edEmail.getText().toString().isEmpty()
                        || fname.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"please fill all the entries",Toast.LENGTH_LONG).show();
                }else{

                    if(j.equals("Email")){
                     phone=edEmail.getText().toString();
                        Log.e("phone=",phone);
                    }else{
//                        phone=email;
                        email=edEmail.getText().toString();
                        Log.e("phone=",phone);
                        Log.e("email=",email);
                    }

                        String url =
                                getResources().getString(R.string.url)+"signup" +
                                        "&email=" +email+
                                        "&mobile="+phone+
                                        "&country="+country+
                                        "&password=" +pass1.getText().toString()+
                                        "&fname="+fname.getText().toString()+
                                        "&lname="+lname.getText().toString()+
                                        "&username="+pass2.getText().toString();
                    Log.e("Url=",url);

                    Communicator cm = (Communicator) getActivity();
                    cm.email(email,phone,pass1.getText().toString(),fname.getText().toString(),lname.getText().toString(),
                    pass2.getText().toString(),country);

//                        pDialog.setMessage("Loading...");
//                        pDialog.show();
//                        RequestQueue queue = Volley.newRequestQueue(getActivity());
//                        StringRequest strReq = new StringRequest(Request.Method.GET,
//                                url, new Response.Listener<String>() {
//
//                            @Override
//                            public void onResponse(String response) {
//                                Log.e( "response",response);
//                                pDialog.hide();
//                                try {
//
//                                    Toast.makeText(getActivity(),new JSONObject(response).getString("message"),Toast.LENGTH_LONG).show();
//
//                                    if(new JSONObject(response).getString("success").equals("1")){
//
//                                        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("piyush",Context.MODE_PRIVATE);
//                                        SharedPreferences.Editor editor=sharedPreferences.edit();
//                                        editor.putString("login","success");
//                                        editor.putString("userId",pass2.getText().toString());
//                                        editor.apply();
//
//                                        Intent in=new Intent(getActivity(), HomeActivity.class);
//                                        startActivity(in);
//                                        getActivity().finish();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                VolleyLog.e( "Error: " + error.getMessage());
//                                pDialog.hide();
//                            }
//                        });
//
//                  queue.add(strReq);



                }
            }
        });

//
//        Button btn1= (Button) getActivity().findViewById(R.id.LoginButton);
//
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginFragment s=new LoginFragment();
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
//
//
//
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void change(String i) {
    email=i;


    }

    String country="+91";

    public void change(String i, String j) {
        this.j=j;
        if(j.equals("Email")){
            email=i;
        }else{

//            String str[]=i.split("-");

//            country="+91";
            phone=i;

        }
    }
}
