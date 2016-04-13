package com.wildsmith.jarble.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Map;

class SharedPreferencesHelper {

    static void putInt(Context context, String key, Integer value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null) {
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        if (editor == null) {
            return;
        }

        if (value == null) {
            value = -1;
        }

        editor.putInt(key, value).apply();
    }

    static Integer getInt(Context context, String key) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null) {
            return null;
        }

        Integer value = null;
        if (preferences.contains(key)) {
            Map<String, ?> allPreferences = preferences.getAll();
            Object preference = allPreferences.get(key);
            if (preference instanceof Integer) {
                value = (Integer) preference;
            }
        }

        if (value != null && value == -1) {
            return null;
        }

        return value;
    }

    static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }

        String value = null;
        if (preferences.contains(key)) {
            Map<String, ?> allPreferences = preferences.getAll();
            Object preference = allPreferences.get(key);
            if (preference instanceof String) {
                //String
                value = (String) preference;
            }
        }

        if (TextUtils.isEmpty(value)) {
            value = defaultValue;
        }

        return value;
    }

    static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null) {
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        if (editor == null) {
            return;
        }

        editor.putBoolean(key, value).apply();
    }

    static Boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }

        return preferences.getBoolean(key, defaultValue);
    }
}
