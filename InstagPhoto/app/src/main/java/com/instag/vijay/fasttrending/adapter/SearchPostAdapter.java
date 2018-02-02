package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.PostView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.Posts;

import java.util.List;

/**
 * Created by vijay on 3/2/18.
 */

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Posts> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlimage:
                Object object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, PostView.class);
                    intent.putExtra("postId", post.getPost_id());
                    activity.startActivity(intent);
                }
                break;
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
        private ImageView postImage;
        private View rlimage;
        private View ibPlay;

        private MyViewHolder(View view) {
            super(view);
            postImage = view.findViewById(R.id.postImage);
            ibPlay = view.findViewById(R.id.ibPlay);
            rlimage = view.findViewById(R.id.rlimage);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public SearchPostAdapter(Activity activity, List<Posts> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public SearchPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.search_post_item, parent, false);
        return new SearchPostAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchPostAdapter.MyViewHolder holder, int position) {
        Posts post = originalList.get(position);
        holder.rlimage.setTag(post);
        holder.rlimage.setOnClickListener(this);

        if (post.getVideoThumb() != null && !post.getVideoThumb().isEmpty()) {
            byte[] decodedString = Base64.decode(post.getVideoThumb().getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            if (bitmap != null)
                holder.postImage.setImageBitmap(bitmap);
            holder.ibPlay.setVisibility(View.VISIBLE);

        } else if (post.getImage() != null && !post.getImage().isEmpty()) {
            holder.ibPlay.setVisibility(View.GONE);
            Glide.with(activity).load(post.getImage()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.postImage);
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}