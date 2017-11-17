package com.example.chandru.myadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.myadmin.Base.BaseAcitivity;
import com.example.chandru.myadmin.Pojo.DashBoard;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ManagementLoginActivity extends BaseAcitivity implements AdapterView.OnItemSelectedListener {
    private Button btnSubmitManagement, btnClearManagement, btnCancelManagement;
    private String value;
    private List<DashBoard> listOne = new ArrayList<>();
    private EditText etFloorNo;
    private EditText etCreateBy;
    private EditText etMemberCode;
    private EditText etFlatNo; private EditText etMemberName; private EditText etMobile; private EditText etEmail; private EditText etArea;
    private Spinner spinner, spinnerOne;
    private String societycode,data,floor, flatNo, memebername, memebercode, mobile, update,email, squrefit, password, confirmPassword, Username;
    private int selfoccupie, parking;
    private ImageView ivSignOut;
    private PopupWindow popupWindow;
    private TextView singleView,bulkView ;
    private boolean flag =true;
    private  RadioButton radioActive,radioDeactive;
    private ArrayAdapter<String> dataAdapter;
    private  RadioGroup rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management_login);
        btnSubmitManagement = (Button) findViewById(R.id.btnSubmitManagement);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinnerOne = (Spinner) findViewById(R.id.spinerOne);
        etFloorNo = (EditText) findViewById(R.id.etFloorNo);
        etFlatNo = (EditText) findViewById(R.id.etFlatNo);
        etMemberName = (EditText) findViewById(R.id.etMemberName);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etArea = (EditText) findViewById(R.id.etArea);
        etCreateBy = (EditText) findViewById(R.id.etCreateBy);
        etMemberCode= (EditText) findViewById(R.id.etMemberCode);
        ivSignOut = (ImageView) findViewById(R.id.ivSignOut);
        singleView = (TextView)findViewById(R.id.singleView);
        bulkView = (TextView)findViewById(R.id.bulkView);

         rg = (RadioGroup)findViewById(R.id.radioGroup);
        radioActive = (RadioButton) findViewById(R.id.radioActive);
        radioDeactive = (RadioButton) findViewById(R.id.radioDeactive);

        final Intent loginTag = getIntent();
        value = loginTag.getStringExtra("LoginTag");

        if(value.equals("0")){
          //  spinner.setSelection(2);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

            societycode =  pref.getString("societycode", null);
            String responceOne =  pref.getString("responceOne", null);

            try {
                JSONObject jsa = new JSONObject(responceOne);
                etFlatNo.setText(jsa.getString("FLAT_NO"));
                etFloorNo.setText(jsa.getString("FLOOR_NO"));
                etMemberName.setText(jsa.getString("MEMBER_NAME"));
                etMemberCode.setText(jsa.getString("MEMBER_CODE"));
                etMobile.setText(jsa.getString("MOBILE_NO"));
                etEmail.setText(jsa.getString("EMAIL_ID"));
                etArea.setText(jsa.getString("FLAT_AREA"));
                update =jsa.getString("Userid");

                String redi =jsa.getString("STATUS");
                if (redi.equals("0")){
                    radioActive.setChecked(true);
                }else {
                    radioDeactive.setChecked(true);
                }




               // ((RadioButton) rg.getChildAt(2)).setText("De-active");




              // String occupied =jsa.getString("SELF_OCCUPIED");
                String occupied ="No";
                spinner.setSelection(1);


                for(int i = 0; i < spinner.getCount(); i++){
                    if(spinner.getItemAtPosition(i).toString().equals(occupied)){
                        spinner.setSelection(i);
                        break;
                    }
                }


              /*  List<String> categories = new ArrayList<String>();
                categories.add("Yes");
                categories.add("No");
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);*/

                   // int spinnerPosition = dataAdapter.getPosition(occupied);
             //   System.out.println(occupied);
              //  System.out.println(spinnerPosition);

                    spinner.setSelection(1);
                    spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(this);

                // dataAdapter.notifyDataSetChanged();
                  // spinner.setSelection(1);
                    //String.valueOf(spinner.getSelectedItem());


                String parking =jsa.getString("NO_OF_PARKING");
                if(parking.equals("owner")){
                   // spinnerOne.setSelection(1);
                }else {
                   // spinnerOne.setSelection(2);
                }

                //spinnerOne.setSelection(2);

                /*dboard.setFLAT_AREA(jsa.getString("FLAT_AREA"));
                dboard.setFLAT_NO(jsa.getString("FLAT_NO"));
                dboard.setFLOOR_NO(jsa.getString("FLOOR_NO"));
                dboard.setMEMBER_CODE( jsa.getString("MEMBER_CODE"));
                dboard.setMEMBER_NAME(jsa.getString("MEMBER_NAME"));
                dboard.setMOBILE_NO(jsa.getString("MOBILE_NO"));
                dboard.setNO_OF_PARKING(jsa.getString("NO_OF_PARKING"));
                dboard.setSELF_OCCUPIED(jsa.getString("SELF_OCCUPIED"));
                dboard.setSOCIETY_CODE(jsa.getString("SOCIETY_CODE"));
                dboard.setSTATUS(jsa.getString("STATUS"));*/
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }else {

        }
















        btnClearManagement = (Button) findViewById(R.id.btnClearManagement);
        btnCancelManagement = (Button) findViewById(R.id.btnCancelManagement);


        /*RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int selectedId=radioGroup.getCheckedRadioButtonId();
         radioButton = (RadioButton) findViewById(selectedId);*/


        btnCancelManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signout = new Intent(ManagementLoginActivity.this, LoginActivity.class);
                startActivity(signout);
            }
        });

        btnClearManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                floor = "";
                flatNo = "";
                memebername = "";
                mobile = "";
                email = "";
                squrefit = "";
                memebercode = "";

                etFloorNo.setText(floor);
                etFlatNo.setText(flatNo);
                etMemberName.setText(memebername);
                etMobile.setText(mobile);
                etEmail.setText(email);
                etArea.setText(squrefit);
                etMemberCode.setText(memebercode);

            }
        });

/*

        singleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent singel = new Intent(ManagementLoginActivity.this, LoginActivity.class);
                //startActivity(singel);
            }
        });*/

        bulkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singel = new Intent(ManagementLoginActivity.this, bulkViewActivity.class);
                startActivity(singel);
            }
        });


        //listOne = getIntent().getParcelableExtra("mylist");
        // listOne = (List<DashBoard>) getIntent().getSerializableExtra("mylist");
      /*  Intent i = getIntent();
        listOne = (ArrayList<DashBoard>) i.getSerializableExtra("mylist");*/


        spinner.setOnItemSelectedListener(this);
        spinnerOne.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Select");
        categories.add("Yes");
        categories.add("No");
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);



        List<String> categoriesOne = new ArrayList<>();
        categoriesOne.add("Select");
        categoriesOne.add("OutSide");
        categoriesOne.add("Owner");
        ArrayAdapter<String> dataAdapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoriesOne);
        dataAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOne.setAdapter(dataAdapters);

       // listOne = getIntent().getExtras().getParcelableArrayList("mylist");



        /*if (value.equals("1")) {
            btnSubmitManagement.setText("Log in");
        } else {*/
           /* if (listOne != null) {

                floor = listOne.get(0).getFLOOR_NO();
                flatNo = listOne.get(0).getFLAT_NO();
                memebername = listOne.get(0).getMEMBER_NAME();
                mobile = listOne.get(0).getMOBILE_NO();
                email = listOne.get(0).getEMAIL_ID();
                selfoccupie = Integer.valueOf(listOne.get(0).getSELF_OCCUPIED());
                squrefit = listOne.get(0).getFLAT_AREA();
                parking = Integer.valueOf(listOne.get(0).getNO_OF_PARKING());

                btnSubmitManagement.setText("Submit");
                etFloorNo.setText(floor);
                etFlatNo.setText(flatNo);
                etMemberName.setText(memebername);
                etMobile.setText(mobile);
                etEmail.setText(email);
                etArea.setText(squrefit);
                spinner.setSelection(2);
                spinnerOne.setSelection(2);

            }*/

      //  }


        btnSubmitManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String active = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();


               // String active =(String)  radioButton.getText();

                String activeInt ;
                if(active.equals("Active")){
                    activeInt = "0";
                }else {
                    activeInt = "1";
                }
                 memebercode = etMemberCode.getText().toString();
                flatNo = etFlatNo.getText().toString();
                floor = etFloorNo.getText().toString();
                memebername = etMemberName.getText().toString();
                mobile = etMobile.getText().toString();
                email = etEmail.getText().toString();
                squrefit = etArea.getText().toString();
                String occupier = String.valueOf(spinner.getSelectedItem());
                String park = String.valueOf(spinnerOne.getSelectedItem());
                String Create =etCreateBy.getText().toString();

                if (value.equals("1")) {
                   // LoginPopup();
                    //52.66.129.48/TUS_SERVICE/Service1.svc/societyuserREG/1234/12/Ravi/100/100/9443079499/chandruviswachan@@gmail.com/yes/100/owner/0/test/insert/aravind
                    String Url ="http://52.66.129.48/TUS_SERVICE/Service1.svc/societyuserREG/"+societycode+"/"+memebercode+"/"+memebername+"/"+flatNo+"/"+floor+"/"+mobile+"/"+email+"/"+occupier.toLowerCase()+"/"+squrefit+"/"+park.toString()+"/"+activeInt+"/"+Create+"/insert/"+email;
                    new server().execute(Url);
                } else {
                    String Url1 ="http://52.66.129.48/TUS_SERVICE/Service1.svc/societyuserREG/"+societycode+"/"+memebercode+"/"+memebername+"/"+flatNo+"/"+floor+"/"+mobile+"/"+email+"/"+occupier.toLowerCase()+"/"+squrefit+"/"+park.toString()+"/"+activeInt+"/"+Create+"/update/"+update;
                    new server().execute(Url1);
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) view).setTextColor(ContextCompat.getColor(this,R.color.black));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


  /*  public void LoginPopup() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.loginpopup, null);

       // final EditText etPopupUsername = (EditText) popupView.findViewById(R.id.etPopupUsername);
        final EditText etPopupPassword = (EditText) popupView.findViewById(R.id.etPopupPassword);
        final EditText etPopupConfirmPassword = (EditText) popupView.findViewById(R.id.etPopupConfirmPassword);
        Button btnPopupLogin = (Button) popupView.findViewById(R.id.btnPopupLogin);

        btnPopupLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = etPopupPassword.getText().toString();
                confirmPassword = etPopupConfirmPassword.getText().toString();
                Username = etPopupUsername.getText().toString();
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(ManagementLoginActivity.this, "Password dose not match", Toast.LENGTH_SHORT).show();
                } else {
                    popupWindow.dismiss();
                    String Url ="http://52.66.129.48/TUS_SERVICE/Service1.svc/societyuserREG/"+societycode+"/12/Ravi/100/100/9443079499/chandruviswachan@@gmail.com/yes/100/owner/0/test/insert/aravind"+"/"+password+"/";
                    new server().execute(Url);
                }

            }
        });
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(ManagementLoginActivity.this, R.color.transparent)));
        popupWindow.showAtLocation(popupView, Gravity.TOP | Gravity.CENTER, 0, 210);
    }*/

   /* public void postUserDetails() {
        flatNo = etFlatNo.getText().toString();
        floor = etFloorNo.getText().toString();
        memebername = etMemberName.getText().toString();
        mobile = etMobile.getText().toString();
        email = etEmail.getText().toString();
        squrefit = etArea.getText().toString();

        String urlFormat = flatNo + "/" + floor + "/" + memebername + "/" + "/" + mobile + "/" + email + "/" + squrefit + "/" + Username + "/" + password;
        Interface request = RetrofitApiCall.getClient().create(Interface.class);
        Call<LoginRespose> calls = request.postDetails(urlFormat);
        calls.enqueue(new Callback<LoginRespose>() {
            @Override
            public void onResponse(Call<LoginRespose> call, Response<LoginRespose> response) {
                String messgae = response.body().getMessage();
                if (messgae.equals("Success")) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginRespose> call, Throwable t) {

            }
        });

    }*/

    private class server extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(ManagementLoginActivity.this);
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
                String Razor_pay_key=jsono.getString("status");
                String msg=jsono.getString("message");
               // String societycode =jsono.getString("societycode");
               // String societystatus =jsono.getString("societystatus");
                System.out.println("Razor_pay_key---" + Razor_pay_key);
                if(Razor_pay_key.equals("true"))
                {
                    //getApplicationContext().getSharedPreferences("MyPref", 0).edit().clear().commit();

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editors = pref.edit();
                    editors.putString("responce",jsono.toString());
                    editors.apply();


                    Intent Int_login = new Intent(ManagementLoginActivity.this,MainActivity.class);
                    startActivity(Int_login);
                }else
                {
                    Toast.makeText(ManagementLoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }



            }catch (JSONException e) {
                e.printStackTrace();
            }

            //}
        }


    }
}
