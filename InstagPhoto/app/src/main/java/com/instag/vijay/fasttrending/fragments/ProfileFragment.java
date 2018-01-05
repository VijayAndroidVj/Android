package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.adapter.ImageAdapter;
import com.instag.vijay.fasttrending.adapter.PostAdapter;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;
import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 21/11/17.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static ProfileFragment profileFragment;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private Activity activity;
    private ArrayList<Posts> postsArrayList = new ArrayList<>();
    private PostAdapter postAdapter;
    private TextView viewInfo, txtPostsCount, txtFollowersCount, txtFolloweringCount;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageAdapter imageAdapter;
    private PreferenceUtil preferenceUtil;

    public static ProfileFragment getInstance() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        return profileFragment;
    }

    private SelectableRoundedImageView ivProfile1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profilefragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        TextView txtUsername = view.findViewById(R.id.txtUsername);
        txtPostsCount = view.findViewById(R.id.txtPostsCount);
        txtFollowersCount = view.findViewById(R.id.txtFollowersCount);
        txtFolloweringCount = view.findViewById(R.id.txtFolloweringCount);
        view.findViewById(R.id.btnGrid).setOnClickListener(this);
        view.findViewById(R.id.btnlist).setOnClickListener(this);
        viewInfo = (TextView) view.findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_cyclic);
        recyclerView.setVisibility(View.VISIBLE);
        preferenceUtil = new PreferenceUtil(getActivity());
        txtUsername.setText(preferenceUtil.getUserName());
        ivProfile1 = view.findViewById(R.id.ivProfile1);
        String profileImage = preferenceUtil.getMyProfile();
        if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
            Glide.with(activity)
                    .load(profileImage)
                    .asBitmap()
                    .into(ivProfile1);
        }

//        dynamicToolbarColor();
        toolbarTextAppernce();

        getMyPosts();
    }

    private void getMyPosts() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                String usermail = preferenceUtil.getUserMailId();
                Call<PostModelMain> call = service.getposts(usermail, usermail);
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        if (postModelMain != null) {
                            postsArrayList = postModelMain.getPostsArrayList();
                            txtPostsCount.setText(String.valueOf(postModelMain.getTotalposts()));
                            txtFolloweringCount.setText(String.valueOf(postModelMain.getTotal_followering()));
                            txtFollowersCount.setText(String.valueOf(postModelMain.getTotal_followers()));
                        }
                        showGrid();
                    }

                    @Override
                    public void onFailure(Call<PostModelMain> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Post available";
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

    private void showGrid() {
        viewInfo.setVisibility(View.GONE);
        imageAdapter = new ImageAdapter(activity, postsArrayList);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        if (postsArrayList.size() == 0) {
            showView(1, "No Posts available");
        } else {
            progressBar.setVisibility(View.GONE);
            viewInfo.setVisibility(View.GONE);
        }
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            String profileImage = preferenceUtil.getMyProfile();
            if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
                Glide.with(activity)
                        .load(profileImage)
                        .asBitmap()
                        .into(ivProfile1);
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

            postAdapter = new PostAdapter(activity, postsArrayList);
            recyclerView.setAdapter(postAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            postAdapter.notifyDataSetChanged();
            recyclerView.setNestedScrollingEnabled(false);
            if (postsArrayList.size() == 0) {
                showView(1, "No Posts available");
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

    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.btnGrid:
                    showGrid();
                    break;
                case R.id.btnlist:
                    setList();
                    recyclerView.setVisibility(View.VISIBLE);

                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}