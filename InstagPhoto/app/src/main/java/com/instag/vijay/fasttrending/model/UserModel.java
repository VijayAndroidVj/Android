package com.instag.vijay.fasttrending.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iyara_rajan on 19-06-2017.
 */

public class UserModel implements Parcelable {
    public static final String color[] = {"#F44336", "#E91E63", "#9C27B0", "#FF5252", "#FF1744", "#D50000", "#FF8A80", "#EA80FC", "#FFEA00", "#FFC400", "#FFC400", "#795548", "#9E9E9E", "#607D8B", "#E040FB", "#D500F9", "#C0CA33", "#00E676", "#FFC107", "#76FF03", "#FF9800", "#C6FF00", "#EEFF41", "#43A047", "#5E35B1", "#3949AB", "#1E88E5", "#3D5AFE", "#039BE5", "#00ACC1", "#00E5FF", "#00897B"};
    public static final String Audio = "audio";
    public static final String Video = "video";
    public static final String Chat = "chat";

    public static final String MODEL = "MODEL";
    public static int MISSEDCALL = 3;
    public static int RECIVEDCALL = 2;
    public static int DIALEDCALL = 1;
    public static int RECIVECALL = 0;

    private String id;
    private String name;
    private String image;
    private String email;
    private String message;
    private String UserId;
    private String code;
    private String status;
    private String description;
    private String Date;
    private String time;
    private String timeZone;
    private String initials;
    private int callType;
    private String callDuration;
    private int callCount;
    private String mode;
    private int colorState;
    private String callMode;
    private long timemillis;
    private String to_token;


    public UserModel() {

    }

    public UserModel(String name, String email, String ccode, String status) {
        this.name = name;
        this.email = email;
        this.code = ccode;
        this.status = status;
    }

    protected UserModel(Parcel in) {
        MISSEDCALL = in.readInt();
        RECIVEDCALL = in.readInt();
        DIALEDCALL = in.readInt();
        id = in.readString();
        name = in.readString();
        image = in.readString();
        email = in.readString();
        message = in.readString();
        UserId = in.readString();
        code = in.readString();
        status = in.readString();
        description = in.readString();
        Date = in.readString();
        time = in.readString();
        timeZone = in.readString();
        initials = in.readString();
        callType = in.readInt();
        callDuration = in.readString();
        callCount = in.readInt();
        mode = in.readString();
        colorState = in.readInt();
        callMode = in.readString();
        timemillis = in.readLong();
        to_token = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }

    public String getCallMode() {
        return callMode;
    }

    public void setCallMode(String callMode) {
        this.callMode = callMode;
    }

    public long getTimemillis() {
        return timemillis;
    }

    public void setTimemillis(long timemillis) {
        this.timemillis = timemillis;
    }


    public String getTo_token() {
        return to_token;
    }

    public void setTo_token(String to_token) {
        this.to_token = to_token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(MISSEDCALL);
        parcel.writeInt(RECIVEDCALL);
        parcel.writeInt(DIALEDCALL);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(email);
        parcel.writeString(message);
        parcel.writeString(UserId);
        parcel.writeString(code);
        parcel.writeString(status);
        parcel.writeString(description);
        parcel.writeString(Date);
        parcel.writeString(time);
        parcel.writeString(timeZone);
        parcel.writeString(initials);
        parcel.writeInt(callType);
        parcel.writeString(callDuration);
        parcel.writeInt(callCount);
        parcel.writeString(mode);
        parcel.writeInt(colorState);
        parcel.writeString(callMode);
        parcel.writeLong(timemillis);
        parcel.writeString(to_token);
    }
}
