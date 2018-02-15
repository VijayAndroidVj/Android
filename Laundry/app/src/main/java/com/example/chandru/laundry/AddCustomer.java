package com.example.chandru.laundry;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.laundry.Api.Api;
import com.example.chandru.laundry.Api.ApiInterface;
import com.example.chandru.laundry.Pojo.customer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomer extends AppCompatActivity implements View.OnClickListener {
    private static EditText fullName, mobileNumber,location;
    private static Button signUpBtn;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }

        initViews();
        setListeners();
    }

    private void initViews() {



        fullName = (EditText) findViewById(R.id.fullName);
        mobileNumber = (EditText) findViewById(R.id.mobileNumber);
        location = (EditText) findViewById(R.id.location);
        signUpBtn = (Button)findViewById(R.id.signUpBtn);

//
//        forgotPassword = (TextView) findViewById(R.id.forgot_password);
//        signUp = (TextView)findViewById(R.id.createAccount);
//        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
//        loginLayout = (LinearLayout)findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);


    }

    private void setListeners() {
        signUpBtn.setOnClickListener(this);
        //forgotPassword.setOnClickListener(this);
        //signUp.setOnClickListener(this);

//        show_hide_password
//                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                    @Override
//                    public void onCheckedChanged(CompoundButton button,
//                                                 boolean isChecked) {
//
//                        if (isChecked) {
//
//                            show_hide_password.setText(R.string.hide_pwd);
//                            password.setInputType(InputType.TYPE_CLASS_TEXT);
//                            password.setTransformationMethod(HideReturnsTransformationMethod
//                                    .getInstance());// show password
//                        } else {
//                            show_hide_password.setText(R.string.show_pwd);
//
//                            password.setInputType(InputType.TYPE_CLASS_TEXT
//                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                            password.setTransformationMethod(PasswordTransformationMethod
//                                    .getInstance());
//
//                        }
//
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:
                checkValidation();
                break;

            case R.id.forgot_password:


                break;

        }

    }


    private void checkValidation() {

        final String name = fullName.getText().toString();
        final String contact = mobileNumber.getText().toString();
        final String address = location.getText().toString();

        if (name.equals("") || name.length() == 0
                || contact.equals("") || contact.length() == 0 ||  address.equals("") || address.length() == 0) {
            //loginLayout.startAnimation(shakeAnimation);
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show();

        }
        else{
            ApiInterface apiService =
                    Api.getClient().create(ApiInterface.class);

            Call<customer> call = apiService.getCustomer(name,address,contact);
            call.enqueue(new Callback<customer>() {
                @Override
                public void onResponse(Call<customer>call, Response<customer> response) {
                    String movies =response.body().getError();
                    String msg=response.body().getError_msg();

                    Log.d("success", "Number of movies received: " + movies);
                    if(movies.equals("false")){
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("cname", name);
                        editor.putString("contact", contact);
                        editor.putString("address", address);
                        editor.commit();
                        Toast.makeText(AddCustomer.this, msg, Toast.LENGTH_SHORT)
                                .show();

                        Intent myIntent = new Intent(AddCustomer.this, Order.class);
                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(AddCustomer.this, R.anim.left_enter, R.anim.left_out);
                        startActivity(myIntent, options.toBundle());
                    }else{
                       // loginLayout.startAnimation(shakeAnimation);
                        Toast.makeText(AddCustomer.this, "Insert error", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<customer>call, Throwable t) {
                    // Log error here since request failed
                    Log.e("error", t.toString());
                }
            });
            // loginLayout.startAnimation(shakeAnimation);
            //Toast.makeText(this, "Invalid username and password", Toast.LENGTH_SHORT)
            //  .show();
//            Intent news=new Intent(this,MainActivity.class);
//            startActivity(news);
        }


    }
}
