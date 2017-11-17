package com.example.chandru.myadmin.Pojo;

/**
 * Created by chandru on 10/15/2017.
 */

public class Maintain {



    private String amount;
    private String maintancecode;
    private String maintancename;
    private String maintancetype;

    public Maintain(){

    }

    public Maintain(String amount,String maintancecode,String maintancename,String maintancetype){

        this.amount=amount;
        this.maintancecode=maintancecode;
        this.maintancename=maintancename;
        this.maintancetype=maintancetype;




    }



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMaintancecode() {
        return maintancecode;
    }

    public void setMaintancecode(String maintancecode) {
        this.maintancecode = maintancecode;
    }

    public String getMaintancename() {
        return maintancename;
    }

    public void setMaintancename(String maintancename) {
        this.maintancename = maintancename;
    }

    public String getMaintancetype() {
        return maintancetype;
    }

    public void setMaintancetype(String maintancetype) {
        this.maintancetype = maintancetype;
    }


}
