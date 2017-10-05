package coustomer.cinderella.vajra.cinderellacustomerapp;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import common.AsyncPOST;
import common.Config;
import common.Response;

public class SignUP extends AppCompatActivity implements Response {
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs_signup = new ArrayList<NameValuePair>();
    private EditText edt_name;
    private EditText edt_mobile;
    private EditText edt_email;
    private EditText edt_pwd;
    private EditText edt_con_pwd;
    private EditText edt_address;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cinderella-SignUp");

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
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signUpValidation()) {
                    nameValuePairs_signup.add(new BasicNameValuePair("name", edt_name.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("mobile", edt_mobile.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("email", edt_email.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("pwd", edt_pwd.getText().toString().trim()));
                    nameValuePairs_signup.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));


                    System.out.println("signup_params" + nameValuePairs_signup.toString());

                    asyncPOST = new AsyncPOST(nameValuePairs_signup, SignUP.this, common.Config.MAIN_URL + common.Config.SIGNUP_CUSTOMER, SignUP.this);
                    asyncPOST.execute();

                }

            }
        });
    }

    private boolean signUpValidation() {
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
        if (edt_email.getText().toString().equals("")) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill Your E-Mail.", Toast.LENGTH_SHORT).show();
            return true;

        }


        if (!Config.isValidEmaillId(edt_email.getText().toString().trim())) {
            edt_email.requestFocus();
            Toast.makeText(getApplicationContext(), "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (edt_address.getText().toString().equals("")) {
            edt_mobile.requestFocus();
            Toast.makeText(getApplicationContext(), "Address Should Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_pwd.getText().toString().equals("")) {
            edt_pwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Password Should Not Empty.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_con_pwd.getText().toString().equals("")) {
            edt_con_pwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Confirm Your Password.", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (!edt_con_pwd.getText().toString().equals(edt_pwd.getText().toString())) {
            edt_pwd.requestFocus();
            Toast.makeText(getApplicationContext(), "Password Doesn't Match With Confirm Password.", Toast.LENGTH_SHORT).show();
            return true;

        }
        return false;
    }

    @Override
    public void processFinish(String output) {

        try {
            JSONObject jsonObject=new JSONObject(output);
            if (jsonObject.getString("success").equals("2")){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUP.this);
                builder.setMessage(jsonObject.getString("message")).setTitle("Response From Cinderella.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                alert.setCancelable(false);
            }
            else if (jsonObject.getString("success").equals("1")){
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SignUP.this);
                builder.setMessage(jsonObject.getString("message")).setTitle("Response From Cinderella.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                                finish();
                            }
                        });
                android.app.AlertDialog alert = builder.create();
                alert.show();
                alert.setCancelable(false);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
