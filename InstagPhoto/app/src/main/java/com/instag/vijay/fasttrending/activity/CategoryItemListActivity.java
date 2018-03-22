package com.instag.vijay.fasttrending.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.adapter.CategoryListAdapter;
import com.instag.vijay.fasttrending.model.CategoryItem;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by vijay on 19/3/18.
 */

public class CategoryItemListActivity extends AppCompatActivity {

    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchactivity);
        activity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        view.findViewById(R.id.txtAppName).setVisibility(GONE);
        view.findViewById(R.id.iv_actionbar_noti).setVisibility(GONE);
        view.findViewById(R.id.searchview).setVisibility(GONE);
        final EditText searchEditText = view.findViewById(R.id.edtsearchview);
        searchEditText.setVisibility(VISIBLE);
//        searchEditText.setTextColor(getResources().getColor(R.color.black));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.grey1));
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // refreshItems(searchEditText.getText().toString());
            }
        });

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);
        viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // refreshItems(searchEditText.getText().toString());
            }
        });

        refreshItems();
    }


    private void refreshItems() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(VISIBLE);
            PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ArrayList<CategoryItem>> call = apiService.getcategoryItemList(preferenceUtil.getUserMailId());
            call.enqueue(new Callback<ArrayList<CategoryItem>>() {
                @Override
                public void onResponse(Call<ArrayList<CategoryItem>> call, Response<ArrayList<CategoryItem>> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(GONE);
                    ArrayList<CategoryItem> sigInResponse = response.body();
                    if (sigInResponse != null) {
                        ilist = sigInResponse;
                        setList();
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<CategoryItem>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    progressBar.setVisibility(GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(GONE);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
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

    private Activity activity;

//    public void refreshItems(final String query) {
//
//        if (CommonUtil.isNetworkAvailable(activity)) {
//            if (TextUtils.isEmpty(query)) {
//                Toast.makeText(activity, "Please type something to search", Toast.LENGTH_SHORT).show();
//            } else {
//                progressBar.setVisibility(VISIBLE);
//                viewInfo.setVisibility(GONE);
//                recyclerView.setVisibility(VISIBLE);
//                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);
//
//                ApiInterface apiService =
//                        ApiClient.getClient().create(ApiInterface.class);
//                Call<ArrayList<FavModel>> call = apiService.search_user(preferenceUtil.getUserMailId(), query);
//                call.enqueue(new Callback<ArrayList<FavModel>>() {
//                    @Override
//                    public void onResponse(Call<ArrayList<FavModel>> call, Response<ArrayList<FavModel>> response) {
//                        Log.d("", "response: " + response.body());
//                        swipeRefreshLayout.setRefreshing(false);
//                        progressBar.setVisibility(GONE);
//                        ArrayList<FavModel> sigInResponse = response.body();
//                        if (sigInResponse != null) {
//                            ilist = sigInResponse;
//                            setList();
//                        } else {
//                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<ArrayList<FavModel>> call, Throwable t) {
//                        // Log error here since request failed
//                        Log.e("", t.toString());
//                        progressBar.setVisibility(GONE);
//                        swipeRefreshLayout.setRefreshing(false);
//                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } else {
//            progressBar.setVisibility(GONE);
//            swipeRefreshLayout.setRefreshing(false);
//            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
//        }
//    }


    public void showView(int item, String text) {
        try {
            switch (item) {
                case 0:
                    progressBar.setVisibility(VISIBLE);
                    viewInfo.setVisibility(GONE);
                    recyclerView.setVisibility(VISIBLE);
                    break;
                case 1:
                    if (progressBar != null) {
                        progressBar.setVisibility(GONE);
                        viewInfo.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                        if (TextUtils.isEmpty(text)) {
                            viewInfo.setText("No user available");
                        } else {
                            viewInfo.setText(text);
                        }
                    }
                    break;
                case 2:
                    progressBar.setVisibility(GONE);
                    viewInfo.setVisibility(GONE);
                    recyclerView.setVisibility(VISIBLE);
                    setList();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CategoryItem> ilist = new ArrayList<>();

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);

            CategoryListAdapter logAdapter = new CategoryListAdapter(activity, ilist);
            recyclerView.setAdapter(logAdapter);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recycleraddItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
            logAdapter.notifyDataSetChanged();
            if (ilist.size() == 0) {
                showView(1, "No user available");
            } else {
                progressBar.setVisibility(GONE);
                viewInfo.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static SearchActivity searchFragment;

    public static SearchActivity getInstance() {
        if (searchFragment == null) {
            searchFragment = new SearchActivity();
        }
        return searchFragment;
    }
}

