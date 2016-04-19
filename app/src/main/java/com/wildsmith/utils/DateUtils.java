package com.wildsmith.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String FULL_MONTH_FORMAT = "LLLL";

    public static final String HALF_MONTH_FORMAT = "MMM";

    public static final String FULL_DAY_FORMAT = "EEEE";

    public static String reformatTimestamp(@NonNull String timestamp, @NonNull String newFormat) {
        return reformatDate(timestamp, TIME_STAMP_FORMAT, newFormat);
    }

    public static String reformatDate(@NonNull String dateString, @NonNull String oldFormat, @NonNull String newFormat) {
        return new SimpleDateFormat(newFormat, Locale.getDefault()).format(getDateFromString(dateString, oldFormat));
    }

    @Nullable
    public static Date getDateFromTimestamp(@NonNull String timestamp) {
        return getDateFromString(timestamp, TIME_STAMP_FORMAT);
    }

    @Nullable
    public static Date getDateFromString(@NonNull String dateString, @NonNull String oldFormat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(oldFormat, Locale.getDefault());
            return formatter.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Could not parse timestamp to the requested format.", e);
        }

        return null;
    }

    public static String getTimestampAsString(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_FORMAT, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}
