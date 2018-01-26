package com.bignerdranch.android.criminalintent.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.fragments.CrimeFragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentAbstractActivity {

    private static final String TAG = "CrimeActivity";

    private static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    private static final String EXTRA_CRIME_POSITION = "com.bignerdranch.android.criminalintent.crime_position";

    public static Intent newIntent(Context packageContext, UUID crimeId, int crimePosition) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, crimePosition);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        int crimePosition = (int) getIntent().getSerializableExtra(EXTRA_CRIME_POSITION);
        return CrimeFragment.newInstance(crimeId, crimePosition);
    }
}
