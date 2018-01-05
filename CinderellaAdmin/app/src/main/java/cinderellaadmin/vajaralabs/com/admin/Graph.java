package cinderellaadmin.vajaralabs.com.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jjoe64.graphview.GraphView;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class Graph extends AppCompatActivity implements Response {

    private Graph activity;
    private EditText edt_fromdate;
    private EditText edt_todate, edt_search, edt_company_name, edt_farm_name, edt_type;
    private Button btn_submit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private GraphView graph;
    private double rate_avg = 0;
    private AsyncPOST asyncPOST;
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<BarEntry> entries2 = new ArrayList<>();
    ArrayList<String> arrayList_dates=new ArrayList<String>();
    ArrayList<BarEntry> entries3 = new ArrayList<>();

    List<String> list = new ArrayList<String>();
    List<String> list3 = new ArrayList<String>();
    List<Double> list2 = new ArrayList<Double>();

    private String type = "";

    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<String> labels2 = new ArrayList<String>();
    ArrayList<String> labels3 = new ArrayList<String>();
    private double sum2=0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewise_graph);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Graph-Total Sales");
        edt_fromdate = (EditText) findViewById(R.id.edt_from);
        edt_todate = (EditText) findViewById(R.id.edt_to);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_type = (EditText) findViewById(R.id.edt_type);

//        graph = (GraphView) findViewById(R.id.graph);
//        edt_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final Dialog dialog=new Dialog(activity);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.customer_price_list.sort_by);
//                final TextView txt_detailer=(TextView)dialog.findViewById(R.id.txt_detailer);
//                final TextView txt_dis=(TextView)dialog.findViewById(R.id.txt_dis);
//                final TextView txt_date=(TextView)dialog.findViewById(R.id.txt_date);
//
//                txt_detailer.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        type="detailer";
//                        dialog.dismiss();
//                        edt_type.setText(txt_detailer.getText().toString());
//
//                    }
//                });
//
//                txt_dis.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        type="dis";
//                        dialog.dismiss();
//                        edt_type.setText(txt_dis.getText().toString());
//
//
//                    }
//                });
//                txt_date.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        type="date";
//                        dialog.dismiss();
//                        edt_type.setText(txt_date.getText().toString());
//
//                    }
//                });
//                dialog.show();
//            }
//        });

        edt_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_fromdate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                String inputPattern = "yyyy-M-d";
                                String outputPattern = "yyyy-MM-dd";
                                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                Date date = null;
                                String str = null;

                                try {
                                    date = inputFormat.parse(edt_fromdate.getText().toString());
                                    str = outputFormat.format(date);
                                    edt_fromdate.setText(str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        edt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edt_todate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                String inputPattern = "yyyy-M-d";
                                String outputPattern = "yyyy-MM-dd";
                                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                                Date date = null;
                                String str = null;

                                try {
                                    date = inputFormat.parse(edt_todate.getText().toString());
                                    str = outputFormat.format(date);
                                    edt_todate.setText(str);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_todate.getText().toString().equals("") && !edt_fromdate.getText().toString().equals("")
                        ) {

                    nameValuePairs.add(new BasicNameValuePair("from_date", edt_fromdate.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("to_date", edt_todate.getText().toString().trim()));
                    asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL +
                            Configg.GET_GRAPH, activity);
                    asyncPOST.execute();
                    System.out.println("list_datewise" + nameValuePairs.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Select The Date First.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    @Override
    public void processFinish(String output) {
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        BarDataSet dataset = null;

        BarData data = null;


        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
            labels.clear();
            list.clear();
            entries.clear();
            list2.clear();
            arrayList_dates.clear();

//            dataset.clear();
//            data.clearValues();
            barChart.clear();
        }

        try {
            HashMap<String, String> stringHashMap;
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("completed_date"));
                stringHashMap.put("given_amt", jsonArray.getJSONObject(i).getString("given_amt"));

                hashMapArrayList.add(stringHashMap);
                String day[] = jsonArray.getJSONObject(i).getString("completed_date").split("-");
//                s = day[2];
                list.add(jsonArray.getJSONObject(i).getString("completed_date"));
                arrayList_dates.add(jsonArray.getJSONObject(i).getString("completed_date"));

                list2.add(Double.parseDouble(jsonArray.getJSONObject(i).getString("given_amt")));

                System.out.println("array"+hashMapArrayList.toString());


            }

            Set<String> primesWithoutDuplicates = new LinkedHashSet<String>(arrayList_dates); // now let's clear the ArrayList so that we can copy all elements from LinkedHashSet primes.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

            arrayList_dates.clear(); // copying elements but without any duplicates primes.addAll(primesWithoutDuplicates);

            arrayList_dates.addAll(primesWithoutDuplicates);

            for (int i=0;i<arrayList_dates.size();i++){

                for (int i1=0;i1<hashMapArrayList.size();i1++){
                    if (arrayList_dates.get(i).equals(hashMapArrayList.get(i1).get("completed_date"))) {
                    sum2 += Double.parseDouble(hashMapArrayList.get(i1).get(hashMapArrayList.get(i1).get("given_amt")));
                    System.out.println("Sum" + ": " + sum2);
                    Toast.makeText(activity,"sum"+sum2,1000).show();

                }
                }

//
            }

            Set<String> unique = new HashSet<String>(list);



            int inc = -1;
            double sum = 0;

            for (String key : unique) {

                System.out.println(key + ": " + Collections.frequency(list, key));


//                for (int amt = 0; amt < hashMapArrayList.size(); amt++) {
//                    if (key.equals(hashMapArrayList.get(amt).get("completed_date"))) {
//                        sum += Double.parseDouble(hashMapArrayList.get(amt).get(hashMapArrayList.get(amt).get("given_amt")));
//                        System.out.println("Sum" + ": " + sum);
//                        Toast.makeText(activity,"sum"+sum,1000).show();
//
//                    }
//                }
                inc++;



                entries.add(new BarEntry(Collections.frequency(list, key), inc));

                labels.add(String.valueOf(key));
                dataset = new BarDataSet(entries, "# of Calls");
                data = new BarData(labels, dataset);
                barChart.setData(data);
                barChart.animateY(5000);
            }
            sum = 0;
            inc = 0;


            // dataset.setColors(ColorTemplate.COLORFUL_COLORS); //


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
