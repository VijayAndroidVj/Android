package com.instag.vijay.fasttrending.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.instag.vijay.fasttrending.fragments.FavFragment;
import com.instag.vijay.fasttrending.fragments.NewsfeedFragment;
import com.instag.vijay.fasttrending.fragments.PhotoFragment;
import com.instag.vijay.fasttrending.fragments.ProfileFragment;
import com.instag.vijay.fasttrending.fragments.SearchFragment;

/**
 * Created by vijay on 21/11/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return NewsfeedFragment.getInstance();
            case 1:
                return SearchFragment.getInstance();
            case 2:
                return PhotoFragment.getInstance();
            case 3:
                return FavFragment.getInstance();
            case 4:
                return ProfileFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}