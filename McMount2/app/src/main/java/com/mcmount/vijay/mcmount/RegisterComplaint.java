package com.mcmount.vijay.mcmount;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by vijay on 23/9/17.
 */

public class RegisterComplaint extends AppCompatActivity {

    private Activity activity;
    private Spinner spinnerState;
    private Spinner spinnerCity;


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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_complaint);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Register Complaint");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        activity = this;
        spinnerState = (Spinner) findViewById(R.id.spinner);
        spinnerCity = (Spinner) findViewById(R.id.spinnerCity);

        ArrayAdapter adapterState = ArrayAdapter.createFromResource(this, R.array.spinnerState_array, R.layout.spinner_item);
        spinnerState.setAdapter(adapterState);

        ArrayAdapter adapterCity = ArrayAdapter.createFromResource(this, R.array.spinnerCity_array, R.layout.spinner_item);
        spinnerCity.setAdapter(adapterCity);


    }
}