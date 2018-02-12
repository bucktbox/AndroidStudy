package com.bignerdranch.android.photogallery.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.photogallery.fragments.PhotoPageFragment;

/**
 * Created by Kyeongil Han on 2018-02-08.
 */

public class PhotoPageActivity extends SingleFragmentAbstractActivity {

    public static Intent newIntent(Context context, Uri photoPageUri) {
        Intent intent = new Intent(context, PhotoPageActivity.class);
        intent.setData(photoPageUri);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return PhotoPageFragment.newInstance(getIntent().getData());
    }
}
