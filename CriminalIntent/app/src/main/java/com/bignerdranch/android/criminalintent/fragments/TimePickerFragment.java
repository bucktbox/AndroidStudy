package com.bignerdranch.android.criminalintent.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.bignerdranch.android.criminalintent.R;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Kyeongil Han on 2018-01-29.
 */

public class TimePickerFragment extends DialogFragment{

    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";

    private TimePicker mTimePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker)view.findViewById(R.id.dialog_time_time_picker);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int hour;
                        int minute;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }

                        Date date = new GregorianCalendar(0,0,0,hour,minute).getTime();

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                    "HH:mm", Locale.getDefault());
                            sendResult(Activity.RESULT_OK, dateFormat.format(date));
                        } else {
                            sendResult(Activity.RESULT_OK, date.toString());
                        }
                    }
                })
                .show();
    }

    private void sendResult(int resultCode, String time) {
        if(getTargetFragment() == null) return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
