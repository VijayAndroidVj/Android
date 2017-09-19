package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vijay on 17/9/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Cateegory> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtname);
            imageView = (ImageView) view.findViewById(R.id.ivImage);
        }
    }

    private Activity activity;

    public CategoryAdapter(List<Cateegory> moviesList, Activity activity) {
        this.moviesList = moviesList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cateegory movie = moviesList.get(position);
        holder.title.setText(movie.getName());

        //Loading Image from URL
        Picasso.with(activity)
                .load("https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png")
                .placeholder(R.drawable.ic_account)   // optional
                .resize(400, 400)                        // optional
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
