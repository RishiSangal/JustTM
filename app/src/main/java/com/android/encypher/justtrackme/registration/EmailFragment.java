package com.android.encypher.justtrackme.registration;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.adapter.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class EmailFragment extends Fragment {

    String str;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return inflater.inflate(R.layout.fragment_email,container,false);
    }
    ArrayList<String> data = new ArrayList<>(233);
    String country;


    public void getCountryCode(){

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("countries.dat"), "UTF-8"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String str[]=line.split(",");
                data.add(str[0]+" (+"+str[2]+")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        TextView tv= (TextView) getActivity().findViewById(R.id.tetxEmail);
        Spinner  sp= (Spinner) getActivity().findViewById(R.id.spinner);

        new Adb().execute();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_dropdown_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  country=parent.getItemAtPosition(position).toString();

                Log.e("country",country);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button btn1= (Button) getActivity().findViewById(R.id.emailButton);
        final EditText edt= (EditText) getActivity().findViewById(R.id.emailEditText);
//        img= (ImageView) getActivity().findViewById(R.id.emaailImage);
        if(str.equals("Email")){
            edt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
//            tv.setText("Continue with your Email id");
            edt.setHint("Enter your email id");
        }else{
            edt.setHint("Mobile");
            edt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
//            tv.setText("Continue with your Mobile");
            LinearLayout rl= (LinearLayout) getActivity().findViewById(R.id.emailRel);
            sp.setVisibility(View.VISIBLE);
            edt.setInputType(InputType.TYPE_CLASS_NUMBER);

        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(edt.getText().toString().isEmpty())) {


                    if(str.equals("Email")) {

                        if (isValidEmailAddress(edt.getText().toString())) {
//                            img.setVisibility(View.VISIBLE);

                            Communicator cm = (Communicator) getActivity();
                            cm.emial(edt.getText().toString(),"Email");
                        } else {
                            Toast.makeText(getActivity(), "Enter your correct email id" , Toast.LENGTH_LONG).show();

                        }
                    }else{

                        if(edt.getText().toString().length()==10) {
                            Communicator cm = (Communicator) getActivity();
                            Log.e("phone",edt.getText().toString()+"-"+country);
                            cm.emial(edt.getText().toString(),"phone");
                        }else{
                            Toast.makeText(getActivity(), "Enter your correct mobile number", Toast.LENGTH_LONG).show();

                        }
                    }
                }else {
                    Toast.makeText(getActivity(),"Enter your "+str,Toast.LENGTH_LONG).show();
                }




            }
        });
    }

public boolean isValidEmailAddress(String email) {
    String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
    java.util.regex.Matcher m = p.matcher(email);
    return m.matches();
}
    public void change(String i) {
        str=i;
    }


    private  class  Adb extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            getCountryCode();
            return null;
        }
    }
}
