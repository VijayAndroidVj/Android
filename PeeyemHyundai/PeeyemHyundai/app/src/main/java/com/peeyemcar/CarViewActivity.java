package com.peeyemcar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.peeyem.app.R;
import com.peeyemcar.models.CarModel;

/**
 * Created by vijay on 8/1/18.
 */

public class CarViewActivity extends AppCompatActivity {

    Activity activity;
    private TextView txtCarModel, txtCarkms, txtCarprice;
    private TextView txtCaryear;
    private ImageView ivCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.car_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Used Car");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        txtCarModel = (TextView) findViewById(R.id.txtCarModel);
//            txtCarModel.setTypeface(font, Typeface.BOLD);
        txtCarkms = (TextView) findViewById(R.id.txtCarkms);
        txtCaryear = (TextView) findViewById(R.id.txtCarYear);
        ivCar = (ImageView) findViewById(R.id.ivCar);
        txtCarprice = (TextView) findViewById(R.id.txtCarPrice);
        CarModel userModel = getIntent().getParcelableExtra("model");
        String string = "";
        byte[] utf8 = null;
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");

            if (TextUtils.isEmpty(userModel.getModelname())) {
                txtCarModel.setText("");
            } else {
                txtCarModel.setText(userModel.getModelname());
                actionBar.setTitle(userModel.getModelname());
            }

            if (TextUtils.isEmpty(userModel.getKmsRunned())) {
                txtCarkms.setText("");
            } else {
                txtCarkms.setText(userModel.getKmsRunned());
            }

            if (TextUtils.isEmpty(userModel.getYear())) {
                txtCaryear.setText("");
            } else {
                txtCaryear.setText(userModel.getYear());
            }

            if (TextUtils.isEmpty(userModel.getPrice())) {
                txtCarprice.setText("");
            } else {
                txtCarprice.setText(string + " " + userModel.getPrice());
            }

            if (userModel.getCarImagePath() != null && !userModel.getCarImagePath().isEmpty()) {

                Glide.with(activity).load(userModel.getCarImagePath()).asBitmap().centerCrop()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivCar);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
