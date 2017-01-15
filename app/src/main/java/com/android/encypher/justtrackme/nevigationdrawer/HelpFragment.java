package com.android.encypher.justtrackme.nevigationdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.home.HomeActivity;


public class HelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView back= (ImageView) getActivity().findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               getActivity().startActivity( new Intent(getActivity(),HomeActivity.class));

            }
        });

        TextView tv= (TextView) getActivity().findViewById(R.id.aboutUs);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://justtrack.me/legal/terms_and_policy"));
                startActivity(browserIntent);
            }
        });

        TextView tv1= (TextView) getActivity().findViewById(R.id.aboutAppl);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://justtrack.me/faqs"));
                startActivity(browserIntent);
            }
        });

        TextView tv2= (TextView) getActivity().findViewById(R.id.helpDoc);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("http://justtrack.me/about_us"));
                startActivity(browserIntent);
            }
        });
        TextView tv4= (TextView) getActivity().findViewById(R.id.contactUs);
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUsFragment s = new ContactUsFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.drawer_layout, s);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });



    }
}
