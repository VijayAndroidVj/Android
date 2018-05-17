package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.CategoryExploreActivity;
import com.instag.vijay.fasttrending.adapter.GridSearchElementAdapter;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

/**
 * Created by vijay on 21/11/17.
 */

public class SearchFragment extends Fragment {

    private GridView horizontalGridView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FlipperLayout flipperLayout;
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
        flipperLayout = view.findViewById(R.id.flipper_layout);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (categoryMainArrayList != null) {
                    categoryMainArrayList.clear();
                }
                getCategoryList();
            }
        });
        view.findViewById(R.id.exploreAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, CategoryExploreActivity.class);
                startActivity(intent);
            }
        });

        getCategoryList();
        getBanners();
    }

    private void getBanners() {
        try {

            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                PreferenceUtil preferenceUtil = new PreferenceUtil(activity);

                String usermail = preferenceUtil.getUserMailId();
                Call<ArrayList<CategoryMain>> call = service.getbanner(usermail);
                call.enqueue(new Callback<ArrayList<CategoryMain>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CategoryMain>> call, Response<ArrayList<CategoryMain>> response) {
                        ArrayList<CategoryMain> postModelMain = response.body();
                        if (postModelMain != null) {
                            for (int i = 0; i < postModelMain.size(); i++) {
                                CategoryMain categoryMain = postModelMain.get(i);
                                FlipperView view1 = new FlipperView(activity);
                                view1.setImageUrl("http://www.xooads.com/FEELOUTADMIN/img/upload/" + categoryMain.getImage())
                                        // .setImageDrawable(R.drawable.test) // Use one of setImageUrl() or setImageDrawable() functions, otherwise IllegalStateException will be thrown
                                        .setImageScaleType(ImageView.ScaleType.CENTER_CROP) //You can use any ScaleType
                                        .setDescription("")
                                        .setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                                            @Override
                                            public void onFlipperClick(FlipperView flipperView) {
                                                //Handle View Click here
                                            }
                                        });
                                flipperLayout.setScrollTimeInSec(3); //setting up scroll time, by default it's 3 seconds
//                                flipperLayout.getScrollTimeInSec(); //returns the scroll time in sec
//                                flipperLayout.getCurrentPagePosition(); //returns the current position of pager
                                flipperLayout.addFlipperView(view1);
                            }
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
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CategoryMain>> call, Throwable t) {
                        // Log error here since request failed
                        swipeRefreshLayout.setRefreshing(false);
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
                setCategoryList();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private void setCategoryList() {
        try {
            gridViewSetting();
            horizontalGridView.setNumColumns(4);
            GridSearchElementAdapter gridElementAdapter = new GridSearchElementAdapter(activity, categoryMainArrayList);
            horizontalGridView.setAdapter(gridElementAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gridViewSetting() {

        // this is size of your list with data
        int size = categoryMainArrayList.size();
        // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
        int width = 80;

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

//        int totalWidth = (int) (width * size * density);
        int singleItemWidth = (int) (width * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        horizontalGridView.setLayoutParams(params);
        horizontalGridView.setColumnWidth(singleItemWidth);
        horizontalGridView.setHorizontalSpacing(2);
        horizontalGridView.setVerticalSpacing(2);
        horizontalGridView.setStretchMode(GridView.STRETCH_SPACING);
        horizontalGridView.setNumColumns(4);

    }

    public void refreshItems() {
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
            setCategoryList();
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private Activity activity;
    private static SearchFragment searchFragment;

    public static SearchFragment getInstance() {
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        return searchFragment;
    }
}
