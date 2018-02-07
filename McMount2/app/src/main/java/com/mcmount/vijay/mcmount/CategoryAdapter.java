package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import static com.mcmount.vijay.mcmount.retrofit.ApiClient.serverAddress1;

/**
 * Created by vijay on 17/9/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Cateegory> moviesList;
    private ListItemClickListener listItemClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private ImageView imageView;
        private View card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtname);
            imageView = (ImageView) view.findViewById(R.id.ivImage);
            card_view = view.findViewById(R.id.card_view);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                switch (view.getId()) {
                    case R.id.card_view:
                        Object obj = view.getTag();
                        if (obj instanceof Integer) {
                            int pos = (int) obj;
                            listItemClickListener.itemClicked(pos, moviesList.get(pos));
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Activity activity;

    public CategoryAdapter(List<Cateegory> moviesList, Activity activity, ListItemClickListener listItemClickListener) {
        this.moviesList = moviesList;
        this.activity = activity;
        this.listItemClickListener = listItemClickListener;
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
        holder.card_view.setTag(position);
        String url = movie.getIcon_image();
        if (movie.getIcon_image().contains("..")) {
            url = url.substring(2, url.length());
        }
        Glide.with(activity).load(serverAddress1 + url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
