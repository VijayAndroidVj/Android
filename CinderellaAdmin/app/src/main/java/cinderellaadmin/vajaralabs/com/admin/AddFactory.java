package cinderellaadmin.vajaralabs.com.admin;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import common.AsyncPOST;
import common.Configg;
import common.Response;
import common.SendMail;

public class AddFactory extends AppCompatActivity implements Response{

    EditText edt_shop_name, edt_contact_person, edt_mobile_no, edt_email, edt_address;
    Button btn_submit;
    private AsyncPOST asyncPOST;
    private List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
    private String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_factory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add-Factory Person");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        edt_shop_name = (EditText) findViewById(R.id.edt_shop_name);
        edt_contact_person = (EditText) findViewById(R.id.edt_contact_person);
        edt_mobile_no = (EditText) findViewById(R.id.edt_mobile_no);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_address = (EditText) findViewById(R.id.edt_address);
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
                    nameValuePairs.add(new BasicNameValuePair("factory_name", edt_shop_name.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("contact_person", edt_contact_person.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("mobile", edt_mobile_no.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("email", edt_email.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("address", edt_address.getText().toString().trim()));
                    nameValuePairs.add(new BasicNameValuePair("password", password.trim()));

                    nameValuePairs.add(new BasicNameValuePair("mobile_date", currentDateTime));
//                    if (new ConnectionDetector(CreateShop.this).isConnectingToInternet()) {
                    asyncPOST = new AsyncPOST(nameValuePairs, AddFactory.this, Configg.MAIN_URL + Configg.CREATE_FACTORY, AddFactory.this);
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


    }

    private boolean signUpValidation() {
        if (edt_shop_name.getText().toString().equals("")) {
            edt_shop_name.requestFocus();

            Toast.makeText(getApplicationContext(), "Kindly Fill The Factory Name", Toast.LENGTH_SHORT).show();
            return true;

        }
        if (edt_contact_person.getText().toString().equals("")) {
            edt_contact_person.requestFocus();
            Toast.makeText(getApplicationContext(), "Kindly Fill The Contact Name", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"SuccessFully Factory Person Account Created.",Toast.LENGTH_SHORT).show();
                new SendMail(AddFactory.this, edt_email.getText().toString().trim(), "Cinderella Factory Person Credential", "User Id:" + edt_mobile_no.getText().toString() + '\n' +
                        "Password:" + password).execute();
            }
            if (jsonObject.getString("success").equals("3")) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddFactory.this);
                builder.setMessage(jsonObject.getString("message")).setTitle("Response From Server")
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
