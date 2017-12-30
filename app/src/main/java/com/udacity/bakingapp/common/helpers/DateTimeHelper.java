package com.udacity.bakingapp.common.helpers;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {
    private static final String YYYY_MM_DD = "yyyy-mm-dd";

    private static String formatDate(String oldFormat, String newFormat, String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat fmt = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        Date date;
        try {
            date = fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        return fmtOut.format(date);
    }

    public static String formatDate(String oldFormat, String newFormat, String dateStr, Locale oldLocale, Locale newLocale) {
        if (oldLocale == null) {
            return formatDate(oldFormat, newFormat, dateStr);
        }

        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }
        SimpleDateFormat fmt = new SimpleDateFormat(oldFormat, oldLocale);
        Date date;
        try {
            date = fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateStr;
        }

        SimpleDateFormat fmtOut = new SimpleDateFormat(newFormat, newLocale);
        return fmtOut.format(date);
    }

    /**
     * Converts The date object to String
     *
     * @param date   The date object
     * @param format The format to display the date String
     */
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.ENGLISH);
        return dateFormatter.format(date);
    }

    public static String convertDateToStringForServer(Date date, String format) {
        Locale localeObject = Locale.ENGLISH;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, localeObject);
        return dateFormatter.format(date);
    }

    /**
     * Converts the date String returned from server to Date object
     *
     * @param msJsonDateTime date String
     * @return Date Object
     */
    public static Date parseMsTimestampToDateWithFormat(String msJsonDateTime) {
        if (msJsonDateTime == null)
            return null;
        int startIndex = msJsonDateTime.indexOf("Date(") + 5;
        int endIndex = msJsonDateTime.indexOf(")");
        long ts;
        int timeIndex = 0;
        // int sign = 0;
        if (msJsonDateTime.indexOf("+") > 0) {
            // sign = 1;
            timeIndex = msJsonDateTime.indexOf("+");
        } else if (msJsonDateTime.indexOf("-") > 0) {
            // sign = -1;
            timeIndex = msJsonDateTime.indexOf("-");
        }
        if (timeIndex > 0) {

            ts = Long
                    .parseLong(msJsonDateTime.substring(startIndex, timeIndex));
        } else {
            ts = Long.parseLong(msJsonDateTime.substring(startIndex, endIndex));
        }
        return new Date(ts);
    }

    public static String serializeDateToMsTimestamp(final Date date) {
        Long ticks = date.getTime();
        return "/Date(" + ticks.toString() + ")/";
    }

    public static String timestampToDateString(long milliSeconds, String dateFormat) {
        try {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getAgeFromDate(String birthDate) {
        int age = 0;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD, Locale.ENGLISH);
            Date date1 = dateFormat.parse(birthDate);
            Calendar now = Calendar.getInstance();
            Calendar dob = Calendar.getInstance();
            dob.setTime(date1);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int year1 = now.get(Calendar.YEAR);
            int year2 = dob.get(Calendar.YEAR);
            age = year1 - year2;
            if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;

        } catch (ParseException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
