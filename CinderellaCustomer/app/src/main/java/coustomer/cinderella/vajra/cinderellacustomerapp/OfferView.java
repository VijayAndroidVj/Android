package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Gokul on 7/9/2017.
 */

public class OfferView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer_view_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Offer View");


        TextView txtOfferTitle = (TextView) findViewById(R.id.txtOfferTitle);
        TextView txtOfferCode = (TextView) findViewById(R.id.txtOfferCode);

        ImageView ivImageOffer = (ImageView) findViewById(R.id.ivImageOffer);
        TextView txtOfferValid = (TextView) findViewById(R.id.txtOfferValid);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String title = intent.getStringExtra("title");
            String des = intent.getStringExtra("des");
            String path = intent.getStringExtra("path");
            String posted_by = intent.getStringExtra("posted_by");

            txtOfferTitle.setText(title);
            txtOfferCode.setText(des);
            txtOfferValid.setText(posted_by);
            Picasso.with(this).load(path).into(ivImageOffer);

        }
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
}
