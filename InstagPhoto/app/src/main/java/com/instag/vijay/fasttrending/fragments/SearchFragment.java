package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.CategoryItemListActivity;
import com.instag.vijay.fasttrending.adapter.GridElementAdapter;
import com.instag.vijay.fasttrending.adapter.SearchPostAdapter;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 21/11/17.
 */

public class SearchFragment extends Fragment {

    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GridView horizontalGridView;
    private ArrayList<CategoryMain> categoryMainArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        horizontalGridView = view.findViewById(R.id.gridView);
        viewInfo = (TextView) view.findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
                getCategoryList();
            }
        });
        getCategoryList();
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
                        String message = t.getMessage();
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
        try {
            gridViewSetting();
//            horizontalGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//            horizontalGridView.setNumColumns(categoryMainArrayList.size());
            GridElementAdapter gridElementAdapter = new GridElementAdapter(activity, categoryMainArrayList);
            horizontalGridView.setAdapter(gridElementAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gridViewSetting() {

        // this is size of your list with data
        int size = categoryMainArrayList.size();
        // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
        int width = 60;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        horizontalGridView.setLayoutParams(params);
        horizontalGridView.setColumnWidth(singleItemWidth);
        horizontalGridView.setHorizontalSpacing(2);
        horizontalGridView.setStretchMode(GridView.STRETCH_SPACING);
        horizontalGridView.setNumColumns(size);

    }

    public void refreshItems() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<PostModelMain> call = apiService.getsearchpost(preferenceUtil.getUserMailId());
            call.enqueue(new Callback<PostModelMain>() {
                @Override
                public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    PostModelMain sigInResponse = response.body();
                    if (sigInResponse != null) {
                        ilist = sigInResponse.getPostsArrayList();
                        setList();
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<PostModelMain> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    progressBar.setVisibility(View.GONE);
                    String message = t.getMessage();
                    swipeRefreshLayout.setRefreshing(false);
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else if (message.contains("Expected")) {
                        message = "No List available";
                    } else {
                        message = "Failed";
                    }
                    setList();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private Activity activity;

    public void showView(int item, String text) {
        try {
            switch (item) {
                case 0:
                    progressBar.setVisibility(View.VISIBLE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                        viewInfo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        if (TextUtils.isEmpty(text)) {
                            viewInfo.setText("No user available");
                        } else {
                            viewInfo.setText(text);
                        }
                    }
                    break;
                case 2:
                    progressBar.setVisibility(View.GONE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    setList();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Posts> ilist = new ArrayList<>();

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            SearchPostAdapter logAdapter = new SearchPostAdapter(activity, ilist);
            recyclerView.setAdapter(logAdapter);
            StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (ilist.size() == 0) {
                showView(1, "No user available");
            } else {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static SearchFragment searchFragment;

    public static SearchFragment getInstance() {
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        return searchFragment;
    }
}
