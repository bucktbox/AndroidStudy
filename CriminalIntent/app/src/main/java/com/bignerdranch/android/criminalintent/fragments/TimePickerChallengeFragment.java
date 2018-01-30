package com.bignerdranch.android.criminalintent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Kyeongil Han on 2018-01-29.
 */

public class TimePickerChallengeFragment extends Fragment {

    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";

    private TimePicker mTimePicker;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_time_challenge, container, false);

        mButton = (Button)view.findViewById(R.id.dialog_time_challenge_button);
        mTimePicker = (TimePicker)view.findViewById(R.id.dialog_time_time_picker_challenge);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "시간이 선택되었습니다", Toast.LENGTH_SHORT).show();
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
                    setResult(dateFormat.format(date));
                } else {
                    setResult(date.toString());
                }
                getActivity().finish();
            }
        });

        return view;
    }

    /*@NonNull
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
*/
    private void setResult(String time) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getActivity().setResult(Activity.RESULT_OK, intent);
    }
}
