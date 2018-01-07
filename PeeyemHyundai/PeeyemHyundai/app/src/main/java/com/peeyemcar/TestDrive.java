package com.peeyemcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.peeyem.app.R;


public class TestDrive extends AppCompatActivity {
    String str = "my string \n my other string";
    Spinner  carModel,state,city,dealerLocation;
    EditText  preferredDate, fullName, mobNum, emailID, comment,currentVehicle;
    RadioGroup rg;
    RelativeLayout submit, reset;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drive);
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

        preferredDate = (EditText) findViewById(R.id.preferred_date);
        fullName = (EditText) findViewById(R.id.full_name);
        mobNum = (EditText) findViewById(R.id.mobile_num);
        emailID = (EditText) findViewById(R.id.email_id);
        comment = (EditText) findViewById(R.id.comment);
        currentVehicle=(EditText)findViewById(R.id.current_vehicle);

        carModel = (Spinner) findViewById(R.id.car_model);
        state = (Spinner) findViewById(R.id.state);
        city = (Spinner) findViewById(R.id.city);
        dealerLocation = (Spinner) findViewById(R.id.dealer_location);

        rg = (RadioGroup)findViewById(R.id.radioFuel);


        submit = (RelativeLayout) findViewById(R.id.submit_rl);
        reset = (RelativeLayout) findViewById(R.id.reset_rl);
        preferredDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(TestDrive.this, date, myCalendar
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
        Intent in =new Intent(TestDrive.this,TestDrive.class);
                startActivity(in);
        finish();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        preferredDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void checkFieldInForm() {
        if (preferredDate.getText().length() > 0 && fullName.getText().length() > 0 && mobNum.getText().length() > 0 && emailID.getText().length() > 0 && !dealerLocation.getSelectedItem().toString().contains("Dealer Location") &&!state.getSelectedItem().toString().contains("Select State") && !city.getSelectedItem().toString().contains("Select City")  && comment.getText().length() > 0 && !carModel.getSelectedItem().toString().contains("Car Model")) {


            str =
                     "Car Model : " + carModel.getSelectedItem().toString() + "\n"
                    + "Preferred Date : " + preferredDate.getText().toString() + "\n"
                    + "Fuel Type : " + ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString() + "\n"
                    + "Full Name : " + fullName.getText().toString() + "\n"
                    + "Mobile Number : " + mobNum.getText().toString() + "\n"
                    + "E-Mail ID : " + emailID.getText().toString() + "\n"
                    + "State : " + state.getSelectedItem().toString() + "\n"
                    + "City : " + city.getSelectedItem().toString()+ "\n"
                    + "Dealer Location : " + city.getSelectedItem().toString()+ "\n"
                    + "Current Vehicle : " + currentVehicle.getText().toString()+ "\n"
                    + "Comment : " + comment.getText().toString() + "\n";
            sendTestEmail();
        } else if (carModel.getSelectedItem().toString().contains("Car Model")) {
            ((TextView) carModel.getSelectedView()).setError("Please select Value");
        } else if (currentVehicle.getText().length() < 1) {
            currentVehicle.setError("Please fill out this field");
        }  else if (fullName.getText().length() < 1) {
            fullName.setError("Please fill out this field");
        } else if (mobNum.getText().length() < 1) {
            mobNum.setError("Please fill out this field");
        } else if (emailID.getText().length() < 1) {
            emailID.setError("Please fill out this field");
        } else if (state.getSelectedItem().toString().contains("Select State")) {
            ((TextView) state.getSelectedView()).setError("Please select Value");
        } else if (city.getSelectedItem().toString().contains("Select City")) {
            ((TextView) city.getSelectedView()).setError("Please select Value");
        }else if (dealerLocation.getSelectedItem().toString().contains("Dealer Location")) {
            ((TextView) dealerLocation.getSelectedView()).setError("Please select Value");
        } else if (comment.getText().length() < 1) {
            comment.setError("Please fill out this field");
        }
    }

    private void sendTestEmail() {
        BackgroundMail.newBuilder(this)
                .withUsername("peeyamhyundai@gmail.com")
                .withPassword("pmhyundai2017")
                .withMailto("peeyemhyundai@gmail.com")
                .withSubject("Enquiry Form")
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
