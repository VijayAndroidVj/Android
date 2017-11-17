package com.example.chandru.myadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.myadmin.Adapter.MainranceAdapter;
import com.example.chandru.myadmin.Base.BaseAcitivity;
import com.example.chandru.myadmin.Listener.AdapterListener;
import com.example.chandru.myadmin.Pojo.Maintain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.chainsaw.Main;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chandru on 10/13/2017.
 */

public class MaintenceActivity extends BaseAcitivity implements AdapterListener {
    private String societycodes, dataOne,three,datas, floor, flatNo, memebername, memebercode, mobile, email, squrefit, password, confirmPassword, Username;
    private int selfoccupie, parking;
    private ImageView ivSignOut;
    private PopupWindow popupWindow;
    private TextView singleView, bulkView;
    private boolean flag = true;
    private RadioButton radioButton;
    private TextView tvAddNewOne;

    private MainranceAdapter lAdapter;
    private RecyclerView recycler_view;

    private List<String> categoriesOne = new ArrayList<>();
    private List<Maintain> maintain = new ArrayList<>();
    private  String[] spinnerArray ;
    private  HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
    private EditText searchMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        searchMain = (EditText)findViewById(R.id.searchMain);
       ImageView logout = (ImageView) findViewById(R.id.mainLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Int_login = new Intent(MaintenceActivity.this,LoginActivity.class);
                startActivity(Int_login);
            }
        });


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        societycodes = pref.getString("societycode", null);

        String Url = "http://52.66.129.48/TUS_SERVICE/Service1.svc/maintanceentry/" + societycodes;
        new getSpinnervalue().execute(Url);


        tvAddNewOne = (TextView) findViewById(R.id.tvAddNewOne);
        tvAddNewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag =true;
                LoginPopup();

            }
        });

        addTextListenerOne();


    }


    @Override
    public void adapterActionListener(int state, Object data) {
        if (state == MainranceAdapter.LIST_TAGs && data != null) {
            int pois = (int) data;

            selfoccupie=pois;
            flag=false;
            LoginPopup();



            /*Intent gerDetails = new Intent(MainActivity.this, ManagementLoginActivity.class);
            gerDetails.putExtra("LoginTag", "0");
            gerDetails.putExtra("mylist", (Serializable) list.get(pois));
            startActivity(gerDetails);*/
        }
    }

    public void LoginPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.loginpopup, null);

        final Spinner spPopupUsername = (Spinner) popupView.findViewById(R.id.spPopupUsername);
        final Spinner spname = (Spinner) popupView.findViewById(R.id.spinnertwo);
        final TextView txCobtri = (TextView) popupView.findViewById(R.id.textVs);
        //final EditText etPopupUsername = (EditText) popupView.findViewById(R.id.etPopupUsername);
        final EditText etPopupPassword = (EditText) popupView.findViewById(R.id.etPopupPassword);
        //final EditText etPopupConfirmPassword = (EditText) popupView.findViewById(R.id.etPopupConfirmPassword);
        Button btnPopupLogin = (Button) popupView.findViewById(R.id.btnPopupLogin);
        if (flag==false){
            spPopupUsername.setVisibility(View.GONE);
            txCobtri.setVisibility(View.VISIBLE);

            String one =maintain.get(selfoccupie).getMaintancename();
            String two =maintain.get(selfoccupie).getAmount();
             three =maintain.get(selfoccupie).getMaintancecode();
            txCobtri.setText(one);
            etPopupPassword.setText(two, TextView.BufferType.EDITABLE);

        }else {
            spPopupUsername.setVisibility(View.VISIBLE);
            txCobtri.setVisibility(View.GONE);

        }


        ArrayAdapter<String> dataAdapters = new ArrayAdapter<String>(MaintenceActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
        dataAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPopupUsername.setAdapter(dataAdapters);

        final List<String> categosOne = new ArrayList<>();
        categosOne.add("Select");
        categosOne.add("Flat");
        categosOne.add("Squareft");
        ArrayAdapter<String> dataAdaptersOne = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categosOne);
        dataAdaptersOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spname.setAdapter(dataAdaptersOne);


        btnPopupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = etPopupPassword.getText().toString();
                String occupier = String.valueOf(spname.getSelectedItem());
                String contrubutename  ;
                //http://52.66.129.48/TUS_SERVICE/Service1.svc/maintanceinsentry/1234/310103/S/640/0


                if (flag==true){
                    String name = spPopupUsername.getSelectedItem().toString();
                    contrubutename = spinnerMap.get(spPopupUsername.getSelectedItemPosition());
                    System.out.println(contrubutename);
                    // contrubutename  = String.valueOf(spPopupUsername.getSelectedItem());


                }else {

                    contrubutename=three;

                }

                if(occupier == "Select"){
                    Toast.makeText(MaintenceActivity.this, "Please select State", Toast.LENGTH_SHORT).show();
                }else {
                    if(occupier =="Flat" ){
                        occupier = "F";
                    }else {
                        occupier = "S";
                    }

                    /*for(int i=0;i<categoriesOne.size();i++){
                        if(categoriesOne.get(i).contains(contrubutename)){
                            contrubutename =categoriesOne.get(i);
                        }
                    }*/

                    popupWindow.dismiss();
                    String Url = "http://52.66.129.48/TUS_SERVICE/Service1.svc/maintanceinsentry/"+societycodes+"/"+contrubutename+"/"+occupier+"/"+password+"/0";
                    //String ork ="http://52.66.129.48/TUS_SERVICE/Service1.svc/maintanceinsentry/1234/310103/S/640/0";
                    new serverUpload().execute(Url);
                }





            }
        });
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(MaintenceActivity.this, R.color.transparent)));
        popupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.CENTER, 0, 410);
    }


    private class getSpinnervalue extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MaintenceActivity.this);
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
                dataOne = (EntityUtils.toString(entity));

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
                JSONObject jsono = new JSONObject(dataOne);
                // String Razor_pay_key=jsono.getString("status");
                String msg = jsono.getString("message");
                // String societycode =jsono.getString("societycode");
                // String societystatus =jsono.getString("societystatus");
                System.out.println("Razor_pay_key---" + msg);
                if (msg.equals("success")) {

                    JSONArray jsonArray = jsono.getJSONArray("newmaintance");

                    spinnerArray = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject js = jsonArray.getJSONObject(i);
                        spinnerMap.put(i,js.getString("maintancecode"));
                        spinnerArray[i] = js.getString("maintancename");
                    }





                    JSONArray jsonOneArray = jsono.getJSONArray("entrymaintance");
                    for (int i = 0; i < jsonOneArray.length(); i++) {
                        JSONObject jss = jsonOneArray.getJSONObject(i);

                       /* db.addImage(
                                js.getString("maintancecode"),
                                js.getString("maintancename")
                        );*/

                        Maintain dboard = new Maintain();
                        dboard.setAmount(jss.getString("amount"));
                        dboard.setMaintancename(jss.getString("maintancename"));
                        dboard.setMaintancecode(jss.getString("maintancecode"));
                        dboard.setMaintancetype(jss.getString("maintancetype"));

                        maintain.add(dboard);


                    }


                    recycler_view = (RecyclerView) findViewById(R.id.recycler_view_one);

                    // mylist = getIntent().getExtras().getParcelableArrayList("mylist");

                    LinearLayoutManager lmanager = new LinearLayoutManager(MaintenceActivity.this);
                    recycler_view.setLayoutManager(lmanager);

                    lAdapter = new MainranceAdapter(maintain,MaintenceActivity.this,MaintenceActivity.this);
                    recycler_view.setAdapter(lAdapter);
                    lAdapter.notifyDataSetChanged();




                   /* JSONArray jsonArrayOne = jsono.getJSONArray("entrymaintance");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js =jsonArray.getJSONObject(i);

                       *//* db.addImage(
                                js.getString("maintancecode"),
                                js.getString("maintancename")
                        );*//*
                       // categoriesOne.add( js.getString("maintancename"));


                    }*/


                } else {
                    Toast.makeText(MaintenceActivity.this, msg, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


    private class serverUpload extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MaintenceActivity.this);
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


                HttpEntity entity = response.getEntity();
                dataOne = (EntityUtils.toString(entity));


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
                JSONObject jsono = new JSONObject(dataOne);
                // String Razor_pay_key=jsono.getString("status");
                String msg = jsono.getString("message");
                // String societycode =jsono.getString("societycode");
                // String societystatus =jsono.getString("societystatus");
                System.out.println("Razor_pay_key---" + msg);
                if (msg.equals("success")) {
                    maintain.clear();
                    JSONArray jsonOneArray = jsono.getJSONArray("entrymaintance");
                    for (int i = 0; i < jsonOneArray.length(); i++) {
                        JSONObject jss = jsonOneArray.getJSONObject(i);

                       /* db.addImage(
                                js.getString("maintancecode"),
                                js.getString("maintancename")
                        );*/

                        Maintain dboard = new Maintain();
                        dboard.setAmount(jss.getString("amount"));
                        dboard.setMaintancename(jss.getString("maintancename"));
                        dboard.setMaintancecode(jss.getString("maintancecode"));
                        dboard.setMaintancetype(jss.getString("maintancetype"));

                        maintain.add(dboard);


                    }


                    recycler_view = (RecyclerView) findViewById(R.id.recycler_view_one);

                    // mylist = getIntent().getExtras().getParcelableArrayList("mylist");

                    LinearLayoutManager lmanager = new LinearLayoutManager(MaintenceActivity.this);
                    recycler_view.setLayoutManager(lmanager);

                    lAdapter = new MainranceAdapter(maintain,MaintenceActivity.this,MaintenceActivity.this);
                    recycler_view.setAdapter(lAdapter);
                    lAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(MaintenceActivity.this, msg, Toast.LENGTH_LONG).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        }


    public void addTextListenerOne(){

        searchMain.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<Maintain> filteredList = new ArrayList<>();

                for (int i = 0; i < maintain.size(); i++) {

                    final String text = maintain.get(i).getMaintancename().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(maintain.get(i));
                    }
                }

                recycler_view.setLayoutManager(new LinearLayoutManager(MaintenceActivity.this));
                lAdapter = new MainranceAdapter(filteredList, MaintenceActivity.this, MaintenceActivity.this);
                recycler_view.setAdapter(lAdapter);
                lAdapter.notifyDataSetChanged();
            }
        });
    }



}
