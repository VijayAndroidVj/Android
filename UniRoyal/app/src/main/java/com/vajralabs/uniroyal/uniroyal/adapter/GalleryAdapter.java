package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.activity.SpacePhotoActivity;
import com.vajralabs.uniroyal.uniroyal.model.CategoryItem;

import java.util.List;

/**
 * Created by vijay on 24/12/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryItem> originalList;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivGallery;
        private View recyclerviewContact;
        private View topview;

        private MyViewHolder(View view) {
            super(view);
            topview = view;
            recyclerviewContact = view.findViewById(R.id.recyclerviewContact);
            ivGallery = view.findViewById(R.id.ivGallery);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public GalleryAdapter(Activity activity, List<CategoryItem> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.category_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.recyclerviewContact.setVisibility(View.GONE);
        holder.ivGallery.setVisibility(View.VISIBLE);

        CategoryItem post = originalList.get(position);

        if (post.getImage_path() != null && !post.getImage_path().isEmpty()) {
            Glide.with(activity).load(post.getImage_path())
                    .into(holder.ivGallery);
        }

        holder.topview.setTag(post);
        holder.topview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CategoryItem categoryItem = (CategoryItem) view.getTag();
                Intent intent = new Intent(activity, SpacePhotoActivity.class);
                intent.putExtra(SpacePhotoActivity.EXTRA_SPACE_PHOTO, categoryItem);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }


}
