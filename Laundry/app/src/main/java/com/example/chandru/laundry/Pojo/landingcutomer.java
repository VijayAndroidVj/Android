package com.example.chandru.laundry.Pojo;



public class landingcutomer {

    private String item;
    private String qty;
    private String unit;
    private String amt;
    private String serviceId;
    private String serviceName;
    private String itemid;

    public landingcutomer(){

    }


    public landingcutomer(String item, String qty,String unit, String amt,String serviceId, String serviceName,String itemid) {
        this.item = item;
        this.qty = qty;
        this.unit = unit;
        this.amt = amt;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.itemid=itemid;




    }
}
