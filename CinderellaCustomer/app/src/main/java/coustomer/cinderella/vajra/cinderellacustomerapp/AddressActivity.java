package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import common.Config;
import common.Response;

public class AddressActivity extends AppCompatActivity implements Response {

    private EditText edt_locality;
    private EditText edt_city;
    private ArrayList<String> arrayList_city = new ArrayList<String>();
    private ArrayList<String> arrayList_locality = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private Button btn_submit;
    private ArrayList<String> city_id = new ArrayList<String>();
    private ArrayList<String> locality_id = new ArrayList<String>();

    private EditText edt_street, edt_flat, edt_building, edt_landmark;
    private String aid="",cid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        edt_locality = (EditText) findViewById(R.id.edt_locality);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_street = (EditText) findViewById(R.id.edt_street);
        edt_flat = (EditText) findViewById(R.id.edt_landmark);
        edt_building = (EditText) findViewById(R.id.edt_building);
        edt_landmark = (EditText) findViewById(R.id.edt_landmark);


        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(arrayList_city.size() == 0)) {
                    arrayList_city.clear();
                }
                for (int j = 0; j < hashMapArrayList.size(); j++) {
                    arrayList_city.add(hashMapArrayList.get(j).get("city"));


                }
                final Dialog dialog = new Dialog(AddressActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.city_list);
                ListView list_city = (ListView) dialog.findViewById(R.id.list_city);
                ArrayAdapter arrayAdapter = new ArrayAdapter(AddressActivity.this, android.R.layout.simple_list_item_1, arrayList_city);
                list_city.setAdapter(arrayAdapter);
                list_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        edt_city.setText(hashMapArrayList.get(i).get("city"));
                        dialog.dismiss();
                        edt_locality.getText().clear();
                    }
                });
                dialog.show();
            }
        });
        edt_locality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("locality" + hashMapArrayList.toString());
                if (!edt_city.getText().toString().equals("")) {
                    if (!(arrayList_locality.size() == 0)) {
                        arrayList_locality.clear();
                        city_id.clear();
                        locality_id.clear();
                    }
                    for (int j = 0; j < hashMapArrayList.size(); j++) {
                        if (hashMapArrayList.get(j).get("city").trim().equals(edt_city.getText().toString().trim())) {
                            arrayList_locality.add(hashMapArrayList.get(j).get("locality"));
                            city_id.add(hashMapArrayList.get(j).get("cid"));
                            locality_id.add(hashMapArrayList.get(j).get("aid"));

                        }

                    }
                    final Dialog dialog = new Dialog(AddressActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.locality_list);
                    ListView list_locality = (ListView) dialog.findViewById(R.id.list_locality);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(AddressActivity.this, android.R.layout.simple_list_item_1, arrayList_locality);
                    list_locality.setAdapter(arrayAdapter);
                    list_locality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            edt_locality.setText(arrayList_locality.get(i));
                            aid=locality_id.get(i);
                            cid=city_id.get(i);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "First Choose The City.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (Config.isNetworkAvailable(this)) {
            volley_chk_login();
        } else {
            Config.alert("Warning...!", "Please Check Your Internet Connection.", 0, AddressActivity.this);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valiDation()) {

                    Config.storeDATA(AddressActivity.this, "city", edt_city.getText().toString().trim());
                    Config.storeDATA(AddressActivity.this, "city_id", cid);
                    Config.storeDATA(AddressActivity.this, "locality_id", aid);

                    Config.storeDATA(AddressActivity.this, "locality", edt_locality.getText().toString().trim());
                    if (getIntent().getStringExtra("address").equals("home")) {
                        Config.storeDATA(AddressActivity.this, "address_home", edt_street.getText().toString() + "," +
                                 edt_flat.getText().toString() + "," +  edt_building.getText().toString()
                                + "," +edt_landmark.getText().toString());
                    }
                    else if (getIntent().getStringExtra("address").equals("office")) {
                        Config.storeDATA(AddressActivity.this, "address_office", edt_street.getText().toString() + "," +
                                 edt_flat.getText().toString() + "," +  edt_building.getText().toString()
                                + "," +  edt_landmark.getText().toString());
                    }
                    else if (getIntent().getStringExtra("address").equals("other")) {
                        Config.storeDATA(AddressActivity.this, "address_other", edt_street.getText().toString() + "," +
                                edt_flat.getText().toString() + "," +  edt_building.getText().toString()
                                + "," +edt_landmark.getText().toString());
                    }
                    Config.alert("Pleasure Moment...", "Address Saved SuccessFull.", 2, AddressActivity.this);
                }


            }
        });
    }

    private boolean valiDation() {
        if (edt_city.getText().toString().equals("")) {
            edt_city.requestFocus();

            Toast.makeText(getApplicationContext(), "Select The City First.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_locality.getText().toString().equals("")) {
            edt_locality.requestFocus();
            Toast.makeText(getApplicationContext(), "Select The Locality.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_street.getText().toString().equals("")) {
            edt_street.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Street.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_flat.getText().toString().equals("")) {
            edt_flat.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Flat/Apartment/House.No.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_building.getText().toString().equals("")) {
            edt_building.requestFocus();
            Toast.makeText(getApplicationContext(), "Building/Society Name.", Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void volley_chk_login() {
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }


        RequestQueue queue = Volley.newRequestQueue(AddressActivity.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Config.MAIN_URL + Config.GET_LOCALITY, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    HashMap<String, String> stringHashMap;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        stringHashMap = new HashMap<String, String>();
                        stringHashMap.put("cid", jsonArray.getJSONObject(i).getString("cid"));
                        stringHashMap.put("city", jsonArray.getJSONObject(i).getString("city"));
                        stringHashMap.put("locality", jsonArray.getJSONObject(i).getString("locality"));
                        stringHashMap.put("aid", jsonArray.getJSONObject(i).getString("aid"));


//                        arrayList_city.add(jsonArray.getJSONObject(i).getString("city"));
//
//                        arrayList_locality.add(jsonArray.getJSONObject(i).getString("locality"));
                        hashMapArrayList.add(stringHashMap);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);

    }


    @Override
    public void processFinish(String output) {

    }
}
