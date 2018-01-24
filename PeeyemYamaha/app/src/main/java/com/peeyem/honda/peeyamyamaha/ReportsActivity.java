package com.peeyem.honda.peeyamyamaha;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.peeyem.honda.peeyamyamaha.adapter.ExpandListAdapter;
import com.peeyem.honda.peeyamyamaha.models.CarModel;
import com.peeyem.honda.peeyamyamaha.models.Group;

import java.util.ArrayList;


/**
 * Created by razin on 23-08-2017.
 */

public class ReportsActivity extends AppCompatActivity {
    // The TransferUtility is the primary class for managing transfer to S3
//    private TransferUtility transferUtility;
    android.widget.PopupWindow showPopup;
    private TextView txtLabel;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;
    private ExpandListAdapter ExpAdapter;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExpListItems = new ArrayList<>();
        activity = this;
        setContentView(R.layout.activity_reports);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Products");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        ExpandList = (ExpandableListView) findViewById(R.id.expandListview);

        txtLabel = (TextView) findViewById(R.id.txtLabel);
        txtLabel.setVisibility(View.GONE);
        reloadReports();
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
    protected void onResume() {
        super.onResume();

    }


    private void reloadReports() {

        try {
            if (ExpListItems != null) {
                ExpListItems.clear();
            }
            txtLabel.setVisibility(View.GONE);

            ArrayList<CarModel> documentsList = new ArrayList<>();
            ArrayList<CarModel> snaplist = new ArrayList<>();

            CarModel carModel = new CarModel();
            carModel.setModelname("YZF R1");
            carModel.setLocalImage(R.drawable.yzfr1);
            carModel.setTopic("CPU");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-yzf-r1.html");
            documentsList.add(carModel);

            carModel = new CarModel();
            carModel.setModelname("MT-09");
            carModel.setLocalImage(R.drawable.mt_09);
            carModel.setTopic("");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-mt09.html");
            documentsList.add(carModel);


/////////////////////////////////////////////////


            carModel = new CarModel();
            carModel.setModelname("FAZER25");
            carModel.setLocalImage(R.drawable.fazer25);
            carModel.setTopic("Domestic");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-fazer25.html");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("FZ25");
            carModel.setLocalImage(R.drawable.fz25);
            carModel.setTopic("");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-fz-25.html");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("YZF R15 VER 2.0");
            carModel.setLocalImage(R.drawable.yzfr15);
            carModel.setTopic("");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-yzf-r15.html");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("YZF R15s");
            carModel.setLocalImage(R.drawable.yzfr15s);
            carModel.setTopic("");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-yzf-r15s.html");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("FAZER-FI");
            carModel.setLocalImage(R.drawable.fazerf1);
            carModel.setTopic("");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-fazer-fi.html");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("FZS-FI");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-fzs-fi.html");
            carModel.setLocalImage(R.drawable.fzsf1);
            carModel.setTopic("");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("FZ-FI");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-fz-fi.html");
            carModel.setLocalImage(R.drawable.fzf1);
            carModel.setTopic("");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("SZ-RR VER 2.0");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-sz-rr-bluecore.html");
            carModel.setLocalImage(R.drawable.szrr);
            carModel.setTopic("");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("SALUTO DISK BRAKE");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-saluto-diskbrake.html");
            carModel.setLocalImage(R.drawable.solutodisk);
            carModel.setTopic("");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("SALUTO");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-saluto.html");
            carModel.setLocalImage(R.drawable.soluto);
            carModel.setTopic("");
            documentsList.add(carModel);


            carModel = new CarModel();
            carModel.setModelname("SALUTO RX");
            carModel.setUrl("http://www.yamaha-motor-india.com/motorcycle-saluto-rx.html");
            carModel.setLocalImage(R.drawable.solutorx);
            carModel.setTopic("");
            documentsList.add(carModel);


            Group group = new Group();
            group.setName("MotorCycles");
            group.setLabreports(documentsList);
            ExpListItems.add(group);

/////////////////////////////////////////////


            carModel = new CarModel();
            carModel.setModelname("FASCINO");
            carModel.setUrl("http://www.yamaha-motor-india.com/scooter-fascino.html");
            carModel.setLocalImage(R.drawable.fazino);
            carModel.setTopic("");
            snaplist.add(carModel);

            carModel = new CarModel();
            carModel.setModelname("ALPHA");
            carModel.setUrl("http://www.yamaha-motor-india.com/scooter-alpha.html");
            carModel.setLocalImage(R.drawable.alpha);
            carModel.setTopic("");
            snaplist.add(carModel);

            carModel = new CarModel();
            carModel.setModelname("RAY-Z");
            carModel.setUrl("http://www.yamaha-motor-india.com/scooter-rayz.html");
            carModel.setLocalImage(R.drawable.rax);
            carModel.setTopic("");
            snaplist.add(carModel);

            carModel = new CarModel();
            carModel.setModelname("RAY ZR");
            carModel.setUrl("http://www.yamaha-motor-india.com/scooter-rayzr.html");
            carModel.setLocalImage(R.drawable.rayzr);
            carModel.setTopic("");
            snaplist.add(carModel);

            group = new Group();
            group.setName("Scooters");
            group.setLabreports(snaplist);
            ExpListItems.add(group);

            ExpAdapter = new ExpandListAdapter(activity, ExpListItems);
            ExpandList.setAdapter(ExpAdapter);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
