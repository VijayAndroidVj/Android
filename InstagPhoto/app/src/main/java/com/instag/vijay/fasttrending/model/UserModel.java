package com.instag.vijay.fasttrending.model;

/**
 * Created by iyara_rajan on 19-06-2017.
 */

public class UserModel {
    public static final String color[] = {"#F44336", "#E91E63", "#9C27B0", "#FF5252", "#FF1744", "#D50000", "#FF8A80", "#EA80FC", "#FFEA00", "#FFC400", "#FFC400", "#795548", "#9E9E9E", "#607D8B", "#E040FB", "#D500F9", "#C0CA33", "#00E676", "#FFC107", "#76FF03", "#FF9800", "#C6FF00", "#EEFF41", "#43A047", "#5E35B1", "#3949AB", "#1E88E5", "#3D5AFE", "#039BE5", "#00ACC1", "#00E5FF", "#00897B"};

    private String name;
    private String userId;
    private String date;
    private String time;
    private String image;
    private String to_token;
    private int status;
    private String mimeType;
    private String message;
    private String messageId;
    private int messageSeenStatus;
    private String fileName;
    private int unseenMessageCount;
    private int colorState;
    private String displayName;
    private String agentKey;

    public UserModel() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageSeenStatus() {
        return messageSeenStatus;
    }

    public void setMessageSeenStatus(int messageSeenStatus) {
        this.messageSeenStatus = messageSeenStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getUnseenMessageCount() {
        return unseenMessageCount;
    }

    public void setUnseenMessageCount(int unseenMessageCount) {
        this.unseenMessageCount = unseenMessageCount;
    }

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAgentKey() {
        return agentKey;
    }

    public void setAgentKey(String agentKey) {
        this.agentKey = agentKey;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTo_token() {
        return to_token;
    }

    public void setTo_token(String to_token) {
        this.to_token = to_token;
    }
}

