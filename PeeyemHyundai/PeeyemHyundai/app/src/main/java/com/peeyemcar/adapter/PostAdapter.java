package com.peeyemcar.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.peeyem.app.R;
import com.peeyemcar.models.Vechile;
import com.peeyemcar.utils.PreferenceUtil;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> implements View.OnClickListener {

    private List<Vechile> originalList;
    private PreferenceUtil preferenceUtil;

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
        private TextView input_model;
        private TextView input_color;
        private TextView input_enginno;
        private TextView input_exv;
        private TextView input_teflon;
        private TextView input_address;

        private MyViewHolder(View view) {
            super(view);
            input_model = view.findViewById(R.id.input_model);
            input_color = view.findViewById(R.id.input_color);
            input_enginno = view.findViewById(R.id.input_enginno);
            input_exv = view.findViewById(R.id.input_exv);
            input_teflon = view.findViewById(R.id.input_teflon);
            input_address = view.findViewById(R.id.input_address);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public PostAdapter(Activity activity, List<Vechile> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
        preferenceUtil = new PreferenceUtil(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.myvechle, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Vechile post = originalList.get(position);

        if (TextUtils.isEmpty(post.getModel())) {
            holder.input_model.setText("");
        } else {
            holder.input_model.setText(post.getModel());
        }

        if (TextUtils.isEmpty(post.getColor())) {
            holder.input_color.setText("");
        } else {
            holder.input_color.setText(post.getColor());
        }

        if (TextUtils.isEmpty(post.getEngine_no())) {
            holder.input_enginno.setText("");
        } else {
            holder.input_enginno.setText(post.getEngine_no());
        }

        if (TextUtils.isEmpty(post.getExtended_warrenty())) {
            holder.input_exv.setText("");
        } else {
            holder.input_exv.setText(post.getExtended_warrenty());
        }

        if (TextUtils.isEmpty(post.getTeflon())) {
            holder.input_teflon.setText("");
        } else {
            holder.input_teflon.setText(post.getTeflon());
        }

        if (TextUtils.isEmpty(post.getAddress())) {
            holder.input_address.setText("");
        } else {
            holder.input_address.setText(post.getAddress());
        }

    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}