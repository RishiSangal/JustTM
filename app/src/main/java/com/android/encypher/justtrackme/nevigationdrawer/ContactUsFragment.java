package com.android.encypher.justtrackme.nevigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.encypher.justtrackme.R;


public class ContactUsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final EditText edt= (EditText) getActivity().findViewById(R.id.contactEdittext);

        Button btn= (Button) getActivity().findViewById(R.id.contactButton);

        ImageView img= (ImageView) getActivity().findViewById(R.id.back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               HelpFragment s = new HelpFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.commit();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt.getText().toString().isEmpty()){


                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@justtrack.me"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Form");
                    intent.putExtra(Intent.EXTRA_TEXT, edt.getText().toString());
                    startActivity(Intent.createChooser(intent, "Send Email"));

                }else{
                    Toast.makeText(getActivity(),"Enter your request",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
