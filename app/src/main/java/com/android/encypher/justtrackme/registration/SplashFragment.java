package com.android.encypher.justtrackme.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.adapter.Communicator;


public class SplashFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_welcome,container,false);
    }
    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button splashP= (Button) getActivity().findViewById(R.id.signupPhone);
        Button login= (Button) getActivity().findViewById(R.id.signlogin);


//        splashe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Communicator cm = (Communicator) getActivity();
//                cm.response("Email");
//
//            }
//        });
        splashP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Communicator cm = (Communicator) getActivity();
                cm.response("Phone");
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment s=new LoginFragment();
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_right);
//                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.replace(R.id.fragment_container, s);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

//        loginText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginFragment s=new LoginFragment();
//                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
////                transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_right);
//
//                transaction.replace(R.id.fragment_container, s);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        });




    }

//

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
