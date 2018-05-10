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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;


public class DataBaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ReachApp";
    private static final int DATABASE_VERSION = 1;

    //TODO Tables
    private static final String TROVACONTACTDISPLAYCALLLOGS = "trovacontactdisplaycalllogs";
    private static final String TROVAMISSEDCALLLOGS = "trovamissedcalllogs";
    //TODO Columns
    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_USER_ID = "userid";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_TIME = "time";
    private static final String FIELD_TIME_MILLIS = "timemillis";
    private static final String FIELD_TIMEZONE = "timezone";
    private static final String FIELD_CALLCOUNT = "count";
    private static final String FIELD_CALLDURATION = "callduration";
    private static final String FIELD_CALL_TYPE = "calltype";
    private static final String FIELD_CALLMODE = "callmode";
    private static final String FIELD_MESSAGE = "message";

    private static DataBaseHandler dataBaseHandler;

    //tbl_chatmessages
    private static final String TBLCHATMESSAGES = "tbl_chat_message";
    private static final String CHATFIELDID = "chat_id";
    private static final String CHATFIELDMESSAGEID = "chat_message_id";
    private static final String CHATFIELDMESSAGETYPE = "chat_message_type";
    private static final String CHATFIELDMESSAGE = "chat_message_text";
    private static final String CHATFIELDMESSAGEMIMETYPE = "chat_message_mimetype";
    private static final String CHATFIELDNAME = "chat_name";
    private static final String CHATFIELDUSERID = "chat_user_id";
    private static final String CHATFIELDDATE = "chat_date";
    private static final String CHATFIELDTIME = "chat_time";
    private static final String CHATFIELDTIMEZONE = "chat_timezone";
    private static final String CHATFIELDSEENSTATUS = "chat_seenstatus";
    private static final String CHATFIELDTIMEMILLISECONDS = "chat_timemilliseconds";
    private static final String CHATFIELDFILEPATH = "chat_filepath";
    private static final String CHATFIELDFILENAME = "chat_filename";
    private static final String CHATFIELDFILESIZE = "chat_filesize";
    private static final String CHATFIELDFILEMEDIALINK = "chat_medialink";
    private static final String CHATFIELDFILEDURATION = "chat_duration";

    private static final String TBLFILTEREDCONTACTS = "tbl_filtered_contacts";
    private static final String CONTACTS_NAME = "contact_name";
    private static final String CONTACTS_USERID = "contact_user_id";
    private static final String CONTACTS_USER_IMAGE = "contact_user_image";
    private static final String CONTACTS_USER_TO_TOKEN = "contact_user_token";


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

        String CREATE_TABLE2 = "CREATE TABLE " + TROVACONTACTDISPLAYCALLLOGS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_NAME + " TEXT," + FIELD_USER_ID + " TEXT," + FIELD_DATE + " TEXT,"
                + FIELD_TIME + " TEXT," + FIELD_TIME_MILLIS + " LONG," + FIELD_TIMEZONE + " TEXT," + FIELD_CALL_TYPE + " INTEGER," + FIELD_CALLMODE + " TEXT," + FIELD_MESSAGE + " TEXT,"
                + FIELD_CALLDURATION + " TEXT," + FIELD_CALLCOUNT + " INTEGER,"
                + FIELD_STATUS + " INTEGER)";
        db.execSQL(CREATE_TABLE2);

        String CREATE_TABLE_CHAT = "CREATE TABLE " + TBLCHATMESSAGES + "("
                + CHATFIELDID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CHATFIELDMESSAGEID + " TEXT,"
                + CHATFIELDMESSAGE + " TEXT,"
                + CHATFIELDMESSAGETYPE + " INTEGER,"
                + CHATFIELDMESSAGEMIMETYPE + " TEXT,"
                + CHATFIELDNAME + " TEXT,"
                + CHATFIELDUSERID + " TEXT,"
                + CHATFIELDDATE + " TEXT,"
                + CHATFIELDTIME + " TEXT,"
                + CHATFIELDTIMEZONE + " TEXT,"
                + CHATFIELDSEENSTATUS + " INTEGER,"
                + CHATFIELDTIMEMILLISECONDS + " TEXT,"
                + CHATFIELDFILEPATH + " TEXT,"
                + CHATFIELDFILENAME + " TEXT,"
                + CHATFIELDFILEMEDIALINK + " TEXT,"
                + CHATFIELDFILEDURATION + " TEXT,"
                + CHATFIELDFILESIZE + " TEXT)";
        db.execSQL(CREATE_TABLE_CHAT);

        String CREATE_TABLE13 = "CREATE TABLE " + TBLFILTEREDCONTACTS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + CONTACTS_NAME + " TEXT," + CONTACTS_USERID + " TEXT," + CONTACTS_USER_TO_TOKEN + " TEXT," + CONTACTS_USER_IMAGE + " TEXT)";
        db.execSQL(CREATE_TABLE13);

        String CREATE_TABLE_MISSEDCALL = "CREATE TABLE " + TROVAMISSEDCALLLOGS + "("
                + FIELD_ID + " INTEGER PRIMARY KEY,"
                + FIELD_NAME + " TEXT," + FIELD_USER_ID + " TEXT," + FIELD_DATE + " TEXT,"
                + FIELD_TIME + " TEXT," + FIELD_TIME_MILLIS + " LONG," + FIELD_TIMEZONE + " TEXT," + FIELD_CALL_TYPE + " INTEGER," + FIELD_CALLMODE + " TEXT,"
                + FIELD_CALLDURATION + " TEXT," + FIELD_CALLCOUNT + " INTEGER,"
                + FIELD_STATUS + " INTEGER)";
        db.execSQL(CREATE_TABLE_MISSEDCALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables();
        onCreate(db);
    }

    public void deleteContactLogTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TROVACONTACTDISPLAYCALLLOGS, null, null);
        db.close();
    }

    public void dropTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TBLCHATMESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TBLFILTEREDCONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TROVACONTACTDISPLAYCALLLOGS);
        db.execSQL("DROP TABLE IF EXISTS " + TROVAMISSEDCALLLOGS);
    }


    public void updateFileMessage(String filePath, String serverPath) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TBLCHATMESSAGES + " WHERE " + CHATFIELDFILEPATH + "= '" + filePath + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() != 0) {
                    ContentValues values = new ContentValues();
                    values.put(CHATFIELDFILEMEDIALINK, serverPath);
                    db.update(TBLCHATMESSAGES, values, CHATFIELDFILEPATH + " = ?",
                            new String[]{filePath});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveFilteredContacts(UserModel userModel) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " WHERE " + CONTACTS_USERID + "= '" + userModel.getUserId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() != 0) {
                ContentValues values = new ContentValues();
                values.put(CONTACTS_NAME, userModel.getName());
                values.put(CONTACTS_USERID, userModel.getUserId());
                values.put(CONTACTS_USER_IMAGE, userModel.getImage());
                values.put(CONTACTS_USER_TO_TOKEN, userModel.getTo_token());
                db.update(TBLFILTEREDCONTACTS, values, CONTACTS_USERID + " = ?",
                        new String[]{userModel.getUserId()});
            } else {
                ContentValues values = new ContentValues();
                values.put(CONTACTS_NAME, userModel.getName());
                values.put(CONTACTS_USERID, userModel.getUserId());
                values.put(CONTACTS_USER_IMAGE, userModel.getImage());
                values.put(CONTACTS_USER_TO_TOKEN, userModel.getTo_token());
                long status = db.insert(TBLFILTEREDCONTACTS, null, values);
                Log.i("insert to tbl-contacts", ":" + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<UserModel> getFilteredContactList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " ORDER BY " + CONTACTS_NAME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<UserModel> contactList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    UserModel chatMessageModel = new UserModel();
                    chatMessageModel.setName(cursor.getString(cursor.getColumnIndex(CONTACTS_NAME)));
                    chatMessageModel.setUserId(cursor.getString(cursor.getColumnIndex(CONTACTS_USERID)));
                    chatMessageModel.setImage(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_IMAGE)));
                    chatMessageModel.setTo_token(cursor.getString(cursor.getColumnIndex(CONTACTS_USER_TO_TOKEN)));
                    contactList.add(chatMessageModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return contactList;
    }

    public int getContactCount() {
        int contactCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT Count(" + CONTACTS_NAME + ") FROM " + TBLFILTEREDCONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    contactCount = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return contactCount;
    }

    public boolean isContactAvailable(String userId) {
        int contactCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " WHERE " + CONTACTS_USERID + " = '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    contactCount = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        if (contactCount > 0) {
            return true;
        } else {
            return false;
        }
    }


    public String getUserName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " WHERE " + CONTACTS_USERID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String name = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                name = cursor.getString(cursor.getColumnIndex(CONTACTS_NAME));
            }
            cursor.close();
        }
        return name;
    }

    public String getUserImage(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " WHERE " + CONTACTS_USERID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String name = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                name = cursor.getString(cursor.getColumnIndex(CONTACTS_USER_IMAGE));
            }
            cursor.close();
        }
        return name;
    }

    public String getUserToken(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLFILTEREDCONTACTS + " WHERE " + CONTACTS_USERID + "= '" + userId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String token = "";
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                token = cursor.getString(cursor.getColumnIndex(CONTACTS_USER_TO_TOKEN));
            }
            cursor.close();
        }
        return token;
    }

    public boolean isFileUploaded(String localPath) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLCHATMESSAGES + " WHERE " + CHATFIELDFILEPATH + "= '" + localPath + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean uploaded = false;
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                uploaded = true;
            }
            cursor.close();
        }
        return uploaded;
    }

    public void saveChatMessage(ChatMessageModel chatMessageModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CHATFIELDMESSAGEID, chatMessageModel.getMessageId());
        values.put(CHATFIELDMESSAGETYPE, chatMessageModel.getMessageSentOrReceived());
        values.put(CHATFIELDMESSAGE, chatMessageModel.getMessage());
        values.put(CHATFIELDMESSAGEMIMETYPE, chatMessageModel.getMimeType());
        values.put(CHATFIELDNAME, chatMessageModel.getName());
        values.put(CHATFIELDUSERID, chatMessageModel.getUserID());
        values.put(CHATFIELDDATE, chatMessageModel.getDate());
        values.put(CHATFIELDTIME, chatMessageModel.getTime());
        values.put(CHATFIELDTIMEZONE, chatMessageModel.getTimeZone());
        values.put(CHATFIELDSEENSTATUS, chatMessageModel.getSeenstatus());
        values.put(CHATFIELDTIMEMILLISECONDS, chatMessageModel.getTimemilliseconds());
        values.put(CHATFIELDFILEPATH, chatMessageModel.getFilePath());
        values.put(CHATFIELDFILENAME, chatMessageModel.getFileName());
        values.put(CHATFIELDFILESIZE, chatMessageModel.getFileSize());
        values.put(CHATFIELDFILEMEDIALINK, chatMessageModel.getMediaLink());
        values.put(CHATFIELDFILEDURATION, chatMessageModel.getDurationTime());

        long status = db.insert(TBLCHATMESSAGES, null, values);
        Log.i("insert to chat table", ":" + status);

        UserModel userModel = new UserModel();
        userModel.setUserId(chatMessageModel.getUserID());
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        String timezone = tz.getDisplayName();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currDate = df.format(cal.getTime());
        df = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String time = df.format(cal.getTime());
        String callDuration = "00:00:00";
        userModel.setDate(currDate);
        userModel.setTime(time);
        userModel.setTimeZone(timezone);
        userModel.setCallDuration(callDuration);
        userModel.setCallCount(0);
        if (chatMessageModel.getMessageSentOrReceived() == 0) {
            userModel.setStatus("1");
            userModel.setCallType(UserModel.DIALEDCALL);
        } else {
            userModel.setStatus("0");
            userModel.setCallType(UserModel.RECIVEDCALL);
        }

        userModel.setCallMode("chat");
        userModel.setMessage(chatMessageModel.getMessage());
        userModel.setName(getUserName(userModel.getUserId()));
        userModel.setTimemillis(chatMessageModel.getTimemilliseconds());
        saveContactLogs(userModel);
        saveCallLogs(userModel);


    }

    public ArrayList<ChatMessageModel> getChatMessages(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TBLCHATMESSAGES + " WHERE " + CHATFIELDUSERID + " = '" + number + "' ORDER BY " + CHATFIELDTIMEMILLISECONDS + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ChatMessageModel> chatmessageList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setMessageId(cursor.getLong(cursor.getColumnIndex(CHATFIELDMESSAGEID)));
                    chatMessageModel.setName(cursor.getString(cursor.getColumnIndex(CHATFIELDNAME)));
                    chatMessageModel.setUserID(cursor.getString(cursor.getColumnIndex(CHATFIELDUSERID)));
                    chatMessageModel.setMessage(cursor.getString(cursor.getColumnIndex(CHATFIELDMESSAGE)));
                    chatMessageModel.setDate(cursor.getString(cursor.getColumnIndex(CHATFIELDDATE)));
                    chatMessageModel.setMimeType(cursor.getString(cursor.getColumnIndex(CHATFIELDMESSAGEMIMETYPE)));
                    chatMessageModel.setMessageSentOrReceived(cursor.getInt(cursor.getColumnIndex(CHATFIELDMESSAGETYPE)));
                    chatMessageModel.setTime(cursor.getString(cursor.getColumnIndex(CHATFIELDTIME)));
                    chatMessageModel.setFilePath(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEPATH)));
                    chatMessageModel.setTimeZone(cursor.getString(cursor.getColumnIndex(CHATFIELDTIMEZONE)));
                    chatMessageModel.setSeenstatus(cursor.getInt(cursor.getColumnIndex(CHATFIELDSEENSTATUS)));
                    chatMessageModel.setFileName(cursor.getString(cursor.getColumnIndex(CHATFIELDFILENAME)));
                    chatMessageModel.setTimemilliseconds(Long.parseLong(cursor.getString(cursor.getColumnIndex(CHATFIELDTIMEMILLISECONDS))));
                    chatMessageModel.setMediaLink(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEMEDIALINK)));
                    chatMessageModel.setDurationTime(cursor.getString(cursor.getColumnIndex(CHATFIELDFILEDURATION)));
                    chatmessageList.add(chatMessageModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return chatmessageList;
    }

    public void updateChatMessageStatus(String userId, String messageId, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        db.update(TBLCHATMESSAGES, cv, CHATFIELDUSERID + "=? AND " + CHATFIELDMESSAGEID + " =?", new String[]{userId, messageId});
    }

    public void updateChatStatus(int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        db.update(TBLCHATMESSAGES, cv, null, null);
    }

    public void updateChatFileStatus(String server_path, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CHATFIELDSEENSTATUS, status);
        db.update(TBLCHATMESSAGES, cv, CHATFIELDFILEMEDIALINK + " =?", new String[]{server_path});
    }

    public void saveContactLogs(UserModel trovaAgentCallLogs) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //System.out.println(trovaAgentCallLogs);
            String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCALLLOGS + " WHERE " + FIELD_USER_ID + "='" + trovaAgentCallLogs.getUserId() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            ContentValues values = new ContentValues();
            if (!TextUtils.isEmpty(trovaAgentCallLogs.getName()))
                values.put(FIELD_NAME, trovaAgentCallLogs.getName());
            values.put(FIELD_USER_ID, trovaAgentCallLogs.getUserId());
            values.put(FIELD_DATE, trovaAgentCallLogs.getDate());
            values.put(FIELD_TIME, trovaAgentCallLogs.getTime());
            values.put(FIELD_TIME_MILLIS, trovaAgentCallLogs.getTimemillis());
            values.put(FIELD_TIMEZONE, trovaAgentCallLogs.getTimeZone());
            values.put(FIELD_CALLDURATION, trovaAgentCallLogs.getCallDuration());
            values.put(FIELD_STATUS, trovaAgentCallLogs.getStatus());
            values.put(FIELD_CALL_TYPE, trovaAgentCallLogs.getCallType());
            values.put(FIELD_CALLMODE, trovaAgentCallLogs.getCallMode());
            values.put(FIELD_MESSAGE, trovaAgentCallLogs.getMessage());
            if (cursor != null && cursor.getCount() > 0) {
                db.update(TROVACONTACTDISPLAYCALLLOGS, values, FIELD_USER_ID + " = ?",
                        new String[]{trovaAgentCallLogs.getUserId()});
            } else {
                db.insert(TROVACONTACTDISPLAYCALLLOGS, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<UserModel> getAllContactDisplayCallLogs() {
        ArrayList<UserModel> trovaAgentCallLogsList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String selectQuery = "SELECT * FROM " + TROVACONTACTDISPLAYCALLLOGS + " ORDER BY " + FIELD_TIME_MILLIS + " DESC";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() != 0) {
                    do {
                        UserModel userModel = new UserModel();
                        userModel.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                        userModel.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                        userModel.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
                        userModel.setColorState(Color.parseColor(UserModel.color[new Random().nextInt(UserModel.color.length)]));
                        userModel.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
                        userModel.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                        userModel.setTimemillis(cursor.getLong(cursor.getColumnIndex(FIELD_TIME_MILLIS)));
                        userModel.setTimeZone(cursor.getString(cursor.getColumnIndex(FIELD_TIMEZONE)));
                        userModel.setCallDuration(cursor.getString(cursor.getColumnIndex(FIELD_CALLDURATION)));
                        userModel.setCallCount(cursor.getInt(cursor.getColumnIndex(FIELD_CALLCOUNT)));
                        userModel.setStatus(cursor.getString(cursor.getColumnIndex(FIELD_STATUS)));
                        userModel.setCallType(cursor.getInt(cursor.getColumnIndex(FIELD_CALL_TYPE)));
                        userModel.setCallMode(cursor.getString(cursor.getColumnIndex(FIELD_CALLMODE)));
                        userModel.setMessage(cursor.getString(cursor.getColumnIndex(FIELD_MESSAGE)));
                        userModel.setImage(getUserImage(userModel.getUserId()));
                        trovaAgentCallLogsList.add(userModel);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trovaAgentCallLogsList;
    }

    public void saveCallLogs(UserModel trovaLogs) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //System.out.println(trovaAgentCallLogs);
            String selectQuery = "SELECT * FROM " + TROVAMISSEDCALLLOGS + " WHERE " + FIELD_USER_ID + "='" + trovaLogs.getUserId()
                    + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            ContentValues values = new ContentValues();
            if (!TextUtils.isEmpty(trovaLogs.getName()))
                values.put(FIELD_NAME, trovaLogs.getName());
            values.put(FIELD_USER_ID, trovaLogs.getUserId());
            values.put(FIELD_DATE, trovaLogs.getDate());
            values.put(FIELD_TIME, trovaLogs.getTime());
            values.put(FIELD_TIME_MILLIS, trovaLogs.getTimemillis());
            values.put(FIELD_TIMEZONE, trovaLogs.getTimeZone());
            values.put(FIELD_CALLDURATION, trovaLogs.getCallDuration());
            values.put(FIELD_STATUS, trovaLogs.getStatus());
            values.put(FIELD_CALL_TYPE, trovaLogs.getCallType());
            values.put(FIELD_CALLMODE, trovaLogs.getCallMode());

            if (cursor != null && cursor.getCount() > 0) {
                db.update(TROVAMISSEDCALLLOGS, values, FIELD_USER_ID + " = ?",
                        new String[]{trovaLogs.getUserId()});
                cursor.close();
            } else {

                db.insert(TROVAMISSEDCALLLOGS, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMissedCallLogs(UserModel trovaMissedLogs) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //System.out.println(trovaAgentCallLogs);
            String selectQuery = "SELECT * FROM " + TROVAMISSEDCALLLOGS + " WHERE " + FIELD_USER_ID + "='" + trovaMissedLogs.getUserId()
                    + "' and " + FIELD_CALL_TYPE + "='" + UserModel.RECIVECALL
                    + "' and " + FIELD_TIME_MILLIS + " ='" + trovaMissedLogs.getTimemillis() + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            ContentValues values = new ContentValues();
            values.put(FIELD_STATUS, "0");
            values.put(FIELD_CALL_TYPE, UserModel.MISSEDCALL);

            if (cursor != null && cursor.getCount() > 0) {
                db.update(TROVAMISSEDCALLLOGS, values, FIELD_USER_ID + " =? and " + FIELD_TIME_MILLIS + " =? and " + FIELD_CALLMODE + " =? ",
                        new String[]{trovaMissedLogs.getUserId(), String.valueOf(trovaMissedLogs.getTimemillis()), trovaMissedLogs.getCallMode()});
            } else {
//                db.insert(TROVAMISSEDCALLLOGS, null, values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateSeenCallLogs(String type) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //System.out.println(trovaAgentCallLogs);
            ContentValues values = new ContentValues();
            values.put(FIELD_STATUS, "1");
            String where = null;
            if (type.equalsIgnoreCase("audio")) {
                where = FIELD_CALLMODE + "= 'audio'";
            } else if (type.equalsIgnoreCase("video")) {
                where = FIELD_CALLMODE + "= 'video'";
            } else if (type.equalsIgnoreCase("all")) {
                where = null;
            }

            db.update(TROVAMISSEDCALLLOGS, values, where,
                    null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<UserModel> getAllContactCallLogs(String type) {

        String where = "";
        if (type.equalsIgnoreCase("audio")) {
            where = FIELD_CALLMODE + "= 'audio' and";
        } else if (type.equalsIgnoreCase("video")) {
            where = FIELD_CALLMODE + "= 'video' and";
        } else if (type.equalsIgnoreCase("all")) {
            where = "";
        }


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVAMISSEDCALLLOGS + " where " + where + " ORDER BY " + FIELD_TIME_MILLIS + " DESC ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<UserModel> trovaAgentCallLogsList = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    UserModel userModel = new UserModel();
                    userModel.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                    userModel.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    userModel.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
                    userModel.setColorState(Color.parseColor(UserModel.color[new Random().nextInt(UserModel.color.length)]));
                    userModel.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
                    userModel.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                    userModel.setTimemillis(cursor.getLong(cursor.getColumnIndex(FIELD_TIME_MILLIS)));
                    userModel.setTimeZone(cursor.getString(cursor.getColumnIndex(FIELD_TIMEZONE)));
                    userModel.setCallDuration(cursor.getString(cursor.getColumnIndex(FIELD_CALLDURATION)));
                    userModel.setCallCount(cursor.getInt(cursor.getColumnIndex(FIELD_CALLCOUNT)));
                    userModel.setStatus(cursor.getString(cursor.getColumnIndex(FIELD_STATUS)));
                    userModel.setCallType(cursor.getInt(cursor.getColumnIndex(FIELD_CALL_TYPE)));
                    userModel.setCallMode(cursor.getString(cursor.getColumnIndex(FIELD_CALLMODE)));

                    trovaAgentCallLogsList.add(userModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return trovaAgentCallLogsList;
    }


    public int getAudioMissedCall() {
        int callCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) as count FROM " + TROVAMISSEDCALLLOGS + " WHERE " + FIELD_CALLMODE + " = 'audio' and " + FIELD_STATUS + " = '0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                callCount = cursor.getInt(cursor.getColumnIndex("count"));
            }
            cursor.close();
        }

        return callCount;
    }

    public int getVideoMissedCall() {
        int callCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) as count FROM " + TROVAMISSEDCALLLOGS + " WHERE " + FIELD_CALLMODE + " = 'video' and " + FIELD_STATUS + " = '0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                callCount = cursor.getInt(cursor.getColumnIndex("count"));
            }
            cursor.close();
        }

        return callCount;
    }


    public ArrayList<UserModel> getMissedCallLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TROVAMISSEDCALLLOGS + " ORDER BY " + FIELD_ID + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<UserModel> trovaMissedLogsList = new ArrayList<>();
        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                do {
                    UserModel userModel = new UserModel();
                    userModel.setId(cursor.getString(cursor.getColumnIndex(FIELD_ID)));
                    userModel.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
                    userModel.setUserId(cursor.getString(cursor.getColumnIndex(FIELD_USER_ID)));
                    userModel.setColorState(Color.parseColor(UserModel.color[new Random().nextInt(UserModel.color.length)]));
                    userModel.setDate(cursor.getString(cursor.getColumnIndex(FIELD_DATE)));
                    userModel.setTime(cursor.getString(cursor.getColumnIndex(FIELD_TIME)));
                    userModel.setTimemillis(cursor.getLong(cursor.getColumnIndex(FIELD_TIME_MILLIS)));
                    userModel.setTimeZone(cursor.getString(cursor.getColumnIndex(FIELD_TIMEZONE)));
                    userModel.setCallDuration(cursor.getString(cursor.getColumnIndex(FIELD_CALLDURATION)));
                    userModel.setCallCount(cursor.getInt(cursor.getColumnIndex(FIELD_CALLCOUNT)));
                    userModel.setStatus(cursor.getString(cursor.getColumnIndex(FIELD_STATUS)));
                    userModel.setCallType(cursor.getInt(cursor.getColumnIndex(FIELD_CALL_TYPE)));
                    userModel.setCallMode(cursor.getString(cursor.getColumnIndex(FIELD_CALLMODE)));

                    trovaMissedLogsList.add(userModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return trovaMissedLogsList;
    }

    public int getChatMessagesCount() {
        int messageCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) as count FROM " + TBLCHATMESSAGES + " where " + CHATFIELDSEENSTATUS + " = '0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<ChatMessageModel> chatmessageList = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                messageCount = cursor.getInt(cursor.getColumnIndex("count"));
            }
            cursor.close();
        }
        return messageCount;
    }

}
