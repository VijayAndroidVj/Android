package com.instag.vijay.fasttrending.fragments;

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

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.adapter.PostNewsfeedAdapter;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

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
    private ArrayList<CategoryMain> categoryMainArrayList;
    private PostNewsfeedAdapter postAdapter;
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

        viewInfo = view.findViewById(R.id.txtContactInfo);
        recyclerView = view.findViewById(R.id.recyclerviewContact);
        progressBar = view.findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
                getCategoryList();
            }
        });
        getCategoryList();
        refreshItems();

    }

    public void refreshItems() {

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
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
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

    public void getCategoryList() {

        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                String usermail = preferenceUtil.getUserMailId();
                Call<ArrayList<CategoryMain>> call = service.getcategory(usermail);
                call.enqueue(new Callback<ArrayList<CategoryMain>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CategoryMain>> call, Response<ArrayList<CategoryMain>> response) {
                        ArrayList<CategoryMain> postModelMain = response.body();
                        if (postModelMain != null) {
                            categoryMainArrayList = postModelMain;
                            setCategoryList();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryMain>> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.toString();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Newsfeed available";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                setCategoryList();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setCategoryList() {

    }


    private void setNewsfeedList() {
        try {
            if (postsArrayList != null && postsArrayList.size() > 0) {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                viewInfo.setText("");

                postAdapter = new PostNewsfeedAdapter(activity, postsArrayList, categoryMainArrayList);
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
