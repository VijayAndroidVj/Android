package com.example.chandru.laundry.Pojo;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class login {
    @SerializedName("error")
    private String error;
    @SerializedName("u_id")
    private String u_id;

//    @SerializedName("user")
//    private List<user> results;

    public login(String error, String u_id) {
        this.error = error;
        this.u_id = u_id;
      //  this.results = results;

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }


}

class user{

    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    public user(String name, String type) {
        this.name = name;
        this.type = type;


    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
