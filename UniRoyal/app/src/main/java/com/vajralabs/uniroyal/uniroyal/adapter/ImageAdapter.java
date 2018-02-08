package com.vajralabs.uniroyal.uniroyal.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryItem> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btnpostDelete:
//                Object object = v.getTag();
//                if (object instanceof Posts) {
//                    Posts post = (Posts) object;
//                    showMeetingtAlert(activity, activity.getString(R.string.app_name), "Are you sure want to delete this post?", post);
//                }
//                break;
//            case R.id.likePost:
//                object = v.getTag();
//                if (object instanceof Posts) {
//                    Posts post = (Posts) object;
//                    likePost(post);
//                }
//                break;
//            case R.id.commentPost:
//                object = v.getTag();
//                if (object instanceof Posts) {
//                    Posts post = (Posts) object;
//                    Intent intent = new Intent(activity, Comment.class);
//                    intent.putExtra("post_id", post.getPost_id());
//                    activity.startActivity(intent);
//                }
//                break;
//            case R.id.rlgrid:
//                object = v.getTag();
//                if (object instanceof Posts) {
//                    Posts post = (Posts) object;
//                    Intent intent = new Intent(activity, PostView.class);
//                    intent.putExtra("postId", post.getPost_id());
//                    activity.startActivity(intent);
//                }
//                break;
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
        //        private TextView txtMeetingName, txtPostDescription, txtPostLikesCount;
//        private Button btnpostDelete;
        private ImageView postImage;
        private View rlgrid;
        private TextView cname;
//        private ImageView likePost, commentPost;

        private MyViewHolder(View view) {
            super(view);
//            txtMeetingName = view.findViewById(R.id.txtMeetingName);
//            txtPostDescription = view.findViewById(R.id.txtPostDescription);
//            txtPostLikesCount = view.findViewById(R.id.txtPostLikesCount);
            postImage = view.findViewById(R.id.postImg);
            rlgrid = view.findViewById(R.id.rlgrid);
            cname = view.findViewById(R.id.cname);
//            txtMeetingName.setTypeface(font, Typeface.BOLD);
//            btnpostDelete = view.findViewById(R.id.btnpostDelete);
//            likePost = view.findViewById(R.id.likePost);
//            commentPost = view.findViewById(R.id.commentPost);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    int width;

    public ImageAdapter(Activity activity, List<CategoryItem> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.grid_image_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryItem post = originalList.get(position);
        holder.rlgrid.getLayoutParams().width = width / 3;
        holder.postImage.getLayoutParams().width = width / 3;
        holder.rlgrid.setOnClickListener(this);
        holder.rlgrid.setTag(post);
        holder.cname.setTag(post.getItem_name());
        if (post.getImage_path() != null && !post.getImage_path().isEmpty()) {

            Glide.with(activity).load(post.getImage_path()).centerCrop()
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