package com.bignerdranch.android.criminalintent.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Callbacks mCallbacks;

    private String mTime;

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void returnResult() {
        Intent data = new Intent();
        getActivity().setResult(Activity.RESULT_OK, data);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null) {
            mTime = savedInstanceState.getString(DIALOG_TIME);
        }

        UUID crimeId = (UUID)getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);

        returnResult();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("crimeFragment", "onPause");
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DIALOG_TIME, mTime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_remove_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(mCrime.getId());
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText)view.findViewById(R.id.crime_title);
        mDateButton = (Button)view.findViewById(R.id.crime_date);
        mTimeButton = (Button)view.findViewById(R.id.crime_time);
        mSolvedCheckBox = (CheckBox)view.findViewById(R.id.crime_solved);
        mReportButton = (Button)view.findViewById(R.id.crime_report);
        mSuspectButton = (Button)view.findViewById(R.id.crime_suspect);

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
                updateCrime();
                /*Intent intent = TimePickerActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_TIME);*/

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
                updateCrime();
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
                /*Intent intent = DatePickerActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_DATE);*/
            }
        });

        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                updateCrime();
            }
        });

        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));*/

                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .createChooserIntent();
                startActivity(intent);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if(mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        updateDate();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == REQUEST_DATE) {
            Log.d("crimeFragment", "update Date");
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            updateDate();
        }

        if(requestCode == REQUEST_TIME) {
            mTime = (String)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mTimeButton.setText(mTime);
        }

        if(requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                if (c != null && c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                c.close();
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());

        if(mTime != null) {
            mTimeButton.setText(mTime);
        }
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = String.valueOf(android.text.format.DateFormat.format(dateFormat, mCrime.getDate()));

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void updateCrime() {
        CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }
}
