package com.android.mymedicine.java.retrofit;


import com.android.mymedicine.java.model.CallEvent;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by razin on 02-11-2017.
 */

public interface ApiInterface {

    @Headers({
            "Content-Type: application/json",
            "ApiKey: F8t8iEH487zqmrXza349440O6yLQ7kLP"
    })
    @POST("index.php/register")
    Call<ArrayList<CallEvent>> register(@Body HashMap<String, Object> body);

    @Headers({
            "Content-Type: application/json",
            "ApiKey: F8t8iEH487zqmrXza349440O6yLQ7kLP"
    })
    @POST("index.php/login")
    Call<ArrayList<CallEvent>> login(@Body HashMap<String, Object> body);
}
