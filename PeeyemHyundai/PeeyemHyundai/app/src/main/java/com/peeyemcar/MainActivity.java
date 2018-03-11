package com.peeyemcar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.peeyem.app.R;
import com.peeyemcar.adapter.DrawerListAdapter;
import com.peeyemcar.adapter.UltraPagerAdapter;
import com.peeyemcar.utils.PreferenceUtil;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    CardView product, aboutus, service, breakdown, testdrive, sOffer, locateus, shareapp, callHelpline;

    private static ProgressDialog progressDoalog;
    private Activity activity;

    public static void showProgress(Activity activity) {

        try {
            if (progressDoalog == null) {
                progressDoalog = new ProgressDialog(activity);
                progressDoalog.setMax(100);
                progressDoalog.setMessage("Please wait....");
                progressDoalog.setTitle(activity.getString(R.string.app_name));
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDoalog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (progressDoalog != null && progressDoalog.isShowing())
            progressDoalog.dismiss();
        progressDoalog = null;
    }

    private UltraViewPager.Orientation gravity_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        initNavigationDrawer();
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("New 2017 Creta", R.drawable.article1);
        file_maps.put("Hyundai No.1", R.drawable.article2);
        file_maps.put("#RoadChaser", R.drawable.article4);
        file_maps.put("The Next Gen Verna", R.drawable.article5);

        product = (CardView) findViewById(R.id.products);
        aboutus = (CardView) findViewById(R.id.aboutus);
        service = (CardView) findViewById(R.id.service);
        breakdown = (CardView) findViewById(R.id.breakdown);
        testdrive = (CardView) findViewById(R.id.test_drive);
        sOffer = (CardView) findViewById(R.id.s_offer);
        locateus = (CardView) findViewById(R.id.locateus);
        shareapp = (CardView) findViewById(R.id.shareapp);
        callHelpline = (CardView) findViewById(R.id.call_h);

        final UltraViewPager ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
//initialize UltraPagerAdapter，and add child view to UltraViewPager
        final UltraPagerAdapter adapter = new UltraPagerAdapter(this, true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String val = adapter.colors[new Random().nextInt(adapter.colors.length)];
                ultraViewPager.setBackgroundColor(Color.parseColor(val));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ultraViewPager.setMultiScreen(0.4f);
        ultraViewPager.setItemRatio(2.0f);
        ultraViewPager.setRatio(1.0f);
//        ultraViewPager.setMaxHeight(1400);
//        ultraViewPager.setAutoMeasureHeight(true);
        gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
//        ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
        ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
//initialize built-in indicator
//        ultraViewPager.initIndicator();
////set style of indicators
//        ultraViewPager.getIndicator()
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusColor(Color.GREEN)
//                .setNormalColor(Color.WHITE)
//                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
//set the alignment
//        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
////construct built-in indicator, and add it to  UltraViewPager
//        ultraViewPager.getIndicator().build();

//set an infinite loop
        ultraViewPager.setInfiniteLoop(true);
//enable auto-scroll mode
//        ultraViewPager.setAutoScroll(2000);

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ProductsListActivity.class);
                startActivity(in);
            }
        });

        findViewById(R.id.lay8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, UsedCarsActivity.class);
                startActivity(in);
            }
        });


        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, AboutUs.class);
                startActivity(in);
            }
        });

        findViewById(R.id.lay7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, EMICalculator.class);
                startActivity(in);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Service.class);
                startActivity(in);
            }
        });

        testdrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, TestDrive.class);
                startActivity(in);
            }
        });

        sOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ViewPDFActivity.class);
                startActivity(in);
            }
        });

        locateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ContactUs.class);
                startActivity(in);
            }
        });

        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(getShareIntent(), getResources().getString(R.string.send_to)));
            }
        });

        callHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "8281015333";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });
        breakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "0490-2328800";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);

            }
        });

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        ListView l = (ListView) findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }*/
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public Toolbar toolbar;
    public ActionBar actionBar;
    private ArrayList<String> navigation_items;
    private ImageView iv_drawermenu;
    private float lastTranslate = 0.0f;
    private PreferenceUtil preferenceUtil;

    public void setActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);

        iv_drawermenu = view.findViewById(R.id.iv_drawermenu);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);


    }

    public void initNavigationDrawer() {
        try {
            toolbar = findViewById(R.id.toolbar);
            setActionBar();

            drawerLayout = findViewById(R.id.drawer_layout);

            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

                @Override
                public void onDrawerClosed(View v) {
                    super.onDrawerClosed(v);
                }

                @Override
                public void onDrawerOpened(View v) {
                    super.onDrawerOpened(v);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);

                    float moveFactor = (drawerView.getWidth() * slideOffset);
                    TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
                    anim.setDuration(0);
                    anim.setFillAfter(true);
                    lastTranslate = moveFactor;

                }
            };
            TextView txtProfileName = findViewById(R.id.txtProfileName);
            preferenceUtil = new PreferenceUtil(MainActivity.this);

            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            iv_drawermenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    } else
                        drawerLayout.openDrawer(GravityCompat.START);
                }
            });
//            toolbar.setNavigationIcon(R.drawable.home);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//            });

            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            navigation_items = new ArrayList<>();
            navigation_items.add(getString(R.string.products));
            navigation_items.add(getString(R.string.myvechile));
            navigation_items.add(getString(R.string.service));
            navigation_items.add(getString(R.string.test_drive));
            navigation_items.add(getString(R.string.breakdown));
            navigation_items.add(getString(R.string.emi));
            navigation_items.add(getString(R.string.car_used));
            navigation_items.add(getString(R.string.offer));
            navigation_items.add(getString(R.string.about));
            navigation_items.add(getString(R.string.locateus));
            navigation_items.add(getString(R.string.share));
            navigation_items.add(getString(R.string.help));
            navigation_items.add(getString(R.string.feedback));
            navigation_items.add(getString(R.string.referearn));
            if (!TextUtils.isEmpty(preferenceUtil.getUserEmail())) {
//                txtProfileName.setText(preferenceUtil.getUserName());
                navigation_items.add(getString(R.string.logout));
            }

            int[] drawer_icons;
            drawer_icons = new int[]{R.drawable.products, R.drawable.myvecihle,
                    R.drawable.service, R.drawable.testdrive, R.drawable.breakdown, R.drawable.emi, R.drawable.usedcar, R.drawable.offer, R.drawable.aboutus, R.drawable.locateus, R.drawable.share, R.drawable.help, R.drawable.feedback, R.drawable.refer_and_earn, R.drawable.logout};
            ListView lv_drawer = findViewById(R.id.lv_drawer);
            DrawerListAdapter drawerListAdapter = new DrawerListAdapter(MainActivity.this, navigation_items, drawer_icons);
            lv_drawer.setAdapter(drawerListAdapter);
            lv_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if (navigation_items.get(position).equalsIgnoreCase("Products")) {
                            moveToActivity(0);
                        } else if (navigation_items.get(position).equalsIgnoreCase(getString(R.string.myvechile))) {
                            moveToActivity(13);
                        } else if (navigation_items.get(position).equalsIgnoreCase("Service")) {
                            moveToActivity(1);
                        } else if (navigation_items.get(position).equalsIgnoreCase("Test Drive")) {
                            moveToActivity(2);
                        } else if (navigation_items.get(position).equalsIgnoreCase("BreakDown")) {
                            moveToActivity(3);
                        } else if (navigation_items.get(position).equalsIgnoreCase("Emi Calculator")) {
                            moveToActivity(4);
                        } else if (navigation_items.get(position).equalsIgnoreCase("Car Used")) {
                            moveToActivity(5);

                        } else if (navigation_items.get(position).equalsIgnoreCase("Offer")) {
                            moveToActivity(6);

                        } else if (navigation_items.get(position).equalsIgnoreCase("About us")) {
                            moveToActivity(7);

                        } else if (navigation_items.get(position).equalsIgnoreCase("Locate us")) {
                            moveToActivity(8);

                        } else if (navigation_items.get(position).equalsIgnoreCase("Share")) {
                            moveToActivity(9);

                        } else if (navigation_items.get(position).equalsIgnoreCase("Help")) {
                            moveToActivity(10);

                        } else if (navigation_items.get(position).equalsIgnoreCase("FeedBack")) {
                            moveToActivity(11);

                        } else if (navigation_items.get(position).equalsIgnoreCase(getString(R.string.referearn))) {
                            moveToActivity(14);

                        } else if (navigation_items.get(position).equalsIgnoreCase(getString(R.string.logout))) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                            alertDialogBuilder.setView(dialogView);
                            TextView textViewtitle = (TextView) dialogView.findViewById(R.id.title);
                            TextView textViewmessage = (TextView) dialogView.findViewById(R.id.message);
                            textViewtitle.setText(getString(R.string.app_name));
                            textViewmessage.setText("Are you sure want to logout?");

                            // create alert dialog
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Button dialogButtonCancel = (Button) dialogView.findViewById(R.id.customDialogCancel);
                            Button dialogButtonOk = (Button) dialogView.findViewById(R.id.customDialogOk);
                            // Click cancel to dismiss android custom dialog box
                            dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                            // Action for custom dialog ok button click
                            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                    moveToActivity(12);
                                    // if this button is clicked, close
                                    // current activity

                                }
                            });
                            // show it
                            alertDialog.show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public Intent getShareIntent() {
        String playStoreLink = "https://play.google.com/store/apps/details?id=com.peeyem.app";
        String sharedText = "Download This app ! It's free, easy and smart.\n\n" + playStoreLink;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        return shareIntent;
    }

    public void moveToActivity(int position) {
        try {
            switch (position) {
                case 0:
                    Intent in = new Intent(MainActivity.this, ProductsListActivity.class);
                    startActivity(in);
                    break;
                case 1:
                    in = new Intent(MainActivity.this, Service.class);
                    startActivity(in);
//                    }
                    break;
                case 2:
                    in = new Intent(MainActivity.this, TestDrive.class);
                    startActivity(in);
//                    }
                    break;
                case 3:
                    String phone = "9497718766";
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                    break;
                case 4:
                    in = new Intent(MainActivity.this, EMICalculator.class);
                    startActivity(in);
                    break;
                case 5:
                    in = new Intent(MainActivity.this, UsedCarsActivity.class);
                    startActivity(in);
                    break;
                case 6:
                    in = new Intent(MainActivity.this, ViewPDFActivity.class);
                    startActivity(in);
                    break;
                case 7:
                    in = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(in);
                    break;
                case 8:
                    in = new Intent(MainActivity.this, ContactUs.class);
                    startActivity(in);
                    break;
                case 9:
                    startActivity(Intent.createChooser(getShareIntent(), getResources().getString(R.string.send_to)));
                    break;
                case 10:
                    phone = "8281015333";
                    intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                    break;
                case 11:
                    in = new Intent(MainActivity.this, FeedBackActivity.class);
                    startActivity(in);
//                    }
                    break;
                case 12:
                    preferenceUtil.logout();
                    in = new Intent(MainActivity.this, SplashActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(in);
                    finish();
                    break;
                case 13:
                    in = new Intent(MainActivity.this, MyVechle.class);
                    startActivity(in);
//                    }
                    break;
                case 14:
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    // Add data to the intent, the receiving app will decide
                    // what to do with it.
                    share.putExtra(Intent.EXTRA_SUBJECT, "Refer and Earn");
                    share.putExtra(Intent.EXTRA_TEXT, "Use my Refer Code " + preferenceUtil.getUserReferId() + " to purchase");
                    startActivity(Intent.createChooser(share, "Share link!"));
//                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
