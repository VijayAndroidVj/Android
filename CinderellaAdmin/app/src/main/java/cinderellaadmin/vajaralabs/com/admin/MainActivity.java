package cinderellaadmin.vajaralabs.com.admin;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.Configg;
import util.NotificationUtils;
import util.PermissionCheck;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Button btn_add_shop, btn_factory, btn_add_delivery_person;
    private Button btn_price_list;
    private Button btn_locality;
    private Button btn_booking_zone;
    private Activity activity;
    private TextView txt_name;
    private Button btn_city;
    private Button btn_delivery_booking_zone, btn_pickuped;
    private Button btn_delivery_home;
    private Button btn_assigned_factory;
    private LinearLayout linear_list;
    private Button btn_view_delivery, btn_completed;
    private Button btn_view_shop;
    private Button btn_view_factory;
    private Button btn_customer;


    private double latitude;
    private double longitude;

    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    static final Integer CAMERA = 0x5;
    static final Integer ACCOUNTS = 0x6;
    static final Integer GPS_SETTINGS = 0x7;
    //    GoogleApiClient client;
    LocationRequest mLocationRequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 5000 * 300 * 1; // 1 minute

    PendingResult<LocationSettingsResult> result;
    private GPSTracker gps;
    private Button btn_notification;
    private Button btn_pickuped_zone;
    private Button btn_delivery_zone;
    private GoogleApiClient client;
    private Button btn_logut;
    private LinearLayout linear_main;
    private Button btn_report;
    private Button btn_feed;
    private Button btn_graph;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static ProgressDialog progressDialog;
    private Button btn_walkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        activity = this;

        pendingPermissions = PermissionCheck.checkPermission(activity, PermissionCheck.getAllPermissions());
        if (pendingPermissions.size() > 0) {
            PermissionCheck.requestPermission(activity, pendingPermissions, 1000);
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        btn_pickuped = (Button) findViewById(R.id.btn_pickuped);
        btn_delivery_home = (Button) findViewById(R.id.btn_delivery_home);
        btn_view_delivery = (Button) findViewById(R.id.btn_view_delivery);
        btn_customer = (Button) findViewById(R.id.btn_customer);
        btn_view_shop = (Button) findViewById(R.id.btn_view_shop);
        btn_graph = (Button) findViewById(R.id.btn_graph);
        btn_walkin = (Button) findViewById(R.id.btn_walkin);
        linear_list = (LinearLayout) findViewById(R.id.linear_list);
        btn_notification = (Button) findViewById(R.id.btn_notification);
        btn_completed = (Button) findViewById(R.id.btn_completed);
        linear_main = (LinearLayout) findViewById(R.id.linear_main);
        btn_report = (Button) findViewById(R.id.btn_report);
        btn_pickuped_zone = (Button) findViewById(R.id.btn_pickuped_zone);
        btn_feed = (Button) findViewById(R.id.btn_feed);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        btn_booking_zone = (Button) findViewById(R.id.btn_booking_zone);


        btn_add_shop = (Button) findViewById(R.id.btn_add_shop);
        txt_name = (TextView) findViewById(R.id.txt_name);

        btn_factory = (Button) findViewById(R.id.btn_factory);
        btn_city = (Button) findViewById(R.id.btn_city);
        btn_delivery_booking_zone = (Button) findViewById(R.id.btn_delivery_booking_zone);
        btn_assigned_factory = (Button) findViewById(R.id.btn_assigned_factory);
        btn_view_factory = (Button) findViewById(R.id.btn_view_factory);
        btn_delivery_zone = (Button) findViewById(R.id.btn_delivery_zone);

        btn_logut = (Button) findViewById(R.id.btn_logut);
        btn_price_list = (Button) findViewById(R.id.btn_price_list);
        btn_locality = (Button) findViewById(R.id.btn_locality);
        btn_add_delivery_person = (Button) findViewById(R.id.btn_add_delivery_person);
        btn_walkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WalkinCustomer.class);
                startActivity(intent);
            }
        });
        btn_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Graph.class);
                startActivity(intent);
            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, Report.class);
                startActivity(intent);

            }
        });
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ClientFeedback.class);
                startActivity(intent);

            }
        });
        btn_logut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configg.getDATA(activity, "type").equals("shop")) {
                    Configg.removeDATA(activity, "type");
                    finish();
                    Configg.removeDATA(activity, "password2");

                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);

                } else if (Configg.getDATA(activity, "type").equals("delivery")) {
                    Configg.removeDATA(activity, "type");
                    Configg.removeDATA(activity, "password2");

                    finish();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);

                } else if (Configg.getDATA(activity, "type").equals("factory")) {
                    Configg.removeDATA(activity, "type");
                    finish();
                    Configg.removeDATA(activity, "password2");

                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);

                } else {
                    Configg.removeDATA(activity, "type");
                    finish();
                    Configg.removeDATA(activity, "password2");

                    Intent intent = new Intent(activity, LoginActivity.class);
                    startActivity(intent);

                }
            }
        });
        if (Configg.getDATA(activity, "type").equals("factory")) {
            btn_booking_zone.setVisibility(View.GONE);
            btn_pickuped.setText("From Shop");
            btn_delivery_home.setText("Done Jobs");
            btn_booking_zone.setVisibility(View.VISIBLE);
            btn_pickuped.setVisibility(View.GONE);
            btn_delivery_home.setVisibility(View.GONE);

            btn_delivery_booking_zone.setVisibility(View.GONE);
            btn_walkin.setVisibility(View.GONE);

        }
        if (Configg.getDATA(activity, "type").equals("delivery")) {
            btn_booking_zone.setVisibility(View.GONE);
            btn_pickuped.setVisibility(View.GONE);
            btn_pickuped.setText("My Pickup's");
            btn_delivery_home.setText("Delivery To Home");
            btn_delivery_booking_zone.setVisibility(View.GONE);
            btn_booking_zone.setVisibility(View.VISIBLE);
            btn_delivery_home.setVisibility(View.GONE);


        }

        if (Configg.getDATA(activity, "type").equals("shop")) {

            btn_delivery_booking_zone.setVisibility(View.GONE);
            btn_delivery_booking_zone.setText("Assigned Data To Delivery Person");
            btn_assigned_factory.setVisibility(View.GONE);
            linear_list.setVisibility(View.VISIBLE);


        }
        if (Configg.getDATA(activity, "type").equals("admin")) {
            linear_list.setVisibility(View.VISIBLE);
            btn_view_shop.setVisibility(View.VISIBLE);
            btn_view_factory.setVisibility(View.VISIBLE);
            btn_walkin.setVisibility(View.GONE);

        }
        btn_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Notification.class);
                startActivity(intent);

            }
        });
        btn_delivery_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BookingZoneDelivery.class);
                startActivity(intent);
            }
        });
        btn_view_factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ViewFactoryPerson.class);
                startActivity(intent);
            }
        });
        btn_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewCustomers.class);
                startActivity(intent);
            }
        });
        btn_delivery_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ViewDeliveryPerson.class);
                startActivity(intent);
            }
        });

        btn_booking_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BookingZone.class);
                startActivity(intent);

            }
        });
        btn_pickuped_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, BookingZonePickuped.class);
                startActivity(intent);

            }
        });
        btn_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DoneJobs.class);
                startActivity(intent);
            }
        });
        btn_pickuped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AssignFactory.class);
                startActivity(intent);
            }
        });


        btn_delivery_booking_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DeliveryBookingZone.class);
                startActivity(intent);

            }
        });

        btn_locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddLocality.class);
                startActivity(intent);

            }
        });
        btn_view_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ViewShopPerson.class);
                startActivity(intent);
            }
        });

        btn_assigned_factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FactoryAssignedData.class);
                startActivity(intent);
            }
        });

        btn_price_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PriceList.class);
                startActivity(intent);
            }
        });
        btn_add_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateShop.class);
                startActivity(intent);
            }
        });
        btn_factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFactory.class);
                startActivity(intent);
            }
        });
        btn_add_delivery_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddDealer.class);
                startActivity(intent);
            }
        });
        btn_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCity.class);
                startActivity(intent);
            }
        });
        btn_view_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ViewDeliveryPerson.class);
                startActivity(intent);

            }
        });
        if (Configg.getDATA(activity, "type").equals("factory")) {
            btn_pickuped_zone.setVisibility(View.GONE);
            btn_delivery_zone.setVisibility(View.GONE);
        }

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();

        //  askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);

        if (Configg.getDATA(activity, "type").equals("shop")) {
            txt_name.setText("Shop Keeper " + Configg.getDATA(activity, "shop_keeper") + "-" +
                    Configg.getDATA(activity, "shop_code_and_name"));
            linear_list.setVisibility(View.VISIBLE);
            btn_add_shop.setVisibility(View.GONE);
            btn_factory.setVisibility(View.GONE);
            btn_price_list.setVisibility(View.GONE);
            btn_city.setVisibility(View.GONE);
            btn_locality.setVisibility(View.GONE);
            btn_notification.setVisibility(View.GONE);
            btn_view_shop.setVisibility(View.GONE);


        } else if (Configg.getDATA(activity, "type").equals("delivery")) {
            txt_name.setText("Deliver " + Configg.getDATA(activity, "delivery_person") + "-" +
                    Configg.getDATA(activity, "shop_code_and_name"));
            linear_list.setVisibility(View.GONE);


        } else if (Configg.getDATA(activity, "type").equals("factory")) {
            txt_name.setText("Factory " + Configg.getDATA(activity, "factory_person") + "-" +
                    Configg.getDATA(activity, "factory_name"));
            linear_list.setVisibility(View.GONE);

        }
        if (!Configg.getDATA(activity, "type").equals("admin")) {
            new updateFcm().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
//                if (intent.getAction().equals(Configg.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Configg.TOPIC_GLOBAL);
//
//                    displayFirebaseRegId();
//
//                } else if (intent.getAction().equals(Configg.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//
////                    txtMessage.setText(message);
//                }

                if (intent.getAction().equals(Configg.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Configg.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                }
            }
        };

//        displayFirebaseRegId();
    }

    public static void updateFcmToken(final Context context) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.FCM_TOKEN_UPDATE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
//                        Config.alert("Booking Cancel", jsonObject.getString("message"), 0, context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                String type = Configg.getDATA(context, "type");
                params.put("fcmid", refreshedToken);
                params.put("type", type);
                params.put("clearall", "");
                params.put("mobile", Configg.getDATA(context, "mobile"));
                if (type.equalsIgnoreCase("shop")) {
                    params.put("password", Configg.getDATA(context, "password1"));
                } else if (type.equalsIgnoreCase("delivery")) {
                    params.put("password", Configg.getDATA(context, "password2"));
                } else if (type.equalsIgnoreCase("factory")) {
                    params.put("password", Configg.getDATA(context, "password3"));
                }

//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                Log.w("params_out", params.toString());

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    ArrayList<String> pendingPermissions = new ArrayList<>();

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Configg.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Configg.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Configg.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
//
//            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            android.location.LocationListener mlocListener = new MyLocationListener2();
//
//            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
//            Toast.makeText(this, "" + permission + " is.", Toast.LENGTH_SHORT).show();


            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This

// is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
//
//            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            android.location.LocationListener mlocListener = new MyLocationListener2();
//
//            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

            if (Configg.getDATA(activity, "type").equals("delivery")) {

                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("did", getIntent().getStringExtra("did"));

                startService(intent);
            }

//            Configg.alert("Pleasur Moment...","Successfully Added",2,MainActivity.this);


//            gps = new GPSTracker(MainActivity.this);
//
//            // check if GPS enabled
//            if(gps.canGetLocation()){
//
//                double latitude = gps.getLatitude();
//                double longitude = gps.getLongitude();
//
//                // \n is for new line
//                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//            }else{
//                // can't get location
//                // GPS or Network is not enabled
//                // Ask user to enable GPS/network in settings
//                gps.showSettingsAlert();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:
//                    askForGPS();
                    break;
                //Call
                case 2:
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + "{This is a telephone number}"));
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                    }
                    break;
                //Write external Storage
                case 3:
                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;
                //Camera
                case 5:
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, 12);
                    }
                    break;
                //Accounts
                case 6:
                    AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
                    Account[] list = manager.getAccounts();
                    Toast.makeText(this, "" + list[0].name, Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < list.length; i++) {
                        Log.e("Account " + i, "" + list[i].name);
                    }
            }

            Toast.makeText(this, "Permission granted2", Toast.LENGTH_SHORT).show();
            if (Configg.getDATA(activity, "type").equals("delivery")) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("did", getIntent().getStringExtra("did"));

                startService(intent);
            }

        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }


    private void askForGPS() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        result = LocationServices.SettingsApi.checkLocationSettings(client, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(MainActivity.this, GPS_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }


    public void SendQueryString() {
        new Thread() {
            public void run() {

                HttpClient httpClient = new DefaultHttpClient();
                // replace with your url
                HttpPost httpPost = new HttpPost(Configg.MAIN_URL + Configg.INSERT_LOCATION);


                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("lattitude", String.valueOf(latitude)));
                nameValuePair.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
                nameValuePair.add(new BasicNameValuePair("did", getIntent().getStringExtra("did")));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }

                //making POST request.
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    Log.d("Http Post Response:", response.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (ClientProtocolException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                }

            }

        }.start();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class updateFcm extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            updateFcmToken(MainActivity.this);
            return null;
        }
    }
}
