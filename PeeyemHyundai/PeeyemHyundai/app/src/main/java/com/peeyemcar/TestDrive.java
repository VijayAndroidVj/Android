package com.peeyemcar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.peeyem.app.R;
import com.peeyemcar.models.EventResponse;
import com.peeyemcar.retrofit.ApiClient;
import com.peeyemcar.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestDrive extends AppCompatActivity {
    String str = "my string \n my other string";
    Spinner carModel, state, city, dealerLocation;
    EditText preferredDate, fullName, mobNum, emailID, comment, currentVehicle;
    RadioGroup rg;
    View submit, reset;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_drive);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Test Drive");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

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

        preferredDate = findViewById(R.id.preferred_date);
        fullName = findViewById(R.id.full_name);
        mobNum = findViewById(R.id.mobile_num);
        emailID = findViewById(R.id.email_id);
        comment = findViewById(R.id.comment);
        currentVehicle = findViewById(R.id.current_vehicle);

        carModel = findViewById(R.id.car_model);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        dealerLocation = findViewById(R.id.dealer_location);

        rg = findViewById(R.id.radioFuel);


        submit = findViewById(R.id.submit_rl);
        reset = findViewById(R.id.reset_rl);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetForm() {
        Intent in = new Intent(TestDrive.this, TestDrive.class);
        startActivity(in);
        finish();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        preferredDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void checkFieldInForm() {
        if (preferredDate.getText().length() > 0 && fullName.getText().length() > 0 && mobNum.getText().length() > 0 && emailID.getText().length() > 0 && !dealerLocation.getSelectedItem().toString().contains("Dealer Location") && !state.getSelectedItem().toString().contains("Select State") && !city.getSelectedItem().toString().contains("Select City") && comment.getText().length() > 0 && !carModel.getSelectedItem().toString().contains("Car Model")) {
            str =
                    "Car Model : " + carModel.getSelectedItem().toString() + "\n"
                            + "Preferred Date : " + preferredDate.getText().toString() + "\n"
                            + "Fuel Type : " + ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString() + "\n"
                            + "Full Name : " + fullName.getText().toString() + "\n"
                            + "Mobile Number : " + mobNum.getText().toString() + "\n"
                            + "E-Mail ID : " + emailID.getText().toString() + "\n"
                            + "State : " + state.getSelectedItem().toString() + "\n"
                            + "City : " + city.getSelectedItem().toString() + "\n"
                            + "Dealer Location : " + dealerLocation.getSelectedItem().toString() + "\n"
                            + "Current Vehicle : " + currentVehicle.getText().toString() + "\n"
                            + "Comment : " + comment.getText().toString() + "\n";
            if (CommonUtil.isNetworkAvailable(TestDrive.this)) {
                MainActivity.showProgress(TestDrive.this);
                ApiInterface apiService =
                        ApiClient.getClient().create(ApiInterface.class);
                Call<EventResponse> call = apiService.customer_testdrive("2", ((RadioButton) findViewById(rg.getCheckedRadioButtonId())).getText().toString(),
                        carModel.getSelectedItem().toString(),
                        preferredDate.getText().toString(),
                        fullName.getText().toString(),
                        mobNum.getText().toString(),
                        emailID.getText().toString(),
                        state.getSelectedItem().toString(),
                        city.getSelectedItem().toString(),
                        dealerLocation.getSelectedItem().toString(),
                        currentVehicle.getText().toString(),
                        comment.getText().toString());

                call.enqueue(new Callback<EventResponse>() {
                    @Override
                    public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                        Log.d("", "Number of movies received: " + response.body());
                        MainActivity.dismissProgress();
                        EventResponse sigInResponse = response.body();
                        if (sigInResponse != null) {
                            if (sigInResponse.getResult().equalsIgnoreCase("success")) {
                                try {
                                    sendTestEmail();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                onBackPressed();
                            }
                            Toast.makeText(TestDrive.this, sigInResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TestDrive.this, "Could not connect to server.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<EventResponse> call, Throwable t) {
                        // Log error here since request failed
                        Log.e("", t.toString());
                        MainActivity.dismissProgress();
                        Toast.makeText(TestDrive.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(TestDrive.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            }

        } else if (carModel.getSelectedItem().toString().contains("Car Model")) {
            ((TextView) carModel.getSelectedView()).setError("Please select Value");
            preferredDate.requestFocus();
        } else if (currentVehicle.getText().length() < 1) {
            currentVehicle.setError("Please fill out this field");
            currentVehicle.requestFocus();
        } else if (fullName.getText().length() < 1) {
            fullName.setError("Please fill out this field");
            fullName.requestFocus();
        } else if (mobNum.getText().length() < 1) {
            mobNum.setError("Please fill out this field");
            mobNum.requestFocus();
        } else if (emailID.getText().length() < 1) {
            emailID.setError("Please fill out this field");
            emailID.requestFocus();
        } else if (state.getSelectedItem().toString().contains("Select State")) {
            ((TextView) state.getSelectedView()).setError("Please select Value");
        } else if (city.getSelectedItem().toString().contains("Select City")) {
            ((TextView) city.getSelectedView()).setError("Please select Value");
        } else if (dealerLocation.getSelectedItem().toString().contains("Dealer Location")) {
            ((TextView) dealerLocation.getSelectedView()).setError("Please select Value");
        } else if (comment.getText().length() < 1) {
            comment.setError("Please fill out this field");
            comment.requestFocus();
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
