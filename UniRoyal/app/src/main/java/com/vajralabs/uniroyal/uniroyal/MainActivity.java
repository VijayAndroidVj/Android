package com.vajralabs.uniroyal.uniroyal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vajralabs.uniroyal.uniroyal.activity.AboutUs;
import com.vajralabs.uniroyal.uniroyal.activity.ContactUs;
import com.vajralabs.uniroyal.uniroyal.activity.DownloadCatelog;
import com.vajralabs.uniroyal.uniroyal.activity.Enquiry;
import com.vajralabs.uniroyal.uniroyal.activity.Gallery;
import com.vajralabs.uniroyal.uniroyal.activity.Mission;
import com.vajralabs.uniroyal.uniroyal.activity.OurProducts;
import com.vajralabs.uniroyal.uniroyal.adapter.CustomPagerAdapter;
import com.vajralabs.uniroyal.uniroyal.model.BannerModel;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiClient;
import com.vajralabs.uniroyal.uniroyal.retrofit.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;
    private CustomPagerAdapter customPagerAdapter;
    private ViewPager mViewPager;
    private ArrayList<BannerModel> list = new ArrayList<>();
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    Activity activity;
    View flProducts;
    View flGallery;
    View flContactUs;
    View flEnquiry;
    View flDownloaddCatelog;
    View flShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;
        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = navHeader.findViewById(R.id.name);
        txtWebsite = navHeader.findViewById(R.id.website);
        imgNavHeaderBg = navHeader.findViewById(R.id.img_header_bg);
        imgProfile = navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();
        showProgress();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
//        list.add("https://thumbs.dreamstime.com/z/banner-image-blog-blue-keywords-29080904.jpg");
//        list.add("https://news.fiu.edu/wp-content/uploads/Banner-Image.JPG");
//        list.add("http://sr-jobs.in/wp-content/uploads/2016/07/banner-image-home.jpg");

        getBannerLists();
        setBannerList();

    }

    private ProgressDialog progressBar1;

    private void showProgress() {
        progressBar1 = new ProgressDialog(this);
        progressBar1.setCancelable(true);//you can cancel it by pressing back button
        progressBar1.setMessage("Loading ...");
        progressBar1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar1.setProgress(0);//initially progress is 0
        progressBar1.setMax(100);//sets the maximum value 100
        progressBar1.show();//displays the progress bar
    }

    private void hideProgress() {
        if (progressBar1 != null && progressBar1.isShowing()) {
            progressBar1.dismiss();
            progressBar1 = null;
        }
    }

    private void getBannerLists() {
        try {
            if (CommonUtil.isNetworkAvailable(activity)) {
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<ArrayList<BannerModel>> call = apiService.get_banners();
                call.enqueue(new Callback<ArrayList<BannerModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BannerModel>> call, Response<ArrayList<BannerModel>> response) {
                        hideProgress();
                        Log.d("", "response: " + response.body());
                        ArrayList<BannerModel> sigInResponse = response.body();
                        if (sigInResponse != null) {
                            list = sigInResponse;
                            setBannerList();
                        } else {
                            Toast.makeText(activity, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<BannerModel>> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("", t.toString());
                        hideProgress();
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                hideProgress();
                Toast.makeText(activity, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
        }
    }

    private void setBannerList() {
        customPagerAdapter = new CustomPagerAdapter(this, list);
        mViewPager = findViewById(R.id.pager);
        flProducts = findViewById(R.id.flProducts);
        flGallery = findViewById(R.id.flGallery);
        flContactUs = findViewById(R.id.flContactUs);
        flEnquiry = findViewById(R.id.flEnquiry);
        flDownloaddCatelog = findViewById(R.id.flDownloaddCatelog);
        flShare = findViewById(R.id.flShare);
        mViewPager.setAdapter(customPagerAdapter);
        flProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OurProducts.class));
            }
        });
        flGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Gallery.class));
            }
        });
        flContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContactUs.class));
            }
        });
        flEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Enquiry.class));
            }
        });
        flDownloaddCatelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DownloadCatelog.class));
            }
        });
        flShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShareIntent();
            }
        });

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(getString(R.string.app_name));
        txtWebsite.setText("");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(R.drawable.uniroyallogo)
                .crossFade()
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
//        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
       /* Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }*/

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

        /*viewInfo = (TextView) findViewById(R.id.txtContactInfo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewContact);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getBannerLists();
                getcategoryLists();
                swipeRefreshLayout.setRefreshing(false);
                //setList();
            }
        });

        getcategoryLists();*/
//        setList();
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

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
//                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_aboutus:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        startActivity(new Intent(MainActivity.this, AboutUs.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_mission:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        startActivity(new Intent(MainActivity.this, Mission.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_products:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        startActivity(new Intent(MainActivity.this, OurProducts.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_gallery:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        startActivity(new Intent(MainActivity.this, Gallery.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_contactus:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_SETTINGS;
                        startActivity(new Intent(MainActivity.this, ContactUs.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_enquiry:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Enquiry.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_catelog:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, DownloadCatelog.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        // launch new intent instead of loading fragment
                        getShareIntent();
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    public void getShareIntent() {
        String playStoreLink = "https://play.google.com/store/apps/details?id=com.vajralabs.uniroyal.uniroyal&hl=en";
        String sharedText = "Download This app ! It's free, easy and smart.\n\n" + playStoreLink;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        startActivity(Intent.createChooser(shareIntent, "Send To"));
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_logout) {
//            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
//            return true;
//        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


}

