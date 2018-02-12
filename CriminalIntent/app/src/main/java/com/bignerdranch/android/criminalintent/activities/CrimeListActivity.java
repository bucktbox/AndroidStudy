package com.bignerdranch.android.criminalintent.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.fragments.CrimeFragment;
import com.bignerdranch.android.criminalintent.fragments.CrimeListFragment;
import com.bignerdranch.android.criminalintent.models.Crime;

/**
 * Created by Kyeongil Han on 2018-01-26.
 */

public class CrimeListActivity extends SingleFragmentAbstractActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
