package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.adapter.SearchPostAdapter;
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

public class SearchFragment extends Fragment {

    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.searchfragment, container, false);
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
                refreshItems("");
            }
        });

        refreshItems();

    }

    private void refreshItems() {
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
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private Activity activity;

    public void refreshItems(final String query) {
        if (activity == null) {
            activity = getActivity();
        }
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
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
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


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
