package com.wildsmith.jarble.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import com.wildsmith.jarble.R;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //add xml
        addPreferencesFromResource(R.xml.settings);

        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(getActivity()),
            JarblePreferencesHelper.Key.BEHAVIOR_DURATION.name());
    }

    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        JarblePreferencesHelper.Key keyEnum = JarblePreferencesHelper.Key.fromString(key);
        if (preference == null || keyEnum == null) {
            return;
        }

        String summary = null;
        switch (keyEnum) {
            case BEHAVIOR_DURATION:
                int
                    behaviorDuration =
                    Integer.parseInt(sharedPreferences.getString(key, JarblePreferencesHelper.BEHAVIOR_DURATION_DEFAULT));
                String behaviorDurationString = "%s minute";
                if (behaviorDuration > 1) {
                    behaviorDurationString += "s";
                }

                summary = String.format(Locale.getDefault(), getString(R.string.behavior_duration_summary),
                    String.format(Locale.getDefault(), behaviorDurationString, behaviorDuration));
                break;
            case MARBLE_ACHIEVEMENTS:
                int marbleAchievements = Integer.parseInt(sharedPreferences.getString(key,
                    JarblePreferencesHelper.MARBLE_ACHIEVEMENTS_DEFAULT));
                String achievementString = "%s marble";
                if (marbleAchievements > 1) {
                    achievementString += "s";
                }

                summary = String.format(Locale.getDefault(), getString(R.string.marble_achievements_summary),
                    String.format(Locale.getDefault(), achievementString, marbleAchievements));
                break;
        }

        if (!TextUtils.isEmpty(summary)) {
            preference.setSummary(summary);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}