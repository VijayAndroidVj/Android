package com.android.mymedicine.java.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbStructure extends SQLiteOpenHelper {

    private static String DATABASE = "db_mymedicine";

    public DbStructure(Context context) {
        super(context, DATABASE, null, 1);
    }

    //tbl_medicine
    public static final String TBLMEDICINE = "tbl_medicine";
    public static final String FIELDMEDICINEID = "medicine_id";
    public static final String FIELDMEDICINENAME = "medicine_name";
    public static final String FIELDDOSAGE = "dosage";
    public static final String FIELDDOSAGETYPE = "dosage_type";
    public static final String FIELDUNIT = "unit";
    public static final String FIELDLOCALPATH = "local_image_path";
    public static final String FIELDSERVERPATH = "server_image_path";
    public static final String FIELDFREQUENCY = "frequency";
    public static final String FIELDSTARTDATE = "start_date";
    public static final String FIELDENDDATE = "end_date";
    public static final String FIELDHOWMANYTIMES = "how_many_times";
    public static final String FIELDTIMESLOT1 = "time_slot_1";
    public static final String FIELDTIMESLOT2 = "time_slot_2";
    public static final String FIELDTIMESLOT3 = "time_slot_3";
    public static final String FIELDTIMESLOT4 = "time_slot_4";
    public static final String FIELDTIMESLOT5 = "time_slot_5";
    public static final String FIELDTIMESLOT6 = "time_slot_6";
    public static final String FIELDTIMESLOT7 = "time_slot_7";
    public static final String FIELDTIMESLOT8 = "time_slot_8";
    public static final String FIELDTIMESLOT9 = "time_slot_9";
    public static final String FIELDTIMESLOT10 = "time_slot_10";
    public static final String FIELDTIMESLOT11 = "time_slot_11";
    public static final String FIELDTIMESLOT12 = "time_slot_12";
    public static final String FIELDTIMESLOT13 = "time_slot_13";
    public static final String FIELDTIMESLOT14 = "time_slot_14";
    public static final String FIELDTIMESLOT15 = "time_slot_15";
    public static final String FIELDTIMESLOT16 = "time_slot_16";
    public static final String FIELDTIMESLOT17 = "time_slot_17";
    public static final String FIELDTIMESLOT18 = "time_slot_18";
    public static final String FIELDTIMESLOT19 = "time_slot_19";
    public static final String FIELDTIMESLOT20 = "time_slot_20";
    public static final String FIELDTIMESLOT21 = "time_slot_21";
    public static final String FIELDTIMESLOT22 = "time_slot_22";
    public static final String FIELDTIMESLOT23 = "time_slot_23";
    public static final String FIELDTIMESLOT24 = "time_slot_24";
    public static final String FIELDCOUNTSLOT1 = "medicine_count_slot_1";
    public static final String FIELDCOUNTSLOT2 = "medicine_count_slot_2";
    public static final String FIELDCOUNTSLOT3 = "medicine_count_slot_3";
    public static final String FIELDCOUNTSLOT4 = "medicine_count_slot_4";
    public static final String FIELDCOUNTSLOT5 = "medicine_count_slot_5";
    public static final String FIELDCOUNTSLOT6 = "medicine_count_slot_6";
    public static final String FIELDCOUNTSLOT7 = "medicine_count_slot_7";
    public static final String FIELDCOUNTSLOT8 = "medicine_count_slot_8";
    public static final String FIELDCOUNTSLOT9 = "medicine_count_slot_9";
    public static final String FIELDCOUNTSLOT10 = "medicine_count_slot_10";
    public static final String FIELDCOUNTSLOT11 = "medicine_count_slot_11";
    public static final String FIELDCOUNTSLOT12 = "medicine_count_slot_12";
    public static final String FIELDCOUNTSLOT13 = "medicine_count_slot_13";
    public static final String FIELDCOUNTSLOT14 = "medicine_count_slot_14";
    public static final String FIELDCOUNTSLOT15 = "medicine_count_slot_15";
    public static final String FIELDCOUNTSLOT16 = "medicine_count_slot_16";
    public static final String FIELDCOUNTSLOT17 = "medicine_count_slot_17";
    public static final String FIELDCOUNTSLOT18 = "medicine_count_slot_18";
    public static final String FIELDCOUNTSLOT19 = "medicine_count_slot_19";
    public static final String FIELDCOUNTSLOT20 = "medicine_count_slot_20";
    public static final String FIELDCOUNTSLOT21 = "medicine_count_slot_21";
    public static final String FIELDCOUNTSLOT22 = "medicine_count_slot_22";
    public static final String FIELDCOUNTSLOT23 = "medicine_count_slot_23";
    public static final String FIELDCOUNTSLOT24 = "medicine_count_slot_24";
    public static final String FIELDISSELECTEDSUNDAY = "is_selected_sunday";
    public static final String FIELDISSELECTEDMONDAY = "is_selected_monday";
    public static final String FIELDISSELECTEDTUESDAY = "is_selected_tuesday";
    public static final String FIELDISSELECTEDWEDNESDAY = "is_selected_wednesday";
    public static final String FIELDISSELECTEDTHURSDAY = "is_selected_thursday";
    public static final String FIELDISSELECTEDFRIDAY = "is_selected_friday";
    public static final String FIELDISSELECTEDSATURDAY = "is_selected_saturday";
    public static final String FIELDCREATEDDATE = "created_datetime";
    public static final String FIELDMEDICINETAKEN = "medicine_taken";
    public static final String FIELDMEDICINESKIPPED = "medicine_skipped";
    public static final String FIELDNEXTNOTIFICATION = "next_notification_time";
    public static final String FIELDNEXTMEDICINECOUNT = "next_medicine_count";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TBLMEDICINE + "("
//                + FIELDMEDICINEID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + FIELDMEDICINEID + " TEXT,"
                + FIELDMEDICINENAME + " TEXT,"
                + FIELDDOSAGE + " INTEGER,"
                + FIELDDOSAGETYPE + " INTEGER,"
                + FIELDUNIT + " TEXT,"
                + FIELDLOCALPATH + " TEXT,"
                + FIELDSERVERPATH + " TEXT,"
                + FIELDFREQUENCY + " TEXT,"
                + FIELDSTARTDATE + " INTEGER,"
                + FIELDENDDATE + " INTEGER,"
                + FIELDHOWMANYTIMES + " INTEGER,"
                + FIELDTIMESLOT1 + " INTEGER,"
                + FIELDTIMESLOT2 + " INTEGER,"
                + FIELDTIMESLOT3 + " INTEGER,"
                + FIELDTIMESLOT4 + " INTEGER,"
                + FIELDTIMESLOT5 + " INTEGER,"
                + FIELDTIMESLOT6 + " INTEGER,"
                + FIELDTIMESLOT7 + " INTEGER,"
                + FIELDTIMESLOT8 + " INTEGER,"
                + FIELDTIMESLOT9 + " INTEGER,"
                + FIELDTIMESLOT10 + " INTEGER,"
                + FIELDTIMESLOT11 + " INTEGER,"
                + FIELDTIMESLOT12 + " INTEGER,"
                + FIELDTIMESLOT13 + " INTEGER,"
                + FIELDTIMESLOT14 + " INTEGER,"
                + FIELDTIMESLOT15 + " INTEGER,"
                + FIELDTIMESLOT16 + " INTEGER,"
                + FIELDTIMESLOT17 + " INTEGER,"
                + FIELDTIMESLOT18 + " INTEGER,"
                + FIELDTIMESLOT19 + " INTEGER,"
                + FIELDTIMESLOT20 + " INTEGER,"
                + FIELDTIMESLOT21 + " INTEGER,"
                + FIELDTIMESLOT22 + " INTEGER,"
                + FIELDTIMESLOT23 + " INTEGER,"
                + FIELDTIMESLOT24 + " INTEGER,"
                + FIELDCOUNTSLOT1 + " INTEGER,"
                + FIELDCOUNTSLOT2 + " INTEGER,"
                + FIELDCOUNTSLOT3 + " INTEGER,"
                + FIELDCOUNTSLOT4 + " INTEGER,"
                + FIELDCOUNTSLOT5 + " INTEGER,"
                + FIELDCOUNTSLOT6 + " INTEGER,"
                + FIELDCOUNTSLOT7 + " INTEGER,"
                + FIELDCOUNTSLOT8 + " INTEGER,"
                + FIELDCOUNTSLOT9 + " INTEGER,"
                + FIELDCOUNTSLOT10 + " INTEGER,"
                + FIELDCOUNTSLOT11 + " INTEGER,"
                + FIELDCOUNTSLOT12 + " INTEGER,"
                + FIELDCOUNTSLOT13 + " INTEGER,"
                + FIELDCOUNTSLOT14 + " INTEGER,"
                + FIELDCOUNTSLOT15 + " INTEGER,"
                + FIELDCOUNTSLOT16 + " INTEGER,"
                + FIELDCOUNTSLOT17 + " INTEGER,"
                + FIELDCOUNTSLOT18 + " INTEGER,"
                + FIELDCOUNTSLOT19 + " INTEGER,"
                + FIELDCOUNTSLOT20 + " INTEGER,"
                + FIELDCOUNTSLOT21 + " INTEGER,"
                + FIELDCOUNTSLOT22 + " INTEGER,"
                + FIELDCOUNTSLOT23 + " INTEGER,"
                + FIELDCOUNTSLOT24 + " TEXT,"
                + FIELDISSELECTEDSUNDAY + " INTEGER,"
                + FIELDISSELECTEDMONDAY + " INTEGER,"
                + FIELDISSELECTEDTUESDAY + " INTEGER,"
                + FIELDISSELECTEDWEDNESDAY + " INTEGER,"
                + FIELDISSELECTEDTHURSDAY + " INTEGER,"
                + FIELDISSELECTEDFRIDAY + " INTEGER,"
                + FIELDISSELECTEDSATURDAY + " INTEGER,"
                + FIELDCREATEDDATE + " INTEGER,"
                + FIELDMEDICINETAKEN + " INTEGER,"
                + FIELDMEDICINESKIPPED + " INTEGER,"
                + FIELDNEXTNOTIFICATION + " INTEGER,"
                + FIELDNEXTMEDICINECOUNT + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
