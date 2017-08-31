package com.yackeen.livehealthy.sales.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Abdelrhman Walid on 5/17/2017.
 */

public class FormatUtil {
    public static String formatTime(String hour) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(hour);
            return new SimpleDateFormat("hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(String hour, String minute) {
        String time = hour + ":" + minute;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(time);
            return new SimpleDateFormat("hh:mm a").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateWithT(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
        if (time == null)
            return null;
        try {
            Date date = format.parse(time);
            return new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateTimeWithT(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
        if (time == null)
            return null;
        try {
            Date date = format.parse(time);
            return new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date formatDateTimeWithTDate(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.ENGLISH);
        if (time == null)
            return null;
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long formatTimeStringWithTToTimeStamp(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
            Date parsedDate = dateFormat.parse(date);
            return parsedDate.getTime();
        } catch (Exception e) {//this generic but you can control another types of exception
            Log.e("", "");
        }
        return null;
    }

    public static String getRelativeTime(Context context, Long time) {
        if (time == null)
            return null;
        return DateUtils.getRelativeDateTimeString(
                context,
                time,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_ALL
        ).toString();
    }


}
