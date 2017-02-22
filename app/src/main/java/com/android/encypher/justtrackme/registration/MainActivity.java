package com.android.encypher.justtrackme.registration;

import android.app.FragmentManager;
import android.os.Bundle;

import com.android.encypher.justtrackme.R;
import com.android.encypher.justtrackme.activities.BaseActivity;
import com.android.encypher.justtrackme.adapter.Communicator;

@Deprecated
public class MainActivity extends BaseActivity implements Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.scree_n));

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        SplashFragment loginFragment =  new SplashFragment();
//        NoNetworkFragment loginFragment =  new NoNetworkFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, loginFragment).commit();

    }

    @Override
    public void response(String i) {

        EmailFragment s = new EmailFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_right);
        transaction.replace(R.id.fragment_container, s);
        transaction.addToBackStack(null);
        transaction.commit();
        s.change(i);

    }



    @Override
    public void emial(String i) {

        SignUpFragment s = new SignUpFragment();
//                SplashFragment ws=new SplashFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);
        transaction.replace(R.id.fragment_container, s);
        transaction.addToBackStack(null);
        transaction.commit();

        s.change(i);
    }

    @Override
    public void emial(String i, String j) {
        SignUpFragment s = new SignUpFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_left);
        transaction.replace(R.id.fragment_container, s);
        transaction.addToBackStack(null);
        transaction.commit();

        s.change(i,j);
    }

    @Override
    public void email(String email, String phone, String s, String s1, String s2, String s3,String country) {

        ImageLoaderFragment sa = new ImageLoaderFragment();
//                SplashFragment ws=new SplashFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right);

// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, sa);
        transaction.addToBackStack(null);
//                 transaction.remove(ws);
        transaction.commit();

        sa.change(email,phone,s,s1,s2,s3,country);
    }

    @Override
    public void floatToConatct(String home, String circle_id) {

    }


}

