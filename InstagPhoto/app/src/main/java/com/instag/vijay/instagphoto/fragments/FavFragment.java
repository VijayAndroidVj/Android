package com.instag.vijay.instagphoto.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.instag.vijay.instagphoto.CommonUtil;
import com.instag.vijay.instagphoto.FavModel;
import com.instag.vijay.instagphoto.MainActivity;
import com.instag.vijay.instagphoto.PreferenceUtil;
import com.instag.vijay.instagphoto.R;
import com.instag.vijay.instagphoto.adapter.MeetingAdapter;
import com.instag.vijay.instagphoto.retrofit.ApiClient;
import com.instag.vijay.instagphoto.retrofit.ApiInterface;

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

    private boolean followers = false;

    private void refreshItems() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            MainActivity.showProgress(activity);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ArrayList<FavModel>> call = apiService.follow_followers(preferenceUtil.getUserMailId(), followers);
            call.enqueue(new Callback<ArrayList<FavModel>>() {
                @Override
                public void onResponse(Call<ArrayList<FavModel>> call, Response<ArrayList<FavModel>> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    MainActivity.dismissProgress();
                    ArrayList<FavModel> sigInResponse = response.body();
                    if (sigInResponse != null) {
                        ilist = sigInResponse;
                        if (sigInResponse.size() > 0) {
                            setList();
                        } else {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<FavModel>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    MainActivity.dismissProgress();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            MainActivity.dismissProgress();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    ProgressDialog progress;

    public void showProgress() {
        if (progress == null) {
            progress = new ProgressDialog(activity);
            progress.setMessage("Checking please wait... ");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    public void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        try {
            setList();
            // Stop refresh animation
            swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
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

    public void setInfoText(String text) {
        viewInfo.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        viewInfo.setText(text);
    }


    public void hideRefresh() {
        try {
            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<FavModel> ilist = new ArrayList<>();

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            MeetingAdapter logAdapter = new MeetingAdapter(activity, ilist, followers);
            recyclerView.setAdapter(logAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (ilist.size() == 0) {
                if (followers) {
                    showView(1, "No followers available");
                } else {
                    showView(1, "No one followed you");
                }
            } else {
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
                    if (ilist != null) {
                        ilist.clear();
                    }
                    viewUpcoming.setBackgroundResource(R.drawable.left_round_corner_rect_blue);
                    viewPast.setBackgroundResource(R.drawable.right_round_corner_rect_white);
                    txtUpcoming.setTextColor(CommonUtil.getColorWrapper(activity, R.color.white));
                    txtPast.setTextColor(CommonUtil.getColorWrapper(activity, R.color.black));
                    followers = false;
                    refreshItems();
                    break;
                case R.id.viewPast:
                    if (ilist != null) {
                        ilist.clear();
                    }
                    followers = true;
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
