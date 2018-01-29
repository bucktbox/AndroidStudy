package com.bignerdranch.android.criminalintent.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.models.Crime;
import com.bignerdranch.android.criminalintent.models.CrimeLab;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Kyeongil Han on 2018-01-25.
 */

public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";
    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_CRIME_POSITION = "crime_position";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId, int crimePosition) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putSerializable(ARG_CRIME_POSITION, crimePosition);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static int getResultPosition(Intent result) {
        return (int) result.getSerializableExtra(ARG_CRIME_POSITION);
    }

    public void returnResult(int position) {
        Intent data = new Intent();
        data.putExtra(ARG_CRIME_POSITION, position);
        getActivity().setResult(Activity.RESULT_OK, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        int position = (int)getArguments().getSerializable(ARG_CRIME_POSITION);

        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);

        returnResult(position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText)view.findViewById(R.id.crime_title);
        mDateButton = (Button)view.findViewById(R.id.crime_date);
        mTimeButton = (Button)view.findViewById(R.id.crime_time);
        mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);

        updateDate();

        mTimeButton.setText("발생 시간 등록");
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                if (fragmentManager != null) {
                    timePickerFragment.show(fragmentManager, DIALOG_TIME);
                }

            }
        });

        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "textChanged");
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                if (fragmentManager != null) {
                    datePickerFragment.show(fragmentManager, DIALOG_DATE);
                }
            }
        });

        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }

        if(requestCode == REQUEST_TIME) {
            String time = (String)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mTimeButton.setText(time);
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
