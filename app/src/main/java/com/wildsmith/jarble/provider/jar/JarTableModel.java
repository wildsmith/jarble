package com.wildsmith.jarble.provider.jar;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class JarTableModel implements Parcelable {

    public static final Creator<JarTableModel> CREATOR = new Creator<JarTableModel>() {
        @Override
        public JarTableModel createFromParcel(Parcel source) {
            return new JarTableModel(source);
        }

        @Override
        public JarTableModel[] newArray(int size) {
            return new JarTableModel[size];
        }
    };

    private int year;

    private int month;

    private int weekOfMonth;

    private int dayOfMonth;

    private int dayOfWeek;

    private long timestamp;

    private boolean inProgress;

    private byte[] image;

    private JarTableMarbleModel[] marbles;

    private JarTableModel(int year, int month, int weekOfMonth, int dayOfMonth, int dayOfWeek, @NonNull long timestamp,
        boolean inProgress, byte[] image, @NonNull JarTableMarbleModel[] marbles) {
        this.year = year;
        this.month = month;
        this.weekOfMonth = weekOfMonth;
        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.timestamp = timestamp;
        this.inProgress = inProgress;
        this.image = image;
        this.marbles = marbles;
    }

    private JarTableModel(Parcel in) {
        if (in == null) {
            return;
        }

        year = in.readInt();
        month = in.readInt();
        weekOfMonth = in.readInt();
        dayOfMonth = in.readInt();
        dayOfWeek = in.readInt();
        timestamp = in.readLong();
        inProgress = in.readByte() != 0;
        marbles = in.createTypedArray(JarTableMarbleModel.CREATOR);
        image = new byte[in.readInt()];
        if (image.length != 0) {
            in.readByteArray(image);
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public JarTableMarbleModel[] getMarbles() {
        return marbles;
    }

    public void setMarbles(JarTableMarbleModel[] marbles) {
        this.marbles = marbles;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JarTableStructure.Column.YEAR.title(), year);
        contentValues.put(JarTableStructure.Column.MONTH.title(), month);
        contentValues.put(JarTableStructure.Column.WEEK_OF_MONTH.title(), weekOfMonth);
        contentValues.put(JarTableStructure.Column.DAY_OF_MONTH.title(), dayOfMonth);
        contentValues.put(JarTableStructure.Column.DAY_OF_WEEK.title(), dayOfWeek);
        contentValues.put(JarTableStructure.Column.TIME_STAMP.title(), timestamp);
        contentValues.put(JarTableStructure.Column.IN_PROGRESS.title(), inProgress);
        contentValues.put(JarTableStructure.Column.IMAGE.title(), image);

        if (marbles != null && marbles.length != 0) {
            if (marbles[0] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_ZERO_ID.title(), marbles[0].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_ZERO_STATE.title(), marbles[0].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_ZERO_COLOR.title(), marbles[0].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_ZERO_PURPOSE_NOTES.title(), marbles[0].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_ZERO_PERFORMANCE_NOTES.title(), marbles[0].getPerformanceNotes());
            }
            if (marbles[1] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_ONE_ID.title(), marbles[1].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_ONE_STATE.title(), marbles[1].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_ONE_COLOR.title(), marbles[1].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_ONE_PURPOSE_NOTES.title(), marbles[1].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_ONE_PERFORMANCE_NOTES.title(), marbles[1].getPerformanceNotes());
            }
            if (marbles[2] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_TWO_ID.title(), marbles[2].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_TWO_STATE.title(), marbles[2].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_TWO_COLOR.title(), marbles[2].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_TWO_PURPOSE_NOTES.title(), marbles[2].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_TWO_PERFORMANCE_NOTES.title(), marbles[2].getPerformanceNotes());
            }
            if (marbles[3] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_THREE_ID.title(), marbles[3].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_THREE_STATE.title(), marbles[3].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_THREE_COLOR.title(), marbles[3].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_THREE_PURPOSE_NOTES.title(), marbles[3].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_THREE_PERFORMANCE_NOTES.title(), marbles[3].getPerformanceNotes());
            }
            if (marbles[4] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_FOUR_ID.title(), marbles[4].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_FOUR_STATE.title(), marbles[4].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_FOUR_COLOR.title(), marbles[4].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_FOUR_PURPOSE_NOTES.title(), marbles[4].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_FOUR_PERFORMANCE_NOTES.title(), marbles[4].getPerformanceNotes());
            }
            if (marbles[5] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_FIVE_ID.title(), marbles[5].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_FIVE_STATE.title(), marbles[5].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_FIVE_COLOR.title(), marbles[5].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_FIVE_PURPOSE_NOTES.title(), marbles[5].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_FIVE_PERFORMANCE_NOTES.title(), marbles[5].getPerformanceNotes());
            }
            if (marbles[6] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_SIX_ID.title(), marbles[6].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_SIX_STATE.title(), marbles[6].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_SIX_COLOR.title(), marbles[6].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_SIX_PURPOSE_NOTES.title(), marbles[6].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_SIX_PERFORMANCE_NOTES.title(), marbles[6].getPerformanceNotes());
            }
            if (marbles[7] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_SEVEN_ID.title(), marbles[7].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_SEVEN_STATE.title(), marbles[7].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_SEVEN_COLOR.title(), marbles[7].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_SEVEN_PURPOSE_NOTES.title(), marbles[7].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_SEVEN_PERFORMANCE_NOTES.title(), marbles[7].getPerformanceNotes());
            }
            if (marbles[8] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_EIGHT_ID.title(), marbles[8].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_EIGHT_STATE.title(), marbles[8].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_EIGHT_COLOR.title(), marbles[8].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_EIGHT_PURPOSE_NOTES.title(), marbles[8].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_EIGHT_PERFORMANCE_NOTES.title(), marbles[8].getPerformanceNotes());
            }
            if (marbles[9] != null) {
                contentValues.put(JarTableStructure.Column.MARBLE_NINE_ID.title(), marbles[9].getId());
                contentValues.put(JarTableStructure.Column.MARBLE_NINE_STATE.title(), marbles[9].getState());
                contentValues.put(JarTableStructure.Column.MARBLE_NINE_COLOR.title(), marbles[9].getColor());
                contentValues.put(JarTableStructure.Column.MARBLE_NINE_PURPOSE_NOTES.title(), marbles[9].getPurposeNotes());
                contentValues.put(JarTableStructure.Column.MARBLE_NINE_PERFORMANCE_NOTES.title(), marbles[9].getPerformanceNotes());
            }
        }

        return contentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(weekOfMonth);
        dest.writeInt(dayOfMonth);
        dest.writeInt(dayOfWeek);
        dest.writeLong(timestamp);
        dest.writeByte((byte) (inProgress ? 1 : 0));
        dest.writeTypedArray(marbles, 0);
        dest.writeInt((image != null) ? image.length : 0);
        dest.writeByteArray(image);
    }

    public static class Builder {

        enum Extras {
            YEAR,
            MONTH,
            WEEK_OF_MONTH,
            DAY_OF_MONTH,
            DAY_OF_WEEK,
            TIME_STAMP,
            IN_PROGRESS,
            IMAGE,
            MARBLES
        }

        private Map<Extras, Object> dataMap = new HashMap<>(8);

        public Builder setYear(int year) {
            dataMap.put(Extras.YEAR, year);
            return this;
        }

        public Builder setMonth(int month) {
            dataMap.put(Extras.MONTH, month);
            return this;
        }

        public Builder setWeekOfMonth(int week) {
            dataMap.put(Extras.WEEK_OF_MONTH, week);
            return this;
        }

        public Builder setDayOfMonth(int day) {
            dataMap.put(Extras.DAY_OF_MONTH, day);
            return this;
        }

        public Builder setDayOfWeek(int day) {
            dataMap.put(Extras.DAY_OF_WEEK, day);
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            dataMap.put(Extras.TIME_STAMP, timestamp);
            return this;
        }

        public Builder setInProgress(boolean inProgress) {
            dataMap.put(Extras.IN_PROGRESS, inProgress);
            return this;
        }

        public Builder setImage(byte[] image) {
            dataMap.put(Extras.IMAGE, image);
            return this;
        }

        public Builder setMarbles(JarTableMarbleModel[] marbles) {
            dataMap.put(Extras.MARBLES, marbles);
            return this;
        }

        public JarTableModel build(long timestamp) {
            int year = -1;
            int month = -1;
            int weekOfMonth = -1;
            int dayOfMonth = -1;
            int dayOfWeek = -1;
            boolean inProgress = false;
            byte[] image = null;
            JarTableMarbleModel[] marbles = new JarTableMarbleModel[0];

            for (Map.Entry<Extras, Object> entry : dataMap.entrySet()) {
                switch (entry.getKey()) {
                    case YEAR:
                        year = (Integer) dataMap.get(Extras.YEAR);
                        break;
                    case MONTH:
                        month = (Integer) dataMap.get(Extras.MONTH);
                        break;
                    case WEEK_OF_MONTH:
                        weekOfMonth = (Integer) dataMap.get(Extras.WEEK_OF_MONTH);
                        break;
                    case DAY_OF_MONTH:
                        dayOfMonth = (Integer) dataMap.get(Extras.DAY_OF_MONTH);
                        break;
                    case DAY_OF_WEEK:
                        dayOfWeek = (Integer) dataMap.get(Extras.DAY_OF_WEEK);
                        break;
                    case TIME_STAMP:
                        timestamp = (long) dataMap.get(Extras.TIME_STAMP);
                        break;
                    case IN_PROGRESS:
                        inProgress = (Boolean) dataMap.get(Extras.IN_PROGRESS);
                        break;
                    case IMAGE:
                        image = (byte[]) dataMap.get(Extras.IMAGE);
                        break;
                    case MARBLES:
                        marbles = (JarTableMarbleModel[]) dataMap.get(Extras.MARBLES);
                        break;
                }
            }

            dataMap.clear();

            return new JarTableModel(year, month, weekOfMonth, dayOfMonth, dayOfWeek, timestamp, inProgress, image, marbles);
        }
    }

    public String getTimestampAsString() {
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeInMillis(timestamp);
        calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}