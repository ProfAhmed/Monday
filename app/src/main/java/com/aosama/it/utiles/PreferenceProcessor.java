package com.aosama.it.utiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceProcessor {
    private static PreferenceProcessor mInstance;

    private static SharedPreferences prefs;

    @SuppressLint("CommitPrefEdits")
    private PreferenceProcessor(Context context) {
        prefs = context.getSharedPreferences(MyConfig.MyPrefs.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceProcessor getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PreferenceProcessor(context);
        }
        return mInstance;
    }

    public void setInt(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key, Integer defValue) {
        return prefs.getInt(key, defValue);
    }

    public void setFloat(String key, float value) {
        prefs.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key, Float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public void setStr(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String getStr(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public void setBool(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public boolean getBool(String key, Boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

}
