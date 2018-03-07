package com.example.chandru.laundry.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Api {
    //http://demo.adityametals.com/api/login.php/api/login.php?username=admin&password=admin

    //http://demo.adityametals.com/api/add_customer.php?name=pradeep&address=pradeep&contact=12345678900

    public static final String BASE_URL = "http://demo.adityametals.com/api/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
