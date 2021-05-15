package com.shine.apps.utils;


import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.shine.apps.MainApplication;

public class SharedPreferenceUtils {
    private static final SharedPreferenceUtils mInstance = new SharedPreferenceUtils();

    public static SharedPreferenceUtils getInstance() {
        return mInstance;
    }

    public boolean getBoolean(String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext());
        return sharedPreferences.getBoolean(key, false);
    }
}
