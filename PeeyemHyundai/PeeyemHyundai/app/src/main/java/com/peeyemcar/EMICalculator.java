package com.peeyemcar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.peeyem.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 7/1/18.
 */

public class EMICalculator extends AppCompatActivity {

    TextView txtroadPrice, txtLoanAmount, txtEMI;
    EditText edtRateOfInterest, edtDownPayment;
    boolean interestchanged = false;
    List<String> modellist = new ArrayList<String>();
    List<String> variantlist = new ArrayList<String>();
    List<String> tenurelist = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emi_calculator);

        tenurelist.add("Tenure");
        tenurelist.add("12 Months");
        tenurelist.add("24 Months");
        tenurelist.add("36 Months");
        tenurelist.add("48 Months");
        tenurelist.add("60 Months");
        tenurelist.add("72 Months");

        modellist.add("Select Model*");
        modellist.add("Eon");
        modellist.add("i10");

        variantlist.add("Select Variant*");
        variantlist.add("D Lite+ (M)");
        variantlist.add("Era+ (S)");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("EMI Calculator");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);

        txtroadPrice = (TextView) findViewById(R.id.txt_OnRoadPrice);
        txtLoanAmount = (TextView) findViewById(R.id.txt_LoanAmount);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        spinner1 = (Spinner) findViewById(R.id.spinner2);
        spinner1.setVisibility(View.VISIBLE);

        spinner2 = (Spinner) findViewById(R.id.spinner3);
        spinner2.setVisibility(View.VISIBLE);


        txtEMI = (TextView) findViewById(R.id.txtEMI);
        findViewById(R.id.rlModel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.callOnClick();
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, modellist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
//                    if (type == 0) {
//                        txt_model.setText(list.get(i));
//                    } else if (type == 1) {
//                        txt_variant.setText(list.get(i));
//                    } else {
//                        txtTenure.setText(list.get(i));
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, variantlist);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
//                    if (type == 0) {
//                        txt_model.setText(list.get(i));
//                    } else if (type == 1) {
//                        txt_variant.setText(list.get(i));
//                    } else {
//                        txtTenure.setText(list.get(i));
//                    }
                    txtroadPrice.setText("500000");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tenurelist);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
//                    if (type == 0) {
//                        txt_model.setText(list.get(i));
//                    } else if (type == 1) {
//                        txt_variant.setText(list.get(i));
//                    } else {
//                        txtTenure.setText(list.get(i));
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        findViewById(R.id.rlVarient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner1.callOnClick();
            }
        });

        findViewById(R.id.rlTenure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner2.callOnClick();
            }
        });

        edtRateOfInterest = (EditText) findViewById(R.id.txt_Interest);
        edtDownPayment = (EditText) findViewById(R.id.txt_DownPayment);

        edtRateOfInterest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String interest = edtRateOfInterest.getText().toString();
                if (!interestchanged && interest.length() > 0) {
                    if (Integer.parseInt(interest) > 20) {
                        interestchanged = true;
                        Toast.makeText(EMICalculator.this, "Interest should not be greater than 20", Toast.LENGTH_SHORT).show();
                        edtRateOfInterest.setText("");
                        interestchanged = false;
                    } else {
                        calculateEmi();
                    }
                }

            }
        });

        edtDownPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String downPayment = edtDownPayment.getText().toString();
                String roadPrice = txtroadPrice.getText().toString();
                int rdpce = 0;
                if (roadPrice.length() > 0) {
                    rdpce = Integer.parseInt(roadPrice);
                }
                if (!interestchanged && downPayment.length() > 0) {
                    if (Integer.parseInt(downPayment) > rdpce) {
                        interestchanged = true;
                        Toast.makeText(EMICalculator.this, "Down Payment should not be greater than Road Price", Toast.LENGTH_SHORT).show();
                        edtDownPayment.setText("");
                        interestchanged = false;
                    } else if (rdpce > 0) {
                        int loanAmount = rdpce - Integer.parseInt(downPayment);
                        txtLoanAmount.setText("" + loanAmount);
                        calculateEmi();
                    }
                }
            }
        });
    }

    Spinner spinner;
    Spinner spinner1;
    Spinner spinner2;

    private void openSpinnerList(final int type, final List<String> list) {

    }

    private void calculateEmi() {
        try {
            String st1 = txtLoanAmount.getText().toString();
            String st2 = edtRateOfInterest.getText().toString();
            String st3 = (String) spinner2.getSelectedItem();

            if (TextUtils.isEmpty(st1)) {
                return;
            }

            if (TextUtils.isEmpty(st2)) {
                edtRateOfInterest.setError("Enter Interest Rate");
                edtRateOfInterest.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(st3) || st3.equalsIgnoreCase("Tenure")) {
                return;
            }

            st3 = st3.split(" ")[0];

            float p = Float.parseFloat(st1);
            float i = Float.parseFloat(st2);
            float y = Float.parseFloat(st3);

            float Principal = calPric(p);

            float Rate = calInt(i);

            float Months = calMonth(y);

            float Dvdnt = calDvdnt(Rate, Months);

            float FD = calFinalDvdnt(Principal, Rate, Dvdnt);

            float D = calDivider(Dvdnt);

            float emi = calEmi(FD, D);

//            float TA = calTa(emi, Months);

//            float ti = calTotalInt(TA, Principal);
            txtEMI.setText(String.valueOf(emi));
//            TI.setText(String.valueOf(ti));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public float calPric(float p) {

        return (float) (p);

    }

    public float calInt(float i) {

        return (float) (i / 12 / 100);

    }

    public float calMonth(float y) {

        return (float) (y * 12);

    }

    public float calDvdnt(float Rate, float Months) {

        return (float) (Math.pow(1 + Rate, Months));

    }

    public float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

        return (float) (Principal * Rate * Dvdnt);

    }

    public float calDivider(float Dvdnt) {

        return (float) (Dvdnt - 1);

    }

    public float calEmi(float FD, Float D) {

        return (float) (FD / D);

    }

    public float calTa(float emi, Float Months) {

        return (float) (emi * Months);

    }

    public float calTotalInt(float TA, float Principal) {

        return (float) (TA - Principal);

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
}
