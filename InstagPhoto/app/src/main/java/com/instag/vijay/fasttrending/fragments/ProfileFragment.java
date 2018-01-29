package com.instag.vijay.fasttrending.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EditProfile;
import com.instag.vijay.fasttrending.PostView;
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
import java.util.List;

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
    private GridView gridView;
    private ProgressBar progressBar;
    private ImageAdapter imageAdapter;
    private PreferenceUtil preferenceUtil;
    private ImageView imgGrid;
    private ImageView btnlist;

    public static ProfileFragment getInstance() {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        return profileFragment;
    }

    private SelectableRoundedImageView ivProfile1;
    private TextView txtUsername, txtBiography;

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
        txtUsername = view.findViewById(R.id.txtUsername);
        txtBiography = view.findViewById(R.id.txtBiography);
        txtPostsCount = view.findViewById(R.id.txtPostsCount);
        txtFollowersCount = view.findViewById(R.id.txtFollowersCount);
        txtFolloweringCount = view.findViewById(R.id.txtFolloweringCount);
        imgGrid = view.findViewById(R.id.btnGrid);
        btnlist = view.findViewById(R.id.btnlist);
        imgGrid.setOnClickListener(this);
        btnlist.setOnClickListener(this);
        view.findViewById(R.id.btnEditProfile).setOnClickListener(this);
        viewInfo = view.findViewById(R.id.txtContactInfo);
        gridView = view.findViewById(R.id.gridview);
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
        imgGrid.setColorFilter(ContextCompat.getColor(activity, R.color.button_background));
        btnlist.setColorFilter(ContextCompat.getColor(activity, R.color.grey));
        gridView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        viewInfo.setVisibility(View.GONE);
//        imageAdapter = new ImageAdapter(activity, postsArrayList);
        MyAdapter imageAdapter = new MyAdapter(activity, postsArrayList);
        gridView.setAdapter(imageAdapter);
//        recyclerView.setHasFixedSize(true);
//        EqualSpaceItemDecoration itemDecoration = new EqualSpaceItemDecoration(1);
//
//        recyclerView.addItemDecoration(itemDecoration);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 3);
//        recyclerView.setLayoutManager(layoutManager);
        if (postsArrayList.size() == 0) {
            showView(1, "No Posts available");
        } else {
            imageAdapter.notifyDataSetChanged();
            setGridViewHeightBasedOnChildren(gridView, 3);
            progressBar.setVisibility(View.GONE);
            viewInfo.setVisibility(View.GONE);
        }

    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        MyAdapter listAdapter = (MyAdapter) gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

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
            txtUsername.setText(preferenceUtil.getUserName());
            txtBiography.setText(preferenceUtil.getUserAboutMe());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList() {
        try {
            imgGrid.setColorFilter(ContextCompat.getColor(activity, R.color.grey));
            btnlist.setColorFilter(ContextCompat.getColor(activity, R.color.button_background));
            gridView.setVisibility(View.GONE);
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
                case R.id.btnEditProfile:
                    Intent in = new Intent(activity, EditProfile.class);
                    startActivity(in);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class EqualSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int mSpaceHeight;

        public EqualSpaceItemDecoration(int mSpaceHeight) {
            this.mSpaceHeight = mSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpaceHeight;
            outRect.top = mSpaceHeight;
            outRect.left = mSpaceHeight;
            outRect.right = mSpaceHeight;
        }
    }


    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {

            mItemOffset = itemOffset;

        }


        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {

            this(context.getResources().getDimensionPixelSize(itemOffsetId));

        }

        @Override

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,

                                   RecyclerView.State state) {

            super.getItemOffsets(outRect, view, parent, state);

            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);

        }

    }

    public class MyAdapter extends BaseAdapter implements View.OnClickListener {

        private List<Posts> originalList;
        int width;

        public MyAdapter(Context c, List<Posts> originalList) {
            this.originalList = originalList;
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
        }

        @Override
        public int getCount() {
            return originalList.size();
        }

        @Override
        public Posts getItem(int arg0) {
            return originalList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View grid = convertView;
            if (originalList.size() > 0) {
                if (grid == null) {
                    LayoutInflater inflater = activity.getLayoutInflater();
                    grid = inflater.inflate(R.layout.grid_image_layout, parent, false);
                }
                grid.getLayoutParams().width = width / 3;
                ImageView postImg = grid.findViewById(R.id.postImg);
                View rlgrid = grid.findViewById(R.id.rlgrid);
                View ibPlay = grid.findViewById(R.id.ibPlay);
                rlgrid.getLayoutParams().width = width / 3;
                postImg.getLayoutParams().width = width / 3;
                Posts post = originalList.get(position);
                rlgrid.setOnClickListener(this);
                ibPlay.setOnClickListener(this);
                ibPlay.setTag(post);
                rlgrid.setTag(post);
                if (post.getFileType().equalsIgnoreCase("2")) {
                    if (post.getVideoThumb() != null && !post.getVideoThumb().isEmpty()) {
                        byte[] decodedString = Base64.decode(post.getVideoThumb(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        if (bitmap != null)
                            postImg.setImageBitmap(bitmap);
                        ibPlay.setVisibility(View.VISIBLE);
                    } else if (post.getImage() != null && !post.getImage().isEmpty()) {
                        ibPlay.setVisibility(View.GONE);
                        Glide.with(activity).load(post.getImage()).centerCrop()
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(postImg);
                    }

                } else {
                    ibPlay.setVisibility(View.GONE);
                    if (post.getImage() != null && !post.getImage().isEmpty()) {

                        Glide.with(activity).load(post.getImage()).centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(postImg);
                    }
                }
            }
            return grid;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ibPlay:
                case R.id.rlgrid:
                    Object object = view.getTag();
                    if (object instanceof Posts) {
                        Posts post = (Posts) object;
                        Intent intent = new Intent(activity, PostView.class);
                        intent.putExtra("postId", post.getPost_id());
                        activity.startActivity(intent);
                    }
                    break;
            }
        }
    }

}
