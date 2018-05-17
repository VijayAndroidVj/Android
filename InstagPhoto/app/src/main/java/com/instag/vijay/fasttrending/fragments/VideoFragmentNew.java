package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.adapter.TrendingAdapter;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by vijay on 26/3/18.
 */

public class VideoFragmentNew extends Fragment {

    int position;

    private TextView viewInfo;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static VideoFragmentNew videoFragmentNew;

    public static VideoFragmentNew getInstance() {
//        if (videoFragmentNew == null) {
        videoFragmentNew = new VideoFragmentNew();
//        }
        return videoFragmentNew;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        EditText searchEditText = view.findViewById(R.id.searchview);
//        searchEditText.setVisibility(VISIBLE);
////        searchEditText.setTextColor(getResources().getColor(R.color.black));
////        searchEditText.setHintTextColor(getResources().getColor(R.color.grey1));
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // refreshItems(searchEditText.getText().toString());
//                makeSearch(editable.toString());
//            }
//        });

        viewInfo = (TextView) view.findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                // refreshItems(searchEditText.getText().toString());
                refreshItems();
            }
        });

        refreshItems();
    }

    private void refreshItems() {
        activity = getActivity();
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<ArrayList<Posts>> call = apiService.getTrendingVideos("10");
            call.enqueue(new Callback<ArrayList<Posts>>() {
                @Override
                public void onResponse(Call<ArrayList<Posts>> call, Response<ArrayList<Posts>> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(GONE);
                    ArrayList<Posts> sigInResponse = response.body();
                    if (sigInResponse != null) {
                        ilist = sigInResponse;
                        setList();
                    } else {
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Posts>> call, Throwable t) {
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
                            viewInfo.setText("No data available");
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

    public ArrayList<Posts> ilist = new ArrayList<>();

    public void setList() {
        try {
            //ilist = getMeetingList(isPast ? PAST : UPCOMING);
            viewInfo.setVisibility(GONE);
            recyclerView.setVisibility(VISIBLE);
            recyclerView.setHasFixedSize(true);

            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerView.setLayoutManager(gaggeredGridLayoutManager);

            TrendingAdapter logAdapter = new TrendingAdapter(activity, ilist);

//            recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(logAdapter);
            logAdapter.notifyDataSetChanged();
            if (ilist.size() == 0) {
                showView(1, "No data available");
            } else {
                progressBar.setVisibility(GONE);
                viewInfo.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}