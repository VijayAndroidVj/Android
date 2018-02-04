package com.peeyemcar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peeyem.app.R;
import com.peeyemcar.adapter.PostAdapter;
import com.peeyemcar.models.Vechile;
import com.peeyemcar.retrofit.ApiClient;
import com.peeyemcar.retrofit.ApiInterface;
import com.peeyemcar.utils.PreferenceUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 4/2/18.
 */

public class MyVechle extends AppCompatActivity {
    private PreferenceUtil preferenceUtil;
    private Activity activity;
    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Vechile> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        activity = this;
        preferenceUtil = new PreferenceUtil(activity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("MyVechle");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getMyNotifications();
            }
        });
        getMyNotifications();
    }

    private void getMyNotifications() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<ArrayList<Vechile>> call = apiService.getmyvechile("2", preferenceUtil.getUserEmail(), preferenceUtil.getUserPhone());
                call.enqueue(new Callback<ArrayList<Vechile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Vechile>> call, Response<ArrayList<Vechile>> response) {
                        Log.d("", "response: " + response.body());
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        ArrayList<Vechile> sigInResponse = response.body();
                        if (sigInResponse != null) {
                            list = sigInResponse;
                            setList();
                        } else {
                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Vechile>> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("", t.toString());
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        list = new ArrayList<>();
                        setList();
                        Toast.makeText(activity, "No result found", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PostAdapter logAdapter;

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            logAdapter = new PostAdapter(activity, list);
            recyclerView.setAdapter(logAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (list.size() == 0) {
                showView(1, "No result available");
                progressBar.setVisibility(View.GONE);
            } else {
                viewInfo.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ProgressDialog progressDoalog;

    private void initProgress(String title) {
        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage(title);
            progressDoalog.setTitle(activity.getString(R.string.app_name));
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
        } else {
            progressDoalog.hide();
            progressDoalog = null;
        }
    }

    private void closeProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.hide();
        progressDoalog = null;
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
                            viewInfo.setText("No list available");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

