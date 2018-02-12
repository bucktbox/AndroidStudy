package com.bignerdranch.android.criminalintent.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.models.Crime;
import com.bignerdranch.android.criminalintent.models.CrimeLab;

import java.util.List;

/**
 * Created by Kyeongil Han on 2018-01-26.
 */

public class CrimeListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subTitle";
    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private TextView mEmptyTextView;
    private CrimeAdapter mCrimeAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView)view.findViewById(R.id.crime_recycler_view);
        mEmptyTextView = (TextView)view.findViewById(R.id.crime_list_empty_text_view);

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CRIME) {
            Log.d(CrimeListFragment.class.getName(), "activity result received");
            Toast.makeText(getActivity(), "리스트로 돌아왔습니다", Toast.LENGTH_SHORT).show();

            updateUI();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subTitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible) subTitleItem.setTitle(R.string.hide_subtitle);
        else subTitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                /*Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivityForResult(intent, REQUEST_CRIME);*/
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;

            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        @SuppressLint("StringFormatMatches") String subTitle = getString(R.string.subtitle_format, crimeCount);

        if (!mSubtitleVisible) subTitle = null;

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.getSupportActionBar().setSubtitle(subTitle);
    }

    public void updateUI() {

        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        /*if (crimes.size() == 0) {
            Intent intent = new Intent(getActivity(), CrimeEmptyActivity.class);
            startActivity(intent);
        } else*/
        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.setCrimes(crimes);
            mCrimeAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            if (mCrimes.size() == 0) {
                Log.d("crimelist", "crime list size 0");
                mCrimeRecyclerView.setVisibility(View.INVISIBLE);
                mEmptyTextView.setVisibility(View.VISIBLE);
            } else {
                Log.d("crimelist", "crime list size = " + mCrimes.size());
                mCrimeRecyclerView.setVisibility(View.VISIBLE);
                mEmptyTextView.setVisibility(View.INVISIBLE);
            }
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)itemView.findViewById(R.id.list_item_crime_solved_check_box);

            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mCrime.setSolved(b);
                    CrimeLab.getInstance(getActivity()).updateCrime(mCrime);
                }
            });
        }

        public void bindCrime(Crime crime) {

            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " 선택됨!", Toast.LENGTH_SHORT).show();

            /*Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);*/
            mCallbacks.onCrimeSelected(mCrime);
        }


    }

}
