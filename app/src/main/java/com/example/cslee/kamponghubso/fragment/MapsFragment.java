package com.example.cslee.kamponghubso.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cslee.kamponghubso.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;
    public static final String SHOP_LATITUDE_KEY = "shop_latitude_key";
    public static final String SHOP_LONGTITUDE_KEY = "shop_longtitude_key";

    private String mLat;
    private String mLong;
    private LatLng latLng;

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps, container, false);
        // Get long/lat key from intent
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mLat = bundle.getString(SHOP_LATITUDE_KEY, null);
            mLong = bundle.getString(SHOP_LONGTITUDE_KEY, null);

        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView)mView.findViewById(R.id.map);
        if (mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if ((mLat!=null && !mLat.isEmpty()) && (mLong!=null && !mLong.isEmpty())){
            latLng = new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLong));
        }else{
            latLng = new LatLng(40.689247, -74.044502);
        }
        googleMap.addMarker(new MarkerOptions().position(latLng)
                .title("statue")
                .snippet("Woohoo"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        googleMap.animateCamera(cameraUpdate);
    }
}
