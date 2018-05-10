package com.instag.vijay.fasttrending.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by iyara_rajan on 10-07-2017.
 */

public class CommonDateTimeZone {
    public String timezone;
    public String time;
    public String currDate;
    public long messageId;
    long timemilliseconds;
    public int timeZoneOffsetMilliSeconds;

    public CommonDateTimeZone() {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        this.timezone = tz.getDisplayName();
        this.timeZoneOffsetMilliSeconds = tz.getRawOffset();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        this.currDate = df.format(cal.getTime());
        df = new SimpleDateFormat("HH:mm");
        this.time = df.format(cal.getTime());
        this.messageId = System.currentTimeMillis();
        this.timemilliseconds = System.currentTimeMillis();
    }
}
