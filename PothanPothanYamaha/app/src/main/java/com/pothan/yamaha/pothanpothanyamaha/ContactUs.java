package com.pothan.yamaha.pothanpothanyamaha;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView google_plus, facebook, twitter, map;
    String twitterURL = "https://twitter.com/India_Yamaha";
    String facebookURL = "https://www.facebook.com/yamahamotorindia";
    String googleURL = "https://plus.google.com/+yamahaindia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        email = (TextView) findViewById(R.id.email_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        address = (TextView) findViewById(R.id.address);
        address.setText(Html.fromHtml(StaticValus.address));
        map = (ImageView) findViewById(R.id.map);
        map.setVisibility(View.GONE);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?daddr=" + 11.7678609 + "," + 75.4738065 + " (" + "Peeyem Hyundai " + ")";
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

                intent.setData(Uri.parse("yes@yamaha-motor-india.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "My Query");
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Pioneer Yamaha Team");
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

        google_plus = (ImageView) findViewById(R.id.google_plus);
        google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSocialMedia(googleURL);
            }
        });

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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
