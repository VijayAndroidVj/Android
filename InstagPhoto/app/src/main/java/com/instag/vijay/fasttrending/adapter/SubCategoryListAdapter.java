package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.CategoryItemListActivity;
import com.instag.vijay.fasttrending.model.SubCategory;

import java.util.List;

/**
 * Created by vijay on 24/12/17.
 */

public class SubCategoryListAdapter extends RecyclerView.Adapter<SubCategoryListAdapter.MyViewHolder> implements View.OnClickListener {

    private List<SubCategory> originalList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSubCategory:
                Object object = v.getTag();
                if (object instanceof SubCategory) {
                    SubCategory userModel = (SubCategory) object;
                    Intent intent = new Intent(activity, CategoryItemListActivity.class);
                    intent.putExtra("category", userModel);
                    activity.startActivity(intent);
                }
                break;
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtSubCategory;

        private MyViewHolder(View view) {
            super(view);
            txtSubCategory = view.findViewById(R.id.txtSubCategory);
        }
    }

    private Activity activity;
    private LayoutInflater layoutInflater;

    public SubCategoryListAdapter(Activity activity, List<SubCategory> moviesList) {
        this.originalList = moviesList;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.subcategory_main_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCategory post = originalList.get(position);
        holder.txtSubCategory.setOnClickListener(this);
        holder.txtSubCategory.setTag(post);

        if (TextUtils.isEmpty(post.getCategory())) {
            holder.txtSubCategory.setText("");
        } else {
            holder.txtSubCategory.setText(post.getCategory());
        }
    }

    @Override
    public int getItemCount() {
        return originalList == null ? 0 : originalList.size();
    }

}
