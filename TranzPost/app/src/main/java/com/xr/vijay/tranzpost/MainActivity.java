package com.xr.vijay.tranzpost;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    TextView txtLabel, btnBookTruck, txtBookLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentActivity = this;

        initNavigationDrawer();

        ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() == 0) {
        } else {
            PermissionCheck.requestPermission(this, pendingPermissions, 111);
        }

        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.custom_container);
        View view = View.inflate(activity, R.layout.activity_home, parentLayout);
        setInitUI(view);
        setRegisterUI();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        initNavigationDrawer();

        BannerModel bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQdZwQ5n0GMCNgFRBlnTv1vDT5Ks1QjicAg2S6VA5qV7Ko1fcJ");
        bannerModelArrayList.add(bannerModel);

        bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQ5WmooFciyO8ccZIOVqjeohJgk83gSkbqkwN5V0U6bt9AQoaITQ");
        bannerModelArrayList.add(bannerModel);

        bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://static.vyom.com/img/banner.png");
        bannerModelArrayList.add(bannerModel);


        bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSpO5FGyagKefZQKPrNpFH917R7GRnr00KKlSgjLKpvU09B_tNM");
        bannerModelArrayList.add(bannerModel);
        bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQyMkA7kW192VsSlV4HgE8edjx25injDErZfdQdbPcBTW-1lSjnRQ");
        bannerModelArrayList.add(bannerModel);

        bannerModel = new BannerModel();
        bannerModel.setTitle("click here");
        bannerModel.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe7qKrWr8ueLAw2oVSYP6nirIY-SD91hrVyVX8i-3EBSZzXVrRiA");
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

    }

    private void setRegisterUI() {


    }

    private void setInitUI(View view) {
        txtLabel = (TextView) view.findViewById(R.id.txtLabel);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        btnBookTruck = (TextView) view.findViewById(R.id.btnBookTruck);
        txtBookLoad = (TextView) view.findViewById(R.id.txtBookLoad);
        if (preferenceUtil.getLogintype().equalsIgnoreCase("Customer")) {
            txtBookLoad.setText("Find trucks across India");
            btnBookTruck.setText("Find Trucks");
        } else {
            txtBookLoad.setText("Book loads for your truck all across India");
            btnBookTruck.setText("Book Loads");
        }

    }
}
