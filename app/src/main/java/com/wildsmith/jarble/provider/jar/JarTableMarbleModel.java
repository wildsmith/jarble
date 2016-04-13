package com.wildsmith.jarble.provider.jar;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class JarTableMarbleModel implements Parcelable {

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

    public static final Creator<JarTableMarbleModel> CREATOR = new Creator<JarTableMarbleModel>() {
        @Override
        public JarTableMarbleModel createFromParcel(Parcel source) {
            return new JarTableMarbleModel(source);
        }

        @Override
        public JarTableMarbleModel[] newArray(int size) {
            return new JarTableMarbleModel[size];
        }
    };

    private JarTableMarbleModel(Parcel in) {
        if (in == null) {
            return;
        }

        id = in.readInt();
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
        dest.writeInt(id);
        dest.writeInt(state);
        dest.writeString(color);
        dest.writeString(purposeNotes);
        dest.writeString(performanceNotes);
    }

    private int id;

    private int state;

    private String color;

    private String purposeNotes;

    private String performanceNotes;

    public JarTableMarbleModel(int id, int state, String color, String purposeNotes, String performanceNotes) {
        this.id = id;
        this.state = state;
        this.color = color;
        this.purposeNotes = purposeNotes;
        this.performanceNotes = performanceNotes;
    }

    public int getId() {
        return id;
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

    public static JarTableMarbleModel[] buildEmptyMarbleArray() {
        JarTableMarbleModel[] marbles = new JarTableMarbleModel[MAX_MARBLE_COUNT];
        for (int i = 0; i < marbles.length; i++) {
            marbles[i] = new JarTableMarbleModel(i, JarTableMarbleModel.State.NOT_STARTED.ordinal(), null, null, null);
        }

        return marbles;
    }

    public static void flipInProgressOrEditingToDone(@NonNull JarTableMarbleModel[] marbles, int availableAchievements) {
        for (JarTableMarbleModel marble : marbles) {
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

    public static void flipAllToDone(@NonNull JarTableMarbleModel[] marbles) {
        for (JarTableMarbleModel marble : marbles) {
            if (marble == null) {
                continue;
            }

            marble.setState(State.DONE);
        }
    }

    public static void flipInProgressToEditing(@NonNull JarTableMarbleModel[] marbles) {
        for (JarTableMarbleModel marble : marbles) {
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

    public static boolean hasInProgressMarbles(@NonNull JarTableMarbleModel[] marbles) {
        return hasMarbleOfState(marbles, State.IN_PROGRESS);
    }

    public static boolean hasEditingMarbles(@NonNull JarTableMarbleModel[] marbles) {
        return hasMarbleOfState(marbles, State.EDITING);
    }

    @Nullable
    public static JarTableMarbleModel getInProgressMarble(@NonNull JarTableMarbleModel[] marbles) {
        return getMarbleOfState(marbles, State.IN_PROGRESS);
    }

    @Nullable
    public static JarTableMarbleModel getEditingMarble(@NonNull JarTableMarbleModel[] marbles) {
        return getMarbleOfState(marbles, State.EDITING);
    }

    private static boolean hasMarbleOfState(@NonNull JarTableMarbleModel[] marbles, @NonNull State state) {
        for (JarTableMarbleModel marble : marbles) {
            if (marble == null || marble.getStateAsEnum() != state) {
                continue;
            }
            return true;
        }
        return false;
    }

    @Nullable
    private static JarTableMarbleModel getMarbleOfState(@NonNull JarTableMarbleModel[] marbles, @NonNull State state) {
        for (JarTableMarbleModel marble : marbles) {
            if (marble == null || marble.getStateAsEnum() != state) {
                continue;
            }
            return marble;
        }
        return null;
    }

    public static void updateForNotes(@NonNull JarTableMarbleModel[] marbles, @NonNull JarTableMarbleModel updatedMarble) {
        for (JarTableMarbleModel marble : marbles) {
            if (marble == null) {
                continue;
            }

            switch (marble.getStateAsEnum()) {
                case IN_PROGRESS:
                case EDITING:
                    marble.purposeNotes = updatedMarble.purposeNotes;
                    marble.performanceNotes = updatedMarble.performanceNotes;
                    break;
            }
        }
    }

    public static void updateForColor(@NonNull JarTableMarbleModel[] marbles, @NonNull JarTableMarbleModel updatedMarble) {
        for (JarTableMarbleModel marble : marbles) {
            if (marble == null || marble.getId() != updatedMarble.getId()) {
                continue;
            }

            marble.setColor(updatedMarble.getColor());
            break;
        }
    }
}
