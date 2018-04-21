package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.Comment;
import com.instag.vijay.fasttrending.CommonUtil;
import com.instag.vijay.fasttrending.EventResponse;
import com.instag.vijay.fasttrending.Likes;
import com.instag.vijay.fasttrending.PostView;
import com.instag.vijay.fasttrending.PreferenceUtil;
import com.instag.vijay.fasttrending.ProfileView;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.VideoViewActivity;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Posts> originalList;
    private PreferenceUtil preferenceUtil;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibPlay:
                Posts posts = (Posts) v.getTag();
                Intent myIntent = new Intent(activity,
                        VideoViewActivity.class);
                myIntent.putExtra("post", posts);
                activity.startActivity(myIntent);
                break;
            case R.id.btnpostDelete:
                Object object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    showMeetingtAlert(activity, "Delete Post", "Are you sure want to delete this post?", post);
                }
                break;
            case R.id.likePost:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    likePost(post);
                }
                break;
            case R.id.txtViewAllComments:
            case R.id.commentPost:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, Comment.class);
                    intent.putExtra("post_id", post.getPost_id());
                    activity.startActivity(intent);
                }
                break;
            case R.id.rlParentMeeting:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, PostView.class);
                    intent.putExtra("postId", post.getPost_id());
                    activity.startActivity(intent);
                }
                break;
            case R.id.rlMeeting1:
                object = v.getTag();
                if (object instanceof Posts && !(activity instanceof ProfileView)) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, ProfileView.class);
                    intent.putExtra("profileId", post.getPostmail());
                    activity.startActivity(intent);
                }
                break;

            case R.id.txtPostLikesCount:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, Likes.class);
                    intent.putExtra("postid", post.getPost_id());
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
                    String message = t.toString();
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

        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
//                .setCustomImage(R.drawable.app_logo_back)
                .setCancelText("No").setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        deletePost(post);
                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
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

        alertDialog.show();*/
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
                    String message = t.toString();
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
        private ImageView postImage;
        private View rlParentMeeting, ibPlay;

        private MyViewHolder(View view) {
            super(view);
            postImage = view.findViewById(R.id.postImage);
            rlParentMeeting = view.findViewById(R.id.rlParentMeeting);
            ibPlay = view.findViewById(R.id.ibPlay);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat sdf;
    private SimpleDateFormat formatter;
    private ColorStateList selectedColorStateList;
    private ColorStateList unselectedColorStateList;

    public TrendingAdapter(Activity activity, List<Posts> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        selectedColorStateList = CommonUtil.getColorStateList(activity, R.color.red);
        unselectedColorStateList = CommonUtil.getColorStateList(activity, R.color.black);
        layoutInflater = LayoutInflater.from(activity);
        preferenceUtil = new PreferenceUtil(activity);
        sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
        formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.getDefault());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.trendingvideo_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Posts post = originalList.get(position);
        holder.rlParentMeeting.setTag(post);
        holder.rlParentMeeting.setOnClickListener(this);
        holder.ibPlay.setTag(post);
        holder.ibPlay.setOnClickListener(this);

        if (post.getFileType().equalsIgnoreCase("2")) {
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

        } else {
            holder.ibPlay.setVisibility(View.GONE);
            if (post.getImage() != null && !post.getImage().isEmpty()) {
                Glide.with(activity).load(post.getImage()).centerCrop()
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.postImage);
            }
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}