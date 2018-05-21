package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.instag.vijay.fasttrending.Utils.ListListener;
import com.instag.vijay.fasttrending.activity.VideoViewActivity;
import com.instag.vijay.fasttrending.chat.TrovaChat;
import com.instag.vijay.fasttrending.model.CategoryMain;
import com.instag.vijay.fasttrending.model.Posts;
import com.instag.vijay.fasttrending.retrofit.ApiClient;
import com.instag.vijay.fasttrending.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostNewsfeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<Posts> originalList;
    private PreferenceUtil preferenceUtil;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 2;
    private ArrayList<CategoryMain> categoryMainArrayList;

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
            case R.id.commentSendMessage:
                Object object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    Intent CallActivity = new Intent(activity, TrovaChat.class);
                    CallActivity.putExtra("otherUserID", post.getPostmail());
                    CallActivity.putExtra("otherUserName", post.getUsername());
                    CallActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(CallActivity);
                }
                break;
            case R.id.btnpostDelete:
                object = v.getTag();
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
            case R.id.ivSavePost:
                object = v.getTag();
                if (object instanceof Posts) {
                    Posts post = (Posts) object;
                    savePost(post);
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

    private void savePost(final Posts post) {
        if (CommonUtil.isNetworkAvailable(activity)) {
            initProgress("Please wait...");
            ApiInterface service =
                    ApiClient.getClient().create(ApiInterface.class);
            String usermail = preferenceUtil.getUserMailId();
            Call<EventResponse> call = service.add_save_post(usermail, post.getPost_id(), !post.isSaved());
            call.enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    EventResponse patientDetails = response.body();
                    Log.i("patientDetails", response.toString());
                    if (patientDetails != null && patientDetails.getResult().equalsIgnoreCase("success")) {
                        post.setSaved(!post.isSaved());
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

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private GridView horizontalGridView;

        private HeaderViewHolder(View view) {
            super(view);
            horizontalGridView = view.findViewById(R.id.gridView);
        }

        private void gridViewSetting() {

            // this is size of your list with data
            int size = categoryMainArrayList.size();
            // Calculated single Item Layout Width for each grid element .. for me it was ~100dp
            int width = 75;

            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;

            int totalWidth = (int) (width * size * density);
            int singleItemWidth = (int) (width * density);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

            horizontalGridView.setLayoutParams(params);
            horizontalGridView.setColumnWidth(singleItemWidth);
            horizontalGridView.setHorizontalSpacing(1);
            horizontalGridView.setStretchMode(GridView.STRETCH_SPACING);
            horizontalGridView.setNumColumns(size);

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtMeetingName, txtPostDescription, txtPostLikesCount, txtCreatedDate;
        private TextView txtViewAllComments;
        private View viewLineLikes;
        private TextView txtMeetingState;
        private ImageView btnpostDelete;
        private ImageView postImage, ivProfile;
        private ImageView likePost, commentPost, ivSavePost, commentSendMessage;
        private View rlParentMeeting, rlMeeting1, ibPlay;

        private MyViewHolder(View view) {
            super(view);
            txtPostLikesCount = view.findViewById(R.id.txtPostLikesCount);
            txtViewAllComments = view.findViewById(R.id.txtViewAllComments);
            viewLineLikes = view.findViewById(R.id.viewLineLikes);
            txtMeetingName = view.findViewById(R.id.txtMeetingName);
            txtMeetingState = view.findViewById(R.id.txtMeetingState);
            txtPostDescription = view.findViewById(R.id.txtPostDescription);
            postImage = view.findViewById(R.id.postImage);
            ivProfile = view.findViewById(R.id.ivProfile);
            txtMeetingName.setTypeface(font, Typeface.BOLD);
            btnpostDelete = view.findViewById(R.id.btnpostDelete);
            likePost = view.findViewById(R.id.likePost);
            commentPost = view.findViewById(R.id.commentPost);
            ivSavePost = view.findViewById(R.id.ivSavePost);
            commentSendMessage = view.findViewById(R.id.commentSendMessage);
            rlParentMeeting = view.findViewById(R.id.rlParentMeeting);
            rlMeeting1 = view.findViewById(R.id.rlMeeting1);
            txtCreatedDate = view.findViewById(R.id.txtCreatedDate);
            ibPlay = view.findViewById(R.id.ibPlay);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;
    private Typeface font;
    private SimpleDateFormat sdf;
    private SimpleDateFormat formatter;
    private ColorStateList selectedColorStateList;
    private ColorStateList unselectedColorStateList;
    private ListListener listListener;

    public PostNewsfeedAdapter(Activity activity, List<Posts> moviesList, ArrayList<CategoryMain> categoryMainArrayList, ListListener listListener) {
        this.originalList = moviesList;
        this.categoryMainArrayList = categoryMainArrayList;
        this.activity = activity;
        this.listListener = listListener;
        selectedColorStateList = CommonUtil.getColorStateList(activity, R.color.white);
        unselectedColorStateList = CommonUtil.getColorStateList(activity, R.color.white);
        layoutInflater = LayoutInflater.from(activity);
        preferenceUtil = new PreferenceUtil(activity);
        font = Typeface.createFromAsset(activity.getAssets(), "fontawesome-webfont.ttf");
        sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
        formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.getDefault());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = layoutInflater.inflate(R.layout.post_item, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = layoutInflater.from(parent.getContext()).inflate(R.layout.category_nearby, parent, false);
            return new HeaderViewHolder(itemView);
        } else return null;


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) viewHolder;
            try {
                headerHolder.gridViewSetting();
                GridElementAdapter gridElementAdapter = new GridElementAdapter(activity, categoryMainArrayList);
                headerHolder.horizontalGridView.setAdapter(gridElementAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MyViewHolder holder = (MyViewHolder) viewHolder;
            Posts post = originalList.get(position - 1);
            holder.rlParentMeeting.setTag(post);
            holder.rlParentMeeting.setOnClickListener(this);
            holder.rlMeeting1.setTag(post);
            holder.ibPlay.setTag(post);

            holder.rlMeeting1.setOnClickListener(this);
            holder.ibPlay.setOnClickListener(this);
            holder.txtViewAllComments.setOnClickListener(this);
            if (post.getPostmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                holder.commentSendMessage.setVisibility(View.GONE);
            } else {
                holder.commentSendMessage.setVisibility(View.VISIBLE);
                holder.commentSendMessage.setTag(post);
                holder.commentSendMessage.setOnClickListener(this);
            }

            if (TextUtils.isEmpty(post.getUsername())) {
                holder.txtMeetingName.setText("");
            } else {
                if (post.getFileType() != null && post.getFileType().equalsIgnoreCase("3")) {
                    String name = post.getUsername();
                    if (preferenceUtil.getUserMailId().equalsIgnoreCase(post.getPostmail())) {
                        name = "You";
                    }
//                holder.txtMeetingName.setText(name + " updated profile");
                    holder.txtMeetingName.setText(Html.fromHtml(" <b><font color='#000000'>" + name + "</font></b>  <n>updated profile</n>"));
                } else {
                    holder.txtMeetingName.setText(post.getUsername());
                }

            }

            if (TextUtils.isEmpty(post.getState()) && TextUtils.isEmpty(post.getCountry())) {
                holder.txtMeetingState.setText("");
                holder.txtMeetingState.setVisibility(View.GONE);
            } else if (TextUtils.isEmpty(post.getState())) {
                holder.txtMeetingState.setText(post.getCountry());
                holder.txtMeetingState.setVisibility(View.VISIBLE);
            } else {
                holder.txtMeetingState.setVisibility(View.VISIBLE);
                holder.txtMeetingState.setText(post.getState() + ", " + post.getCountry());
            }
            if (TextUtils.isEmpty(post.getDescription())) {
                holder.txtPostDescription.setText("");
                holder.txtPostDescription.setVisibility(View.GONE);
            } else {
                holder.txtPostDescription.setVisibility(View.VISIBLE);
                holder.txtPostDescription.setText(post.getDescription());
            }
            if (TextUtils.isEmpty(post.getTotalComments())) {
                holder.txtViewAllComments.setText("");
                holder.txtViewAllComments.setVisibility(View.GONE);
                holder.viewLineLikes.setVisibility(View.GONE);
            } else {
                holder.viewLineLikes.setVisibility(View.VISIBLE);
                holder.txtViewAllComments.setVisibility(View.VISIBLE);
                int total = 0;
                try {
                    total = Integer.valueOf(post.getTotalComments());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (total > 1) {
                    holder.txtViewAllComments.setText("View all " + post.getTotalComments() + " comments");
                } else if (total == 1) {
                    holder.txtViewAllComments.setText("View " + post.getTotalComments() + " comment");
                } else {
                    holder.txtViewAllComments.setText("");
                    holder.txtViewAllComments.setVisibility(View.GONE);
                    holder.viewLineLikes.setVisibility(View.GONE);
                }
            }

            holder.txtPostLikesCount.setText(post.getTotal_likes() + " likes");


            if (post.isLiked()) {
                holder.likePost.setImageResource(R.drawable.like_filled);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.likePost.setImageTintList(selectedColorStateList);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.likePost.setImageTintList(unselectedColorStateList);
                }
                holder.likePost.setImageResource(R.drawable.like);
            }
            if (post.isSaved()) {
                holder.ivSavePost.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                holder.ivSavePost.setImageResource(R.drawable.ic_star_border_black_24dp);
            }


            holder.likePost.setTag(post);
            holder.txtViewAllComments.setTag(post);
            holder.commentPost.setTag(post);
            holder.ivSavePost.setTag(post);
            holder.txtPostLikesCount.setTag(post);
            holder.likePost.setOnClickListener(this);
            holder.commentPost.setOnClickListener(this);
            holder.ivSavePost.setOnClickListener(this);
            holder.txtPostLikesCount.setOnClickListener(this);
            if (post.getPostmail() != null && post.getPostmail().equalsIgnoreCase(preferenceUtil.getUserMailId())) {
                holder.btnpostDelete.setTag(post);
                holder.btnpostDelete.setOnClickListener(this);
                holder.btnpostDelete.setVisibility(View.VISIBLE);
            } else {
                holder.btnpostDelete.setVisibility(View.GONE);
            }

            if (post.getCreated_date() != null && !post.getCreated_date().isEmpty()) {
                holder.txtCreatedDate.setVisibility(View.VISIBLE);
                try {
                    Date testDate = null;
                    testDate = sdf.parse(post.getCreated_date());
                    String newFormat = formatter.format(testDate);
                    holder.txtCreatedDate.setText(newFormat);
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.txtCreatedDate.setText(post.getCreated_date());
                }

            } else {
                holder.txtCreatedDate.setVisibility(View.GONE);
            }

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


            if (post.getProfile_image() != null && !post.getProfile_image().isEmpty()) {

                Glide.with(activity).load(post.getProfile_image()).asBitmap().centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivProfile);
            } else {
                Glide.with(activity).load(R.drawable.profile)
                        .into(holder.ivProfile);
            }

            if (listListener != null && position == (getItemCount() - 1)) {
                listListener.bottomReached();
            }

        }
    }

    @Override
    public int getItemCount() {
        return originalList == null ? 1 : originalList.size() + 1;
    }

}