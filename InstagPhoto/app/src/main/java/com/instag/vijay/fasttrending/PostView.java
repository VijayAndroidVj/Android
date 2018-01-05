package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.model.PostModelMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by vijay on 27/12/17.
 */

public class PostView extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private TextView txtMeetingName, txtPostDescription, txtPostLikesCount;
    private Button btnpostDelete;
    private ImageView postImage, ivProfile;
    private ImageView likePost, commentPost;
    private PreferenceUtil preferenceUtil;
    Posts posts;
    private String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);
        activity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post View");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);


        preferenceUtil = new PreferenceUtil(activity);
        txtMeetingName = findViewById(R.id.txtMeetingName);
        txtPostDescription = findViewById(R.id.txtPostDescription);
        txtPostLikesCount = findViewById(R.id.txtPostLikesCount);
        postImage = findViewById(R.id.postImage);
        ivProfile = findViewById(R.id.ivProfile);
        btnpostDelete = findViewById(R.id.btnpostDelete);
        likePost = findViewById(R.id.likePost);
        commentPost = findViewById(R.id.commentPost);
        try {
            postId = getIntent().getStringExtra("postId");
            if (!TextUtils.isEmpty(postId))
                getPostById(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPostById(String postId) {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface service =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<Posts> call = service.getpostbyid(postId, preferenceUtil.getUserMailId());
                call.enqueue(new Callback<Posts>() {
                    @Override
                    public void onResponse(Call<Posts> call, Response<Posts> response) {
                        Posts postModelMain = response.body();
                        posts = postModelMain;
                        if (postModelMain != null) {
                            setPostValue(postModelMain);
                        }
                    }

                    @Override
                    public void onFailure(Call<Posts> call, Throwable t) {
                        // Log error here since request failed
                        String message = t.getMessage();
                        if (message.contains("Failed to")) {
                            message = "Failed to Connect";
                        } else if (message.contains("Expected")) {
                            message = "No Post available";
                        } else {
                            message = "Failed";
                        }
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPostValue(Posts post) {
        if (TextUtils.isEmpty(post.getUsername())) {
            txtMeetingName.setText("");
        } else {
            txtMeetingName.setText(post.getUsername());
        }

        if (TextUtils.isEmpty(post.getDescription())) {
            txtPostDescription.setText("");
        } else {
            txtPostDescription.setText(post.getDescription());
        }

        txtPostLikesCount.setText(post.getTotal_likes() + " likes");


        if (post.isLiked()) {
            likePost.setImageResource(R.drawable.ic_favorite_black);
        } else {
            likePost.setImageResource(R.drawable.ic_favorite_border_black);
        }


        likePost.setTag(post);
        commentPost.setTag(post);
        txtPostLikesCount.setTag(post);
        likePost.setOnClickListener(this);
        commentPost.setOnClickListener(this);
        txtPostLikesCount.setOnClickListener(this);

        if (post.getPostmail() != null && post.getPostmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
            btnpostDelete.setTag(post);
            btnpostDelete.setOnClickListener(this);
            btnpostDelete.setVisibility(View.VISIBLE);
        } else {
            btnpostDelete.setVisibility(GONE);
        }

        if (post.getImage() != null && !post.getImage().isEmpty()) {

            Glide.with(activity).load(post.getImage()).centerCrop()
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(postImage);
        }

        if (post.getProfile_image() != null && !post.getProfile_image().isEmpty()) {

            Glide.with(activity).load(post.getProfile_image()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivProfile);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnpostDelete:
                Object object = view.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    showMeetingtAlert(activity, activity.getString(R.string.app_name), "Are you sure want to delete this post?", post);
                }
                break;
            case R.id.likePost:
                object = view.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    likePost(post);
                }
                break;
            case R.id.commentPost:
                object = view.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent intent = new Intent(activity, Comment.class);
                    intent.putExtra("post_id", post.getPost_id());
                    activity.startActivity(intent);
                }
                break;
            case R.id.txtPostLikesCount:
                object = view.getTag();
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

                        if (post.isLiked()) {
                            likePost.setImageResource(R.drawable.ic_favorite_black);
                        } else {
                            likePost.setImageResource(R.drawable.ic_favorite_border_black);
                        }
                        txtPostLikesCount.setText(post.getTotal_likes() + " likes");

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
                        closeProgress();
                        onBackPressed();

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
}