package com.example.chandru.myadmin.Pojo;



public class DashBoard  {

    private int KEYID;
    private String EMAIL_ID;
    private String FLAT_AREA;
    private String FLAT_NO;
    private String FLOOR_NO;
    private String MEMBER_CODE;
    private String MEMBER_NAME;
    private String MOBILE_NO;
    private String NO_OF_PARKING;
    private String SELF_OCCUPIED;
    private String SOCIETY_CODE;
    private String STATUS;

    public DashBoard(){

    }
    // constructor
    public DashBoard(int KEYID,String EMAIL_ID,String FLAT_AREA,String FLAT_NO,String FLOOR_NO,String MEMBER_CODE, String MEMBER_NAME,String MOBILE_NO
            ,String NO_OF_PARKING,String SELF_OCCUPIED,String SOCIETY_CODE,String STATUS){
        this.KEYID=KEYID;
        this.EMAIL_ID=EMAIL_ID;
        this.FLAT_AREA=FLAT_AREA;
        this.FLAT_NO=FLAT_NO;
        this.FLOOR_NO=FLOOR_NO;
        this.MEMBER_CODE = MEMBER_CODE;
        this.MEMBER_NAME=MEMBER_NAME;
        this.MOBILE_NO=MOBILE_NO;
        this.NO_OF_PARKING=NO_OF_PARKING;
        this.SELF_OCCUPIED=SELF_OCCUPIED;
        this.SOCIETY_CODE=SOCIETY_CODE;
        this.STATUS=STATUS;




    }

    public DashBoard(String EMAIL_ID,String FLAT_AREA,String FLAT_NO,String FLOOR_NO,String MEMBER_CODE, String MEMBER_NAME,String MOBILE_NO
            ,String NO_OF_PARKING,String SELF_OCCUPIED,String SOCIETY_CODE,String STATUS){
        this.KEYID=KEYID;
        this.EMAIL_ID=EMAIL_ID;
        this.FLAT_AREA=FLAT_AREA;
        this.FLAT_NO=FLAT_NO;
        this.FLOOR_NO=FLOOR_NO;
        this.MEMBER_CODE = MEMBER_CODE;
        this.MEMBER_NAME=MEMBER_NAME;
        this.MOBILE_NO=MOBILE_NO;
        this.NO_OF_PARKING=NO_OF_PARKING;
        this.SELF_OCCUPIED=SELF_OCCUPIED;
        this.SOCIETY_CODE=SOCIETY_CODE;
        this.STATUS=STATUS;




    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getFLAT_NO() {
        return FLAT_NO;
    }

    public void setFLAT_NO(String FLAT_NO) {
        this.FLAT_NO = FLAT_NO;
    }

    public String getFLAT_AREA() {
        return FLAT_AREA;
    }

    public void setFLAT_AREA(String FLAT_AREA) {
        this.FLAT_AREA = FLAT_AREA;
    }

    public String getFLOOR_NO() {
        return FLOOR_NO;
    }

    public void setFLOOR_NO(String FLOOR_NO) {
        this.FLOOR_NO = FLOOR_NO;
    }

    public String getMEMBER_CODE() {
        return MEMBER_CODE;
    }

    public void setMEMBER_CODE(String MEMBER_CODE) {
        this.MEMBER_CODE = MEMBER_CODE;
    }

    public String getMEMBER_NAME() {
        return MEMBER_NAME;
    }

    public void setMEMBER_NAME(String MEMBER_NAME) {
        this.MEMBER_NAME = MEMBER_NAME;
    }

    public String getMOBILE_NO() {
        return MOBILE_NO;
    }

    public void setMOBILE_NO(String MOBILE_NO) {
        this.MOBILE_NO = MOBILE_NO;
    }

    public String getNO_OF_PARKING() {
        return NO_OF_PARKING;
    }

    public void setNO_OF_PARKING(String NO_OF_PARKING) {
        this.NO_OF_PARKING = NO_OF_PARKING;
    }

    public String getSELF_OCCUPIED() {
        return SELF_OCCUPIED;
    }

    public void setSELF_OCCUPIED(String SELF_OCCUPIED) {
        this.SELF_OCCUPIED = SELF_OCCUPIED;
    }

    public String getSOCIETY_CODE() {
        return SOCIETY_CODE;
    }

    public void setSOCIETY_CODE(String SOCIETY_CODE) {
        this.SOCIETY_CODE = SOCIETY_CODE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    // Empty constructor


   /* // constructor
    public DashBoard(String MEMBER_CODE,String EMAIL_ID,String FLAT_AREA,String FLAT_NO,String FLOOR_NO, String MEMBER_NAME,String MOBILE_NO
            ,String NO_OF_PARKING,String SELF_OCCUPIED,String SOCIETY_CODE,String STATUS ){


    }*/
}


   /* public DashBoard(Parcel in) {
        Areasq = in.readString();
        Parking = in.readString();
        Selfoccpied = in.readString();
        adminID = in.readString();
        email = in.readString();
        flatno = in.readString();
        floor = in.readString();
        id = in.readString();
        membername = in.readString();
        mobile = in.readString();
        regdate = in.readString();
        serialno = in.readString();
    }

    public static final Creator<DashBoard> CREATOR = new Creator<DashBoard>() {
        @Override
        public DashBoard createFromParcel(Parcel in) {
            return new DashBoard(in);
        }

        @Override
        public DashBoard[] newArray(int size) {
            return new DashBoard[size];
        }
    };

    public DashBoard() {

    }

    public String getAreasq() {
        return Areasq;
    }

    public void setAreasq(String areasq) {
        Areasq = areasq;
    }

    public String getParking() {
        return Parking;
    }

    public void setParking(String parking) {
        Parking = parking;
    }

    public String getSelfoccpied() {
        return Selfoccpied;
    }

    public void setSelfoccpied(String selfoccpied) {
        Selfoccpied = selfoccpied;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlatno() {
        return flatno;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Areasq);
        parcel.writeString(Parking);
        parcel.writeString(Selfoccpied);
        parcel.writeString(adminID);
        parcel.writeString(email);
        parcel.writeString(flatno);
        parcel.writeString(floor);
        parcel.writeString(id);
        parcel.writeString(membername);
        parcel.writeString(mobile);
        parcel.writeString(regdate);
        parcel.writeString(serialno);
    }
}*/
