package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class BillView extends AppCompatActivity {

    BillView activity;
    private ImageView img_bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bill-"+getIntent().getStringExtra("unique_code"));
        img_bill=(ImageView)findViewById(R.id.img_bill);
        activity = this;
        if (!getIntent().getStringExtra("bill_image").equals(""))
        Picasso.with(activity).load(getIntent().getStringExtra("bill_image")).into(img_bill);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
