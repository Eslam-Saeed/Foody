package com.udacity.bakingapp.common.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {


    public static final String APP_LOCALE_KEY = "appLocale";


    //======================================================================

    public static String getString(String key, Context ctx, String defaultValue) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return appPreferences.getString(key, defaultValue);
    }

    public static void setString(String key, String value, Context ctx) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        appPreferences.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(String key, Context ctx, boolean defaultValue) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return appPreferences.getBoolean(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value, Context ctx) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        appPreferences.edit().putBoolean(key, value).apply();
    }


}
