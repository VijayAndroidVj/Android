package cinderellaadmin.vajaralabs.com.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.UUID;

import common.AsyncPOST;
import common.Configg;
import common.Response;
import common.SendMail;

public class AddDealer extends AppCompatActivity implements Response {

    EditText edt_shop_name, edt_contact_person, edt_mobile_no, edt_email, edt_address;
    Button btn_submit;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private String password = "";
    private EditText edt_license;
    private EditText edt_vehicle;
    private EditText edt_shop;
    private ArrayList<String> arrayList_shopcode = new ArrayList<String>();
    private ArrayList<String> arrayList_shopcde_and_name = new ArrayList<String>();
    private ArrayList<String> arrayList_sid = new ArrayList<String>();
    private AddDealer activity;
    private String shop_name = "", sid = "", shop_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dealer);
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add-Delivery Person");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        edt_contact_person = (EditText) findViewById(R.id.edt_contact_person);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
        edt_vehicle = (EditText) findViewById(R.id.edt_vehicle);
        edt_license = (EditText) findViewById(R.id.edt_license);
        edt_shop = (EditText) findViewById(R.id.edt_shop);
        if (Configg.getDATA(activity, "type").equals("shop")) {
            edt_shop.setText(Configg.getDATA(activity, "shop_code_and_name"));
        }
        edt_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configg.getDATA(activity, "type").equals("admin")) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.locality_list);
                    ListView list_locality = (ListView) dialog.findViewById(R.id.list_locality);
                    ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, arrayList_shopcde_and_name);
                    list_locality.setAdapter(arrayAdapter);
                    list_locality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            edt_shop.setText(arrayList_shopcde_and_name.get(i));
                            shop_name = arrayList_shopcde_and_name.get(i);
                            sid = arrayList_sid.get(i);
                            shop_code = arrayList_shopcode.get(i);
                            dialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "cid" + cid, Toast.LENGTH_SHORT).show();

                        }
                    });

                    dialog.show();
                }
            }
        });


        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = UUID.randomUUID().toString();
                final String[] pwd_spli = pwd.split("-");
                password = pwd_spli[0];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDateTime = dateFormat.format(new Date()); // Find todays dat
                if (!signUpValidation()) {
//                    nameValuePairs.add(new BasicNameValuePair("factory_name", edt_shop_name.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("delivery_person", edt_contact_person.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("mobile", edt_mobile_no.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("email", edt_email.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("password", password.trim()));
                    nameValuePairs.add(new BasicNameValuePair("vehicle", edt_vehicle.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("license", edt_license.getText().toString().trim()));
                    if (Configg.getDATA(activity, "type").equals("shop") || Configg.getDATA(activity, "type").equals("delivery")) {
                        nameValuePairs.add(new BasicNameValuePair("sid", Configg.getDATA(activity, "sid")));
                        nameValuePairs.add(new BasicNameValuePair("shop_code_and_name", Configg.getDATA(activity, "shop_code_and_name")));
                        nameValuePairs.add(new BasicNameValuePair("shop_code", Configg.getDATA(activity, "shop_code")));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("sid", sid));
                        nameValuePairs.add(new BasicNameValuePair("shop_code_and_name", shop_name));
                        nameValuePairs.add(new BasicNameValuePair("shop_code", shop_code));
                    }

                    nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
                    System.out.println("name" + nameValuePairs.toString());
//                    if (new ConnectionDetector(CreateShop.this).isConnectingToInternet()) {
                    asyncPOST = new AsyncPOST(nameValuePairs, AddDealer.this, Configg.MAIN_URL + Configg.CREATE_DELIVERY, AddDealer.this);
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


                }


            }
        });
        volley_shop();

    }

    private boolean signUpValidation() {
//        if (edt_shop_name.getText().toString().equals("")) {
//            edt_shop_name.requestFocus();
//
//            Toast.makeText(getApplicationContext(), "Kindly Fill The Shop Name", Toast.LENGTH_SHORT).show();
//            return true;
//
//        }
        if (edt_contact_person.getText().toString().equals("")) {
            edt_contact_person.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Delivery Person Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_shop.getText().toString().equals("")) {
            edt_shop.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Select The Shop Name.", Toast.LENGTH_SHORT).show();
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
        if (edt_vehicle.getText().toString().equals("")) {
            edt_vehicle.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Dealer Vehicle Number Address.", Toast.LENGTH_SHORT).show();
            return true;

        }

        if (edt_license.getText().toString().equals("")) {
            edt_license.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Dealer Licence Number.", Toast.LENGTH_SHORT).show();
            return true;

        }

        return false;
    }

    private void volley_shop() {
        if (!(arrayList_shopcde_and_name.size() == 0)) {
            arrayList_shopcde_and_name.clear();
            arrayList_shopcode.clear();
            arrayList_sid.clear();


        }


        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Configg.MAIN_URL + Configg.GET_SHOP_LIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    HashMap<String, String> stringHashMap;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        arrayList_shopcode.add(jsonArray.getJSONObject(i).getString("shop_code"));
                        arrayList_shopcde_and_name.add(jsonArray.getJSONObject(i).getString("shop_code_and_name"));
                        arrayList_sid.add(jsonArray.getJSONObject(i).getString("sid"));


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
                Toast.makeText(getApplicationContext(), "SuccessFully Delivery Person Account Created.", Toast.LENGTH_SHORT).show();
                new SendMail(AddDealer.this, edt_email.getText().toString().trim(), "Cinderella Delivery Person Credential", "User Id:" + edt_mobile_no.getText().toString() + '\n' +
                        "Password:" + password).execute();
            }
            if (jsonObject.getString("success").equals("3")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddDealer.this);
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


}
