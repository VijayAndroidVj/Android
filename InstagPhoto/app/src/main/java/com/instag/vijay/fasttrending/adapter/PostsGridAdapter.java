package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.PostView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.Posts;

import java.util.ArrayList;

public class PostsGridAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Posts> timeSlotList;

    public PostsGridAdapter(Activity context, ArrayList<Posts> timeSlotList) {
        this.activity = context;
        this.timeSlotList = timeSlotList;
    }


    @Override
    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        ImageView iv_profile_pic;
        View cv_profile;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final Posts post = timeSlotList.get(position);
        //View grid;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.profile_post_item, null);
            holder.iv_profile_pic = convertView.findViewById(R.id.iv_profile_pic);
            holder.cv_profile = convertView.findViewById(R.id.cv_profile);
            holder.cv_profile.setTag(post);
            holder.cv_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Posts post = (Posts) v.getTag();
                    Intent intent = new Intent(activity, PostView.class);
                    intent.putExtra("postId", post.getPost_id());
                    activity.startActivity(intent);
                }
            });
            if (post.getFileType().equalsIgnoreCase("2")) {
                if (post.getVideoThumb() != null && !post.getVideoThumb().isEmpty()) {
                    byte[] decodedString = Base64.decode(post.getVideoThumb().getBytes(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (bitmap != null)
                        holder.iv_profile_pic.setImageBitmap(bitmap);

                } else if (post.getImage() != null && !post.getImage().isEmpty()) {
                    Glide.with(activity).load(post.getImage()).centerCrop()
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.iv_profile_pic);
                }

            } else if (post.getImage() != null && !post.getImage().isEmpty()) {
                Glide.with(activity).load(post.getImage()).centerCrop()
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_profile_pic);
            } else {
                Glide.with(activity).load(R.drawable.default_profile_photo).centerCrop()
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.iv_profile_pic);
            }
            convertView.setTag(holder);
        }

        return convertView;
    }

}