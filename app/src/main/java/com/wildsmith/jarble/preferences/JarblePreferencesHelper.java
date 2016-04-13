package com.wildsmith.jarble.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Helper to set and retrieve the preferences specific to this app. The preferences are stored in {@link android.content.SharedPreferences}
 * leveraging {@link SharedPreferencesHelper}.
 */
public class JarblePreferencesHelper {

    public enum Key {
        CURRENT_VIEW,
        VIBRATION,
        BEHAVIOR_DURATION,
        NEEDS_FTUX,
        MARBLE_ACHIEVEMENTS;

        public static Key fromString(String keyString) {
            for (Key key : Key.values()) {
                if (key.name().equals(keyString)) {
                    return key;
                }
            }

            return null;
        }
    }

    public enum CurrentView {
        YEARLY,
        MONTHLY,
        WEEKLY,
        DAY;

        @NonNull
        public static CurrentView fromInt(Integer ordinal) {
            if (ordinal == null) {
                return YEARLY;
            }

            for (CurrentView currentView : CurrentView.values()) {
                if (currentView.ordinal() == ordinal) {
                    return currentView;
                }
            }

            return YEARLY;
        }
    }

    public static final String BEHAVIOR_DURATION_DEFAULT = "20";

    public static final String MARBLE_ACHIEVEMENTS_DEFAULT = "2";

    public static final boolean NEEDS_FTUX_DEFAULT = true;

    /**
     * Set the {@link CurrentView} within {@link android.content.SharedPreferences}
     */
    public static void setCurrentView(@NonNull Context context, @NonNull CurrentView currentView) {
        SharedPreferencesHelper.putInt(context, Key.CURRENT_VIEW.name(), currentView.ordinal());
    }

    /**
     * @return the stored {@link CurrentView} within {@link android.content.SharedPreferences}
     */
    @Nullable
    public static CurrentView getCurrentView(@NonNull Context context) {
        return CurrentView.fromInt(SharedPreferencesHelper.getInt(context, Key.CURRENT_VIEW.name()));
    }

    public static boolean isVibrationEnabled(@NonNull Context context, boolean defaultValue) {
        return SharedPreferencesHelper.getBoolean(context, Key.VIBRATION.name(), defaultValue);
    }

    public static int getBehaviorDurationAsInteger(@NonNull Context context) {
        return Integer.parseInt(SharedPreferencesHelper.getString(context, Key.BEHAVIOR_DURATION.name(), BEHAVIOR_DURATION_DEFAULT));
    }

    public static int getMarbleAchievementsAsInteger(@NonNull Context context) {
        return Integer.parseInt(SharedPreferencesHelper.getString(context, Key.MARBLE_ACHIEVEMENTS.name(), MARBLE_ACHIEVEMENTS_DEFAULT));
    }

    public static void setFTUX(@NonNull Context context, boolean needsFTUX) {
        SharedPreferencesHelper.putBoolean(context, Key.NEEDS_FTUX.name(), needsFTUX);
    }

    public static boolean needsFTUX(@NonNull Context context) {
        return SharedPreferencesHelper.getBoolean(context, Key.NEEDS_FTUX.name(), NEEDS_FTUX_DEFAULT);
    }
}