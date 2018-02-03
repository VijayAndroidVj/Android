package com.instag.vijay.fasttrending;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.activity.VideoViewActivity;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by vijay on 27/12/17.
 */

public class PostView extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private TextView txtMeetingName, txtPostDescription, txtPostLikesCount, txtCreatedDate, txtViewAllComments;
    private ImageView btnpostDelete;
    private ImageView postImage, ivProfile;
    private ImageView likePost, commentPost;
    private PreferenceUtil preferenceUtil;
    Posts posts;
    private View ibPlay;
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
        txtViewAllComments = findViewById(R.id.txtViewAllComments);
        postImage = findViewById(R.id.postImage);
        ivProfile = findViewById(R.id.ivProfile);
        btnpostDelete = findViewById(R.id.btnpostDelete);
        likePost = findViewById(R.id.likePost);
        txtCreatedDate = findViewById(R.id.txtCreatedDate);
        commentPost = findViewById(R.id.commentPost);
        ibPlay = findViewById(R.id.ibPlay);
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
        try {
            if (TextUtils.isEmpty(post.getUsername())) {
                txtMeetingName.setText("");
            } else {
                if (post.getFileType() != null && post.getFileType().equalsIgnoreCase("3")) {
                    txtMeetingName.setText(post.getUsername() + " updated profile");
                } else {
                    txtMeetingName.setText(post.getUsername());
                }
            }

            if (TextUtils.isEmpty(post.getDescription())) {
                txtPostDescription.setText("");
                txtPostDescription.setVisibility(View.GONE);
            } else {
                txtPostDescription.setVisibility(View.VISIBLE);
                txtPostDescription.setText(post.getDescription());
            }

            txtPostLikesCount.setText(post.getTotal_likes() + " likes");


            if (post.isLiked()) {
                ColorStateList selectedColorStateList = CommonUtil.getColorStateList(activity, R.color.red);
                likePost.setImageTintList(selectedColorStateList);
                likePost.setImageResource(R.drawable.like_filled);
            } else {
                ColorStateList unselectedColorStateList = CommonUtil.getColorStateList(activity, R.color.black);
                likePost.setImageResource(R.drawable.like);
                likePost.setImageTintList(unselectedColorStateList);
            }
            if (post.getCreated_date() != null && !post.getCreated_date().isEmpty()) {
                txtCreatedDate.setVisibility(View.VISIBLE);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.getDefault());
                try {
                    Date testDate = sdf.parse(post.getCreated_date());
                    String newFormat = formatter.format(testDate);
                    txtCreatedDate.setText(newFormat);
                } catch (Exception e) {
                    e.printStackTrace();
                    txtCreatedDate.setText(post.getCreated_date());
                }
            } else {
                txtCreatedDate.setVisibility(View.GONE);
            }


            if (TextUtils.isEmpty(post.getTotalComments())) {
                txtViewAllComments.setText("");
                txtViewAllComments.setVisibility(View.GONE);
            } else {
                txtViewAllComments.setVisibility(View.VISIBLE);
                int total = 0;
                try {
                    total = Integer.valueOf(post.getTotalComments());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (total > 1) {
                    txtViewAllComments.setText("View all " + post.getTotalComments() + " comments");
                } else if (total == 1) {
                    txtViewAllComments.setText("View " + post.getTotalComments() + " comment");
                } else {
                    txtViewAllComments.setText("");
                    txtViewAllComments.setVisibility(View.GONE);
                }
            }


            likePost.setTag(post);
            commentPost.setTag(post);
            txtViewAllComments.setTag(post);
            txtPostLikesCount.setTag(post);
            txtViewAllComments.setOnClickListener(this);
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

                if (post.getFileType().equalsIgnoreCase("2")) {
                    if (post.getVideoThumb() != null && !post.getVideoThumb().isEmpty()) {
                        byte[] decodedString = Base64.decode(post.getVideoThumb(), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        if (bitmap != null)
                            postImage.setImageBitmap(bitmap);
                        ibPlay.setVisibility(View.VISIBLE);
                        ibPlay.setTag(post);
                        ibPlay.setOnClickListener(this);
                    } else if (post.getImage() != null && !post.getImage().isEmpty()) {
                        ibPlay.setVisibility(View.GONE);
                        Glide.with(activity).load(post.getImage()).centerCrop()
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(postImage);
                    }

                } else {
                    ibPlay.setVisibility(View.GONE);
                    Glide.with(activity).load(post.getImage()).centerCrop()
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(postImage);
                }
            }

            if (post.getProfile_image() != null && !post.getProfile_image().isEmpty() && !activity.isFinishing()) {
                Glide.with(activity).load(post.getProfile_image()).asBitmap().centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibPlay:
                Posts posts = (Posts) view.getTag();
                Intent myIntent = new Intent(activity,
                        VideoViewActivity.class);
                myIntent.putExtra("post", posts);
                startActivity(myIntent);
                break;
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
            case R.id.txtViewAllComments:
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
        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Delete Comment")
                .setContentText("Are you sure want to delete this comment?")
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

        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
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
