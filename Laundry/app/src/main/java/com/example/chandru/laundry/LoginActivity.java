package com.example.chandru.laundry;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chandru.laundry.Api.Api;
import com.example.chandru.laundry.Api.ApiInterface;
import com.example.chandru.laundry.Pojo.login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static View view;

    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setListeners();

    }


    private void initViews() {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(this.getResources().getColor(R.color.background_color));
        }


        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button)findViewById(R.id.loginBtn);
        forgotPassword = (TextView) findViewById(R.id.forgot_password);
        signUp = (TextView)findViewById(R.id.createAccount);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
        loginLayout = (LinearLayout)findViewById(R.id.login_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);


    }

    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);

        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);
                            password.setInputType(InputType.TYPE_CLASS_TEXT);
                            password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);

                            password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());

                        }

                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:


                break;

        }

    }


    private void checkValidation() {

        final String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {
            loginLayout.startAnimation(shakeAnimation);


        }else if(getEmailId.equals("chan") && getPassword.equals("chan") ){
            Toast.makeText(this, "Successfull loged in.", Toast.LENGTH_SHORT)
                    .show();


            Intent myIntent = new Intent(this, MainActivity.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(this, R.anim.left_enter, R.anim.left_out);
            startActivity(myIntent, options.toBundle());

        }
        else{
            ApiInterface apiService =
                    Api.getClient().create(ApiInterface.class);

            Call<login> call = apiService.getLogin(getEmailId,getPassword);
            call.enqueue(new Callback<login>() {
                @Override
                public void onResponse(Call<login>call, Response<login> response) {
                   String movies =response.body().getError();

                    Log.d("success", "Number of movies received: " + movies);
                    if(movies.equals("false")){
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("name", getEmailId);
                        editor.commit();
                        PreferenceUtil preferenceUtil = new PreferenceUtil(LoginActivity.this);
                        preferenceUtil.putBoolean("Loggedin", true);
                        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        ActivityOptions options =
                                ActivityOptions.makeCustomAnimation(LoginActivity.this, R.anim.left_enter, R.anim.left_out);
                        startActivity(myIntent, options.toBundle());
                        finish();

                    }else{
                        loginLayout.startAnimation(shakeAnimation);
                        Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT)
                          .show();
                    }
                }

                @Override
                public void onFailure(Call<login>call, Throwable t) {
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
