package com.instag.vijay.instagphoto.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instag.vijay.instagphoto.R;

/**
 * Created by vijay on 21/11/17.
 */

public class NewsfeedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.newsfeedfragment, container, false);
    }
}
