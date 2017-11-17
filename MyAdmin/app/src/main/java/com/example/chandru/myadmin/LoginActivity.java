package com.example.chandru.myadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chandru.myadmin.Base.BaseAcitivity;
import com.example.chandru.myadmin.Pojo.DashBoard;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class LoginActivity extends BaseAcitivity {
    private ImageView ivIcon;
    private EditText etUserName, etPassword, etSoceityCode;
    private Button btnSubmit;
    private RelativeLayout rlUserDetails;
    private ArrayList<DashBoard> lists = new ArrayList<>();
    private String data, username, password, message, socitycoded, societyStatus;
    private boolean sociatytag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy ourPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(ourPolicy);
        InitilizeViews();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUserName.getText().toString();
                password = etPassword.getText().toString();
                if (sociatytag == false) {
                    socitycoded = etSoceityCode.getText().toString();
                }

                String Url = "http://52.66.129.48/TUS_SERVICE/Service1.svc/societyLogin/" + username + "/" + password + "/" + socitycoded;
                new login().execute(Url);
              /*  getResponse();

                if (message.equals("true")){
                    Intent menus = new Intent(LoginActivity.this, MainActivity.class);
                    menus.putExtra("Dashboard",lists);
                    startActivity(menus);
                }else {
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }*/

            }
        });
        /*findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    public void InitilizeViews() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        String sociatyCode = pref.getString("societycode", null);
        System.out.println(sociatyCode);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etSoceityCode = (EditText) findViewById(R.id.etSoceityCode);
        rlUserDetails = (RelativeLayout) findViewById(R.id.rlUserDetails);
        if (sociatyCode != null) {
            sociatytag = true;
            etSoceityCode.setVisibility(View.GONE);
            socitycoded = sociatyCode;
        }


        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            leftRightSpace = (int) (width * 0.0153);
            topBottomSpace = (int) (height * 0.0089);
            LinearLayout.LayoutParams ivIconParams = (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
            ivIconParams.setMargins(0, topBottomSpace * 30, 0, 0);
            ivIcon.setLayoutParams(ivIconParams);

            LinearLayout.LayoutParams etUserNameParams = (LinearLayout.LayoutParams) etUserName.getLayoutParams();
            etUserNameParams.height = topBottomSpace * 7;
            etUserNameParams.width = leftRightSpace * 55;
            etUserName.setCompoundDrawablePadding(5);
            etUserName.setLayoutParams(etUserNameParams);

            LinearLayout.LayoutParams etPasswordParams = (LinearLayout.LayoutParams) etPassword.getLayoutParams();
            etPasswordParams.height = topBottomSpace * 7;
            etPasswordParams.width = leftRightSpace * 55;
            etUserName.setCompoundDrawablePadding(2);
            etPassword.setLayoutParams(etPasswordParams);

            LinearLayout.LayoutParams btnSubmitParams = (LinearLayout.LayoutParams) btnSubmit.getLayoutParams();
            btnSubmitParams.setMargins(0, topBottomSpace * 5, 0, 0);
            btnSubmitParams.height = topBottomSpace * 7;
            btnSubmitParams.width = leftRightSpace * 20;
            btnSubmit.setLayoutParams(btnSubmitParams);

            LinearLayout.LayoutParams rlUserDetailsParams = (LinearLayout.LayoutParams) rlUserDetails.getLayoutParams();
            rlUserDetailsParams.setMargins(leftRightSpace, topBottomSpace * 6, leftRightSpace, 0);
            rlUserDetails.setLayoutParams(rlUserDetailsParams);

        } else {
            leftRightSpace = (int) (height * 0.0153);
            topBottomSpace = (int) (width * 0.0089);
            LinearLayout.LayoutParams ivIconParams = (LinearLayout.LayoutParams) ivIcon.getLayoutParams();
            ivIconParams.setMargins(0, topBottomSpace * 3, 0, 0);
            ivIcon.setLayoutParams(ivIconParams);

            LinearLayout.LayoutParams etUserNameParams = (LinearLayout.LayoutParams) etUserName.getLayoutParams();
            etUserNameParams.height = topBottomSpace * 7;
            etUserNameParams.width = leftRightSpace * 55;
            etUserName.setCompoundDrawablePadding(5);
            etUserName.setLayoutParams(etUserNameParams);

            LinearLayout.LayoutParams etPasswordParams = (LinearLayout.LayoutParams) etPassword.getLayoutParams();
            etPasswordParams.height = topBottomSpace * 7;
            etPasswordParams.width = leftRightSpace * 55;
            etUserName.setCompoundDrawablePadding(2);
            etPassword.setLayoutParams(etPasswordParams);

            LinearLayout.LayoutParams btnSubmitParams = (LinearLayout.LayoutParams) btnSubmit.getLayoutParams();
            btnSubmitParams.setMargins(0, topBottomSpace * 5, 0, 0);
            btnSubmitParams.height = topBottomSpace * 7;
            btnSubmitParams.width = leftRightSpace * 20;
            btnSubmit.setLayoutParams(btnSubmitParams);

            LinearLayout.LayoutParams rlUserDetailsParams = (LinearLayout.LayoutParams) rlUserDetails.getLayoutParams();
            rlUserDetailsParams.setMargins(leftRightSpace, topBottomSpace * 1, leftRightSpace, 0);
            rlUserDetails.setLayoutParams(rlUserDetailsParams);
        }
    }

    public void getResponse() {
       /* String Url =username+"/"+password+"/"+socitycoded;
        Interface request = RetrofitApiCall.getClient().create(Interface.class);
        Call<LoginRespose>call =request.getRespose(Url);
        call.enqueue(new Callback<LoginRespose>() {
            @Override
            public void onResponse(Call<LoginRespose> call, Response<LoginRespose> response) {
                lists= (ArrayList<DashBoard>) response.body().getDashboardlist();
                 message=response.body().getMessage();
                societyStatus = response.body().getSocietystatus();
            }

            @Override
            public void onFailure(Call<LoginRespose> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });*/

       /* OkHttpCxlient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                        Credentials.basic("aUsername", "aPassword"));

                Request newRequest = builder.build();
                return chain.proceed(newRequest);
            }
        }).build();*/


    }


    private class login extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading in.., please wait");
            dialog.setTitle("");
            // dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
            dialog.show();
            dialog.setCancelable(false);


        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {

                // ------------------>>
                HttpGet httppost = new HttpGet(params[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

               /* StatusLine stat = response.getStatusLine();
                String status = String.valueOf(response.getStatusLine().getStatusCode());
                if (status == "true"){*/
                HttpEntity entity = response.getEntity();
                data = (EntityUtils.toString(entity));

                // }
               /* if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);
                    System.out.println("razorurl---" + url);
                    System.out.println("data---" + data);

                    JSONObject jsono = new JSONObject(data);
                  *//*  JSONArray jarray = jsono.getJSONArray("Response");
                    for (int i = 0; i < jarray.length(); i++) {
                        object = jarray.getJSONObject(i);
                        response_code = object.getString("response_code");
                        response_msg = object.getString("response_msg");
                    }
*//*

                }*/
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dialog.dismiss();

            //    if (response_code.equals("1")) {
            try {
                JSONObject jsono = new JSONObject(data);
                String Razor_pay_key = jsono.getString("status");
                String msg = jsono.getString("message");
                String societycode = jsono.getString("societycode");
                String societystatus = jsono.getString("societystatus");
                System.out.println("Razor_pay_key---" + Razor_pay_key);
                if (Razor_pay_key.equals("true")) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("societycode", societycode);  // Saving string
                    editor.putString("responce", jsono.toString());
                    DatabaseHandler db = new DatabaseHandler(LoginActivity.this);

                    editor.commit();

                    JSONObject jsonObj = new JSONObject(data);

                    JSONArray jsonArray = jsonObj.getJSONArray("dashboardlist");
                    ;
                    /*if (jsonArray.length() == 0) {
                        return 0;
                    };*/

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);

                        db.addImage(
                                js.getString("EMAIL_ID"),
                                js.getString("FLAT_AREA"),
                                js.getString("FLAT_NO"),
                                js.getString("FLOOR_NO"),
                                js.getString("MEMBER_CODE"),
                                js.getString("MEMBER_NAME"),
                                js.getString("MOBILE_NO"),
                                js.getString("NO_OF_PARKING"),
                                js.getString("SELF_OCCUPIED"),
                                js.getString("SOCIETY_CODE"),
                                js.getString("STATUS")
                        );


                        // dash.put("EMAIL_ID", jsonObj.getString("EMAIL_ID"));


                       /* args.put("colOne", jsonobject.getInt("colOne"));
                        args.put("colTwo", jsonobject.getString("colTwo"));
*/
                        //  db.insert("dashboardlist", null, dash);

                    }


                    Intent Int_login = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(Int_login);
                } else {
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            //}
        }


    }
}
