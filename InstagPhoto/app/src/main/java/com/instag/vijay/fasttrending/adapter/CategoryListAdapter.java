package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.CategoryItem;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by vijay on 24/12/17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> implements View.OnClickListener {

    private List<CategoryItem> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlivImage:
                Object object = v.getTag();
                if (object instanceof CategoryItem) {
                    CategoryItem userModel = (CategoryItem) object;
//                    Intent intent = new Intent(activity, ProfileView.class);
//                    activity.startActivity(intent);
                }
                break;
            case R.id.bfollow:
                object = v.getTag();
                if (object instanceof CategoryItem) {
                    CategoryItem userModel = (CategoryItem) object;

                }
                break;
            case R.id.btnpostDelete:
                object = v.getTag();
                if (object instanceof CategoryItem) {
                    CategoryItem post = (CategoryItem) object;
                    showMeetingtAlert(activity, activity.getString(R.string.app_name), "Are you sure want to delete this post?", post);
                }
                break;
            case R.id.rlnotification:
                object = v.getTag();
                if (object instanceof CategoryItem) {
                    CategoryItem notification = (CategoryItem) object;
//                    Intent intent = new Intent(activity, PostView.class);
//                    intent.putExtra("postId", notification.getPost_id());
//                    activity.startActivity(intent);
                }
                break;
        }
    }


    private void showMeetingtAlert(Activity activity, String title, String message, final CategoryItem post) {

        new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText(title)
                .setContentText(message)
//                .setCustomImage(R.drawable.app_logo_back)
                .setCancelText("No").setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
//                        deletePost(post);
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
        private TextView shopname;
        private TextView address;
        private ImageView ivImage;
        private TextView mail;
        private TextView subcat;
        private TextView city;
        private TextView state;
        private Button btnview;


        private MyViewHolder(View view) {
            super(view);
            shopname = view.findViewById(R.id.shopname);
            address = view.findViewById(R.id.address);
            ivImage = view.findViewById(R.id.ivImage);
            subcat = view.findViewById(R.id.subcat);
            mail = view.findViewById(R.id.mail);
            city = view.findViewById(R.id.city);
            state = view.findViewById(R.id.state);
            btnview = view.findViewById(R.id.btnview);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public CategoryListAdapter(Activity activity, List<CategoryItem> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.category_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryItem post = originalList.get(position);
        holder.btnview.setOnClickListener(this);
        holder.btnview.setTag(post);
//        holder.txtUsername.setText(post.getUsername());
//        holder.txtTitle.setText(post.getTitle());
//        holder.txtPostDescription.setText(post.getDescription());


        if (TextUtils.isEmpty(post.getBusiness_name())) {
            holder.shopname.setText("");
        } else {
            holder.shopname.setText(post.getBusiness_name());
        }
        if (TextUtils.isEmpty(post.getBusiness_desc())) {
            holder.address.setText("");
        } else {
            holder.address.setText(post.getBusiness_desc());
        }
        if (TextUtils.isEmpty(post.getMail())) {
            holder.mail.setText("");
        } else {
            holder.mail.setText(post.getMail());
        }
        if (TextUtils.isEmpty(post.getState())) {
            holder.state.setText("");
        } else {
            holder.state.setText(post.getState());
        }
        if (TextUtils.isEmpty(post.getBusiness_location())) {
            holder.city.setText("");
        } else {
            holder.city.setText(post.getBusiness_location());
        }

        if (TextUtils.isEmpty(post.getPhone())) {
            holder.subcat.setVisibility(View.INVISIBLE);
            holder.subcat.setText("");
        } else {
            holder.subcat.setVisibility(View.VISIBLE);
            holder.subcat.setText("ph:" + post.getPhone());
        }

        holder.ivImage.setImageBitmap(null);
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            Glide.with(activity).load("http://www.xooads.com/FEELOUTADMIN/img/upload/" + post.getImage()).centerCrop()
                    .thumbnail(0.5f).placeholder(R.drawable.icon)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivImage);
        } else {
            Glide.with(activity).load(R.drawable.icon)
                    .into(holder.ivImage);
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
