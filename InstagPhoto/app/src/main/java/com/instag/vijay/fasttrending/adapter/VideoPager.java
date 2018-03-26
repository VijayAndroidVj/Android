package com.instag.vijay.fasttrending.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.instag.vijay.fasttrending.fragments.VideoFragmentNew;

/**
 * Created by vijay on 26/3/18.
 */

public class VideoPager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    VideoFragmentNew[] videoFragmentNews;

    //Constructor to the class
    public VideoPager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        //Initializing tab count
        this.tabCount = NumOfTabs;
        videoFragmentNews = new VideoFragmentNew[tabCount];
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        //return videoFragmentNews[position] == null ? (videoFragmentNews[position] = new VideoFragmentNew(position)) : videoFragmentNews[position];

        switch (position) {
            case 0:
                return new Fragment();
            case 1:
                return VideoFragmentNew.getInstance();
            case 2:
                return new Fragment();

            default:
                return new Fragment();
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}