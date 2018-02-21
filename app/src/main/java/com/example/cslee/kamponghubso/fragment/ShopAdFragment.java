package com.example.cslee.kamponghubso.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cslee.kamponghubso.R;
import com.example.cslee.kamponghubso.utilities.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopAdFragment extends Fragment {

private Button btnToken;
private TextView textViewToken;
    public ShopAdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Ads");
        View view = inflater.inflate(R.layout.fragment_shop_ad, container, false);
        textViewToken=(TextView)view.findViewById(R.id.textViewToken);
        btnToken = (Button) view.findViewById(R.id.btnToken);
        btnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting token from shared preferences
                String token = SharedPrefManager.getInstance(getActivity()).getDeviceToken();

                //if token is not null
                if (token != null) {
                    //displaying the token
                    textViewToken.setText(token);
                } else {
                    //if token is null that means something wrong
                    textViewToken.setText("Token not generated");
                }
            }
        });
        return view;
    }

}
