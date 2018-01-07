package com.peeyemcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.peeyem.app.R;

public class Service extends AppCompatActivity {
    String str = "my string \n my other string";
    Spinner serviceType, carModel;
    EditText regNum, mileage, serviceDate, fullName, mobNum, emailID, state, city, description;
    CheckBox home_pickup;
    RelativeLayout submit, reset;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        regNum = (EditText) findViewById(R.id.reg_number);
        mileage = (EditText) findViewById(R.id.mileage);
        serviceDate = (EditText) findViewById(R.id.service_date);
        fullName = (EditText) findViewById(R.id.full_name);
        mobNum = (EditText) findViewById(R.id.mobile_num);
        emailID = (EditText) findViewById(R.id.email_id);
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        description = (EditText) findViewById(R.id.description);

        serviceType = (Spinner) findViewById(R.id.service_type);
        carModel = (Spinner) findViewById(R.id.car_model);

        home_pickup = (CheckBox) findViewById(R.id.home_pick);

        submit = (RelativeLayout) findViewById(R.id.submit_rl);
        reset = (RelativeLayout) findViewById(R.id.reset_rl);
        serviceDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Service.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkFieldInForm();
            }


        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetForm();
            }


        });
    }

    private void resetForm() {
        Intent in =new Intent(Service.this,Service.class);
                startActivity(in);
        finish();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        serviceDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void checkFieldInForm() {
        String HOME_PICK;
        if (regNum.getText().length() > 0 && mileage.getText().length() > 0 && serviceDate.getText().length() > 0 && fullName.getText().length() > 0 && mobNum.getText().length() > 0 && emailID.getText().length() > 0 && state.getText().length() > 0 && city.getText().length() > 0 && description.getText().length() > 0 && !serviceType.getSelectedItem().toString().contains("Service Type") && !carModel.getSelectedItem().toString().contains("Car Model")) {
            if (home_pickup.isChecked()) {
                HOME_PICK = "TRUE";
            } else {
                HOME_PICK = "FALSE";
            }
            str = "Service Type : " + serviceType.getSelectedItem().toString() + "\n"
                    + "Car Model : " + carModel.getSelectedItem().toString() + "\n"
                    + "Registration Number : " + regNum.getText().toString() + "\n"
                    + "Mileage : " + mileage.getText().toString() + "\n"
                    + "Service Date : " + serviceDate.getText().toString() + "\n"
                    + "Home PickUp : " + HOME_PICK + "\n"
                    + "Full Name : " + fullName.getText().toString() + "\n"
                    + "Mobile Number : " + mobNum.getText().toString() + "\n"
                    + "E-Mail ID : " + emailID.getText().toString() + "\n"
                    + "State : " + state.getText().toString() + "\n"
                    + "City : " + city.getText().toString() + "\n"
                    + "Description : " + description.getText().toString() + "\n";
            sendTestEmail();
        } else if (serviceType.getSelectedItem().toString().contains("Service Type")) {
            ((TextView) serviceType.getSelectedView()).setError("Please select Value");
        } else if (carModel.getSelectedItem().toString().contains("Car Model")) {
            ((TextView) carModel.getSelectedView()).setError("Please select Value");
        } else if (regNum.getText().length() < 1) {
            regNum.setError("Please fill out this field");
        } else if (mileage.getText().length() < 1) {
            mileage.setError("Please fill out this field");
        } else if (serviceDate.getText().length() < 1) {
            serviceDate.setError("Please fill out this field");
        } else if (fullName.getText().length() < 1) {
            fullName.setError("Please fill out this field");
        } else if (mobNum.getText().length() < 1) {
            mobNum.setError("Please fill out this field");
        } else if (emailID.getText().length() < 1) {
            emailID.setError("Please fill out this field");
        } else if (state.getText().length() < 1) {
            state.setError("Please fill out this field");
        } else if (city.getText().length() < 1) {
            city.setError("Please fill out this field");
        } else if (description.getText().length() < 1) {
            description.setError("Please fill out this field");
        }
    }

    private void sendTestEmail() {
        BackgroundMail.newBuilder(this)
                .withUsername("peeyamhyundai@gmail.com")
                .withPassword("pmhyundai2017")
                .withMailto("peeyemhyundai@gmail.com")
                .withSubject("Book A Service")
                .withBody(str)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }
}
