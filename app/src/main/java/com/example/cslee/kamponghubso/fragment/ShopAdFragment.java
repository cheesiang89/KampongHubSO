package com.example.cslee.kamponghubso.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cslee.kamponghubso.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopAdFragment extends Fragment {


    public ShopAdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Ads");
        View view = inflater.inflate(R.layout.fragment_shop_ad, container, false);
        return view;
    }

}