package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mcmount.vijay.mcmount.retrofit.ApiClient;
import com.mcmount.vijay.mcmount.retrofit.ApiInterface;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    TextView txtLabel;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLabel = (TextView) findViewById(R.id.txtLabel);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        initNavigationDrawer();
        BannerModel bannerModel = new BannerModel();
        bannerModel.setTitle("Offer");
        bannerModel.setImage("https://www.apptha.com/blog/wp-content/uploads/2013/05/magento-product-sold-count-extension.jpg");
        bannerModelArrayList.add(bannerModel);

        bannerModel = new BannerModel();
        bannerModel.setTitle("Offer 1");
        bannerModel.setImage("http://sign-design-software.rapidsoftsystems.com/images/MainBanner.jpg");
        bannerModelArrayList.add(bannerModel);
        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);
        // Pass results to ViewPagerAdapter Class
        adapter = new ViewPagerAdapter(MainActivity.this, bannerModelArrayList);
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getCategories();
            }
        });
        getCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.profile:
//                Intent intentd = new Intent(MainActivity.this, Setting.class);
//
//                startActivity(intentd);
                // Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
                // showOptiontAlert(MainActivity.this, "Choose", "")
                ;
                return (true);
            case R.id.raisebybrand:
//                Intent about = new Intent(MainActivity.this, Aboutus.class);
//
//                startActivity(about);
                // Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
                // showOptiontAlert(MainActivity.this, "Choose", "")
                ;
                return (true);
            case R.id.mcbox:
                // finish();
                // showMeetingtAlert(MainActivity.this, "Logout", "Are you sure want to logout?");
                return (true);
            case R.id.warrenty:
                // finish();
                // showMeetingtAlert(MainActivity.this, "Logout", "Are you sure want to logout?");
                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }


    private ArrayList<Cateegory> categoryListModelArrayList = new ArrayList<>();
    public static ProgressDialog progressDoalog;

    public static void showProgress(Activity activity) {

        if (progressDoalog == null) {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Its loading....");
            progressDoalog.setTitle("ProgressDialog bar example");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        if (!progressDoalog.isShowing())
            progressDoalog.show();
    }

    public static void dismissProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.dismiss();
    }

    private void getCategories() {
        if (CommonUtil.isNetworkAvailable(MainActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryListModel> call = apiService.category();
            call.enqueue(new Callback<CategoryListModel>() {
                @Override
                public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                    Log.d("", "response: " + response.body());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);
                    CategoryListModel categoryListModel = response.body();
                    if (categoryListModel != null) {
                        if (categoryListModel.getCategory_name() == null || categoryListModel.getCategory_name().size() == 0) {
                            Toast.makeText(MainActivity.this, "Category not Found", Toast.LENGTH_SHORT).show();
                            txtLabel.setVisibility(View.VISIBLE);
                        } else {
                            categoryListModelArrayList = categoryListModel.getCategory_name();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryListModelArrayList, MainActivity.this, MainActivity.this);
                            recyclerView.setAdapter(categoryAdapter);
                            txtLabel.setVisibility(View.GONE);
                        }
                    } else {
                        txtLabel.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CategoryListModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(View.GONE);

                    if (t.getMessage() != null && t.getMessage().contains("Expected BEGIN_OBJECT but was BEGIN_ARRAY")) {
                        Toast.makeText(MainActivity.this, "Products not Found", Toast.LENGTH_SHORT).show();
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Products not Found");
                    } else {
                        txtLabel.setVisibility(View.VISIBLE);
                        txtLabel.setText("Could not connect to server");
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClicked(int position, Object object) {
        try {
            Intent intent = new Intent(MainActivity.this, ProductActivity.class);
            intent.putExtra("id", ((Cateegory) object).getRanduniq());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
