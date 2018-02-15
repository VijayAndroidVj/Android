package com.example.chandru.laundry.Api;


import com.example.chandru.laundry.Pojo.cat;
import com.example.chandru.laundry.Pojo.customer;
import com.example.chandru.laundry.Pojo.login;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login.php/api/login.php")
    Call<login> getLogin(@Query("username") String username,@Query("password") String password);

    @GET("add_customer.php?")
    Call<customer> getCustomer(@Query("name") String name,@Query("address") String address,@Query("contact") String contact);

    @GET("service.php?")
    Call<cat> getCat();
}
