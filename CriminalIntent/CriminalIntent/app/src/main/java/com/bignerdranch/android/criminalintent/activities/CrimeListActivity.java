package com.bignerdranch.android.criminalintent.activities;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.fragments.CrimeListFragment;

/**
 * Created by Kyeongil Han on 2018-01-26.
 */

public class CrimeListActivity extends SingleFragmentAbstractActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
