package com.xr.vijay.tranzpost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.xr.vijay.tranzpost.adapter.HaltingPriceAdapter;
import com.xr.vijay.tranzpost.model.haltingPriceModel;

import java.util.ArrayList;

/**
 * Created by vijay on 4/12/17.
 */

public class HaltingPriceActivity extends AppCompatActivity {

    HaltingPriceAdapter haltingPriceAdapter;
    RecyclerView recyclerView;
    ArrayList<haltingPriceModel> haltingPriceModelArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Profile");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_list);
        setAdapter();
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

    private void setAdapter() {
        haltingPriceModelArrayList = new ArrayList<>();
        haltingPriceModel haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("9T");
        haltingPriceModel1.setOnetothree("600.00");
        haltingPriceModel1.setFourtosix("800.00");
        haltingPriceModel1.setAbovesix("1200.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("16T/15T");
        haltingPriceModel1.setOnetothree("800.00");
        haltingPriceModel1.setFourtosix("1000.00");
        haltingPriceModel1.setAbovesix("1500.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("21T/20T");
        haltingPriceModel1.setOnetothree("1000.00");
        haltingPriceModel1.setFourtosix("1500.00");
        haltingPriceModel1.setAbovesix("2000.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("25T/24T");
        haltingPriceModel1.setOnetothree("1200.00");
        haltingPriceModel1.setFourtosix("1800.00");
        haltingPriceModel1.setAbovesix("2400.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("27T/28T");
        haltingPriceModel1.setOnetothree("1600.00");
        haltingPriceModel1.setFourtosix("2200.00");
        haltingPriceModel1.setAbovesix("2800.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("20ft SAC/24ft MAC");
        haltingPriceModel1.setOnetothree("600.00");
        haltingPriceModel1.setFourtosix("900.00");
        haltingPriceModel1.setAbovesix("1200.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("32ft SAC");
        haltingPriceModel1.setOnetothree("700.00");
        haltingPriceModel1.setFourtosix("1000.00");
        haltingPriceModel1.setAbovesix("1500.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);

        haltingPriceModel1 = new haltingPriceModel();
        haltingPriceModel1.setDay("32ft MAC");
        haltingPriceModel1.setOnetothree("900.00");
        haltingPriceModel1.setFourtosix("1200.00");
        haltingPriceModel1.setAbovesix("1800.00");
        haltingPriceModelArrayList.add(haltingPriceModel1);


        haltingPriceModelArrayList.add(haltingPriceModel1);
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(1));
        haltingPriceAdapter = new HaltingPriceAdapter(this, haltingPriceModelArrayList);
        recyclerView.setAdapter(haltingPriceAdapter);
    }
}
