package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import common.AsyncPOST;
import common.Config;
import common.Response;
import util.NotificationUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Response {

    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private TextView txt_customer_name;
    private List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>();

    private ViewPager viewPager2;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private EditText edt_time;
    private EditText edt_date;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Button btn_home, btn_submit;
    private LinearLayout txt_pricing;
    private LinearLayout linear_booking;
    private Button btn_other, b;
    private Button btn_office;
    private TextView txt_profile;
    public static MainActivity activity;
    public static MainActivity activity2;

    private LinearLayout linear_profile;
    private LinearLayout liner_terms;
    private TextView txt_terms;
    private CheckBox chk_terms;
    private TextView txt_address;
    private String address = "";
    private String address_type = "";
    private LinearLayout linear_logout;
    private LinearLayout linear_feedback;
    private LinearLayout linear_share, linear_learn;
    private LinearLayout lineare_about;
    private LinearLayout linear_contact;
    private ProgressDialog progressDialog;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private PackageInfo pInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = this;
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        liner_terms = (LinearLayout) findViewById(R.id.liner_terms);
        linear_logout = (LinearLayout) findViewById(R.id.linear_logout);
        linear_feedback = (LinearLayout) findViewById(R.id.linear_feedback);
        txt_customer_name = (TextView) findViewById(R.id.txt_customer_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_pricing = (LinearLayout) findViewById(R.id.txt_pricing);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_office = (Button) findViewById(R.id.btn_office);
        btn_other = (Button) findViewById(R.id.btn_other);
        txt_profile = (TextView) findViewById(R.id.txt_profile);
        txt_terms = (TextView) findViewById(R.id.txt_terms);
        chk_terms = (CheckBox) findViewById(R.id.chk_terms);
        linear_profile = (LinearLayout) findViewById(R.id.linear_profile);
        linear_share = (LinearLayout) findViewById(R.id.linear_share);
        linear_learn = (LinearLayout) findViewById(R.id.linear_learn);
        lineare_about = (LinearLayout) findViewById(R.id.lineare_about);
        linear_contact = (LinearLayout) findViewById(R.id.linear_contact);


        btn_submit = (Button) findViewById(R.id.btn_submit);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        linear_booking = (LinearLayout) findViewById(R.id.linear_booking);


//        final Ranger ranger = (Ranger) findViewById(R.id.styled_ranger);
//        final LocalDateTime currentDateTime = new LocalDateTime();
////        ranger.setStartAndEndDateWithParts(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.dayOfMonth().withMinimumValue().getDayOfMonth(),2017,8,16);
////        setEndDateWithParts(currentDateTime.getYear(), currentDateTime.getMonthOfYear(), currentDateTime.dayOfMonth().withMaximumValue().getDayOfMonth());
//        ranger.setDayViewOnClickListener(new Ranger.DayViewOnClickListener() {
//            @Override
//            public void onDaySelected(int day) {
////                View parentLayout = findViewById(android.R.id.content);
////                Snackbar.make(parentLayout, "Seleted Day: " +                 ranger.getSelectedDay()
////                , Snackbar.LENGTH_SHORT).show();
//            }
//        });
        linear_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Contactus.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.txt_offer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, OffersActivity.class);
                startActivity(intent);
            }
        });
        linear_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Cinderella Laundry");
                    String sAux = "\nHi, Iam using this App and finding it very useful. Cinderella Laundry has Pick up and Delivery services of Cloths. Try it now.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=coustomer.cinderella.vajra.cinderellacustomerapp&hl=en";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                    new sendSmsAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        findViewById(R.id.txt_refer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Cinderella Laundry");
                    String sAux = "\nHi, Iam using this App and finding it very useful. Cinderella Laundry has Pick up and Delivery services of Cloths. Try it now.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=coustomer.cinderella.vajra.cinderellacustomerapp&hl=en";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                    new sendSmsAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });

        linear_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Cinderella Laundry");
                    String sAux = "\nHi, Iam using this App and finding it very useful. Cinderella Laundry has Pick up and Delivery services of Cloths. Try it now.\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=coustomer.cinderella.vajra.cinderellacustomerapp&hl=en";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));

                    new sendSmsAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    //e.toString();
                }
            }
        });


        lineare_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(activity,Aboutus.class);
//                startActivity(intent);
            }
        });
        linear_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FeedBack.class);
                startActivity(intent);
            }
        });
        linear_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.removeDATA(activity, "mobile");
                Config.removeDATA(activity, "pwd");
                finish();

                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });

        linear_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyProfile.class);
                startActivity(intent);
//                finish();
            }
        });
        txt_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TermsCondition.class);
                startActivity(intent);
            }
        });
        liner_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, TermsCondition.class);
                startActivity(intent);
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.getDATA(activity, "address_home").equals("")) {
                    Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                    intent.putExtra("address", "home");
                    startActivity(intent);

                } else {
                    btn_home.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                    btn_home.setTextColor(getResources().getColor(R.color.white));
                    btn_office.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_office.setTextColor(getResources().getColor(R.color.btn_txt1));
                    btn_other.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_other.setTextColor(getResources().getColor(R.color.btn_txt1));
                    txt_address.setText(Config.getDATA(activity, "address_home"));
                    address_type = "home";
                    address = Config.getDATA(activity, "address_home");
                    txt_address.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.getDATA(activity, "address_office").equals("")) {
                    Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                    intent.putExtra("address", "office");
                    startActivity(intent);
                } else {
                    btn_home.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_home.setTextColor(getResources().getColor(R.color.btn_txt1));
                    btn_office.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                    btn_office.setTextColor(getResources().getColor(R.color.white));
                    btn_other.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_other.setTextColor(getResources().getColor(R.color.btn_txt1));
                    txt_address.setText(Config.getDATA(activity, "address_office"));
                    address_type = "office";
                    address = Config.getDATA(activity, "address_office");
                    txt_address.setVisibility(View.VISIBLE);

                }
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Config.getDATA(activity, "address_other").equals("")) {
                    Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                    intent.putExtra("address", "other");
                    startActivity(intent);
                } else {
                    btn_home.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_home.setTextColor(getResources().getColor(R.color.btn_txt1));
                    btn_office.setBackgroundColor(getResources().getColor(R.color.btn_color));
                    btn_office.setTextColor(getResources().getColor(R.color.btn_txt1));
                    btn_other.setBackgroundColor(getResources().getColor(R.color.sky_blue));
                    btn_other.setTextColor(getResources().getColor(R.color.white));
                    txt_address.setText(Config.getDATA(activity, "address_other"));
                    address_type = "other";
                    address = Config.getDATA(activity, "address_other");
                    txt_address.setVisibility(View.VISIBLE);

                }
            }
        });
        txt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddressActivity.class);
                intent.putExtra("address", address_type);
                startActivity(intent);
            }
        });
        txt_pricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PriceList.class);
                startActivity(intent);
            }
        });
        linear_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyBooking.class);
                startActivity(intent);
            }
        });
        edt_time = (EditText) findViewById(R.id.edt_time);
        edt_date = (EditText) findViewById(R.id.edt_date);
        edt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.time_slot);
                final TextView txt_1 = (TextView) dialog.findViewById(R.id.txt_1);
                final TextView txt_2 = (TextView) dialog.findViewById(R.id.txt_2);
                final TextView txt_3 = (TextView) dialog.findViewById(R.id.txt_3);
                final TextView txt_4 = (TextView) dialog.findViewById(R.id.txt_4);
                txt_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_time.setText(txt_1.getText().toString());
                        dialog.dismiss();
                    }
                });
                txt_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_time.setText(txt_2.getText().toString());
                        dialog.dismiss();

                    }
                });
                txt_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_time.setText(txt_3.getText().toString());
                        dialog.dismiss();

                    }
                });
                txt_4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_time.setText(txt_4.getText().toString());
                        dialog.dismiss();

                    }
                });


                dialog.show();

            }
        });
        edt_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
//                datePickerDialog.getDatePicker().setMaxDate(20);


            }
        });


//        ranger.set
//        ranger.setStartAndEndDateWithParts(2017,4,10,2017,8,16);
//        ranger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ranger.get
//            }
//        });

        txt_customer_name.setText(Config.getDATA(this, "name"));
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3,
                R.layout.welcome_slide4,
                R.layout.welcome_slide5,
                R.layout.welcome_slide6};

        // adding bottom dots
        addBottomDots(0);

        viewPager2 = (ViewPager) findViewById(R.id.viewpager2);
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager2.setAdapter(myViewPagerAdapter);
        viewPager2.addOnPageChangeListener(viewPagerPageChangeListener);
        task();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                if (!address.equals("")) {
                    if (!edt_date.getText().toString().equals("")) {
                        if (!edt_time.getText().toString().equals("")) {


                            if (Config.isNetworkAvailable(MainActivity.this)) {
                                if (chk_terms.isChecked()) {
                                    Random rnd = new Random();
                                    int n = 100000 + rnd.nextInt(900000);
                                    nameValuePairs.add(new BasicNameValuePair("city_id", Config.getDATA(MainActivity.this, "city_id")));
                                    nameValuePairs.add(new BasicNameValuePair("locality_id", Config.getDATA(MainActivity.this, "locality_id")));
                                    nameValuePairs.add(new BasicNameValuePair("customer_name", Config.getDATA(MainActivity.this, "name")));
                                    nameValuePairs.add(new BasicNameValuePair("city", Config.getDATA(MainActivity.this, "city")));
                                    nameValuePairs.add(new BasicNameValuePair("locality", Config.getDATA(MainActivity.this, "locality")));
                                    nameValuePairs.add(new BasicNameValuePair("address", address));
                                    nameValuePairs.add(new BasicNameValuePair("pickup_date", edt_date.getText().toString().trim()));
                                    nameValuePairs.add(new BasicNameValuePair("pickup_time", edt_time.getText().toString()));
                                    nameValuePairs.add(new BasicNameValuePair("mobile", Config.getDATA(MainActivity.this, "mobile")));
                                    nameValuePairs.add(new BasicNameValuePair("cid", Config.getDATA(MainActivity.this, "cid")));
                                    nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
                                    nameValuePairs.add(new BasicNameValuePair("unique_code", "" + n));
                                    nameValuePairs.add(new BasicNameValuePair("customer_token", Config.getDATA(activity, "server_token")));

                                    asyncPOST = new AsyncPOST(nameValuePairs, MainActivity.this, Config.MAIN_URL + Config.CUSTOMER_BOOKING, MainActivity.this);
                                    asyncPOST.execute();
                                } else {
                                    Toast.makeText(activity, "Enable The Terms and Conditions", Toast.LENGTH_SHORT).show();
                                }
//                                final Dialog dialog = new Dialog(activity);
//                                dialog.setContentView(R.layout.terms_condition);
//                                TextView txt_agree = (TextView) dialog.findViewById(R.id.txt_agree);
//                                txt_agree.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//
//                                        dialog.dismiss();
//
//                                    }
//                                });
//                                dialog.show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please Add Your PickUp Time.", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Your PickUp Date.", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Add Your Address.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
                }
            }
        };

        displayFirebaseRegId();

        Intent in = getIntent();
        if (in != null && in.getExtras() != null) {
            if (in.getBooleanExtra("moveToOffers", false)) {
                Intent intent = new Intent(MainActivity.this, OffersActivity.class);
                startActivity(intent);
                return;
            }
            for (String key : in.getExtras().keySet()) {
                try {
                    String value = (String) in.getExtras().get(key);
                    if (key.contains("data") || value.contains("message")) {
                        Intent intent = new Intent(MainActivity.this, OffersActivity.class);
                        startActivity(intent);
                        break;
                    }
                    Log.d(TAG, "Key: " + key + " Value: " + value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class sendSmsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            sendSMS();
            return null;
        }
    }

    private void sendSMS() {
        //Your authentication key

        String authkey = "168136AtkhdmM8UB598355de";
//Multiple mobiles numbers separated by comma
        String mobiles = Config.getDATA(MainActivity.this, "mobile");
//Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "CINDAP";
//Your message to send, Add URL encoding here.
        String message = "\n" +
                "Dear Customer, Please use\n" +
                "Offer Code: CL100 to avail your\n" +
                "offer. Kindly show this message\n" +
                "while booking. Thanks\n" +
                "Cinderella Laundry";
//define route
        String route = "4";

        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

//encoding message
        String encoded_message = URLEncoder.encode(message);

//Send SMS API
        String mainUrl = "https://control.msg91.com/api/sendhttp.php?";

//Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("authkey=" + authkey);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);
        sbPostData.append("&response=" + "json");

//final string
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", "" + response);

            //finally close connection
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        progressDialog.dismiss();
        if (Config.getDATA(activity, "server_token").equals("")) {
            nameValuePairs2.add(new BasicNameValuePair("token", regId));
            nameValuePairs2.add(new BasicNameValuePair("cid", Config.getDATA(activity, "cid")));
            asyncPOST = new AsyncPOST(nameValuePairs2, activity, Config.MAIN_URL +
                    Config.POST_TOKEN, activity);
            asyncPOST.execute();

        } else {
            volley_chk_version("");

        }


//        if (!TextUtils.isEmpty(regId))
////            txtRegId.setText("Firebase Reg Id: " + regId);
//
//
//        else
////            txtRegId.setText("Firebase Reg Id is not received yet!");
    }


    @Override
    public void processFinish(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("type").equals("booking")) {
                if (jsonObject.getString("success").equals("1")) {
                    Config.alert("Pleasure Moment...", jsonObject.getString("message"), 0, MainActivity.this);
                    edt_date.getText().clear();
                    edt_time.getText().clear();

                } else if (jsonObject.getString("success").equals("3")) {
                    Config.alert("Already Exist...", jsonObject.getString("message"), 0, MainActivity.this);

                }
            } else if (jsonObject.getString("success").equals("1")) {

                Config.storeDATA(activity, "server_token", jsonObject.getString("token"));


                volley_chk_version("");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class Task implements Runnable {

        private Object mPauseLock;
        private boolean mPaused;
        private boolean mFinished;

        public Task() {
            mPauseLock = new Object();
            mPaused = false;
            mFinished = false;
        }

        /**
         * Call this on pause.
         */
        public void onPause() {
            synchronized (mPauseLock) {
                mPaused = true;
            }
        }

        /**
         * Call this on resume.
         */
        public void onResume() {
            synchronized (mPauseLock) {
                mPaused = false;
                mPauseLock.notifyAll();
            }
        }

        @Override
        public void run() {
            for (int i = 0; i <= layouts.length; i++) {
                final int value = i;


//                    while (!mFinished) {
//                        // Do stuff.
//
//                        synchronized (mPauseLock) {
//                            while (mPaused) {
//                                try {
//                                    mPauseLock.wait();
//                                } catch (InterruptedException e) {
//                                }
//                            }
//                        }
//                    }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                    int current = getItem(+1);
//                    if (current < layouts.length) {
//                        // move to next screen
//                        viewPager2.setCurrentItem(current);
//                    } else {
////                    launchHomeScreen();
//                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int current = getItem(+1);
                        if (current < layouts.length) {
                            // move to next screen
                            viewPager2.setCurrentItem(current);

                        } else {
//                    launchHomeScreen();

//                                new Thread(new Task()).start();
                        }
//                        new FlipHorizontalAnimation(img_1).setListener(
//                                new AnimationListener() {
//
//                                    @Override
//                                    public void onAnimationEnd(com.easyandroidanimations.library.Animation animation) {
////                        mPlayView.setVisibility(View.VISIBLE);
//                                    }
//                                }).setDuration(4000).animate();
//
//                        new FlipVerticalAnimation(img_4).setDuration(5000).animate();
                    }
                });

                if (value == layouts.length - 1) {
                    new Thread(new Task()).start();

                }

            }

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
//                btnNext.setText("start");
            } else {
                // still pages are left
//                btnNext.setText("next");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void task() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                for (int i = 0; i < layouts.length; i++) {

                    final int value = i;


                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                int current = getItem(+1);
//                                if (current < layouts.length) {
//                                    // move to next screen
//                                    viewPager2.setCurrentItem(i);
//                                }
                            viewPager2.setCurrentItem(value);

                        }
                    });

                    if (i == layouts.length - 1) {
                        task();
                    }


                }


            }
        }).start();
    }
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new About(), "AboutUs");
//        adapter.addFragment(new Committee(), "Committee");
//        adapter.addFragment(new OfficeBearers(), "OfficeBearers");
//
//
//        viewPager.setAdapter(adapter);
//
//
//    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private int getItem(int i) {
        return viewPager2.getCurrentItem() + i;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                if (position == 2)
//                    view.setBackground(getResources().getDrawable(R.drawable.ic_food));
            }
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
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

    private void volley_chk_version(final String type) {

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;
        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, "http://carreto.pt/tools/android-store-version/?package=coustomer.cinderella.vajra.cinderellacustomerapp", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("permission_response" + response);

                pDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("version").equals(String.valueOf(pInfo.versionName))) {


                    } else {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                        builder.setMessage("Kindly Update Your Cinderella Laundry App").setTitle("New Version..!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // do nothing
                                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                        }
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        alert.setCancelable(false);
//                        Toast.makeText(getApplication(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    pDialog.dismiss();

                    e.printStackTrace();
                }

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
}
