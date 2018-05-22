package com.instag.vijay.fasttrending.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.instag.vijay.fasttrending.R;

import java.io.File;

/**
 * Created by vijay on 16/5/18.
 */

public class ImagePreviewActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview_layout);
        ImageView scaleImageView = findViewById(R.id.image_preview);
        if (getIntent() != null && getIntent().getExtras() != null) {
            filePath = getIntent().getStringExtra("filePath");
        }

        if (!TextUtils.isEmpty(filePath)) {

            Uri imageUri = Uri.fromFile(new File(filePath));
            Glide.with(this)
                    .load(imageUri)
                    .into(scaleImageView);
        }
        findViewById(R.id.bt_preview_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("filePath", filePath);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        findViewById(R.id.bt_preview_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
