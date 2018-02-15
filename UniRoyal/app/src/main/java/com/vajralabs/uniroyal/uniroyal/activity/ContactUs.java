package com.vajralabs.uniroyal.uniroyal.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vajralabs.uniroyal.uniroyal.R;
import com.vajralabs.uniroyal.uniroyal.StaticValus;

import java.util.List;


/**
 * Created by ashutosh.kumar on 28/03/16.
 */

public class ContactUs extends AppCompatActivity {

    /***************************
     * Deeplinking stuff
     **************************/
    String article_list = "health-a-z";
    String video_list = "doc-speak";
    String consult_doc = "consult-doc";

    private static final int ARTICLE_LIST_FLAT = 1;
    private static final int VIDEO_LIST_FLAG = 2;
    private static final int CONSULT_DOC = 3;

    private int flag = 3; //default
    /**************************/


    String TAG = ContactUs.class.getSimpleName();
    TextView email, address;
    TextView address1;
    ImageView facebook, twitter, map;
    String twitterURL = "https://twitter.com/uniroyalmaldivs";
    String facebookURL = "https://www.facebook.com/UniRoyal-Maldives-Pvt-Ltd-1769188050064284";
    String googleURL = "https://plus.google.com/+hyundaiindia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        email = findViewById(R.id.email_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.nav_contactus));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        address = findViewById(R.id.address);
        address1 = findViewById(R.id.address1);
        address.setText(Html.fromHtml(StaticValus.address));
        address1.setText(Html.fromHtml(StaticValus.address1));
        map = findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//4.218240, 73.543648
                String uri = "http://maps.google.com/maps?q=loc:" + 4.218240 + "," + 73.543648 + " (" + getString(R.string.app_name) + ")";
//                String uri = "http://maps.google.com/maps?daddr=" + 4.218240 + "," + 73.543648 + " (Hulhumale, Maldives)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);

           /*     String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 11.7678524,75.40596);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);*/
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                final PackageManager pm = getPackageManager();
                final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                ResolveInfo best = null;
                for (final ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
                        best = info;
                        break;
                    }
                }
                if (best != null) {
                    intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                }

                intent.setData(Uri.parse("uniroyal@uniroyal.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "My Query");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Uni Royal Team");
                try {
                    startActivity(intent);

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactUs.this, "Error Sending Email,Try Later.", Toast.LENGTH_SHORT).show();
                }
              /*  Intent gmail = new Intent(Intent.ACTION_VIEW);
                gmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                gmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@modasta.com"});
                gmail.setData(Uri.parse("info@modasta.com"));
                gmail.putExtra(Intent.EXTRA_SUBJECT, "My Query");
                gmail.setType("plain/text");
                gmail.putExtra(Intent.EXTRA_TEXT, "Hi Modasta Team");
                startActivity(gmail);*/
            }
        });
//
//        google_plus = (ImageView) findViewById(R.id.google_plus);
//        google_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openSocialMedia(googleURL);
//            }
//        });

        facebook = (ImageView) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia(facebookURL);
            }
        });

        twitter = (ImageView) findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia(twitterURL);
            }
        });

    }


    public void openSocialMedia(String url) {
        Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
