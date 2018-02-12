package com.bignerdranch.android.photogallery.activities;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.photogallery.fragments.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentAbstractActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
