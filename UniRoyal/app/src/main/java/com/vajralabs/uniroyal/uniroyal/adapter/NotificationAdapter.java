package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.CategoryModel;

import java.util.List;

/**
 * Created by vijay on 24/12/17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryModel> originalList;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        View ivGallery;
        RecyclerView recyclerView;

        private MyViewHolder(View view) {
            super(view);
            ivGallery = view.findViewById(R.id.ivGallery);
            recyclerView = view.findViewById(R.id.recyclerviewContact);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Activity activity, List<CategoryModel> moviesList) {
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
        CategoryModel post = originalList.get(position);
        holder.ivGallery.setVisibility(View.GONE);
        holder.recyclerView.setVisibility(View.VISIBLE);
        CategoryItemAdapter imageAdapter = new CategoryItemAdapter(activity, post.getCategory_item_lists(), post.getCategory_name());
        holder.recyclerView.setAdapter(imageAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext(), R.drawable.list_item_background));
        imageAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
