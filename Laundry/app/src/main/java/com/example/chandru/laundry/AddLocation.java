package com.example.chandru.laundry;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chandru.laundry.Api.Api;
import com.example.chandru.laundry.Api.ApiInterface;
import com.example.chandru.laundry.Pojo.LocationModel;
import com.example.chandru.laundry.Pojo.ServiceMain;
import com.example.chandru.laundry.Pojo.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLocation extends AppCompatActivity {

    private EditText location, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Location");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);


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

    public void addlocate(View view) {

        String loca = location.getText().toString();
        String dest = description.getText().toString();
        if (TextUtils.isEmpty(loca)) {
            location.setError("Enter Location.");
            return;
        }
        if (TextUtils.isEmpty(dest)) {
            description.setError("Enter Description.");
            return;
        }

        ApiInterface apiService =
                Api.getClient().create(ApiInterface.class);

        Call<customer> call = apiService.getlocation(loca, dest);
        call.enqueue(new Callback<customer>() {
            @Override
            public void onResponse(Call<customer> call, Response<customer> response) {
                String movies = response.body().getError();
                String msg = response.body().getError_msg();

                Log.d("success", "Number of movies received: " + movies);
                if (movies.equals("false")) {

                    Toast.makeText(AddLocation.this, msg, Toast.LENGTH_SHORT)
                            .show();

                    // Intent myIntent = new Intent(AddLocation.this, Order.class);
                    // ActivityOptions options =
                    //        ActivityOptions.makeCustomAnimation(AddLocation.this, R.anim.left_enter, R.anim.left_out);
                    //startActivity(myIntent, options.toBundle());
                } else {
                    // loginLayout.startAnimation(shakeAnimation);
                    Toast.makeText(AddLocation.this, "Error while adding location..", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<customer> call, Throwable t) {
                // Log error here since request failed
                Log.e("error", t.toString());
            }
        });


    }


}
