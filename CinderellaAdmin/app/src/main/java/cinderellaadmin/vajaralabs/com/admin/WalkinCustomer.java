package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.Random;
import java.util.UUID;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class WalkinCustomer extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup = new ArrayList<NameValuePair>();
    private EditText edt_name;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    private EditText edt_mobile;
    private EditText edt_email;
    private EditText edt_pwd;
    private EditText edt_con_pwd;
    private EditText edt_address;
    private Button btn_submit;
    private String password = "";
    private WalkinCustomer activity;

    private ArrayList<String> arrayList_city = new ArrayList<String>();
    private ArrayList<String> arrayList_locality = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<HashMap<String, String>>();
    private EditText edt_locality;
    private EditText edt_city;

    private String aid = "", cid = "";
    private ArrayList<String> city_id = new ArrayList<String>();
    private ArrayList<String> locality_id = new ArrayList<String>();
    private String currentDateTime = "";
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkin_customer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Walkin-Customer");
        activity = this;

//        / clear FLAG_TRANSLUCENT_STATUS flag:
        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.setStatusBarColor(this.getResources().getColor(R.color.status));
        }
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_pwd = (EditText) findViewById(R.id.edt_pwd);
        edt_con_pwd = (EditText) findViewById(R.id.edt_con_pwd);
        edt_address = (EditText) findViewById(R.id.edt_address);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_locality = (EditText) findViewById(R.id.edt_locality);

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
                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.city_list);
                ListView list_city = (ListView) dialog.findViewById(R.id.list_city);
                ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, arrayList_city);
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
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.locality_list);
                    ListView list_locality = (ListView) dialog.findViewById(R.id.list_locality);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, arrayList_locality);
                    list_locality.setAdapter(arrayAdapter);
                    list_locality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            edt_locality.setText(arrayList_locality.get(i));
                            aid = locality_id.get(i);
                            cid = city_id.get(i);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "First Choose The City.", Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (Configg.isNetworkAvailable(this)) {
            volley_chk_login();
        } else {
            Configg.alert("Warning...!", "Please Check Your Internet Connection.", 0, activity);
        }
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUpValidation()) {
                    String pwd = UUID.randomUUID().toString();
                    final String[] pwd_spli = pwd.split("-");
                    password = pwd_spli[0];
                    nameValuePairs_signup.add(new BasicNameValuePair("name", edt_name.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("mobile", edt_mobile.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("email", edt_email.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("pwd", password));
                    nameValuePairs_signup.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));


                    System.out.println("signup_params" + nameValuePairs_signup.toString());

                    asyncPOST = new AsyncPOST(nameValuePairs_signup, activity, common.Configg.MAIN_URL + common.Configg.SIGNUP_CUSTOMER, activity);
                    asyncPOST.execute();

                }

            }
        });
    }

    private boolean signUpValidation() {
        if (cid.equals("")) {
            edt_city.requestFocus();

            Toast.makeText(getApplicationContext(), "Choose City.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_locality.getText().toString().equals("")) {
            edt_locality.requestFocus();

            Toast.makeText(getApplicationContext(), "Choose Locality.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_name.getText().toString().equals("")) {
            edt_name.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Fill Your First.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_mobile.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your Mobile Number.", Toast.LENGTH_SHORT).show();
            return true;

        }
//        if (edt_email.getText().toString().equals("")) {
//            edt_email.requestFocus();
//            Toast.makeText(getApplicationContext(), "Kindly Fill Your E-Mail.", Toast.LENGTH_SHORT).show();
//            return true;
//
//        }
//
//
//        if (!Configg.isValidEmaillId(edt_email.getText().toString().trim())) {
//            edt_email.requestFocus();
//            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
//            return true;
//        }

        if (edt_address.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Address Should Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
//        if (edt_pwd.getText().toString().equals("")) {
//            edt_pwd.requestFocus();
//            Toast.makeText(getApplicationContext(), "Password Should Not Empty.", Toast.LENGTH_SHORT).show();
//            return true;
//
//        }
//        if (edt_con_pwd.getText().toString().equals("")) {
//            edt_con_pwd.requestFocus();
//            Toast.makeText(getApplicationContext(), "Confirm Your Password.", Toast.LENGTH_SHORT).show();
//            return true;
//
//        }
//        if (!edt_con_pwd.getText().toString().equals(edt_pwd.getText().toString())) {
//            edt_pwd.requestFocus();
//            Toast.makeText(getApplicationContext(), "Password Doesn't Match With Confirm Password.", Toast.LENGTH_SHORT).show();
//            return true;
//
//        }
        return false;
    }

    @Override
    public void processFinish(String output) {

        try {
            final JSONObject jsonObject = new JSONObject(output);
            if (jsonObject.getString("type").equals("register")) {

                if (jsonObject.getString("success").equals("2")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                    builder.setMessage(jsonObject.getString("message")).setTitle("Response From Cinderella.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("customer_list");

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        currentDateTime = dateFormat.format(new Date()); // Find todays dat

                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        date = sdf.format(new Date());
                                        Random rnd = new Random();
                                        int n = 100000 + rnd.nextInt(900000);
                                        nameValuePairs.add(new BasicNameValuePair("city_id", cid));
                                        nameValuePairs.add(new BasicNameValuePair("locality_id", aid));
                                        nameValuePairs.add(new BasicNameValuePair("customer_name", edt_name.getText().toString().trim()));
                                        nameValuePairs.add(new BasicNameValuePair("city", edt_city.getText().toString()));
                                        nameValuePairs.add(new BasicNameValuePair("locality", edt_locality.getText().toString()));
                                        nameValuePairs.add(new BasicNameValuePair("address", jsonArray.getJSONObject(0).getString("address")));
                                        nameValuePairs.add(new BasicNameValuePair("pickup_date", currentDateTime));
                                        nameValuePairs.add(new BasicNameValuePair("pickup_time", date));
                                        nameValuePairs.add(new BasicNameValuePair("mobile", jsonArray.getJSONObject(0).getString("mobile")));
                                        nameValuePairs.add(new BasicNameValuePair("cid", jsonArray.getJSONObject(0).getString("cid")));
                                        nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
                                        nameValuePairs.add(new BasicNameValuePair("unique_code", "" + n));
                                        nameValuePairs.add(new BasicNameValuePair("customer_token", ""));
                                        nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));

                                        nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
                                        if (Configg.getDATA(activity, "type").equals("shop")) {
                                            nameValuePairs.add(new BasicNameValuePair("completion", ""));
                                            nameValuePairs.add(new BasicNameValuePair("delivery_id", ""));

                                        } else {
                                            nameValuePairs.add(new BasicNameValuePair("completion", "25"));
                                            nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));


                                        }


                                        asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.CUSTOMER_BOOKING, activity);
                                        asyncPOST.execute();
                                        System.out.println("params" + nameValuePairs.toString());

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                    alert.setCancelable(false);
                } else if (jsonObject.getString("success").equals("1")) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
                    builder.setMessage(jsonObject.getString("message")).setTitle("Response From Cinderella.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
//                                    finish();

                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("customer_list");

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        String currentDateTime = dateFormat.format(new Date()); // Find todays dat

                                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                        String date = sdf.format(new Date());
                                        Random rnd = new Random();
                                        int n = 100000 + rnd.nextInt(900000);
                                        nameValuePairs.add(new BasicNameValuePair("city_id", cid));
                                        nameValuePairs.add(new BasicNameValuePair("locality_id", aid));
                                        nameValuePairs.add(new BasicNameValuePair("customer_name", edt_name.getText().toString().trim()));
                                        nameValuePairs.add(new BasicNameValuePair("city", edt_city.getText().toString()));
                                        nameValuePairs.add(new BasicNameValuePair("locality", edt_locality.getText().toString()));
                                        nameValuePairs.add(new BasicNameValuePair("address", jsonArray.getJSONObject(0).getString("address")));
                                        nameValuePairs.add(new BasicNameValuePair("pickup_date", currentDateTime));
                                        nameValuePairs.add(new BasicNameValuePair("pickup_time", date));
                                        nameValuePairs.add(new BasicNameValuePair("mobile", jsonArray.getJSONObject(0).getString("mobile")));
                                        nameValuePairs.add(new BasicNameValuePair("cid", jsonArray.getJSONObject(0).getString("cid")));
                                        nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
                                        nameValuePairs.add(new BasicNameValuePair("shop_id", Configg.getDATA(activity, "sid")));

                                        nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));
                                        if (Configg.getDATA(activity, "type").equals("shop")) {
                                            nameValuePairs.add(new BasicNameValuePair("completion", ""));
                                            nameValuePairs.add(new BasicNameValuePair("delivery_id", ""));

                                        } else {
                                            nameValuePairs.add(new BasicNameValuePair("completion", "25"));
                                            nameValuePairs.add(new BasicNameValuePair("delivery_id", Configg.getDATA(activity, "did")));


                                        }
                                        nameValuePairs.add(new BasicNameValuePair("unique_code", "" + n));
                                        nameValuePairs.add(new BasicNameValuePair("customer_token", ""));

                                        asyncPOST = new AsyncPOST(nameValuePairs, activity, Configg.MAIN_URL + Configg.CUSTOMER_BOOKING, activity);
                                        asyncPOST.execute();
                                        System.out.println("params" + nameValuePairs.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    android.app.AlertDialog alert = builder.create();
                    alert.show();
                    alert.setCancelable(false);

                }

            } else {

                if (jsonObject.getString("success").equals("1")) {
                    finish();
                    Intent intent = new Intent(activity, BookingZone.class);
//                    intent.putExtra("city", edt_city.getText().toString());
//                    intent.putExtra("locality",  edt_locality.getText().toString());
//                    intent.putExtra("cid", hashMapArrayList.get(posistion).get("cid"));
//                    intent.putExtra("mobile", edt_mobile.getText().toString());
//                    intent.putExtra("pickup_date", currentDateTime);
//                    intent.putExtra("pickup_time", date);
//                    intent.putExtra("mobile_date", currentDateTime);
//                    intent.putExtra("city_id", cid);
//                    intent.putExtra("locality_id", aid);
//                    intent.putExtra("delivery_id", hashMapArrayList.get(posistion).get("delivery_id"));
//                    intent.putExtra("booking_id", hashMapArrayList.get(posistion).get("booking_id"));
//                    intent.putExtra("delivery_person", hashMapArrayList.get(posistion).get("delivery_person"));
//                    intent.putExtra("address", hashMapArrayList.get(posistion).get("address"));
//                    intent.putExtra("unique_code", hashMapArrayList.get(posistion).get("unique_code"));
//
//
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void volley_chk_login() {
        if (!(hashMapArrayList.size() == 0)) {
            hashMapArrayList.clear();
        }


        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Configg.MAIN_URL + Configg.GET_LOCALITY, new com.android.volley.Response.Listener<String>() {
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
}
