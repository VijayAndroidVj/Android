package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.Comment;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Posts> originalList;
    private PreferenceUtil preferenceUtil;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnpostDelete:
                Object object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    showMeetingtAlert(activity, activity.getString(R.string.app_name), "Are you sure want to delete this post?", post);
                }
                break;
            case R.id.likePost:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    likePost(post);
                }
                break;
            case R.id.commentPost:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, Comment.class);
                    intent.putExtra("post_id", post.getPost_id());
                    activity.startActivity(intent);
                }
                break;
        }
    }

    private void likePost(final Posts post) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Please wait...");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);

            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.post_like(usermail, preferenceUtil.getUserName(), post.getPost_id(), !post.isLiked());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        post.setLiked(!post.isLiked());
                        if (post.isLiked())
                            post.setTotal_likes(post.getTotal_likes() + 1);
                        else
                            post.setTotal_likes(post.getTotal_likes() - 1);

                        notifyDataSetChanged();
                        closeProgress();
                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    String message = t.getMessage();
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else {
                        message = "Failed";
                    }
                    closeProgress();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }


    private void showMeetingtAlert(Activity activity, String title, String message, final Posts post) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_ok_dialog_, null);
        alertDialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);
        Button dialogButtonCancel = (Button) dialogView.findViewById(R.id.customDialogCancel);

        TextView txtTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView txtMessage = (TextView) dialogView.findViewById(R.id.dialog_message);

        txtTitle.setText(title);
        txtMessage.setText(message);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(post);
                alertDialog.dismiss();
            }
        });

        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void deletePost(final Posts post) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Deleting....");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.delete_post(usermail, post.getPost_id());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        originalList.remove(post);
                        notifyDataSetChanged();
                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    // Log error here since request failed
                    String message = t.getMessage();
                    if (message.contains("Failed to")) {
                        message = "Failed to Connect";
                    } else {
                        message = "Failed";
                    }
                    closeProgress();
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
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
        private TextView txtMeetingName, txtPostDescription, txtPostLikesCount;
        private Button btnpostDelete;
        private ImageView postImage, ivProfile;
        private ImageView likePost, commentPost;

        private MyViewHolder(View view) {
            super(view);
            txtMeetingName = view.findViewById(R.id.txtMeetingName);
            txtPostDescription = view.findViewById(R.id.txtPostDescription);
            txtPostLikesCount = view.findViewById(R.id.txtPostLikesCount);
            postImage = view.findViewById(R.id.postImage);
            ivProfile = view.findViewById(R.id.ivProfile);
            txtMeetingName.setTypeface(font, Typeface.BOLD);
            btnpostDelete = view.findViewById(R.id.btnpostDelete);
            likePost = view.findViewById(R.id.likePost);
            commentPost = view.findViewById(R.id.commentPost);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Typeface font;

    public PostAdapter(Activity activity, List<Posts> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        preferenceUtil = new PreferenceUtil(activity);
        font = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.post_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Posts post = originalList.get(position);


        if (TextUtils.isEmpty(post.getUsername())) {
            holder.txtMeetingName.setText("");
        } else {
            holder.txtMeetingName.setText(post.getUsername());
        }

        if (TextUtils.isEmpty(post.getDescription())) {
            holder.txtPostDescription.setText("");
        } else {
            holder.txtPostDescription.setText(post.getDescription());
        }

        holder.txtPostLikesCount.setText(post.getTotal_likes() + " likes");


        if (post.isLiked()) {
            holder.likePost.setImageResource(R.drawable.ic_favorite_black);
        } else {
            holder.likePost.setImageResource(R.drawable.ic_favorite_border_black);
        }


        holder.likePost.setTag(post);
        holder.commentPost.setTag(post);
        holder.likePost.setOnClickListener(this);
        holder.commentPost.setOnClickListener(this);

        if (post.getPostmail() != null && post.getPostmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
            holder.btnpostDelete.setTag(post);
            holder.btnpostDelete.setOnClickListener(this);
            holder.btnpostDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnpostDelete.setVisibility(View.GONE);
        }

        if (post.getImage() != null && !post.getImage().isEmpty()) {

            Glide.with(activity).load(post.getImage()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.postImage);
        }

        if (post.getProfile_image() != null && !post.getProfile_image().isEmpty()) {

            Glide.with(activity).load(post.getProfile_image()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivProfile);
        }


    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}