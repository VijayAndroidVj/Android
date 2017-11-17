package com.example.chandru.myadmin;

/**
 * Created by vijay on 28/9/17.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.chandru.myadmin.Pojo.DashBoard;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DashboardDB";

    // Contacts table name
    private static final String TABLE_DASHBOARD = "dashboard";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String EMAIL_ID = "EMAIL_ID";
    private static final String FLAT_AREA ="FLAT_AREA";
    private static final String FLAT_NO = "FLAT_NO";
    private static final String FLOOR_NO = "FLOOR_NO";
    private static final String MEMBER_CODE = "MEMBER_CODE";
    private static final String MEMBER_NAME = "MEMBER_NAME";
    private static final String MOBILE_NO = "MOBILE_NO";
    private static final String NO_OF_PARKING ="NO_OF_PARKING";
    private static final String SELF_OCCUPIED ="SELF_OCCUPIED";
    private static final String SOCIETY_CODE = "SOCIETY_CODE";
    private static final String STATUS ="STATUS" ;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DASHBOARD + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + EMAIL_ID + " TEXT"+ FLAT_AREA + " TEXT" + FLAT_NO + " TEXT" + FLOOR_NO + " TEXT"
                + MEMBER_CODE + " TEXT" + MEMBER_NAME + " TEXT" + MOBILE_NO + " TEXT" + NO_OF_PARKING + " TEXT" + SELF_OCCUPIED + " TEXT"
                + SOCIETY_CODE + " TEXT" + STATUS + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addImage(String EMAIL_IDs,String FLAT_AREAs,String FLAT_NOs,String FLOOR_NOs,String MEMBER_CODEs, String MEMBER_NAMEs,String MOBILE_NOs
            ,String NO_OF_PARKINGs,String SELF_OCCUPIEDs,String SOCIETY_CODEs,String STATUSs) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMAIL_ID, EMAIL_IDs); // Contact Name
        values.put(FLAT_AREA, FLAT_AREAs);
        values.put(FLAT_NO, FLAT_NOs);
        values.put(FLOOR_NO, FLOOR_NOs);
        values.put(MEMBER_CODE, MEMBER_CODEs);
        values.put(MEMBER_NAME, MEMBER_NAMEs);
        values.put(MOBILE_NO, MOBILE_NOs);
        values.put(NO_OF_PARKING, NO_OF_PARKINGs);
        values.put(SELF_OCCUPIED, SELF_OCCUPIEDs);
        values.put(SOCIETY_CODE, SOCIETY_CODEs);
        values.put(STATUS, STATUSs);
        // Inserting Row
        db.insert(TABLE_DASHBOARD, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Contacts
    public List<DashBoard> getAllContacts() {
        List<DashBoard> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DASHBOARD;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //String image = (cursor.getString(1));
                // Adding contact to list
                DashBoard contact = new DashBoard();
                contact.setEMAIL_ID(cursor.getString(0));
                contact.setFLAT_AREA(cursor.getString(1));
                contact.setFLAT_NO(cursor.getString(2));
                contact.setFLOOR_NO(cursor.getString(3));
                contact.setMEMBER_NAME(cursor.getString(4));
                contact.setMEMBER_CODE(cursor.getString(5));
                contact.setMOBILE_NO(cursor.getString(6));
                contact.setNO_OF_PARKING(cursor.getString(7));
                contact.setSELF_OCCUPIED(cursor.getString(8));
                contact.setSOCIETY_CODE(cursor.getString(9));
                contact.setSTATUS(cursor.getString(10));



                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    DashBoard getContact(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DASHBOARD, new String[] { KEY_ID,
                         EMAIL_ID, FLAT_AREA, FLAT_NO, FLOOR_NO,MEMBER_CODE, MEMBER_NAME, MOBILE_NO
                        ,NO_OF_PARKING, SELF_OCCUPIED, SOCIETY_CODE, STATUS }, EMAIL_ID + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DashBoard contact = new DashBoard(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)
                , cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));
        // return contact
        return contact;
    }


    // Deleting single contact
   /* public void deleteContact(String contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DASHBOARD, KEY_IMAGE + " = ?",
                new String[]{String.valueOf(contact)});
        db.close();
    }*/


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DASHBOARD;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

}