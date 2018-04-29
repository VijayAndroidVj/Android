package com.instag.vijay.fasttrending.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.instag.vijay.fasttrending.PermissionCheck;
import com.instag.vijay.fasttrending.R;
import com.instag.vijay.fasttrending.model.CategoryItem;

import java.util.ArrayList;

import technolifestyle.com.imageslider.FlipperLayout;
import technolifestyle.com.imageslider.FlipperView;

/**
 * Created by vijay on 21/1/18.
 */

public class CategoryView extends AppCompatActivity implements View.OnClickListener {

    private TextView txtViewTitle;
    private TextView txtViewDes;
    private TextView txtViewAddress;
    private TextView txtProfileWebInfo;
    private TextView ivCategoryViewCall;
    private TextView ivCategoryViewEmail;
    private TextView ivCategoryViewChat;
    private FlipperLayout flipperLayout;
    private CategoryItem categoryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        if (getIntent() != null) {
            categoryItem = getIntent().getParcelableExtra("category");
        }
        flipperLayout = findViewById(R.id.flipper_layout);
        txtViewTitle = findViewById(R.id.txtViewTitle);
        txtViewDes = findViewById(R.id.txtViewDes);
        txtViewAddress = findViewById(R.id.txtViewAddress);
        txtProfileWebInfo = findViewById(R.id.txtProfileWebInfo);
        ivCategoryViewCall = findViewById(R.id.ivCategoryViewCall);
        ivCategoryViewEmail = findViewById(R.id.ivCategoryViewEmail);
        ivCategoryViewChat = findViewById(R.id.ivCategoryViewChat);
        ivCategoryViewCall.setOnClickListener(this);
        ivCategoryViewEmail.setOnClickListener(this);
        ivCategoryViewChat.setOnClickListener(this);

        if (categoryItem != null) {

            FlipperView view1 = new FlipperView(CategoryView.this);
            view1.setImageUrl("http://www.xooads.com/FEELOUTADMIN/img/upload/" + categoryItem.getImage())
                    // .setImageDrawable(R.drawable.test) // Use one of setImageUrl() or setImageDrawable() functions, otherwise IllegalStateException will be thrown
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP) //You can use any ScaleType
                    .setDescription("")
                    .setOnFlipperClickListener(new FlipperView.OnFlipperClickListener() {
                        @Override
                        public void onFlipperClick(FlipperView flipperView) {
                            //Handle View Click here
                        }
                    });
            flipperLayout.setScrollTimeInSec(3); //setting up scroll time, by default it's 3 seconds
//                                flipperLayout.getScrollTimeInSec(); //returns the scroll time in sec
//                                flipperLayout.getCurrentPagePosition(); //returns the current position of pager
            flipperLayout.addFlipperView(view1);

            if (TextUtils.isEmpty(categoryItem.getTitle())) {
                txtViewTitle.setText("");
            } else {
                txtViewTitle.setText(categoryItem.getTitle());
            }
            if (TextUtils.isEmpty(categoryItem.getDes())) {
                txtViewDes.setText("");
            } else {
                txtViewDes.setText(categoryItem.getDes());
            }
            if (TextUtils.isEmpty(categoryItem.getAddress())) {
                txtViewAddress.setText("");
            } else {
                txtViewAddress.setText(categoryItem.getAddress());
            }
            if (TextUtils.isEmpty(categoryItem.getWebsite())) {
                txtProfileWebInfo.setText("");
            } else {
                txtProfileWebInfo.setText(categoryItem.getWebsite());
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.ivCategoryViewCall:
                    String phone = categoryItem.getE_contact();
                    if (TextUtils.isEmpty(phone)) {
                        phone = categoryItem.getE_phone();
                    }
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(CategoryView.this, "Not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            ArrayList<String> pendingPermissions = PermissionCheck.checkPermission(this, PermissionCheck.getCallPermissions());
                            PermissionCheck.requestPermission(this, pendingPermissions, 111);
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                    break;
                case R.id.ivCategoryViewEmail:

                    String mail = categoryItem.getEmail();
                    if (TextUtils.isEmpty(mail)) {
                        Toast.makeText(CategoryView.this, "Email Not Available", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:" + mail));
                        try {
                            startActivity(emailIntent);
                        } catch (ActivityNotFoundException e) {
                            //TODO: Handle case where no email app is available
                        }
                    }


                    break;
                case R.id.ivCategoryViewChat:
                    phone = categoryItem.getE_phone();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(CategoryView.this, "Not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        Uri uri = Uri.parse("smsto:" + phone);
                        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                        it.putExtra("sms_body", "");
                        startActivity(it);
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}