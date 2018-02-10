package com.example.cslee.kamponghubso.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.cslee.kamponghubso.R;

/**
 * Fragment is to allow shop owner to create shop
 * Details include:
 * ShopName, ShopImage, ShopAddress, ShopPostal
 * Calculated: shopLatitude, shopLongtitude, zone
 */
public class CreateShopFragment extends Fragment implements View.OnClickListener, TimePickerFragment.TimePickerDialogFragmentEvents {
    private TextView startTime;
    private TextView endTime;


    public CreateShopFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_shop, container, false);
        startTime = (TextView) view.findViewById(R.id.startTime);
        endTime = (TextView) view.findViewById(R.id.endTime);

        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.startTime) {
            TimePickerFragment startTimeFragment = new TimePickerFragment();
            startTimeFragment.setFlag(TimePickerFragment.FLAG_START_TIME);
            startTimeFragment.setTimePickerDialogFragmentEvents(CreateShopFragment.this); //Changed
            startTimeFragment.show(getActivity().getFragmentManager(), "Time Picker");
        } else if (id == R.id.endTime) {
            TimePickerFragment endTimeFragment = new TimePickerFragment();
            endTimeFragment.setFlag(TimePickerFragment.FLAG_END_TIME);
            endTimeFragment.setTimePickerDialogFragmentEvents(CreateShopFragment.this); //Changed
            endTimeFragment.show(getActivity().getFragmentManager(), "Time Picker");
        }
    }

    @Override
    public void onTimeSelected(String time, int flag) {
        if (flag == 0) {
            startTime.setText(time);
        } else if (flag == 1) {
            endTime.setText(time);
        }
    }

}
