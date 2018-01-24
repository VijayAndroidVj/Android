package com.peeyem.honda.peeyamyamaha;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.peeyem.honda.peeyamyamaha.adapter.MeetingAdapter;
import com.peeyem.honda.peeyamyamaha.models.CarModel;
import com.peeyem.honda.peeyamyamaha.models.EventResponse;
import com.peeyem.honda.peeyamyamaha.retrofit.ApiClient;
import com.peeyem.honda.peeyamyamaha.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 8/1/18.
 */

public class UsedCarsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private Activity activity;
    private ArrayList<CarModel> postsArrayList = new ArrayList<>();
    private MeetingAdapter postAdapter;
    private TextView viewInfo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.product_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Used Cars");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        recyclerView.setVisibility(View.VISIBLE);
        getMyPosts();
    }

    private void getMyPosts() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<EventResponse> call = service.get_used_cars("1");
                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        EventResponse postModelMain = response.body();
                        if (postModelMain != null) {
                            postsArrayList = postModelMain.getItems();
                        }
                        setList();
                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No list available";
                        } else {
                            message = "Failed";
                        }
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList() {
        try {
            viewInfo.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            postAdapter = new MeetingAdapter(activity, postsArrayList);
            recyclerView.setAdapter(postAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            postAdapter.notifyDataSetChanged();
            recyclerView.setNestedScrollingEnabled(false);
            if (postsArrayList.size() == 0) {
                showView(1, "No List available");
            } else {
                progressBar.setVisibility(View.GONE);
                viewInfo.setVisibility(View.GONE);
            }
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
                        viewInfo.setText(text);
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
