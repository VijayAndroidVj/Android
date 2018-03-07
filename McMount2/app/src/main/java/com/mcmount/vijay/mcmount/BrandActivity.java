package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcmount.vijay.mcmount.retrofit.ApiClient;
import com.mcmount.vijay.mcmount.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 23/9/17.
 */

public class BrandActivity extends BaseActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private String categoryId = "";
    private Activity activity;
    private ArrayList<Cateegory> productList = new ArrayList<>();
    private TextView txtLabel;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actionbar_main);
        activity = this;

        onlyActionbar();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        txtLabel = (TextView) findViewById(R.id.txtLabel);

        TextView title = (TextView) findViewById(R.id.txtTitle);
        title.setVisibility(View.VISIBLE);
        title.setText("Select Brand");

        findViewById(R.id.rlViewPager).setVisibility(View.GONE);

//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getIntent() != null && getIntent().getExtras() != null) {
            categoryId = getIntent().getStringExtra("id");
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getBrandProducts();
            }
        });
        getBrandProducts();
    }

    private void getBrandProducts() {
        if (CommonUtil.isNetworkAvailable(activity)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryListModel> call = apiService.brand(categoryId);
            call.enqueue(new Callback<CategoryListModel>() {
                @Override
                public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    CategoryListModel categoryListModel = response.body();
                    if (categoryListModel != null) {
                        if (categoryListModel.getBrand_name() == null || categoryListModel.getBrand_name().size() == 0) {
                            txtLabel.setVisibility(View.VISIBLE);
                            txtLabel.setText("Products not available");
                            Toast.makeText(activity, "Products not Found", Toast.LENGTH_SHORT).show();
                        } else {
                            txtLabel.setVisibility(View.GONE);
                            productList = categoryListModel.getBrand_name();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(productList, activity, BrandActivity.this);
                            recyclerView.setAdapter(categoryAdapter);
                        }
                    } else {
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Could not connect to server");
                        Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CategoryListModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
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
        /*try {
            Intent intent = new Intent(activity, ModelActivity.class);
            intent.putExtra("id", ((Cateegory) object).getBrand_id());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            // Create custom dialog object
//            AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.service_type_layout, null);
            dialog.setView(dialogView);


            // Set dialog title
            final CheckBox checkBox = dialogView.findViewById(R.id.checkbox);
            final CheckBox checkBox2 = dialogView.findViewById(R.id.checkbox2);
            final CheckBox checkBox3 = dialogView.findViewById(R.id.checkbox3);
// Inflate the custom layout/view
            final AlertDialog b = dialog.create();
            dialogView.findViewById(R.id.btnServiceTypeNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (checkBox.isChecked() || checkBox2.isChecked() || checkBox3.isChecked()) {
                        b.dismiss();
                        Intent intent = new Intent(activity, RegisterComplaint.class);
//                    intent.putExtra("id", ((Cateegory) object).getBrand_id());
                        startActivity(intent);
                    } else {
                        Toast.makeText(activity, "Please select service type", Toast.LENGTH_SHORT).show();
                    }
//                    openRegisterComplaintDialog();
                }
            });

            b.show();


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        checkBox2.setChecked(false);
                        checkBox3.setChecked(false);
                    }
                }
            });


            checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        checkBox.setChecked(false);
                        checkBox3.setChecked(false);
                    }
                }
            });

            checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        checkBox2.setChecked(false);
                        checkBox.setChecked(false);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog dialogOpenRegister;

    private void openRegisterComplaintDialog() {
        try {
// Create custom dialog object
            dialogOpenRegister = new Dialog(activity);
            // Include dialog.xml file
            dialogOpenRegister.setContentView(R.layout.register_complaint);
            // Set dialog title
            dialogOpenRegister.setTitle("McMount");

// Inflate the custom layout/view

            dialogOpenRegister.findViewById(R.id.btnRegisterComplaint).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogOpenRegister.dismiss();
                }
            });
            dialogOpenRegister.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
