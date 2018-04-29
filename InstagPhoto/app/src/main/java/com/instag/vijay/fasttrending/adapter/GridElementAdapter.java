package com.instag.vijay.fasttrending.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.activity.SubCategoryItemListActivity;
import com.instag.vijay.fasttrending.model.CategoryMain;

import java.util.List;

/**
 * Created by vijay on 25/3/18.
 */

public class GridElementAdapter extends BaseAdapter {

    private Activity context;
    private List<CategoryMain> categoryMainArrayList;
    // String[] colors = new String[]{"#E91E63", "#4CAF50", "#9C27B0", "#F44336", "#3F51B5", "#2196F3", "#673AB7", "#00BCD4", "#009688", "#03A9F4", "#4CAF50", "#733f55", "#FFC107", "#4CAF50"};

    public GridElementAdapter(Activity context, List<CategoryMain> categoryMainArrayList) {
        this.context = context;
        this.categoryMainArrayList = categoryMainArrayList;

    }


    @Override
    public int getCount() {
        return categoryMainArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryMainArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    LayoutInflater inflater;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View gridView = view;

        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            gridView = inflater.inflate(R.layout.category_main_item, null);
        }

        CategoryMain categoryMain = categoryMainArrayList.get(i);
        TextView txtCategory = gridView.findViewById(R.id.txtCategory);
        ImageView ivCategory = gridView.findViewById(R.id.ivCategory);
        //  CardView cv_category = gridView.findViewById(R.id.cv_category);
        gridView.setTag(categoryMain);
        // cv_category.setCardBackgroundColor(Color.parseColor(colors[new Random().nextInt(colors.length)]));
        if (categoryMain.getName() != null) {
            txtCategory.setText(categoryMain.getName());
        }
        if (categoryMain.getImage() != null && !categoryMain.getImage().isEmpty()) {
            Glide.with(context).load("http://www.xooads.com/FEELOUTADMIN/img/upload/" + categoryMain.getImage()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivCategory);
        }
        gridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryMain categoryMain1 = (CategoryMain) v.getTag();
                Intent intent = new Intent(context, SubCategoryItemListActivity.class);
                intent.putExtra("category", categoryMain1);
                context.startActivity(intent);
            }
        });
        return gridView;
    }
}
/*

public static class SimpleViewHolder extends RecyclerView.ViewHolder {
    private TextView txtCategory;
    private ImageView ivCategory;
    private CardView cv_category;

    public SimpleViewHolder(View view) {
        super(view);
        txtCategory = view.findViewById(R.id.txtCategory);
        ivCategory = view.findViewById(R.id.ivCategory);
        cv_category = view.findViewById(R.id.cv_category);
    }

}

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(this.context).inflate(R.layout.category_main_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        CategoryMain categoryMain = categoryMainArrayList.get(position);

        holder.cv_category.setTag(categoryMain);
        holder.cv_category.setCardBackgroundColor(Color.parseColor(colors[new Random().nextInt(colors.length)]));
        if (categoryMain.getName() != null) {
            holder.txtCategory.setText(categoryMain.getName());
        }
        if (categoryMain.getImage() != null && !categoryMain.getImage().isEmpty()) {
            Glide.with(context).load("http://www.xooads.com/FEELOUTADMIN/img/upload/" + categoryMain.getImage()).asBitmap().centerCrop()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivCategory);
        }
        holder.cv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryMain categoryMain1 = (CategoryMain) v.getTag();
                Intent intent = new Intent(context, CategoryItemListActivity.class);
                intent.putExtra("category", categoryMain1);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return this.categoryMainArrayList.size();
    }
}
*/
