package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vajralabs.uniroyal.uniroyal.R;
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
        private TextView categoryName;
        private GridView gridView;
        private ImageView ivGallery;
        private View cvGallery;

        private MyViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            gridView = view.findViewById(R.id.gridView);
            ivGallery = view.findViewById(R.id.ivGallery);
            cvGallery = view.findViewById(R.id.cvGallery);
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
        holder.categoryName.setVisibility(View.GONE);
        holder.cvGallery.setVisibility(View.GONE);
        holder.ivGallery.setVisibility(View.VISIBLE);
//        holder.txtTitle.setText(post.getTitle());
//        holder.txtPostDescription.setText(post.getDescription());
        holder.gridView.setVisibility(View.GONE);
        //setDynamicWidth(holder.gridView);

        CategoryItem post = originalList.get(position);
        if (post.getImage_path() != null && !post.getImage_path().isEmpty()) {

            Glide.with(activity).load(post.getImage_path()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivGallery);
        }

    }

    private void setDynamicWidth(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }
        int totalWidth;
        int items = gridViewAdapter.getCount();
        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalWidth = listItem.getMeasuredWidth();
        totalWidth = totalWidth * items;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.width = totalWidth;
        gridView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }


}
