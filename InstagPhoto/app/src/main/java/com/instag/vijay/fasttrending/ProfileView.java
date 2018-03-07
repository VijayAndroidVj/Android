package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import static android.view.View.VISIBLE;

/**
 * Created by vijay on 28/12/17.
 */

public class ProfileView extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    private Activity activity;
    private ArrayList<Posts> postsArrayList = new ArrayList<>();
    private PostAdapter postAdapter;
    private TextView viewInfo, txtPostsCount, txtFollowersCount, txtFolloweringCount;
    private TextView txtUsername;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ImageAdapter imageAdapter;
    private PreferenceUtil preferenceUtil;
    private SelectableRoundedImageView ivProfile1;
    private String profileId;


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
        setContentView(R.layout.profileview);

        activity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile View");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
        txtUsername = findViewById(R.id.txtUsername);
        txtPostsCount = findViewById(R.id.txtPostsCount);
        txtFollowersCount = findViewById(R.id.txtFollowersCount);
        txtFolloweringCount = findViewById(R.id.txtFolloweringCount);
        viewInfo = findViewById(R.id.txtContactInfo);
        recyclerView = findViewById(R.id.recyclerviewContact);
        progressBar = findViewById(R.id.progressBar_cyclic);
        recyclerView.setVisibility(VISIBLE);
        preferenceUtil = new PreferenceUtil(activity);
        ivProfile1 = findViewById(R.id.ivProfile1);
       /* String profileImage = preferenceUtil.getMyProfile();
        if (!TextUtils.isEmpty(profileImage) && profileImage.contains("http://")) {
            Glide.with(activity)
                    .load(profileImage)
                    .asBitmap()
                    .into(ivProfile1);
        }*/

//        dynamicToolbarColor();
        toolbarTextAppernce();
        profileId = getIntent().getStringExtra("profileId");
        if (!TextUtils.isEmpty(profileId))
            getMyPosts();


    }

    private void getMyPosts() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<PostModelMain> call = service.getposts(profileId, preferenceUtil.getUserMailId());
                call.enqueue(new Callback<PostModelMain>() {
                    @Override
                    public void onResponse(Call<PostModelMain> call, Response<PostModelMain> response) {
                        PostModelMain postModelMain = response.body();
                        try {

                            if (postModelMain != null) {
                                postsArrayList = postModelMain.getPostsArrayList();
                                txtPostsCount.setText(String.valueOf(postModelMain.getTotalposts()));
                                txtFolloweringCount.setText(String.valueOf(postModelMain.getTotal_followering()));
                                txtFollowersCount.setText(String.valueOf(postModelMain.getTotal_followers()));
                                if (!TextUtils.isEmpty(postModelMain.getProfile_image()) && postModelMain.getProfile_image().contains("http://")) {
                                    if (!activity.isFinishing())
                                        Glide.with(activity)
                                                .load(postModelMain.getProfile_image())
                                                .asBitmap()
                                                .into(ivProfile1);
                                }
                                txtUsername.setText(postModelMain.getUsername());
                            }
                            setList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
}
