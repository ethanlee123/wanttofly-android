package com.example.wanttofly.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String SETTINGS_NAME = "user_preferences";
    private static UserPreferences sSharedPrefs;
    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;

    private UserPreferences() {}

    private UserPreferences(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }


    public static UserPreferences getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new UserPreferences(context.getApplicationContext());
        }

        return sSharedPrefs;
    }

    public static void put(UserPreferencesKeys key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommit();
    }

    public void put(UserPreferencesKeys key, int val) {
        doEdit();
        mEditor.putInt(key.name(), val);
        doCommit();
    }

    public void put(UserPreferencesKeys key, boolean val) {
        doEdit();
        mEditor.putBoolean(key.name(), val);
        doCommit();
    }

    public void put(UserPreferencesKeys key, float val) {
        doEdit();
        mEditor.putFloat(key.name(), val);
        doCommit();
    }

    public boolean getBoolean(UserPreferencesKeys key, boolean defaultValue) {
        return mPref.getBoolean(key.name(), defaultValue);
    }

    public boolean getBoolean(UserPreferencesKeys key) {
        return mPref.getBoolean(key.name(), false);
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    private static void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private static void doCommit() {
        if (mEditor != null) {
            mEditor.apply();
            mEditor = null;
        }
    }
}
