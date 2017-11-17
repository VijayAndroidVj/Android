package com.example.chandru.myadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chandru.myadmin.Adapter.ListAdapter;
import com.example.chandru.myadmin.Listener.AdapterListener;
import com.example.chandru.myadmin.Pojo.DashBoard;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterListener, NavigationView.OnNavigationItemSelectedListener {
    private List<DashBoard> list = new ArrayList<>();
    private ListAdapter lAdapter;
    private RecyclerView recycler_view;
    ArrayList<DashBoard> mylist = new ArrayList<>();
    JSONArray jsonArray;
    private EditText searchDash;

    private static String TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ImageView logout;

    private float lastTranslate = 0.0f;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent log = new Intent(MainActivity.this, MaintenceActivity.class);
            startActivity(log);
        }/* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        searchDash = (EditText) findViewById(R.id.searchDash);
        logout = (ImageView) findViewById(R.id.logoutDash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view, params);
        actionBar.setElevation(0);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Int_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(Int_login);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

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

//                        linearLayout.startAnimation(anim);
                lastTranslate = moveFactor;

            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        view.findViewById(R.id.iv_drawermenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else
                    mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

       /* mNavItems.add(new NavItem("Home", "Meetup destination", R.drawable.ic_action_home));
        mNavItems.add(new NavItem("Preferences", "Change your preferences", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("About", "Get to know about us", R.drawable.ic_action_about));

        // DrawerLayout


        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });*/


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String responce = pref.getString("responce", null);
        try {
            JSONObject js = new JSONObject(responce);
            jsonArray = js.getJSONArray("dashboardlist");
            System.out.println(js.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsa = jsonArray.getJSONObject(i);
                HashMap<String, String> h1 = new HashMap<String, String>();
                h1.put("EMAIL_ID", jsa.getString("EMAIL_ID"));


                DashBoard dboard = new DashBoard();
                dboard.setEMAIL_ID(jsa.getString("EMAIL_ID"));
                dboard.setFLAT_AREA(jsa.getString("FLAT_AREA"));
                dboard.setFLAT_NO(jsa.getString("FLAT_NO"));
                dboard.setFLOOR_NO(jsa.getString("FLOOR_NO"));
                dboard.setMEMBER_CODE(jsa.getString("MEMBER_CODE"));
                dboard.setMEMBER_NAME(jsa.getString("MEMBER_NAME"));
                dboard.setMOBILE_NO(jsa.getString("MOBILE_NO"));
                dboard.setNO_OF_PARKING(jsa.getString("NO_OF_PARKING"));
                dboard.setSELF_OCCUPIED(jsa.getString("SELF_OCCUPIED"));
                dboard.setSOCIETY_CODE(jsa.getString("SOCIETY_CODE"));
                dboard.setSTATUS(jsa.getString("STATUS"));
                mylist.add(dboard);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
       /* searchDash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                goFilter(charSequence);
               // MainActivity.this.lAdapter.

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


        TextView tvAddNew = (TextView) findViewById(R.id.tvAddNew);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);

        // mylist = getIntent().getExtras().getParcelableArrayList("mylist");

        LinearLayoutManager lmanager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(lmanager);

        /*for (int i = 0; i < 3; i++) {
            DashBoard dboard = new DashBoard();
            dboard.setEMAIL_ID();
            dboard.setAreasq("1500");
            dboard.setEmail("chandruviswa@gmail.com");
            dboard.setFlatno("110");
            dboard.setId("1");
            dboard.setMembername("chandru");
            dboard.setMobile("9443079499");
            dboard.setRegdate("12/12/12");
            dboard.setSerialno("1");
            dboard.setParking("1");
            dboard.setSelfoccpied("2");
            dboard.setFloor("5");
            mylist.add(dboard);

        }*/

        lAdapter = new ListAdapter(mylist, this, this);
        recycler_view.setAdapter(lAdapter);
        lAdapter.notifyDataSetChanged();

      /*  searchDash.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                goFilter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });*/


        tvAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent log = new Intent(MainActivity.this, ManagementLoginActivity.class);
                log.putExtra("LoginTag", "1");
                startActivity(log);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        addTextListener();
    }

    public void goFilter(CharSequence charSequence) {
        for (int j = 0; j < mylist.size(); j++) {
            if (mylist.get(j).getMEMBER_NAME().contains(charSequence)) {

                lAdapter = new ListAdapter(mylist, this, this);
                recycler_view.setAdapter(lAdapter);
                lAdapter.notifyDataSetChanged();
            }
        }
    }


    public ActionBar actionBar;


    public void addTextListener() {

        searchDash.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<DashBoard> filteredList = new ArrayList<>();

                for (int i = 0; i < mylist.size(); i++) {

                    final String text = mylist.get(i).getMEMBER_NAME().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(mylist.get(i));
                    }
                }

                recycler_view.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                lAdapter = new ListAdapter(filteredList, MainActivity.this, MainActivity.this);
                recycler_view.setAdapter(lAdapter);
                lAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void adapterActionListener(int state, Object data) {
        if (state == ListAdapter.LIST_TAG && data != null) {
            int pois = (int) data;
            Intent web = new Intent(MainActivity.this, ManagementLoginActivity.class);
            web.putExtra("LoginTag", "0");

            try {


                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("responceOne", jsonArray.getJSONObject(pois).toString());
                editor.commit();

                startActivity(web);
            } catch (JSONException e) {
                e.printStackTrace();
            }



            /*Intent gerDetails = new Intent(MainActivity.this, ManagementLoginActivity.class);
            gerDetails.putExtra("LoginTag", "0");
            gerDetails.putExtra("mylist", (Serializable) list.get(pois));
            startActivity(gerDetails);*/
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
