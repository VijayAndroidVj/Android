package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.model.CategoryItem;
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

        private MyViewHolder(View view) {
            super(view);
            categoryName = view.findViewById(R.id.categoryName);
            gridView = view.findViewById(R.id.gridView);
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
        holder.categoryName.setText(post.getCategoryname());
//        holder.txtTitle.setText(post.getTitle());
//        holder.txtPostDescription.setText(post.getDescription());
        holder.gridView.setVisibility(View.VISIBLE);
        holder.gridView.setNumColumns(post.getCategoryItems().size());
        MyAdapter imageAdapter = new MyAdapter(activity, post.getCategoryItems());
        holder.gridView.setAdapter(imageAdapter);
        setDynamicWidth(holder.gridView);

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

class MyAdapter extends BaseAdapter implements View.OnClickListener {

    private List<CategoryItem> originalList;
    int width;
    Activity c;

    public MyAdapter(Activity c, List<CategoryItem> originalList) {
        this.originalList = originalList;
        Display display = c.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.c = c;
        width = size.x;
    }

    @Override
    public int getCount() {
        return originalList.size();
    }

    @Override
    public CategoryItem getItem(int arg0) {
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
                LayoutInflater inflater = c.getLayoutInflater();
                grid = inflater.inflate(R.layout.grid_image_layout, parent, false);
            }
            ImageView postImg = grid.findViewById(R.id.postImg);
            View rlgrid = grid.findViewById(R.id.rlgrid);
            TextView cname = grid.findViewById(R.id.cname);
            grid.getLayoutParams().width = width / 2;
            rlgrid.getLayoutParams().width = width / 2;
            postImg.getLayoutParams().width = width / 2;
            CategoryItem post = originalList.get(position);
            rlgrid.setOnClickListener(this);
            cname.setText(post.getItemname());
            rlgrid.setTag(post);
            if (post.getItem_image_path() != null && !post.getItem_image_path().isEmpty()) {

                Glide.with(c).load(post.getItem_image_path()).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(postImg);
            }
        }
        return grid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
