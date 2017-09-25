package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcmount.vijay.mcmount.retrofit.ApiClient;
import com.mcmount.vijay.mcmount.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mcmount.vijay.mcmount.MainActivity.dismissProgress;
import static com.mcmount.vijay.mcmount.MainActivity.showProgress;

/**
 * Created by vijay on 23/9/17.
 */

public class ModelActivity extends AppCompatActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private String categoryId = "";
    private Activity activity;
    private ArrayList<Cateegory> productList = new ArrayList<>();
    private TextView txtLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        txtLabel = (TextView) findViewById(R.id.txtLabel);

        findViewById(R.id.rlViewPager).setVisibility(View.GONE);

//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getIntent() != null && getIntent().getExtras() != null) {
            categoryId = getIntent().getStringExtra("id");
        }
        getModelProducts();
    }

    private void getModelProducts() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            showProgress(activity);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryListModel> call = apiService.model(categoryId);
            call.enqueue(new Callback<CategoryListModel>() {
                @Override
                public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                    Log.d("", "response: " + response.body());

                    CategoryListModel categoryListModel = response.body();
                    if (categoryListModel != null) {
                        if (categoryListModel.getModel_name() == null || categoryListModel.getModel_name().size() == 0) {
                            txtLabel.setVisibility(View.VISIBLE);
                            txtLabel.setText("Products not available");
                            Toast.makeText(activity, "Products not Found", Toast.LENGTH_SHORT).show();
                        } else {
                            txtLabel.setVisibility(View.GONE);
                            productList = categoryListModel.getModel_name();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(productList, activity, ModelActivity.this);
                            recyclerView.setAdapter(categoryAdapter);
                        }
                    } else {
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Could not connect to server");
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                    dismissProgress();

                }

                @Override
                public void onFailure(Call<CategoryListModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    dismissProgress();
                    if (t.getMessage() != null && t.getMessage().contains("Expected BEGIN_OBJECT but was BEGIN_ARRAY")) {
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Products not available");
                        Toast.makeText(activity, "Products not Found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Could not connect to server");
                    }
                }
            });
        } else {
            txtLabel.setVisibility(View.VISIBLE);
            txtLabel.setText("Check your internet connection!");
            Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClicked(int position, Object object) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
