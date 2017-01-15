package com.android.encypher.justtrackme.home;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.encypher.justtrackme.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class GroupDetailsFragment extends Fragment implements AbsListView.OnScrollListener  {



    ArrayList<String>  name=new ArrayList<>();

    private int lastTopValue = 0;
    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    ImageView transparentImageView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_details,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        name.add("sdbjvsjk");
        name.add("sdbjvsjk");
        name.add("sdbjvsjk");
        name.add("sdbjvsjk");



        final ListView listView= (ListView) getActivity().findViewById(R.id.groupList1);


        // inflate custom header and attach it to the list
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_test_layout, listView, false);
        listView.addHeaderView(header, null, false);


         supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.headerListFragment);
        final LatLng latLng=new LatLng(28.581056,77.3175023);
        googleMap.addMarker(new MarkerOptions().position(latLng)).setIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.hamburger));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));






                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                listView.requestDisallowInterceptTouchEvent(true);
                            }
                        });
         transparentImageView = (ImageView) header.findViewById(R.id.transparent_image);
//
        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        listView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        listView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        listView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,name));
        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),"clicked"+position,Toast.LENGTH_LONG).show();
            }
        });
//



    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Rect rect = new Rect();

//         view.getLocalVisibleRect(rect);
        transparentImageView.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            transparentImageView.setY((float) (rect.top / 2.0));
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.headerListFragment));
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}
