package com.android.mymedicine.java.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.mymedicine.java.model.MedicineModel;
import com.android.mymedicine.java.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static com.android.mymedicine.java.utils.Utils.getMedicineCount;
import static com.android.mymedicine.java.utils.Utils.getMinimumValue;

public class DbService {
    String TAG = "DbService";
    Context context;
    static SQLiteDatabase db;

    public DbService(Context context) {
        this.context = context;
        DbStructure dbStructure = new DbStructure(context);
        db = dbStructure.getWritableDatabase();
    }

    private SQLiteDatabase open() {
        return db;
    }

    private void close() {
        db.close();
    }

    public boolean saveMedicine(MedicineModel medicineModel) {
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + DbStructure.FIELDMEDICINEID + " = '" + medicineModel.getMedicineId() + "'";
        ContentValues cv = createContentValues(medicineModel);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            int status = db.update(DbStructure.TBLMEDICINE, cv, DbStructure.FIELDMEDICINEID + " = ?",
                    new String[]{cv.getAsString(DbStructure.FIELDMEDICINEID)});

            if (status > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            long status = db.insert(DbStructure.TBLMEDICINE, null, cv);
            if (status > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateNotificationTime(long id) {
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + DbStructure.FIELDMEDICINEID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        MedicineModel medicineModel = new MedicineModel();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    medicineModel = setToMessageModel(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        ArrayList<Long> list = Utils.setTimeSlot(medicineModel);
        ArrayList<Long> sortedlist = list;
        Collections.sort(sortedlist);
        long nextNotificationTime = getMinimumValue(sortedlist, medicineModel);
        int nextMedicineCount = getMedicineCount(list, nextNotificationTime, medicineModel);
        db.execSQL("UPDATE " + DbStructure.TBLMEDICINE + " SET " + DbStructure.FIELDNEXTNOTIFICATION + " = " + nextNotificationTime + " , " + DbStructure.FIELDNEXTMEDICINECOUNT + " = " + nextMedicineCount + " WHERE " + DbStructure.FIELDMEDICINEID + " = '" + id + "'");
    }

    private ContentValues createContentValues(MedicineModel medicineModel) {
        ContentValues cv = new ContentValues();
        cv.put(DbStructure.FIELDMEDICINEID, medicineModel.getMedicineId());
        cv.put(DbStructure.FIELDMEDICINENAME, medicineModel.getMedicineName());
        cv.put(DbStructure.FIELDDOSAGE, medicineModel.getDosage());
        cv.put(DbStructure.FIELDDOSAGETYPE, medicineModel.getDosageType());
        cv.put(DbStructure.FIELDUNIT, medicineModel.getUnit());
        cv.put(DbStructure.FIELDLOCALPATH, medicineModel.getLocalImagePath());
        cv.put(DbStructure.FIELDSERVERPATH, medicineModel.getServerImagePath());
        cv.put(DbStructure.FIELDFREQUENCY, medicineModel.getFrequency());
        cv.put(DbStructure.FIELDSTARTDATE, medicineModel.getStartDate());
        cv.put(DbStructure.FIELDENDDATE, medicineModel.getEndDate());
        cv.put(DbStructure.FIELDHOWMANYTIMES, medicineModel.getHowManyTimes());
        cv.put(DbStructure.FIELDTIMESLOT1, medicineModel.getTimeSlot_1());
        cv.put(DbStructure.FIELDTIMESLOT2, medicineModel.getTimeSlot_2());
        cv.put(DbStructure.FIELDTIMESLOT3, medicineModel.getTimeSlot_3());
        cv.put(DbStructure.FIELDTIMESLOT4, medicineModel.getTimeSlot_4());
        cv.put(DbStructure.FIELDTIMESLOT5, medicineModel.getTimeSlot_5());
        cv.put(DbStructure.FIELDTIMESLOT6, medicineModel.getTimeSlot_6());
        cv.put(DbStructure.FIELDTIMESLOT7, medicineModel.getTimeSlot_7());
        cv.put(DbStructure.FIELDTIMESLOT8, medicineModel.getTimeSlot_8());
        cv.put(DbStructure.FIELDTIMESLOT9, medicineModel.getTimeSlot_9());
        cv.put(DbStructure.FIELDTIMESLOT10, medicineModel.getTimeSlot_10());
        cv.put(DbStructure.FIELDTIMESLOT11, medicineModel.getTimeSlot_11());
        cv.put(DbStructure.FIELDTIMESLOT12, medicineModel.getTimeSlot_12());
        cv.put(DbStructure.FIELDTIMESLOT13, medicineModel.getTimeSlot_13());
        cv.put(DbStructure.FIELDTIMESLOT14, medicineModel.getTimeSlot_14());
        cv.put(DbStructure.FIELDTIMESLOT15, medicineModel.getTimeSlot_15());
        cv.put(DbStructure.FIELDTIMESLOT16, medicineModel.getTimeSlot_16());
        cv.put(DbStructure.FIELDTIMESLOT17, medicineModel.getTimeSlot_17());
        cv.put(DbStructure.FIELDTIMESLOT18, medicineModel.getTimeSlot_18());
        cv.put(DbStructure.FIELDTIMESLOT19, medicineModel.getTimeSlot_19());
        cv.put(DbStructure.FIELDTIMESLOT20, medicineModel.getTimeSlot_20());
        cv.put(DbStructure.FIELDTIMESLOT21, medicineModel.getTimeSlot_21());
        cv.put(DbStructure.FIELDTIMESLOT22, medicineModel.getTimeSlot_22());
        cv.put(DbStructure.FIELDTIMESLOT23, medicineModel.getTimeSlot_23());
        cv.put(DbStructure.FIELDTIMESLOT24, medicineModel.getTimeSlot_24());
        cv.put(DbStructure.FIELDCOUNTSLOT1, medicineModel.getMedicineCountSlot_1());
        cv.put(DbStructure.FIELDCOUNTSLOT2, medicineModel.getMedicineCountSlot_2());
        cv.put(DbStructure.FIELDCOUNTSLOT3, medicineModel.getMedicineCountSlot_3());
        cv.put(DbStructure.FIELDCOUNTSLOT4, medicineModel.getMedicineCountSlot_4());
        cv.put(DbStructure.FIELDCOUNTSLOT5, medicineModel.getMedicineCountSlot_5());
        cv.put(DbStructure.FIELDCOUNTSLOT6, medicineModel.getMedicineCountSlot_6());
        cv.put(DbStructure.FIELDCOUNTSLOT7, medicineModel.getMedicineCountSlot_7());
        cv.put(DbStructure.FIELDCOUNTSLOT8, medicineModel.getMedicineCountSlot_8());
        cv.put(DbStructure.FIELDCOUNTSLOT9, medicineModel.getMedicineCountSlot_9());
        cv.put(DbStructure.FIELDCOUNTSLOT10, medicineModel.getMedicineCountSlot_10());
        cv.put(DbStructure.FIELDCOUNTSLOT11, medicineModel.getMedicineCountSlot_11());
        cv.put(DbStructure.FIELDCOUNTSLOT12, medicineModel.getMedicineCountSlot_12());
        cv.put(DbStructure.FIELDCOUNTSLOT13, medicineModel.getMedicineCountSlot_13());
        cv.put(DbStructure.FIELDCOUNTSLOT14, medicineModel.getMedicineCountSlot_14());
        cv.put(DbStructure.FIELDCOUNTSLOT15, medicineModel.getMedicineCountSlot_15());
        cv.put(DbStructure.FIELDCOUNTSLOT16, medicineModel.getMedicineCountSlot_16());
        cv.put(DbStructure.FIELDCOUNTSLOT17, medicineModel.getMedicineCountSlot_17());
        cv.put(DbStructure.FIELDCOUNTSLOT18, medicineModel.getMedicineCountSlot_18());
        cv.put(DbStructure.FIELDCOUNTSLOT19, medicineModel.getMedicineCountSlot_19());
        cv.put(DbStructure.FIELDCOUNTSLOT20, medicineModel.getMedicineCountSlot_20());
        cv.put(DbStructure.FIELDCOUNTSLOT21, medicineModel.getMedicineCountSlot_21());
        cv.put(DbStructure.FIELDCOUNTSLOT22, medicineModel.getMedicineCountSlot_22());
        cv.put(DbStructure.FIELDCOUNTSLOT23, medicineModel.getMedicineCountSlot_23());
        cv.put(DbStructure.FIELDCOUNTSLOT24, medicineModel.getMedicineCountSlot_24());
        cv.put(DbStructure.FIELDISSELECTEDSUNDAY, medicineModel.getIsSelectedsunday());
        cv.put(DbStructure.FIELDISSELECTEDMONDAY, medicineModel.getIsSelectedmonday());
        cv.put(DbStructure.FIELDISSELECTEDTUESDAY, medicineModel.getIsSelectedtuesday());
        cv.put(DbStructure.FIELDISSELECTEDWEDNESDAY, medicineModel.getIsSelectedwednesday());
        cv.put(DbStructure.FIELDISSELECTEDTHURSDAY, medicineModel.getIsSelectedthursday());
        cv.put(DbStructure.FIELDISSELECTEDFRIDAY, medicineModel.getIsSelectedfriday());
        cv.put(DbStructure.FIELDISSELECTEDSATURDAY, medicineModel.getIsSelectedsaturday());
        cv.put(DbStructure.FIELDCREATEDDATE, medicineModel.getCreatedDatetime());
        cv.put(DbStructure.FIELDMEDICINETAKEN, medicineModel.getMedicineTaken());
        cv.put(DbStructure.FIELDMEDICINESKIPPED, medicineModel.getMedicineSkipped());
        cv.put(DbStructure.FIELDNEXTNOTIFICATION, medicineModel.getNextNotificationTime());
        cv.put(DbStructure.FIELDNEXTMEDICINECOUNT, medicineModel.getNextMedicineCount());
        return cv;
    }

    public MedicineModel getMedicineDetail(long id) {
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + DbStructure.FIELDMEDICINEID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        MedicineModel medicineModel = new MedicineModel();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    medicineModel = setToMessageModel(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return medicineModel;
    }

    public void updateMedicine(String field, long id) {
        db.execSQL("UPDATE " + DbStructure.TBLMEDICINE + " SET " + field + " = " + field + " + 1 WHERE " + DbStructure.FIELDMEDICINEID + " = '" + id + "'");
    }

    public void updateMedicine(long id) {
        db.execSQL("DELETE FROM " + DbStructure.TBLMEDICINE + " WHERE " + DbStructure.FIELDMEDICINEID + " = '" + id + "'");
    }


    public ArrayList<MedicineModel> getNextMedicineList() {
        Calendar cal = Calendar.getInstance();
        long currentDatetimeMillis = convertDate(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + currentDatetimeMillis + " BETWEEN " + DbStructure.FIELDSTARTDATE + " AND " + DbStructure.FIELDENDDATE + " ORDER BY " + DbStructure.FIELDNEXTNOTIFICATION + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<MedicineModel> medicineModel = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    medicineModel.add(setToMessageModel(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return medicineModel;
    }

    public ArrayList<MedicineModel> getFutureMedicineList() {
        Calendar cal = Calendar.getInstance();
        long currentDatetimeMillis = convertDate(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + DbStructure.FIELDSTARTDATE + " > " + currentDatetimeMillis + " ORDER BY " + DbStructure.FIELDSTARTDATE + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<MedicineModel> medicineModel = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    medicineModel.add(setToMessageModel(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return medicineModel;
    }

    public ArrayList<MedicineModel> getHistoryMedicineList() {
        Calendar cal = Calendar.getInstance();
        long currentDatetimeMillis = convertDate(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));
        String selectQuery = "SELECT * FROM " + DbStructure.TBLMEDICINE + " WHERE " + currentDatetimeMillis + " > " + DbStructure.FIELDENDDATE + " ORDER BY " + DbStructure.FIELDENDDATE + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<MedicineModel> medicineModel = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    medicineModel.add(setToMessageModel(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return medicineModel;
    }

    private long convertDate(String date) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    private MedicineModel setToMessageModel(Cursor cursor) {
        MedicineModel medicineModel = new MedicineModel();
        medicineModel.setMedicineId(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDMEDICINEID)));
        medicineModel.setMedicineName(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDMEDICINENAME)));
        medicineModel.setDosage(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDDOSAGE)));
        medicineModel.setDosageType(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDDOSAGETYPE)));
        medicineModel.setUnit(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDUNIT)));
        medicineModel.setLocalImagePath(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDLOCALPATH)));
        medicineModel.setServerImagePath(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDSERVERPATH)));
        medicineModel.setFrequency(cursor.getString(cursor.getColumnIndex(DbStructure.FIELDFREQUENCY)));
        medicineModel.setStartDate(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDSTARTDATE)));
        medicineModel.setEndDate(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDENDDATE)));
        medicineModel.setHowManyTimes(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDHOWMANYTIMES)));
        medicineModel.setTimeSlot_1(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT1)));
        medicineModel.setTimeSlot_2(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT2)));
        medicineModel.setTimeSlot_3(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT3)));
        medicineModel.setTimeSlot_4(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT4)));
        medicineModel.setTimeSlot_5(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT5)));
        medicineModel.setTimeSlot_6(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT6)));
        medicineModel.setTimeSlot_7(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT7)));
        medicineModel.setTimeSlot_8(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT8)));
        medicineModel.setTimeSlot_9(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT9)));
        medicineModel.setTimeSlot_10(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT10)));
        medicineModel.setTimeSlot_11(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT11)));
        medicineModel.setTimeSlot_12(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT12)));
        medicineModel.setTimeSlot_13(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT13)));
        medicineModel.setTimeSlot_14(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT14)));
        medicineModel.setTimeSlot_15(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT15)));
        medicineModel.setTimeSlot_16(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT16)));
        medicineModel.setTimeSlot_17(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT17)));
        medicineModel.setTimeSlot_18(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT18)));
        medicineModel.setTimeSlot_19(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT19)));
        medicineModel.setTimeSlot_20(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT20)));
        medicineModel.setTimeSlot_21(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT21)));
        medicineModel.setTimeSlot_22(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT22)));
        medicineModel.setTimeSlot_23(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT23)));
        medicineModel.setTimeSlot_24(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDTIMESLOT24)));
        medicineModel.setMedicineCountSlot_1(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT1)));
        medicineModel.setMedicineCountSlot_2(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT2)));
        medicineModel.setMedicineCountSlot_3(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT3)));
        medicineModel.setMedicineCountSlot_4(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT4)));
        medicineModel.setMedicineCountSlot_5(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT5)));
        medicineModel.setMedicineCountSlot_6(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT6)));
        medicineModel.setMedicineCountSlot_7(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT7)));
        medicineModel.setMedicineCountSlot_8(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT8)));
        medicineModel.setMedicineCountSlot_9(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT9)));
        medicineModel.setMedicineCountSlot_10(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT10)));
        medicineModel.setMedicineCountSlot_11(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT11)));
        medicineModel.setMedicineCountSlot_12(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT12)));
        medicineModel.setMedicineCountSlot_13(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT13)));
        medicineModel.setMedicineCountSlot_14(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT14)));
        medicineModel.setMedicineCountSlot_15(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT15)));
        medicineModel.setMedicineCountSlot_16(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT16)));
        medicineModel.setMedicineCountSlot_17(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT17)));
        medicineModel.setMedicineCountSlot_18(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT18)));
        medicineModel.setMedicineCountSlot_19(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT19)));
        medicineModel.setMedicineCountSlot_20(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT20)));
        medicineModel.setMedicineCountSlot_21(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT21)));
        medicineModel.setMedicineCountSlot_22(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT22)));
        medicineModel.setMedicineCountSlot_23(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT23)));
        medicineModel.setMedicineCountSlot_24(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDCOUNTSLOT24)));
        medicineModel.setIsSelectedsunday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDSUNDAY)) > 0);
        medicineModel.setIsSelectedmonday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDMONDAY)) > 0);
        medicineModel.setIsSelectedtuesday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDTUESDAY)) > 0);
        medicineModel.setIsSelectedwednesday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDWEDNESDAY)) > 0);
        medicineModel.setIsSelectedthursday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDTHURSDAY)) > 0);
        medicineModel.setIsSelectedfriday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDFRIDAY)) > 0);
        medicineModel.setIsSelectedsaturday(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDISSELECTEDSATURDAY)) > 0);
        medicineModel.setCreatedDatetime(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDCREATEDDATE)));
        medicineModel.setMedicineTaken(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDMEDICINETAKEN)));
        medicineModel.setMedicineSkipped(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDMEDICINESKIPPED)));
        medicineModel.setNextNotificationTime(cursor.getLong(cursor.getColumnIndex(DbStructure.FIELDNEXTNOTIFICATION)));
        medicineModel.setNextMedicineCount(cursor.getInt(cursor.getColumnIndex(DbStructure.FIELDNEXTMEDICINECOUNT)));
        return medicineModel;
    }
}
