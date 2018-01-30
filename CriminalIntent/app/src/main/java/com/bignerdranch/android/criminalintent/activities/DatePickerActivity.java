package com.bignerdranch.android.criminalintent.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.fragments.DatePickerChallengeFragment;

/**
 * Created by Kyeongil Han on 2018-01-30.
 */

public class DatePickerActivity extends SingleFragmentAbstractActivity {

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, DatePickerActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return new DatePickerChallengeFragment();
    }
}
