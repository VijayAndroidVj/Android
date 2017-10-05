package cinderellaadmin.vajaralabs.com.admin;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import common.AsyncPOST;
import common.Configg;
import common.Response;

public class AddLocality extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private Button btn_submit;
    private EditText edt_city, edt_locality;
    private AddLocality activity;
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> city_id = new ArrayList<String>();
    private String cid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_locality);
        activity = this;
        btn_submit = (Button) findViewById(R.id.btn_submit);
        edt_city = (EditText) findViewById(R.id.edt_city);
        edt_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.locality_list);
                ListView list_locality = (ListView) dialog.findViewById(R.id.list_locality);
                ArrayAdapter arrayAdapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, city_list);
                list_locality.setAdapter(arrayAdapter);
                list_locality.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        edt_city.setText(city_list.get(i));
                        cid = city_id.get(i);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        edt_locality = (EditText) findViewById(R.id.edt_locality);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edt_city.getText().toString().equals("")) {
                    if (!edt_locality.getText().toString().equals("")) {
                        nameValuePairs.add(new BasicNameValuePair("city", edt_city.getText().toString().trim()));
                        nameValuePairs.add(new BasicNameValuePair("cid", cid.trim()));

                        nameValuePairs.add(new BasicNameValuePair("locality", edt_locality.getText().toString().trim()));
                        asyncPOST = new AsyncPOST(nameValuePairs, AddLocality.this, Configg.MAIN_URL + Configg.ADD_LOCALITY, AddLocality.this);
                        asyncPOST.execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "Locality Should Not Empty.", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "City Should Not Empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        volley_city();
    }

    private void volley_city() {
        if (!(city_list.size() == 0)) {
            city_list.clear();
            city_id.clear();

        }


        RequestQueue queue = Volley.newRequestQueue(activity);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        pDialog.setCancelable(false);
        StringRequest request = new StringRequest(Request.Method.GET, Configg.MAIN_URL + Configg.GET_CITY_LIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    HashMap<String, String> stringHashMap;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        city_id.add(jsonArray.getJSONObject(i).getString("cid"));
                        city_list.add(jsonArray.getJSONObject(i).getString("city"));

                    }
                    if (city_id.size()==0){
                        Configg.alert("There's No City.","Kindly Add The City In City Zone Than Add Locality.",2,activity);
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
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(output);
            if (jsonObject.getString("success").equals("3")) {
                Configg.alert("Response From Server.", jsonObject.getString("message"), 1, AddLocality.this);


            } else if (jsonObject.getString("success").equals("1")) {

                Configg.alert("Response From Server.", jsonObject.getString("message"), 2, AddLocality.this);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
