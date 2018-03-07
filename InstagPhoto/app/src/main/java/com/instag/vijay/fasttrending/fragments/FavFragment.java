package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.instag.vijay.fasttrending.adapter.NotificationAdapter;
import com.instag.vijay.fasttrending.model.Notification;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 21/11/17.
 */

public class FavFragment extends Fragment implements View.OnClickListener {
    Activity activity;
    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View viewUpcoming, viewPast;
    private TextView txtUpcoming, txtPast;

    private static FavFragment favFragment;

    public static FavFragment getInstance() {
        if (favFragment == null) {
            favFragment = new FavFragment();
        }
        return favFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favfragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();

        viewUpcoming = view.findViewById(R.id.viewUpcoming);
        viewPast = view.findViewById(R.id.viewPast);
        txtUpcoming = (TextView) view.findViewById(R.id.txtUpcoming);
        txtPast = (TextView) view.findViewById(R.id.txtPast);

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
        viewPast.setOnClickListener(this);
        viewUpcoming.setOnClickListener(this);

        refreshItems();


    }

    private boolean followers = true;

    private void refreshItems() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            Call<ArrayList<Notification>> call = apiService.getnotification(followers, preferenceUtil.getUserMailId());
            call.enqueue(new Callback<ArrayList<Notification>>() {
                @Override
                public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    ArrayList<Notification> sigInResponse = response.body();
                    if (sigInResponse != null) {
                        list = sigInResponse;
                        setList();
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
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
                            if (followers) {
                                viewInfo.setText("No followers available");
                            } else {
                                viewInfo.setText("No one followed you");
                            }
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

    public ArrayList<Notification> list = new ArrayList<>();

    private NotificationAdapter logAdapter;

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            logAdapter = new NotificationAdapter(activity, list);
            recyclerView.setAdapter(logAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (list.size() == 0) {
                showView(1, "No notification available");
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.viewUpcoming:
                    if (list != null) {
                        list.clear();
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    viewUpcoming.setBackgroundResource(R.drawable.left_round_corner_rect_blue);
                    viewPast.setBackgroundResource(R.drawable.right_round_corner_rect_white);
                    txtUpcoming.setTextColor(CommonUtil.getColorWrapper(activity, R.color.white));
                    txtPast.setTextColor(CommonUtil.getColorWrapper(activity, R.color.black));
                    followers = true;
                    refreshItems();
                    break;
                case R.id.viewPast:
                    if (list != null) {
                        list.clear();
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    viewInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    followers = false;
                    viewPast.setBackgroundResource(R.drawable.right_round_corner_rect_blue);
                    viewUpcoming.setBackgroundResource(R.drawable.left_round_corner_rect_white);
                    txtUpcoming.setTextColor(CommonUtil.getColorWrapper(activity, R.color.black));
                    txtPast.setTextColor(CommonUtil.getColorWrapper(activity, R.color.white));
                    refreshItems();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
