package com.android.encypher.justtrackme.nevigationdrawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.encypher.justtrackme.utility.CustomVolleyRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.encypher.justtrackme.R;


public class SahreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sahre,null);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        NetworkImageView im = (NetworkImageView) getActivity().findViewById(R.id.imageView6);

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(getActivity())
                .getImageLoader();
        imageLoader.get("http://www.wintellect.com/devcenter/wp-content/uploads/2015/09/share.jpg", ImageLoader.getImageListener(im,
                R.drawable.hamburger, android.R.drawable.ic_dialog_alert));
        assert im != null;
        im.setImageUrl("http://www.wintellect.com/devcenter/wp-content/uploads/2015/09/share.jpg", imageLoader);


        Button btn= (Button) getActivity().findViewById(R.id.shareButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is gps tracker.htttp://google.com");
                sendIntent.setType("text/plain");
                getActivity().startActivity(sendIntent);
            }
        });


    }
}
