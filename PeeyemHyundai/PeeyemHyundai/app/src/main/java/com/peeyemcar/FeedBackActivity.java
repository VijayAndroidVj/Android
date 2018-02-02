package com.peeyemcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.peeyem.app.R;
import com.peeyemcar.models.EventResponse;
import com.peeyemcar.retrofit.ApiClient;
import com.peeyemcar.retrofit.ApiInterface;
import com.peeyemcar.utils.Keys;
import com.peeyemcar.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vijay on 1/2/18.
 */

public class FeedBackActivity extends AppCompatActivity {

    RatingBar ratingsq;
    RatingBar ratinsi;
    RatingBar ratinsf;
    RatingBar ratinsa;

    private EditText edtRemarks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ratingsq = findViewById(R.id.ratingsq);
        ratinsi = findViewById(R.id.ratinsi);
        ratinsf = findViewById(R.id.ratinsf);
        ratinsa = findViewById(R.id.ratinsa);

        edtRemarks = findViewById(R.id.edtRemarks);

        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float fsq = ratingsq.getRating();
                float fsi = ratinsi.getRating();
                float fsf = ratinsf.getRating();
                float fsa = ratinsa.getRating();

                String ratings = edtRemarks.getText().toString().trim();
                PreferenceUtil preferenceUtil = new PreferenceUtil(FeedBackActivity.this);
                String email = preferenceUtil.getString(Keys.EmailID, "");
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(FeedBackActivity.this, "Login and add Feedback", Toast.LENGTH_SHORT).show();
                } else if (CommonUtil.isNetworkAvailable(FeedBackActivity.this)) {
                    MainActivity.showProgress(FeedBackActivity.this);
                    ApiInterface apiService =
                            ApiClient.getClient().create(ApiInterface.class);

                    Call<EventResponse> call = apiService.post_feedback("2", email, fsq, fsi, fsf, fsa, ratings);
                    call.enqueue(new Callback<EventResponse>() {
                        @Override
                        public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                            Log.d("", "Number of movies received: " + response.body());
                            MainActivity.dismissProgress();
                            EventResponse sigInResponse = response.body();
                            if (sigInResponse != null) {
                                if (sigInResponse.getResult().equalsIgnoreCase("success")) {
                                    onBackPressed();
                                }
                                Toast.makeText(FeedBackActivity.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(FeedBackActivity.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<EventResponse> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("", t.toString());
                            MainActivity.dismissProgress();
                            Toast.makeText(FeedBackActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FeedBackActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Feedback");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

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
