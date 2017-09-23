package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.mcmount.vijay.mcmount.retrofit.ApiClient;
import com.mcmount.vijay.mcmount.retrofit.ApiInterface;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);

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
        getCategories();
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
            showProgress(MainActivity.this);
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<CategoryListModel> call = apiService.category();
            call.enqueue(new Callback<CategoryListModel>() {
                @Override
                public void onResponse(Call<CategoryListModel> call, Response<CategoryListModel> response) {
                    Log.d("", "response: " + response.body());

                    CategoryListModel categoryListModel = response.body();
                    if (categoryListModel != null) {
                        if (categoryListModel.getCategory_name() == null || categoryListModel.getCategory_name().size() == 0) {
                            Toast.makeText(MainActivity.this, "Category not Found", Toast.LENGTH_SHORT).show();
                        } else {
                            categoryListModelArrayList = categoryListModel.getCategory_name();
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryListModelArrayList, MainActivity.this, MainActivity.this);
                            recyclerView.setAdapter(categoryAdapter);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                    }

                    dismissProgress();

                }

                @Override
                public void onFailure(Call<CategoryListModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("", t.toString());
                    dismissProgress();
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
