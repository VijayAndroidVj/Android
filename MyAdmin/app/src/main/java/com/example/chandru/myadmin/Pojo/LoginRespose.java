package com.example.chandru.myadmin.Pojo;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginRespose {
    @SerializedName("dashboardlist")
    private List<DashBoard>dashboardlist;
    @SerializedName("message")
    private String message;
    @SerializedName("societycode")
    private String societycode;
    @SerializedName("status")
    private String societystatus;
    @SerializedName("societystatus")
    private String status;

    public List<DashBoard> getDashboardlist() {
        return dashboardlist;
    }

    public void setDashboardlist(List<DashBoard> dashboardlist) {
        this.dashboardlist = dashboardlist;
    }

    public String getSocietycode() {
        return societycode;
    }

    public void setSocietycode(String societycode) {
        this.societycode = societycode;
    }

    public String getSocietystatus() {
        return societystatus;
    }

    public void setSocietystatus(String societystatus) {
        this.societystatus = societystatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
