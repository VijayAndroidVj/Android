package com.peeyem.honda.peeyamyamaha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomePage extends AppCompatActivity {
    Button aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        aboutus = (Button) findViewById(R.id.button);
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(Intent.createChooser(getShareIntent(), getResources().getString(R.string.send_to)));
                Intent in = new Intent(HomePage.this, ContactUs.class);
                startActivity(in);
            }
        });
    }

    public Intent getShareIntent() {
        String playStoreLink = "https://play.google.com/store/apps/details?id=com.modasta.android";
        String sharedText = "Download India's #1 medical app and stay healthy! It's free, easy and smart.\n\n" + playStoreLink;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharedText);
        return shareIntent;
    }
}
