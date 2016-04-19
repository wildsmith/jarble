package com.wildsmith.jarble.marble;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MarbleTableModel implements Parcelable {

    public static final int MAX_MARBLE_COUNT = 10;

    public enum State {
        NOT_STARTED,
        IN_PROGRESS,
        EDITING,
        DONE;

        public static State fromInt(int stateInt) {
            for (State state : State.values()) {
                if (state.ordinal() == stateInt) {
                    return state;
                }
            }

            return NOT_STARTED;
        }
    }

    public static final Creator<MarbleTableModel> CREATOR = new Creator<MarbleTableModel>() {
        @Override
        public MarbleTableModel createFromParcel(Parcel source) {
            return new MarbleTableModel(source);
        }

        @Override
        public MarbleTableModel[] newArray(int size) {
            return new MarbleTableModel[size];
        }
    };

    private MarbleTableModel(Parcel in) {
        if (in == null) {
            return;
        }

        number = in.readInt();
        state = in.readInt();
        color = in.readString();
        purposeNotes = in.readString();
        performanceNotes = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(state);
        dest.writeString(color);
        dest.writeString(purposeNotes);
        dest.writeString(performanceNotes);
    }

    private String timestamp;

    private int number;

    private int state;

    private String color;

    private String purposeNotes;

    private String performanceNotes;

    public MarbleTableModel(String timestamp, int number, int state, String color, String purposeNotes, String performanceNotes) {
        this.timestamp = timestamp;
        this.number = number;
        this.state = state;
        this.color = color;
        this.purposeNotes = purposeNotes;
        this.performanceNotes = performanceNotes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getNumber() {
        return number;
    }

    public int getState() {
        return state;
    }

    public State getStateAsEnum() {
        return State.fromInt(state);
    }

    public void setState(State state) {
        this.state = state.ordinal();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPurposeNotes() {
        return purposeNotes;
    }

    public void setPurposeNotes(String purposeNotes) {
        this.purposeNotes = purposeNotes;
    }

    public String getPerformanceNotes() {
        return performanceNotes;
    }

    public void setPerformanceNotes(String performanceNotes) {
        this.performanceNotes = performanceNotes;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MarbleTableStructure.Column.TIME_STAMP.title(), timestamp);
        contentValues.put(MarbleTableStructure.Column.NUMBER.title(), number);
        contentValues.put(MarbleTableStructure.Column.STATE.title(), state);
        contentValues.put(MarbleTableStructure.Column.COLOR.title(), color);
        contentValues.put(MarbleTableStructure.Column.PURPOSE_NOTES.title(), purposeNotes);
        contentValues.put(MarbleTableStructure.Column.PERFORMANCE_NOTES.title(), performanceNotes);
        return contentValues;
    }

    public static MarbleTableModel[] buildEmptyMarbleArray(String timestamp) {
        MarbleTableModel[] marbles = new MarbleTableModel[MAX_MARBLE_COUNT];
        for (int i = 0; i < marbles.length; i++) {
            marbles[i] = new MarbleTableModel(timestamp, i, MarbleTableModel.State.NOT_STARTED.ordinal(), null, null, null);
        }

        return marbles;
    }

    public static void flipInProgressOrEditingToDone(@NonNull MarbleTableModel[] marbles, int availableAchievements) {
        for (MarbleTableModel marble : marbles) {
            if (marble == null) {
                continue;
            }

            switch (marble.getStateAsEnum()) {
                case IN_PROGRESS:
                case EDITING:
                    marble.setState(State.DONE);
                    break;
                case NOT_STARTED:
                    if (availableAchievements != 0) {
                        marble.setState(State.IN_PROGRESS);
                        availableAchievements--;
                    }
            }

            if (availableAchievements == 0) {
                break;
            }
        }
    }

    public static void flipAllToDone(@NonNull MarbleTableModel[] marbles) {
        for (MarbleTableModel marble : marbles) {
            if (marble == null) {
                continue;
            }

            marble.setState(State.DONE);
        }
    }

    public static void flipInProgressToEditing(@NonNull MarbleTableModel[] marbles) {
        for (MarbleTableModel marble : marbles) {
            if (marble == null) {
                continue;
            }

            switch (marble.getStateAsEnum()) {
                case IN_PROGRESS:
                    marble.setState(State.EDITING);
                    break;
            }
        }
    }

    public static boolean hasInProgressMarbles(@NonNull MarbleTableModel[] marbles) {
        return hasMarbleOfState(marbles, State.IN_PROGRESS);
    }

    public static boolean hasEditingMarbles(@NonNull MarbleTableModel[] marbles) {
        return hasMarbleOfState(marbles, State.EDITING);
    }

    @Nullable
    public static MarbleTableModel getInProgressMarble(@NonNull MarbleTableModel[] marbles) {
        return getMarbleOfState(marbles, State.IN_PROGRESS);
    }

    @Nullable
    public static MarbleTableModel getEditingMarble(@NonNull MarbleTableModel[] marbles) {
        return getMarbleOfState(marbles, State.EDITING);
    }

    private static boolean hasMarbleOfState(@NonNull MarbleTableModel[] marbles, @NonNull State state) {
        for (MarbleTableModel marble : marbles) {
            if (marble == null || marble.getStateAsEnum() != state) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Nullable
    private static MarbleTableModel getMarbleOfState(@NonNull MarbleTableModel[] marbles, @NonNull State state) {
        for (MarbleTableModel marble : marbles) {
            if (marble == null || marble.getStateAsEnum() != state) {
                continue;
            }
            return marble;
        }
        return null;
    }
}
