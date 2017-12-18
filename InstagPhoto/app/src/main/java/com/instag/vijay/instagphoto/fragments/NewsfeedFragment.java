package com.instag.vijay.instagphoto.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.instagphoto.CommonUtil;
import com.instag.vijay.instagphoto.PreferenceUtil;
import com.instag.vijay.instagphoto.R;
import com.instag.vijay.instagphoto.adapter.PostAdapter;
import com.instag.vijay.instagphoto.model.PostModelMain;
import com.instag.vijay.instagphoto.model.Posts;
import com.instag.vijay.instagphoto.retrofit.ApiClient;
import com.instag.vijay.instagphoto.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 21/11/17.
 */

public class NewsfeedFragment extends Fragment {

    private Activity activity;
    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Posts> postsArrayList;
    private PostAdapter postAdapter;
    private static NewsfeedFragment newsfeedFragment;

    public static NewsfeedFragment getInstance() {
        if (newsfeedFragment == null) {
            newsfeedFragment = new NewsfeedFragment();
        }
        return newsfeedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.newsfeedfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        viewInfo = (TextView) view.findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        refreshItems();

    }

    private void refreshItems() {

        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                String usermail = preferenceUtil.getUserMailId();
                Call<PostModelMain> call = service.getmynewsfeed(usermail);
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        if (postModelMain != null) {
                            postsArrayList = postModelMain.getPostsArrayList();
                            setNewsfeedList();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostModelMain> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else {
                            message = "Failed";
                        }
                        setNewsfeedList();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                setNewsfeedList();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setNewsfeedList() {
        try {
            if (postsArrayList != null && postsArrayList.size() > 0) {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                viewInfo.setText("");

                postAdapter = new PostAdapter(activity, postsArrayList);
                recyclerView.setAdapter(postAdapter);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                postAdapter.notifyDataSetChanged();

            } else {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                viewInfo.setText("Your Feed is totally empty");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
