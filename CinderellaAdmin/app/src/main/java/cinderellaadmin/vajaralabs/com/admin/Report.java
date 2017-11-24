package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class Report extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private AdapterReport adapterBooking;
    private ListView list_booking;
    private EditText edt_search;
    private Report activity;
    private ArrayList<String> arrayList=new ArrayList<String>();
    private ArrayList<String> arrayList2=new ArrayList<String>();

    private EditText edt_sort_by;
    private String percentage="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        activity=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Report"+", Total : ");

        edt_search = (EditText) findViewById(R.id.edt_search);
        edt_sort_by=(EditText) findViewById(R.id.edt_sort_by);

        arrayList.add("Total Booking-0%");
        arrayList.add("Total Assigned-25%");
        arrayList.add("Total Pickup-50%");
        arrayList.add("Factory In-75%");
        arrayList.add("Factory Out-90%");
        arrayList.add("Ready To Deliver-95%");
        arrayList2.add("");
        arrayList2.add("25");
        arrayList2.add("50");
        arrayList2.add("75");
        arrayList2.add("90");
        arrayList2.add("95");





        edt_sort_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.sort_by);
                ListView list_sort=(ListView) dialog.findViewById(R.id.list_sort);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1,arrayList);
                list_sort.setAdapter(adapter);
                list_sort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        edt_sort_by.setText(arrayList.get(position));
                        percentage=arrayList2.get(position);
                        String text = percentage;
                        adapterBooking.filter2(text);
                        getSupportActionBar().setTitle("Report"+", "+arrayList.get(position)+
                                " : "+adapterBooking.getCount());

                        dialog.dismiss();

                    }
                });
                dialog.show();
//

            }
        });
        list_booking = (ListView) findViewById(R.id.list_booking);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edt_search.getText().toString().toLowerCase(Locale.getDefault());
                adapterBooking.filter(text);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Configg.isNetworkAvailable(this)) {

                nameValuePairs.add(new BasicNameValuePair("factory_id", Configg.getDATA(this, "fid")));
//                nameValuePairs.add(new BasicNameValuePair("completion", "25"));

                asyncPOST = new AsyncPOST(nameValuePairs, this, Configg.MAIN_URL + Configg.GET_REPORT, this);
                asyncPOST.execute();
                System.out.println("bookingzone" + nameValuePairs.toString());
            }
         else {
            Configg.alert("Internet Lost...", "Please Check Your Internet Connection To View The Booked Data.", 0, this);
        }
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
        System.out.println("booking" + output);
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }
        try {
            JSONArray jsonArray = new JSONArray(output);
            getSupportActionBar().setTitle("Report"+", Total : "+jsonArray.length());

            HashMap<String, String> stringHashMap;
            if (jsonArray.length() == 0) {
                Configg.alert("Theres No Any Recent Bookings.", "", 2, activity);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                stringHashMap = new HashMap<String, String>();
                stringHashMap.put("pcid", jsonArray.getJSONObject(i).getString("pcid"));
                stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                stringHashMap.put("city_id", jsonArray.getJSONObject(i).getString("city_id"));
                stringHashMap.put("completion", jsonArray.getJSONObject(i).getString("completion"));

                stringHashMap.put("customer_id", jsonArray.getJSONObject(i).getString("customer_id"));
                stringHashMap.put("shop_code_and_name", jsonArray.getJSONObject(i).getString("shop_code_and_name"));


                stringHashMap.put("locality_id", jsonArray.getJSONObject(i).getString("locality_id"));
                stringHashMap.put("delivery_contact", jsonArray.getJSONObject(i).getString("delivery_contact"));
                stringHashMap.put("delivery_contact_name", jsonArray.getJSONObject(i).getString("delivery_contact_name"));


                stringHashMap.put("mobile_date", jsonArray.getJSONObject(i).getString("mobile_date"));
                stringHashMap.put("address", jsonArray.getJSONObject(i).getString("address"));
                stringHashMap.put("mobile", jsonArray.getJSONObject(i).getString("mobile"));
                stringHashMap.put("pickup_time", jsonArray.getJSONObject(i).getString("pickup_time"));
                stringHashMap.put("pickup_date", jsonArray.getJSONObject(i).getString("pickup_date"));
                stringHashMap.put("unique_code", jsonArray.getJSONObject(i).getString("unique_code"));
                stringHashMap.put("created_at", jsonArray.getJSONObject(i).getString("created_at"));
                stringHashMap.put("factory_name", jsonArray.getJSONObject(i).getString("factory_name"));
                stringHashMap.put("factory_person", jsonArray.getJSONObject(i).getString("factory_person"));
                stringHashMap.put("factory_contact", jsonArray.getJSONObject(i).getString("factory_contact"));

//                if (jsonArray.getJSONObject(i).getString("cancel").equals("")) {

                    hashMapArrayList.add(stringHashMap);
//                }


            }
            adapterBooking = new AdapterReport(activity, hashMapArrayList);
            list_booking.setAdapter(adapterBooking);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
