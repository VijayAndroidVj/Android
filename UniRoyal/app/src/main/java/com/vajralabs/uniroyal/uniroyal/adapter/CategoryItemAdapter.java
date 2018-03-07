package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.CategoryItem;

import java.util.List;

/**
 * Created by vijay on 24/12/17.
 */

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryItem> originalList;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView postImg;
        private TextView categoryName;
        private TextView categorySubItem;
        private View colorview;

        private MyViewHolder(View view) {
            super(view);
            postImg = view.findViewById(R.id.postImg);
            categoryName = view.findViewById(R.id.categoryName);
            categorySubItem = view.findViewById(R.id.categorySubItem);
            colorview = view.findViewById(R.id.colorview);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    String category_name;

    public CategoryItemAdapter(Activity activity, List<CategoryItem> moviesList, String category_name) {
        this.originalList = moviesList;
        this.activity = activity;
        this.category_name = category_name;
        layoutInflater = LayoutInflater.from(activity);
    }

    public CategoryItemAdapter(Activity activity, List<CategoryItem> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryItem post = originalList.get(position);
        if (post.getImage_path() != null && !post.getImage_path().isEmpty()) {

            Glide.with(activity).load(post.getImage_path()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.postImg);
        }

        if (TextUtils.isEmpty(category_name)) {
            if (TextUtils.isEmpty(post.getCategory_name())) {
                holder.categoryName.setText("");
            } else {
                holder.categoryName.setText(post.getCategory_name());
            }
        } else {
            holder.categoryName.setText(category_name);
        }
        if (TextUtils.isEmpty(post.getItem_name())) {
            holder.categorySubItem.setText("");
        } else {
            holder.categorySubItem.setText(post.getItem_name());
        }
        holder.colorview.setBackgroundColor(post.getColor());

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
