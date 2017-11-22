package com.instag.vijay.instagphoto.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.instag.vijay.instagphoto.fragments.FavFragment;
import com.instag.vijay.instagphoto.fragments.NewsfeedFragment;
import com.instag.vijay.instagphoto.fragments.PhotoFragment;
import com.instag.vijay.instagphoto.fragments.ProfileFragment;
import com.instag.vijay.instagphoto.fragments.SearchFragment;

/**
 * Created by vijay on 21/11/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewsfeedFragment newsfeedFragment = new NewsfeedFragment();
                return newsfeedFragment;
            case 1:
                SearchFragment searchFragment = new SearchFragment();
                return searchFragment;
            case 2:
                PhotoFragment photoFragment = new PhotoFragment();
                return photoFragment;
            case 3:
                FavFragment favFragment = new FavFragment();
                return favFragment;
            case 4:
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}