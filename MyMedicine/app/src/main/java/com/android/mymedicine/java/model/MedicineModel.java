package com.android.mymedicine.java.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineModel implements Parcelable {

    private long medicine_id;
    private String medicine_name;
    private int dosage;
    private String dosage_type;
    private String unit = "";
    private String local_image_path;
    private String server_image_path;
    private String frequency;
    private long start_date;
    private long end_date;
    private int how_many_times;
    private long time_slot_1 = 0;
    private long time_slot_2 = 0;
    private long time_slot_3 = 0;
    private long time_slot_4 = 0;
    private long time_slot_5 = 0;
    private long time_slot_6 = 0;
    private long time_slot_7 = 0;
    private long time_slot_8 = 0;
    private long time_slot_9 = 0;
    private long time_slot_10 = 0;
    private long time_slot_11 = 0;
    private long time_slot_12 = 0;
    private long time_slot_13 = 0;
    private long time_slot_14 = 0;
    private long time_slot_15 = 0;
    private long time_slot_16 = 0;
    private long time_slot_17 = 0;
    private long time_slot_18 = 0;
    private long time_slot_19 = 0;
    private long time_slot_20 = 0;
    private long time_slot_21 = 0;
    private long time_slot_22 = 0;
    private long time_slot_23 = 0;
    private long time_slot_24 = 0;
    private int medicine_count_slot_1 = 0;
    private int medicine_count_slot_2 = 0;
    private int medicine_count_slot_3 = 0;
    private int medicine_count_slot_4 = 0;
    private int medicine_count_slot_5 = 0;
    private int medicine_count_slot_6 = 0;
    private int medicine_count_slot_7 = 0;
    private int medicine_count_slot_8 = 0;
    private int medicine_count_slot_9 = 0;
    private int medicine_count_slot_10 = 0;
    private int medicine_count_slot_11 = 0;
    private int medicine_count_slot_12 = 0;
    private int medicine_count_slot_13 = 0;
    private int medicine_count_slot_14 = 0;
    private int medicine_count_slot_15 = 0;
    private int medicine_count_slot_16 = 0;
    private int medicine_count_slot_17 = 0;
    private int medicine_count_slot_18 = 0;
    private int medicine_count_slot_19 = 0;
    private int medicine_count_slot_20 = 0;
    private int medicine_count_slot_21 = 0;
    private int medicine_count_slot_22 = 0;
    private int medicine_count_slot_23 = 0;
    private int medicine_count_slot_24 = 0;
    private boolean is_selected_sunday;
    private boolean is_selected_monday;
    private boolean is_selected_tuesday;
    private boolean is_selected_wednesday;
    private boolean is_selected_thursday;
    private boolean is_selected_friday;
    private boolean is_selected_saturday;
    private long created_datetime;
    private int medicine_taken;
    private int medicine_skipped;
    private long next_notification_time = 0;
    private int next_medicine_count = 0;

    public long getMedicineId() {
        return medicine_id;
    }

    public void setMedicineId(long medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicineName() {
        return medicine_name;
    }

    public void setMedicineName(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public String getDosageType() {
        return dosage_type;
    }

    public void setDosageType(String dosage_type) {
        this.dosage_type = dosage_type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLocalImagePath() {
        return local_image_path;
    }

    public void setLocalImagePath(String local_image_path) {
        this.local_image_path = local_image_path;
    }

    public String getServerImagePath() {
        return server_image_path;
    }

    public void setServerImagePath(String server_image_path) {
        this.server_image_path = server_image_path;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public long getStartDate() {
        return start_date;
    }

    public void setStartDate(long start_date) {
        this.start_date = start_date;
    }

    public long getEndDate() {
        return end_date;
    }

    public void setEndDate(long end_date) {
        this.end_date = end_date;
    }

    public int getHowManyTimes() {
        return how_many_times;
    }

    public void setHowManyTimes(int how_many_times) {
        this.how_many_times = how_many_times;
    }

    public long getTimeSlot_1() {
        return Math.abs(time_slot_1);
    }

    public void setTimeSlot_1(long time_slot_1) {
        this.time_slot_1 = time_slot_1;
    }

    public long getTimeSlot_2() {
        return time_slot_2;
    }

    public void setTimeSlot_2(long time_slot_2) {
        this.time_slot_2 = time_slot_2;
    }

    public long getTimeSlot_3() {
        return time_slot_3;
    }

    public void setTimeSlot_3(long time_slot_3) {
        this.time_slot_3 = time_slot_3;
    }

    public long getTimeSlot_4() {
        return time_slot_4;
    }

    public void setTimeSlot_4(long time_slot_4) {
        this.time_slot_4 = time_slot_4;
    }

    public long getTimeSlot_5() {
        return time_slot_5;
    }

    public void setTimeSlot_5(long time_slot_5) {
        this.time_slot_5 = time_slot_5;
    }

    public long getTimeSlot_6() {
        return time_slot_6;
    }

    public void setTimeSlot_6(long time_slot_6) {
        this.time_slot_6 = time_slot_6;
    }

    public long getTimeSlot_7() {
        return time_slot_7;
    }

    public void setTimeSlot_7(long time_slot_7) {
        this.time_slot_7 = time_slot_7;
    }

    public long getTimeSlot_8() {
        return time_slot_8;
    }

    public void setTimeSlot_8(long time_slot_8) {
        this.time_slot_8 = time_slot_8;
    }

    public long getTimeSlot_9() {
        return time_slot_9;
    }

    public void setTimeSlot_9(long time_slot_9) {
        this.time_slot_9 = time_slot_9;
    }

    public long getTimeSlot_10() {
        return time_slot_10;
    }

    public void setTimeSlot_10(long time_slot_10) {
        this.time_slot_10 = time_slot_10;
    }

    public long getTimeSlot_11() {
        return time_slot_11;
    }

    public void setTimeSlot_11(long time_slot_11) {
        this.time_slot_11 = time_slot_11;
    }

    public long getTimeSlot_12() {
        return time_slot_12;
    }

    public void setTimeSlot_12(long time_slot_12) {
        this.time_slot_12 = time_slot_12;
    }

    public long getTimeSlot_13() {
        return time_slot_13;
    }

    public void setTimeSlot_13(long time_slot_13) {
        this.time_slot_13 = time_slot_13;
    }

    public long getTimeSlot_14() {
        return time_slot_14;
    }

    public void setTimeSlot_14(long time_slot_14) {
        this.time_slot_14 = time_slot_14;
    }

    public long getTimeSlot_15() {
        return time_slot_15;
    }

    public void setTimeSlot_15(long time_slot_15) {
        this.time_slot_15 = time_slot_15;
    }

    public long getTimeSlot_16() {
        return time_slot_16;
    }

    public void setTimeSlot_16(long time_slot_16) {
        this.time_slot_16 = time_slot_16;
    }

    public long getTimeSlot_17() {
        return time_slot_17;
    }

    public void setTimeSlot_17(long time_slot_17) {
        this.time_slot_17 = time_slot_17;
    }

    public long getTimeSlot_18() {
        return time_slot_18;
    }

    public void setTimeSlot_18(long time_slot_18) {
        this.time_slot_18 = time_slot_18;
    }

    public long getTimeSlot_19() {
        return time_slot_19;
    }

    public void setTimeSlot_19(long time_slot_19) {
        this.time_slot_19 = time_slot_19;
    }

    public long getTimeSlot_20() {
        return time_slot_20;
    }

    public void setTimeSlot_20(long time_slot_20) {
        this.time_slot_20 = time_slot_20;
    }

    public long getTimeSlot_21() {
        return time_slot_21;
    }

    public void setTimeSlot_21(long time_slot_21) {
        this.time_slot_21 = time_slot_21;
    }

    public long getTimeSlot_22() {
        return time_slot_22;
    }

    public void setTimeSlot_22(long time_slot_22) {
        this.time_slot_22 = time_slot_22;
    }

    public long getTimeSlot_23() {
        return time_slot_23;
    }

    public void setTimeSlot_23(long time_slot_23) {
        this.time_slot_23 = time_slot_23;
    }

    public long getTimeSlot_24() {
        return time_slot_24;
    }

    public void setTimeSlot_24(long time_slot_24) {
        this.time_slot_24 = time_slot_24;
    }

    public int getMedicineCountSlot_1() {
        return medicine_count_slot_1;
    }

    public void setMedicineCountSlot_1(int medicine_count_slot_1) {
        this.medicine_count_slot_1 = medicine_count_slot_1;
    }

    public int getMedicineCountSlot_2() {
        return medicine_count_slot_2;
    }

    public void setMedicineCountSlot_2(int medicine_count_slot_2) {
        this.medicine_count_slot_2 = medicine_count_slot_2;
    }

    public int getMedicineCountSlot_3() {
        return medicine_count_slot_3;
    }

    public void setMedicineCountSlot_3(int medicine_count_slot_3) {
        this.medicine_count_slot_3 = medicine_count_slot_3;
    }

    public int getMedicineCountSlot_4() {
        return medicine_count_slot_4;
    }

    public void setMedicineCountSlot_4(int medicine_count_slot_4) {
        this.medicine_count_slot_4 = medicine_count_slot_4;
    }

    public int getMedicineCountSlot_5() {
        return medicine_count_slot_5;
    }

    public void setMedicineCountSlot_5(int medicine_count_slot_5) {
        this.medicine_count_slot_5 = medicine_count_slot_5;
    }

    public int getMedicineCountSlot_6() {
        return medicine_count_slot_6;
    }

    public void setMedicineCountSlot_6(int medicine_count_slot_6) {
        this.medicine_count_slot_6 = medicine_count_slot_6;
    }

    public int getMedicineCountSlot_7() {
        return medicine_count_slot_7;
    }

    public void setMedicineCountSlot_7(int medicine_count_slot_7) {
        this.medicine_count_slot_7 = medicine_count_slot_7;
    }

    public int getMedicineCountSlot_8() {
        return medicine_count_slot_8;
    }

    public void setMedicineCountSlot_8(int medicine_count_slot_8) {
        this.medicine_count_slot_8 = medicine_count_slot_8;
    }

    public int getMedicineCountSlot_9() {
        return medicine_count_slot_9;
    }

    public void setMedicineCountSlot_9(int medicine_count_slot_9) {
        this.medicine_count_slot_9 = medicine_count_slot_9;
    }

    public int getMedicineCountSlot_10() {
        return medicine_count_slot_10;
    }

    public void setMedicineCountSlot_10(int medicine_count_slot_10) {
        this.medicine_count_slot_10 = medicine_count_slot_10;
    }

    public int getMedicineCountSlot_11() {
        return medicine_count_slot_11;
    }

    public void setMedicineCountSlot_11(int medicine_count_slot_11) {
        this.medicine_count_slot_11 = medicine_count_slot_11;
    }

    public int getMedicineCountSlot_12() {
        return medicine_count_slot_12;
    }

    public void setMedicineCountSlot_12(int medicine_count_slot_12) {
        this.medicine_count_slot_12 = medicine_count_slot_12;
    }

    public int getMedicineCountSlot_13() {
        return medicine_count_slot_13;
    }

    public void setMedicineCountSlot_13(int medicine_count_slot_13) {
        this.medicine_count_slot_13 = medicine_count_slot_13;
    }

    public int getMedicineCountSlot_14() {
        return medicine_count_slot_14;
    }

    public void setMedicineCountSlot_14(int medicine_count_slot_14) {
        this.medicine_count_slot_14 = medicine_count_slot_14;
    }

    public int getMedicineCountSlot_15() {
        return medicine_count_slot_15;
    }

    public void setMedicineCountSlot_15(int medicine_count_slot_15) {
        this.medicine_count_slot_15 = medicine_count_slot_15;
    }

    public int getMedicineCountSlot_16() {
        return medicine_count_slot_16;
    }

    public void setMedicineCountSlot_16(int medicine_count_slot_16) {
        this.medicine_count_slot_16 = medicine_count_slot_16;
    }

    public int getMedicineCountSlot_17() {
        return medicine_count_slot_17;
    }

    public void setMedicineCountSlot_17(int medicine_count_slot_17) {
        this.medicine_count_slot_17 = medicine_count_slot_17;
    }

    public int getMedicineCountSlot_18() {
        return medicine_count_slot_18;
    }

    public void setMedicineCountSlot_18(int medicine_count_slot_18) {
        this.medicine_count_slot_18 = medicine_count_slot_18;
    }

    public int getMedicineCountSlot_19() {
        return medicine_count_slot_19;
    }

    public void setMedicineCountSlot_19(int medicine_count_slot_19) {
        this.medicine_count_slot_19 = medicine_count_slot_19;
    }

    public int getMedicineCountSlot_20() {
        return medicine_count_slot_20;
    }

    public void setMedicineCountSlot_20(int medicine_count_slot_20) {
        this.medicine_count_slot_20 = medicine_count_slot_20;
    }

    public int getMedicineCountSlot_21() {
        return medicine_count_slot_21;
    }

    public void setMedicineCountSlot_21(int medicine_count_slot_21) {
        this.medicine_count_slot_21 = medicine_count_slot_21;
    }

    public int getMedicineCountSlot_22() {
        return medicine_count_slot_22;
    }

    public void setMedicineCountSlot_22(int medicine_count_slot_22) {
        this.medicine_count_slot_22 = medicine_count_slot_22;
    }

    public int getMedicineCountSlot_23() {
        return medicine_count_slot_23;
    }

    public void setMedicineCountSlot_23(int medicine_count_slot_23) {
        this.medicine_count_slot_23 = medicine_count_slot_23;
    }

    public int getMedicineCountSlot_24() {
        return medicine_count_slot_24;
    }

    public void setMedicineCountSlot_24(int medicine_count_slot_24) {
        this.medicine_count_slot_24 = medicine_count_slot_24;
    }

    public boolean getIsSelectedsunday() {
        return is_selected_sunday;
    }

    public void setIsSelectedsunday(boolean is_selected_sunday) {
        this.is_selected_sunday = is_selected_sunday;
    }

    public boolean getIsSelectedmonday() {
        return is_selected_monday;
    }

    public void setIsSelectedmonday(boolean is_selected_monday) {
        this.is_selected_monday = is_selected_monday;
    }

    public boolean getIsSelectedtuesday() {
        return is_selected_tuesday;
    }

    public void setIsSelectedtuesday(boolean is_selected_tuesday) {
        this.is_selected_tuesday = is_selected_tuesday;
    }

    public boolean getIsSelectedwednesday() {
        return is_selected_wednesday;
    }

    public void setIsSelectedwednesday(boolean is_selected_wednesday) {
        this.is_selected_wednesday = is_selected_wednesday;
    }

    public boolean getIsSelectedthursday() {
        return is_selected_thursday;
    }

    public void setIsSelectedthursday(boolean is_selected_thursday) {
        this.is_selected_thursday = is_selected_thursday;
    }

    public boolean getIsSelectedfriday() {
        return is_selected_friday;
    }

    public void setIsSelectedfriday(boolean is_selected_friday) {
        this.is_selected_friday = is_selected_friday;
    }

    public boolean getIsSelectedsaturday() {
        return is_selected_saturday;
    }

    public void setIsSelectedsaturday(boolean is_selected_saturday) {
        this.is_selected_saturday = is_selected_saturday;
    }

    public long getCreatedDatetime() {
        return created_datetime;
    }

    public void setCreatedDatetime(long created_datetime) {
        this.created_datetime = created_datetime;
    }

    public int getMedicineTaken() {
        return medicine_taken;
    }

    public void setMedicineTaken(int medicine_taken) {
        this.medicine_taken = medicine_taken;
    }

    public int getMedicineSkipped() {
        return medicine_skipped;
    }

    public void setMedicineSkipped(int medicine_skipped) {
        this.medicine_skipped = medicine_skipped;
    }

    public long getNextNotificationTime() {
        return next_notification_time;
    }

    public void setNextNotificationTime(long next_notification_time) {
        this.next_notification_time = next_notification_time;
    }

    public int getNextMedicineCount() {
        return next_medicine_count;
    }

    public void setNextMedicineCount(int next_medicine_count) {
        this.next_medicine_count = next_medicine_count;
    }

    public static Creator<MedicineModel> getCREATOR() {
        return CREATOR;
    }

    public MedicineModel() {

    }


    public MedicineModel(Parcel in) {
        medicine_id = in.readLong();
        medicine_name = in.readString();
        dosage = in.readInt();
        dosage_type = in.readString();
        unit = in.readString();
        local_image_path = in.readString();
        server_image_path = in.readString();
        frequency = in.readString();
        start_date = in.readInt();
        end_date = in.readInt();
        how_many_times = in.readInt();
        time_slot_1 = in.readLong();
        time_slot_2 = in.readLong();
        time_slot_3 = in.readLong();
        time_slot_4 = in.readLong();
        time_slot_5 = in.readLong();
        time_slot_6 = in.readLong();
        time_slot_7 = in.readLong();
        time_slot_8 = in.readLong();
        time_slot_9 = in.readLong();
        time_slot_10 = in.readLong();
        time_slot_11 = in.readLong();
        time_slot_12 = in.readLong();
        time_slot_13 = in.readLong();
        time_slot_14 = in.readLong();
        time_slot_15 = in.readLong();
        time_slot_16 = in.readLong();
        time_slot_17 = in.readLong();
        time_slot_18 = in.readLong();
        time_slot_19 = in.readLong();
        time_slot_20 = in.readLong();
        time_slot_21 = in.readLong();
        time_slot_22 = in.readLong();
        time_slot_23 = in.readLong();
        time_slot_24 = in.readLong();
        medicine_count_slot_1 = in.readInt();
        medicine_count_slot_2 = in.readInt();
        medicine_count_slot_3 = in.readInt();
        medicine_count_slot_4 = in.readInt();
        medicine_count_slot_5 = in.readInt();
        medicine_count_slot_6 = in.readInt();
        medicine_count_slot_7 = in.readInt();
        medicine_count_slot_8 = in.readInt();
        medicine_count_slot_9 = in.readInt();
        medicine_count_slot_10 = in.readInt();
        medicine_count_slot_11 = in.readInt();
        medicine_count_slot_12 = in.readInt();
        medicine_count_slot_13 = in.readInt();
        medicine_count_slot_14 = in.readInt();
        medicine_count_slot_15 = in.readInt();
        medicine_count_slot_16 = in.readInt();
        medicine_count_slot_17 = in.readInt();
        medicine_count_slot_18 = in.readInt();
        medicine_count_slot_19 = in.readInt();
        medicine_count_slot_20 = in.readInt();
        medicine_count_slot_21 = in.readInt();
        medicine_count_slot_22 = in.readInt();
        medicine_count_slot_23 = in.readInt();
        medicine_count_slot_24 = in.readInt();
//        is_selected_sunday = in.readInt();
//        is_selected_monday = in.readInt();
//        is_selected_tuesday = in.readInt();
//        is_selected_wednesday = in.readInt();
//        is_selected_thursday = in.readInt();
//        is_selected_friday = in.readInt();
//        is_selected_saturday = in.readInt();
        created_datetime = in.readLong();
        medicine_taken = in.readInt();
        medicine_skipped = in.readInt();
        next_notification_time = in.readLong();
        next_medicine_count = in.readInt();
    }

    public static final Creator<MedicineModel> CREATOR = new Creator<MedicineModel>() {
        @Override
        public MedicineModel createFromParcel(Parcel in) {
            return new MedicineModel(in);
        }

        @Override
        public MedicineModel[] newArray(int size) {
            return new MedicineModel[size];
        }
    };

    @Override
    public String toString() {
        return "";
//        return "ChatMessage{" +
//                "message='" + message + '\'' +
//                ", date='" + date + '\'' +
//                ", time='" + time + '\'' +
//                ", status=" + messageSentOrReceived +
//                ", messageId=" + messageId +
//                ", isDone='" + messageDeliveryStatus + '\'' +
//                ", mode='" + mode + '\'' +
//                ", fileExt='" + fileExt + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", fileSize=" + fileSize +
//                ", mimeType='" + mimeType + '\'' +
//                ", thumbPath='" + thumbPath + '\'' +
//                ", filePath='" + filePath + '\'' +
//                ", durationTime='" + durationTime + '\'' +
//                ", uploadedResource=" + uploadedResource +
//                ", mediaLink='" + mediaLink + '\'' +
//                ", readMessage=" + readMessage +
//                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(medicine_id);
        dest.writeString(medicine_name);
        dest.writeInt(dosage);
        dest.writeString(dosage_type);
        dest.writeString(unit);
        dest.writeString(local_image_path);
        dest.writeString(server_image_path);
        dest.writeString(frequency);
        dest.writeLong(start_date);
        dest.writeLong(end_date);
        dest.writeLong(how_many_times);
        dest.writeLong(time_slot_1);
        dest.writeLong(time_slot_2);
        dest.writeLong(time_slot_3);
        dest.writeLong(time_slot_4);
        dest.writeLong(time_slot_5);
        dest.writeLong(time_slot_6);
        dest.writeLong(time_slot_7);
        dest.writeLong(time_slot_8);
        dest.writeLong(time_slot_9);
        dest.writeLong(time_slot_10);
        dest.writeLong(time_slot_11);
        dest.writeLong(time_slot_12);
        dest.writeLong(time_slot_13);
        dest.writeLong(time_slot_14);
        dest.writeLong(time_slot_15);
        dest.writeLong(time_slot_16);
        dest.writeLong(time_slot_17);
        dest.writeLong(time_slot_18);
        dest.writeLong(time_slot_19);
        dest.writeLong(time_slot_20);
        dest.writeLong(time_slot_21);
        dest.writeLong(time_slot_22);
        dest.writeLong(time_slot_23);
        dest.writeLong(time_slot_24);
        dest.writeInt(medicine_count_slot_1);
        dest.writeInt(medicine_count_slot_2);
        dest.writeInt(medicine_count_slot_3);
        dest.writeInt(medicine_count_slot_4);
        dest.writeInt(medicine_count_slot_5);
        dest.writeInt(medicine_count_slot_6);
        dest.writeInt(medicine_count_slot_7);
        dest.writeInt(medicine_count_slot_8);
        dest.writeInt(medicine_count_slot_9);
        dest.writeInt(medicine_count_slot_10);
        dest.writeInt(medicine_count_slot_11);
        dest.writeInt(medicine_count_slot_12);
        dest.writeInt(medicine_count_slot_13);
        dest.writeInt(medicine_count_slot_14);
        dest.writeInt(medicine_count_slot_15);
        dest.writeInt(medicine_count_slot_16);
        dest.writeInt(medicine_count_slot_17);
        dest.writeInt(medicine_count_slot_18);
        dest.writeInt(medicine_count_slot_19);
        dest.writeInt(medicine_count_slot_20);
        dest.writeInt(medicine_count_slot_21);
        dest.writeInt(medicine_count_slot_22);
        dest.writeInt(medicine_count_slot_23);
        dest.writeInt(medicine_count_slot_24);
//        dest.writeInt(is_selected_sunday);
//        dest.writeInt(is_selected_monday);
//        dest.writeInt(is_selected_tuesday);
//        dest.writeInt(is_selected_wednesday);
//        dest.writeInt(is_selected_thursday);
//        dest.writeInt(is_selected_friday);
//        dest.writeInt(is_selected_saturday);
        dest.writeLong(created_datetime);
        dest.writeInt(medicine_taken);
        dest.writeInt(medicine_skipped);
        dest.writeLong(next_notification_time);
        dest.writeInt(next_medicine_count);
    }
}
