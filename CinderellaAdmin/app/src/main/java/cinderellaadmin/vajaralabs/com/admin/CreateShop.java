package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import common.AsyncGET;
import common.AsyncPOST;
import common.Configg;
import common.Response;
import common.SendMail;

public class CreateShop extends AppCompatActivity implements Response {
    EditText edt_shop_name, edt_contact_person, edt_mobile_no, edt_email, edt_address;
    Button btn_submit;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private String password = "";
    private AsyncGET asyncGET;
    private ArrayList<String> arrayList_city = new ArrayList<String>();
    private ArrayList<String> arrayList_aid = new ArrayList<String>();
    private ArrayList<String> arrayList_locality = new ArrayList<String>();

    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private EditText edt_locality, edt_city;
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> city_id = new ArrayList<String>();
    private CreateShop activity;
    private String cid = "";
    private String aid = "";
    private EditText edt_designation;
    private EditText edt_pin_code;
    private String shop_code = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add-Shop Keeper");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        edt_designation = (EditText) findViewById(R.id.edt_designation);
        edt_pin_code = (EditText) findViewById(R.id.edt_pin_code);

        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);


        edt_locality = (EditText) findViewById(R.id.edt_locality);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_contact_person = (EditText) findViewById(R.id.edt_contact_person);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(arrayList_city.size() == 0)) {
                    arrayList_city.clear();
                }
                for (int j = 0; j < hashMapArrayList.size(); j++) {
                    arrayList_city.add(hashMapArrayList.get(j).get("city"));


                }
                final Dialog dialog = new Dialog(CreateShop.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.city_list);
                ListView list_city = (ListView) dialog.findViewById(R.id.list_city);
                ArrayAdapter arrayAdapter = new ArrayAdapter(CreateShop.this, android.R.layout.simple_list_item_1, arrayList_city);
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
                        arrayList_aid.clear();
                    }
                    for (int j = 0; j < hashMapArrayList.size(); j++) {
                        if (hashMapArrayList.get(j).get("city").trim().equals(edt_city.getText().toString().trim())) {
                            arrayList_locality.add(hashMapArrayList.get(j).get("locality"));
                            arrayList_aid.add(hashMapArrayList.get(j).get("aid"));
                            city_id.add(hashMapArrayList.get(j).get("cid"));
                        }

                    }
                    final Dialog dialog = new Dialog(CreateShop.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.locality_list);
                    ListView list_locality = (ListView) dialog.findViewById(R.id.list_locality);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(CreateShop.this, android.R.layout.simple_list_item_1, arrayList_locality);
                    list_locality.setAdapter(arrayAdapter);
                    list_locality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            edt_locality.setText(arrayList_locality.get(i));
                            aid = arrayList_aid.get(i);
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
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = UUID.randomUUID().toString();
                final String[] pwd_spli = pwd.split("-");
                password = pwd_spli[0];
                shop_code = pwd_spli[1];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                Toast.makeText(getApplicationContext(), "cid" + cid, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "aid" + aid, Toast.LENGTH_SHORT).show();

                if (!signUpValidation()) {
                    nameValuePairs.add(new BasicNameValuePair("shop_name", edt_shop_name.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("shop_keeper", edt_contact_person.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("city", edt_city.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("locality", edt_locality.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("cid", cid));
                    nameValuePairs.add(new BasicNameValuePair("aid", aid));

                    nameValuePairs.add(new BasicNameValuePair("mobile", edt_mobile_no.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("email", edt_email.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("password", password.trim()));
                    nameValuePairs.add(new BasicNameValuePair("designation", edt_designation.getText().toString().trim()));

                    nameValuePairs.add(new BasicNameValuePair("pincode", edt_pin_code.getText().toString().trim()));

                    nameValuePairs.add(new BasicNameValuePair("shop_code", shop_code));
                    nameValuePairs.add(new BasicNameValuePair("shop_code_and_name", edt_shop_name.getText().toString().trim() + "-" +
                            "CL" + shop_code));


                    nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
//                    if (new ConnectionDetector(CreateShop.this).isConnectingToInternet()) {
                    asyncPOST = new AsyncPOST(nameValuePairs, CreateShop.this, Configg.MAIN_URL + Configg.CREATE_SHOP, CreateShop.this);
                    asyncPOST.execute();
//                    } else {
//                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateShop.this);
//                        builder.setMessage("Please Check Your Internet Connection").setTitle("Warning..!")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        // do nothing
////                                        finish();
////                                        overridePendingTransition(0, 0);
////                                        startActivity(getIntent());
////                                        overridePendingTransition(0, 0);
//                                    }
//                                });
//                        android.app.AlertDialog alert = builder.create();
//                        alert.show();
//                        alert.setCancelable(false);
//                    }

                    System.out.println("create_shop" + nameValuePairs.toString());
                }


            }
        });
        if (Configg.isNetworkAvailable(this)) {
            volley_chk_login();
        } else {
            Configg.alert("Warning...!", "Please Check Your Internet Connection.", 0, CreateShop.this);
        }


    }

    private boolean signUpValidation() {
        if (edt_shop_name.getText().toString().equals("")) {
            edt_shop_name.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Fill The Shop Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_city.getText().toString().equals("")) {
            edt_city.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Select The City.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_locality.getText().toString().equals("")) {
            edt_locality.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Select The Locality.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_contact_person.getText().toString().equals("")) {
            edt_contact_person.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Shop Keeper Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_designation.getText().toString().equals("")) {
            edt_designation.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Designation.", Toast.LENGTH_SHORT).show();
            return true;

        }
//
        if (edt_mobile_no.getText().toString().equals("")) {
            edt_mobile_no.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Mobile Number.", Toast.LENGTH_SHORT).show();
            return true;


        }
        if (edt_email.getText().toString().equals("")) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter The Email Address", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!Configg.isValidEmaillId(edt_email.getText().toString().trim())) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (edt_address.getText().toString().equals("")) {
            edt_address.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Postal Address.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_pin_code.getText().toString().equals("")) {
            edt_pin_code.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Pin Code.", Toast.LENGTH_SHORT).show();
            return true;

        }

        return false;
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

    private void volley_chk_login() {
        if (!(city_id.size() == 0)) {
            city_id.clear();
            city_list.clear();

        }


        RequestQueue queue = Volley.newRequestQueue(CreateShop.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Configg.MAIN_URL + Configg.GET_LOCALITY, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                System.out.println("city_list" + response);

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
        try {
            JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("1")) {
//                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateShop.this);
//                builder.setMessage(jsonObject.getString("message")).setTitle("Response From Server..")
//                        .setCancelable(false)
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // do nothing
////                                finish();
//
//                            }
//                        });
//                android.app.AlertDialog alert = builder.create();
//                alert.show();
//                alert.setCancelable(false);
                Toast.makeText(getApplicationContext(), "SuccessFully Shop Keeper Account Created.", Toast.LENGTH_SHORT).show();
                new SendMail(CreateShop.this, edt_email.getText().toString().trim(), "Cinderella Shop Keeper Credential", "User Id:" + edt_mobile_no.getText().toString() + '\n' +
                        "Password:" + password).execute();
            }
            if (jsonObject.getString("success").equals("3")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CreateShop.this);
                builder.setMessage(jsonObject.getString("message")).setTitle("Response From Vahan")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
//                                finish();

                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void volley_post_sub(final String cid) {
        if (!(arrayList_aid.size() == 0)) {
            arrayList_aid.clear();
            arrayList_locality.clear();
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.POST, Configg.MAIN_URL + Configg.GET_LOCALITY_LIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.print("response" + response);

                pDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        arrayList_locality.add(jsonArray.getJSONObject(i).getString("locality"));
                        arrayList_aid.add(jsonArray.getJSONObject(i).getString("aid"));
                        city_id.add(jsonArray.getJSONObject(i).getString("cid"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("aid_list" + arrayList_aid.toString());

//                Toast.makeText(getApplicationContext(), "response" + response, 1000).show();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                pDialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", cid);
//                params.put("comment", Uri.encode(comment));
//                params.put("comment_post_ID",String.valueOf(postId));
//                params.put("blogId",String.valueOf(blogId));
                System.out.println("params" + params.toString());

                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


}
