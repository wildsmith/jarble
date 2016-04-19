package com.wildsmith.jarble.jar;

import android.content.ContentValues;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wildsmith.jarble.marble.MarbleTableModel;
import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.utils.CollectionUtils;
import com.wildsmith.utils.DateUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private int dayOfMonth;

    private String timestamp;

    private boolean inProgress;

    private byte[] image;

    private MarbleTableModel[] marbles;

    private JarTableModel(int year, int month, int dayOfMonth, String timestamp, boolean inProgress, byte[] image,
        @NonNull MarbleTableModel[] marbles) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
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
        dayOfMonth = in.readInt();
        timestamp = in.readString();
        inProgress = in.readByte() != 0;
        marbles = in.createTypedArray(MarbleTableModel.CREATOR);
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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getTimestamp() {
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

    public MarbleTableModel[] getMarbles() {
        return marbles;
    }

    void setMarbles(MarbleTableModel[] marbles) {
        this.marbles = marbles;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(JarTableStructure.Column.TIME_STAMP.title(), timestamp);
        contentValues.put(JarTableStructure.Column.IN_PROGRESS.title(), inProgress);
        contentValues.put(JarTableStructure.Column.IMAGE.title(), image);
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
        dest.writeInt(dayOfMonth);
        dest.writeString(timestamp);
        dest.writeByte((byte) (inProgress ? 1 : 0));
        dest.writeTypedArray(marbles, 0);
        dest.writeInt((image != null) ? image.length : 0);
        dest.writeByteArray(image);
    }

    public static class Builder {

        enum Extras {
            TIME_STAMP,
            IN_PROGRESS,
            IMAGE,
            MARBLES
        }

        private Map<Extras, Object> dataMap = new HashMap<>();

        Builder setTimeStamp(String timeStamp) {
            dataMap.put(Extras.TIME_STAMP, timeStamp);
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

        Builder setMarbles(MarbleTableModel[] marbles) {
            dataMap.put(Extras.MARBLES, marbles);
            return this;
        }

        public JarTableModel build(@NonNull Context context, long timestamp) {
            int year = -1;
            int month = -1;
            int dayOfMonth = -1;
            String timestampString = null;
            boolean inProgress = false;
            byte[] image = null;
            MarbleTableModel[] marbles = null;

            for (Map.Entry<Extras, Object> entry : dataMap.entrySet()) {
                switch (entry.getKey()) {
                    case TIME_STAMP:
                        timestampString = (String) dataMap.get(Extras.TIME_STAMP);
                        break;
                    case IN_PROGRESS:
                        inProgress = (Boolean) dataMap.get(Extras.IN_PROGRESS);
                        break;
                    case IMAGE:
                        image = (byte[]) dataMap.get(Extras.IMAGE);
                        break;
                    case MARBLES:
                        marbles = (MarbleTableModel[]) dataMap.get(Extras.MARBLES);
                        break;
                }
            }

            if (TextUtils.isEmpty(timestampString) && timestamp != 0) {
                timestampString = DateUtils.getTimestampAsString(timestamp);
            }

            if (CollectionUtils.isEmpty(marbles)) {
                marbles = MarbleTableModel.buildEmptyMarbleArray(timestampString);
                MarbleTableModel.flipInProgressOrEditingToDone(marbles, JarblePreferencesHelper.getMarbleAchievementsAsInteger(context));
            }

            dataMap.clear();

            if (!TextUtils.isEmpty(timestampString)) {
                Calendar calendar = Calendar.getInstance();
                if (timestamp == 0) {
                    calendar.setTime(DateUtils.getDateFromTimestamp(timestampString));
                } else {
                    calendar.setTimeInMillis(timestamp);
                }
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }

            return new JarTableModel(year, month, dayOfMonth, timestampString, inProgress, image, marbles);
        }
    }

    public void update(@NonNull MarbleTableModel updatedMarble) {
        final int index = updatedMarble.getNumber();
        if (CollectionUtils.isValidIndex(marbles, index)) {
            marbles[updatedMarble.getNumber()] = updatedMarble;
        }
    }
}