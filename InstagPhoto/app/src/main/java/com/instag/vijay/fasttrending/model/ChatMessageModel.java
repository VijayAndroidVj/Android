package com.instag.vijay.fasttrending.model;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessageModel implements Parcelable {

    public static final int TEXT = 0;
    public static final int IAMGE = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;
    public static final int DOC = 4;

    private String id;
    private String name = "";
    private String userID;
    private String message = "";
    private String date;
    private String time;
    private String mode;
    private String fileExt = "";
    private String fileName = "";
    private long fileSize;
    private String mimeType;
    private String thumbPath;
    private String filePath = "";
    private String durationTime;
    private long sentorrecivedsize;
    private int messageSentOrReceived;
    private int seenstatus;
    private long messageId;
    private String messageDeliveryStatus;
    private int unseenmessageCount;
    private String timeZone;
    private long timemilliseconds;
    private boolean fileUpload;
    private boolean isFileUploading;
    private int uploadedResource;
    private String mediaLink = "";
    private int readMessage;
    private int colorState;
    public MediaPlayer player;
    public Handler seekHandler = new Handler();
    public Runnable run;

    public ChatMessageModel() {

    }

    protected ChatMessageModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        userID = in.readString();
        message = in.readString();
        date = in.readString();
        time = in.readString();
        mode = in.readString();
        fileExt = in.readString();
        fileName = in.readString();
        fileSize = in.readLong();
        mimeType = in.readString();
        thumbPath = in.readString();
        filePath = in.readString();
        durationTime = in.readString();
        sentorrecivedsize = in.readLong();
        messageSentOrReceived = in.readInt();
        seenstatus = in.readInt();
        messageId = in.readLong();
        messageDeliveryStatus = in.readString();
        unseenmessageCount = in.readInt();
        timeZone = in.readString();
        timemilliseconds = in.readLong();
        fileUpload = in.readByte() != 0;
        isFileUploading = in.readByte() != 0;
        uploadedResource = in.readInt();
        mediaLink = in.readString();
        readMessage = in.readInt();
        colorState = in.readInt();
    }

    public static final Creator<ChatMessageModel> CREATOR = new Creator<ChatMessageModel>() {
        @Override
        public ChatMessageModel createFromParcel(Parcel in) {
            return new ChatMessageModel(in);
        }

        @Override
        public ChatMessageModel[] newArray(int size) {
            return new ChatMessageModel[size];
        }
    };

    public boolean isFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(boolean fileUpload) {
        this.fileUpload = fileUpload;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFileUploading() {
        return isFileUploading;
    }

    public void setFileUploading(boolean fileUploading) {
        isFileUploading = fileUploading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public long getSentorrecivedsize() {
        return sentorrecivedsize;
    }

    public void setSentorrecivedsize(long sentorrecivedsize) {
        this.sentorrecivedsize = sentorrecivedsize;
    }

    public int getMessageSentOrReceived() {
        return messageSentOrReceived;
    }

    public void setMessageSentOrReceived(int messageSentOrReceived) {
        this.messageSentOrReceived = messageSentOrReceived;
    }

    public int getSeenstatus() {
        return seenstatus;
    }

    public void setSeenstatus(int seenstatus) {
        this.seenstatus = seenstatus;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessageDeliveryStatus() {
        return messageDeliveryStatus;
    }

    public void setMessageDeliveryStatus(String messageDeliveryStatus) {
        this.messageDeliveryStatus = messageDeliveryStatus;
    }

    public int getUnseenmessageCount() {
        return unseenmessageCount;
    }

    public void setUnseenmessageCount(int unseenmessageCount) {
        this.unseenmessageCount = unseenmessageCount;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public long getTimemilliseconds() {
        return timemilliseconds;
    }

    public void setTimemilliseconds(long timemilliseconds) {
        this.timemilliseconds = timemilliseconds;
    }

    public int getUploadedResource() {
        return uploadedResource;
    }

    public void setUploadedResource(int uploadedResource) {
        this.uploadedResource = uploadedResource;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public int getReadMessage() {
        return readMessage;
    }

    public void setReadMessage(int readMessage) {
        this.readMessage = readMessage;
    }

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", status=" + messageSentOrReceived +
                ", messageId=" + messageId +
                ", isDone='" + messageDeliveryStatus + '\'' +
                ", mode='" + mode + '\'' +
                ", fileExt='" + fileExt + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", mimeType='" + mimeType + '\'' +
                ", thumbPath='" + thumbPath + '\'' +
                ", filePath='" + filePath + '\'' +
                ", durationTime='" + durationTime + '\'' +
                ", uploadedResource=" + uploadedResource +
                ", mediaLink='" + mediaLink + '\'' +
                ", readMessage=" + readMessage +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(userID);
        parcel.writeString(message);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(mode);
        parcel.writeString(fileExt);
        parcel.writeString(fileName);
        parcel.writeLong(fileSize);
        parcel.writeString(mimeType);
        parcel.writeString(thumbPath);
        parcel.writeString(filePath);
        parcel.writeString(durationTime);
        parcel.writeLong(sentorrecivedsize);
        parcel.writeInt(messageSentOrReceived);
        parcel.writeInt(seenstatus);
        parcel.writeLong(messageId);
        parcel.writeString(messageDeliveryStatus);
        parcel.writeInt(unseenmessageCount);
        parcel.writeString(timeZone);
        parcel.writeLong(timemilliseconds);
        parcel.writeByte((byte) (fileUpload ? 1 : 0));
        parcel.writeByte((byte) (isFileUploading ? 1 : 0));
        parcel.writeInt(uploadedResource);
        parcel.writeString(mediaLink);
        parcel.writeInt(readMessage);
        parcel.writeInt(colorState);
    }
}
