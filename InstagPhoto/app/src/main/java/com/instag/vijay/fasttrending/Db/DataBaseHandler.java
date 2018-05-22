package com.instag.vijay.fasttrending.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.instag.vijay.fasttrending.model.ChatMessageModel;
import com.instag.vijay.fasttrending.model.UserModel;

import java.util.ArrayList;
import java.util.Random;


public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WebMuo";
    private static final int DATABASE_VERSION = 1;

    //TODO Tables
    private static final String TROVACONTACT = "tblContact";
    private static final String TROVACONTACTDISPLAYCHATLOGS = "tblContactChatLogs";
    private static final String TBLCHATMESSAGES = "tblChatMessage";

    //TODO Columns
    private static final String FIELD_NAME = "name";
    private static final String FIELD_USER_ID = "userid";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_TIME = "time";
    private static final String FIELD_SEND_OR_RECV = "sendorrecv";
    private static final String FIELD_MIME_TYPE = "mimetype";
    private static final String FIELD_MESSAGE_SEEN_STATUS = "messageseenstatus";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_MESSAGE_ID = "message_id";
    private static final String FIELD_FILE_NAME = "filename";
    private static final String FIELD_MESSAGE_UNSEEN_COUNT = "messageunseencount";
    private static final String CONTACTS_USER_IMAGE = "contact_user_image";
    private static final String CONTACTS_USER_TO_TOKEN = "contact_user_token";

    private static DataBaseHandler dataBaseHandler;

    //tbl_chatmessages

    private static final String CHATFIELDMESSAGEID = "chat_message_id";
    private static final String CHATFIELDTYPE = "chat_type";
    private static final String CHATFIELDMESSAGE = "chat_message_text";
    private static final String CHATFIELDMESSAGEMIMETYPE = "chat_message_mimetype";
    private static final String CHATFIELDNAME = "chat_name";
    private static final String CHATFIELDUSERID = "chat_userid";
    private static final String CHATFIELDDATE = "chat_date";
    private static final String CHATFIELDTIME = "chat_time";
    private static final String CHATFIELDSEENSTATUS = "chat_seenstatus";
    private static final String CHATFIELDFILEPATH = "chat_filepath";
    private static final String CHATFIELDFILETHUMBPATH = "chat_filethumbpath";
    private static final String CHATFIELDFILENAME = "chat_filename";
    private static final String CHATFIELDFILESIZE = "chat_file_size";
    private static final String CHATFIELDFILEMEDIALINK = "chat_file_medialink";
    private static final String CHATFIELDFILEDURATION = "chat_file_duration";


    public static DataBaseHandler getInstance(Context context) {
        if (dataBaseHandler == null) {
            dataBaseHandler = new DataBaseHandler(context);
        }
        return dataBaseHandler;
    }

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE2 = "CREATE TABLE " + TROVACONTACTDISPLAYCHATLOGS + "("
                + FIELD_NAME + " TEXT," + FIELD_USER_ID + " TEXT," + FIELD_DATE + " TEXT,"
                + FIELD_TIME + " TEXT," + FIELD_MESSAGE_ID + " TEXT," + FIELD_MESSAGE + " TEXT," + FIELD_SEND_OR_RECV + " INTEGER,"
                + FIELD_MIME_TYPE + " TEXT," + FIELD_MESSAGE_SEEN_STATUS + " INTEGER,"
                + CONTACTS_USER_IMAGE + " TEXT," + CONTACTS_USER_TO_TOKEN + " TEXT,"
                + FIELD_FILE_NAME + " TEXT," + FIELD_MESSAGE_UNSEEN_COUNT + " INTEGER)";
        db.execSQL(CREATE_TABLE2);

        String CREATE_TABLE_CHAT = "CREATE TABLE " + TBLCHATMESSAGES + "("
                + CHATFIELDMESSAGEID + " TEXT,"
                + CHATFIELDTYPE + " INTEGER,"
                + CHATFIELDMESSAGE + " TEXT,"
                + CHATFIELDMESSAGEMIMETYPE + " TEXT,"
                + CHATFIELDNAME + " TEXT,"
                + CHATFIELDUSERID + " TEXT,"
                + CHATFIELDDATE + " TEXT,"
                + CHATFIELDTIME + " TEXT,"
                + CHATFIELDSEENSTATUS + " INTEGER,"
                + CHATFIELDFILEPATH + " TEXT,"
                + CHATFIELDFILETHUMBPATH + " TEXT,"
                + CHATFIELDFILENAME + " TEXT,"
                + CHATFIELDFILEMEDIALINK + " TEXT,"
                + CHATFIELDFILEDURATION + " TEXT,"
                + CHATFIELDFILESIZE + " TEXT)";
        db.execSQL(CREATE_TABLE_CHAT);

        String CREATE_TABLE_TROVACONTACT = "CREATE TABLE " + TROVACONTACT + "("
                + FIELD_NAME + " TEXT," + FIELD_USER_ID + " TEXT,"
                + CONTACTS_USER_IMAGE + " TEXT," + CONTACTS_USER_TO_TOKEN + " TEXT)";
        db.execSQL(CREATE_TABLE_TROVACONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables();
        onCreate(db);
    }

    public void deleteContactLogTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TROVACONTACTDISPLAYCHATLOGS, null, null);
        db.delete(TBLCHATMESSAGES, null, null);
        db.delete(TROVACONTACT, null, null);
        db.close();
    }

    void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TROVACONTACTDISPLAYCHATLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TBLCHATMESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TROVACONTACT);
    }


    public void updateFileMessage(String filePath, String serverPath, long messageId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TBLCHATMESSAGES + " WHERE " + CHATFIELDFILEPATH + "= '" + filePath + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(CHATFIELDFILEMEDIALINK, serverPath);
                    values.put(CHATFIELDMESSAGEID, messageId);
                    db.update(TBLCHATMESSAGES, values, CHATFIELDFILEPATH + " = ?",
                            new String[]{filePath});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateContactToken(String mail, String token) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TROVACONTACT + " WHERE " + FIELD_USER_ID + "= '" + mail + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() != 0) {
                ContentValues values = new ContentValues();
                values.put(CONTACTS_USER_TO_TOKEN, token);
                db.update(TROVACONTACT, values, FIELD_USER_ID + " = ?",
                        new String[]{mail});
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList<UserModel> getFilteredContactList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVACONTACT + " ORDER BY " + FIELD_NAME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<UserModel> contactList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    UserModel chatMessageModel = new UserModel();
                    chatMessageModel.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    chatMessageModel.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
                    chatMessageModel.setImage(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_IMAGE)));
                    chatMessageModel.setTo_token(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_TO_TOKEN)));
                    contactList.add(chatMessageModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return contactList;
    }

    public synchronized void saveFilteredContacts(UserModel userModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TROVACONTACT + " WHERE " + FIELD_USER_ID + "= '" + userModel.getUserId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() != 0) {
                ContentValues values = new ContentValues();
                values.put(FIELD_NAME, userModel.getName());
                values.put(FIELD_USER_ID, userModel.getUserId());
                values.put(CONTACTS_USER_IMAGE, userModel.getImage());
                values.put(CONTACTS_USER_TO_TOKEN, userModel.getTo_token());
                db.update(TROVACONTACT, values, FIELD_USER_ID + " = ?",
                        new String[]{userModel.getUserId()});
            } else {
                ContentValues values = new ContentValues();
                values.put(FIELD_NAME, userModel.getName());
                values.put(FIELD_USER_ID, userModel.getUserId());
                values.put(CONTACTS_USER_IMAGE, userModel.getImage());
                values.put(CONTACTS_USER_TO_TOKEN, userModel.getTo_token());
                long status = db.insert(TROVACONTACT, null, values);
                Log.i("insert to tbl-contacts", ":" + status);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUserName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCHATLOGS + " WHERE " + FIELD_USER_ID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String name = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
            }
            cursor.close();
        }
        return name;
    }

    public void saveChatMessage(ChatMessageModel chatMessageModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CHATFIELDMESSAGEID, chatMessageModel.getMessageId());
        values.put(CHATFIELDTYPE, chatMessageModel.getMessageSentOrReceived());
        values.put(CHATFIELDMESSAGE, chatMessageModel.getMessage());
        values.put(CHATFIELDMESSAGEMIMETYPE, chatMessageModel.getMimeType());
        values.put(CHATFIELDNAME, chatMessageModel.getName());
        values.put(CHATFIELDUSERID, chatMessageModel.getUserId());
        values.put(CHATFIELDDATE, chatMessageModel.getDate());
        values.put(CHATFIELDTIME, chatMessageModel.getTime());
        values.put(CHATFIELDSEENSTATUS, chatMessageModel.getSeenstatus());
        values.put(CHATFIELDFILEPATH, chatMessageModel.getFilePath());
        values.put(CHATFIELDFILETHUMBPATH, chatMessageModel.getFilePath());
        values.put(CHATFIELDFILENAME, chatMessageModel.getFileName());
        values.put(CHATFIELDFILESIZE, chatMessageModel.getFileSize());
        values.put(CHATFIELDFILEMEDIALINK, chatMessageModel.getMediaLink());
        values.put(CHATFIELDFILEDURATION, chatMessageModel.getDurationTime());

        long status = db.insert(TBLCHATMESSAGES, null, values);
        Log.i("insert to chat table", ":" + status);
    }

    public ArrayList<ChatMessageModel> getChatMessages(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLCHATMESSAGES + " WHERE " + CHATFIELDUSERID + " = '" + userID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ChatMessageModel> chatmessageList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setMessageId(cursor.getLong(cursor.getColumnIndex(CHATFIELDMESSAGEID)));
                    chatMessageModel.setName(cursor.getString(cursor.getColumnIndex(CHATFIELDNAME)));
                    chatMessageModel.setMessage(cursor.getString(cursor.getColumnIndex(CHATFIELDMESSAGE)));
                    chatMessageModel.setDate(cursor.getString(cursor.getColumnIndex(CHATFIELDDATE)));
                    chatMessageModel.setMimeType(cursor.getString(cursor.getColumnIndex(CHATFIELDMESSAGEMIMETYPE)));
                    chatMessageModel.setMessageSentOrReceived(cursor.getInt(cursor.getColumnIndex(CHATFIELDTYPE)));
                    chatMessageModel.setTime(cursor.getString(cursor.getColumnIndex(CHATFIELDTIME)));
                    chatMessageModel.setFilePath(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEPATH)));
                    chatMessageModel.setFilePath(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEPATH)));
                    chatMessageModel.setFileSize(cursor.getLong(cursor.getColumnIndex(CHATFIELDFILESIZE)));
                    chatMessageModel.setTimeZone(cursor.getString(cursor.getColumnIndex(CHATFIELDFILETHUMBPATH)));
                    chatMessageModel.setSeenstatus(cursor.getInt(cursor.getColumnIndex(CHATFIELDSEENSTATUS)));
                    chatMessageModel.setFileName(cursor.getString(cursor.getColumnIndex(CHATFIELDFILENAME)));
                    chatMessageModel.setMediaLink(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEMEDIALINK)));
                    chatMessageModel.setDurationTime(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEDURATION)));
                    chatmessageList.add(chatMessageModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return chatmessageList;
    }

    public synchronized void updateChatMessageStatus(String messageId, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        db.update(TBLCHATMESSAGES, cv, CHATFIELDMESSAGEID + " =?", new String[]{messageId});
    }

    public void updateChatStatus(String userid, int status, boolean isSendMessage) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        if (isSendMessage) {
            db.update(TBLCHATMESSAGES, cv, CHATFIELDUSERID + " =? AND " + CHATFIELDTYPE + " =? ", new String[]{userid, "0"});
        } else {
            db.update(TBLCHATMESSAGES, cv, CHATFIELDUSERID + " =? AND " + CHATFIELDTYPE + " =? ", new String[]{userid, "1"});
        }
    }

    public synchronized void updateChatLogSeenStatus(String userid, int status, boolean isSendMessage, String messageId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FIELD_MESSAGE_UNSEEN_COUNT, 0);
        if (isSendMessage) {
            cv.put(FIELD_MESSAGE_SEEN_STATUS, status);
            db.update(TROVACONTACTDISPLAYCHATLOGS, cv, FIELD_USER_ID + " =? AND " + FIELD_SEND_OR_RECV + " =? AND " + FIELD_MESSAGE_ID + " =? ", new String[]{userid, "0", messageId});
        } else {
            db.update(TROVACONTACTDISPLAYCHATLOGS, cv, FIELD_USER_ID + " =? AND " + FIELD_SEND_OR_RECV + " =? ", new String[]{userid, "1"});
        }
    }

    public void updateChatFileStatus(String server_path, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        db.update(TBLCHATMESSAGES, cv, CHATFIELDFILEMEDIALINK + " =?", new String[]{server_path});
    }

    public int unseenCount(String userID) {
        int messageCount = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = "SELECT COUNT(*) as count FROM " + TBLCHATMESSAGES + " where " + CHATFIELDTYPE + " = '1' AND " + CHATFIELDSEENSTATUS + " = '0' AND " + CHATFIELDUSERID + " = '" + userID + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() != 0) {
                    messageCount = cursor.getInt(cursor.getColumnIndex("count"));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageCount;
    }

    public synchronized void updateToken(String mail, String token) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCHATLOGS + " WHERE " + FIELD_USER_ID + "= '" + mail + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() != 0) {
                ContentValues values = new ContentValues();
                values.put(CONTACTS_USER_TO_TOKEN, token);
                db.update(TROVACONTACTDISPLAYCHATLOGS, values, FIELD_USER_ID + " =?",
                        new String[]{mail});
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String getUserToken(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCHATLOGS + " WHERE " + FIELD_USER_ID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String token = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                token = cursor.getString(cursor.getColumnIndex(CONTACTS_USER_TO_TOKEN));
            }
            cursor.close();
        }
        db.close();
        return token;
    }

    public synchronized String getUserImage(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVACONTACT + " WHERE " + FIELD_USER_ID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String name = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                name = cursor.getString(cursor.getColumnIndex(CONTACTS_USER_IMAGE));
            }
            cursor.close();
        }
        db.close();
        return name;
    }


    public void saveContactLogs(UserModel userModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //System.out.println(trovaAgentCallLogs);
            String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCHATLOGS + " WHERE " + FIELD_USER_ID + "='" + userModel.getUserId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            ContentValues values = new ContentValues();
            if (!TextUtils.isEmpty(userModel.getName()))
                values.put(FIELD_NAME, userModel.getName());
            values.put(FIELD_USER_ID, userModel.getUserId());
            values.put(FIELD_DATE, userModel.getDate());
            values.put(FIELD_TIME, userModel.getTime());
            values.put(FIELD_SEND_OR_RECV, userModel.getStatus());
            values.put(FIELD_MIME_TYPE, userModel.getMimeType());
            values.put(FIELD_MESSAGE_SEEN_STATUS, userModel.getMessageSeenStatus());
            values.put(FIELD_MESSAGE, userModel.getMessage());
            values.put(FIELD_MESSAGE_ID, userModel.getMessageId());
            values.put(FIELD_FILE_NAME, userModel.getFileName());
            values.put(FIELD_MESSAGE_UNSEEN_COUNT, userModel.getUnseenMessageCount());
            values.put(CONTACTS_USER_IMAGE, userModel.getImage());
            values.put(CONTACTS_USER_TO_TOKEN, userModel.getTo_token());
            if (cursor != null && cursor.getCount() > 0) {
                db.update(TROVACONTACTDISPLAYCHATLOGS, values, FIELD_USER_ID + " = ?",
                        new String[]{userModel.getUserId()});
            } else {
                db.insert(TROVACONTACTDISPLAYCHATLOGS, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<UserModel> getAllContactDisplayCallLogs() {
        ArrayList<UserModel> callLogsList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCHATLOGS;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() != 0) {
                    do {
                        UserModel userModel = new UserModel();
                        userModel.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                        userModel.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
                        userModel.setColorState(Color.parseColor(UserModel.color[new Random().nextInt(UserModel.color.length)]));
                        userModel.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
                        userModel.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                        userModel.setStatus(cursor.getInt(cursor.getColumnIndex(FIELD_SEND_OR_RECV)));
                        userModel.setMimeType(cursor.getString(cursor.getColumnIndex(FIELD_MIME_TYPE)));
                        userModel.setMessageSeenStatus(cursor.getInt(cursor.getColumnIndex(FIELD_MESSAGE_SEEN_STATUS)));
                        userModel.setMessage(cursor.getString(cursor.getColumnIndex(FIELD_MESSAGE)));
                        userModel.setMessageId(cursor.getString(cursor.getColumnIndex(FIELD_MESSAGE_ID)));
                        userModel.setFileName(cursor.getString(cursor.getColumnIndex(FIELD_FILE_NAME)));
                        userModel.setUnseenMessageCount(cursor.getInt(cursor.getColumnIndex(FIELD_MESSAGE_UNSEEN_COUNT)));
                        userModel.setImage(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_IMAGE)));
                        userModel.setTo_token(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_TO_TOKEN)));
                        callLogsList.add(userModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return callLogsList;
    }

}
