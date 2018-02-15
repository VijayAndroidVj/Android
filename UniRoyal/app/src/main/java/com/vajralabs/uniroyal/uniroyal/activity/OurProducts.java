package com.vajralabs.uniroyal.uniroyal.activity;

import android.app.Activity;
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

import com.vajralabs.uniroyal.uniroyal.CommonUtil;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.adapter.NotificationAdapter;
import com.vajralabs.uniroyal.uniroyal.model.CategoryModel;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiClient;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 14/2/18.
 */

public class OurProducts extends AppCompatActivity {
    private Activity activity;
    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<CategoryModel> categoryArrayList = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Our Products");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getcategoryLists();
                swipeRefreshLayout.setRefreshing(false);
                //setList();
            }
        });

        getcategoryLists();
    }

    private void getcategoryLists() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                progressBar.setVisibility(View.VISIBLE);
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<ArrayList<CategoryModel>> call = apiService.getcategoryList();
                call.enqueue(new Callback<ArrayList<CategoryModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                        Log.d("", "response: " + response.body());
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        ArrayList<CategoryModel> sigInResponse = response.body();
                        if (sigInResponse != null) {
                            categoryArrayList = sigInResponse;
                        } else {
                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }
                        setList();

                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("", t.toString());
                        setList();
                        progressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                viewInfo.setVisibility(View.VISIBLE);
                viewInfo.setText("Check your internet connection!");
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private NotificationAdapter logAdapter;


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
                            viewInfo.setText("No notification available");
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

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            logAdapter = new NotificationAdapter(activity, categoryArrayList);
            recyclerView.setAdapter(logAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (categoryArrayList.size() == 0) {
                showView(1, "No Category available");
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
