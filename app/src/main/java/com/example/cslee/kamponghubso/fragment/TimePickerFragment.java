package com.example.cslee.kamponghubso.fragment;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;

import java.text.DecimalFormat;
import java.util.Calendar;
import android.widget.TimePicker;

import com.example.cslee.kamponghubso.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener  {

    public static final int FLAG_START_TIME = 0;
    public static final int FLAG_END_TIME = 1;

    private int flag = 0;

    //Interface created for communicating this dialog fragment events to called fragment
    public interface TimePickerDialogFragmentEvents{
        void onTimeSelected(String time, int flag);
    }
    TimePickerDialogFragmentEvents tpdfe;
    public void setTimePickerDialogFragmentEvents(TimePickerDialogFragmentEvents tpdfe){
        this.tpdfe = tpdfe;
    }

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
//Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
     /*   return new TimePickerDialog(getActivity(),this, hour, minute,
        DateFormat.is24HourFormat(getActivity()));*/


        // TimePickerDialog Theme : THEME_DEVICE_DEFAULT_LIGHT
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,hour,minute,false);

        // TimePickerDialog Theme : THEME_DEVICE_DEFAULT_DARK
        TimePickerDialog tpd2 = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,this,hour,minute,false);

        // TimePickerDialog Theme : THEME_HOLO_DARK
        TimePickerDialog tpd3 = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_DARK,this,hour,minute,false);

        // TimePickerDialog Theme : THEME_HOLO_LIGHT
        TimePickerDialog tpd4 = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT,this,hour,minute,true);

        // TimePickerDialog Theme : THEME_TRADITIONAL
        TimePickerDialog tpd5 = new TimePickerDialog(getActivity(),
                AlertDialog.THEME_TRADITIONAL,this,hour,minute,false);
        // Return the TimePickerDialog
        return tpd4;
    }
    public void setFlag(int i) {
        flag = i;
    }
    //onTimeSet() callback method
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        //Calculate am/pm
        String format;
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        }
        else if (hourOfDay == 12) {
            format = "PM";
        }
        else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        }
        else {
            format = "AM";
        }
        //Do something with the user chosen time
        DecimalFormat formatter = new DecimalFormat("00");
        String hourFormatted = formatter.format(hourOfDay);
        String minFormatted = formatter.format(minute);

        tpdfe.onTimeSelected(hourFormatted+":"+minFormatted+format,flag); //Changed
    }
}
