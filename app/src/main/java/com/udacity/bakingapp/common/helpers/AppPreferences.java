package com.udacity.bakingapp.common.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.udacity.bakingapp.common.models.Recipe;

public class AppPreferences {


    public static final String APP_LOCALE_KEY = "appLocale";
    public static final String SELECTED_RECIPE = "selected_recipe";
    public static final String SELECTED_STEP = "selected_step";


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


    public static Recipe getDesiredRecipe(Context context) {
        Recipe recipe = null;
        String recipeString;
        Gson gson = new Gson();
        recipeString = getString(SELECTED_RECIPE, context, "");
        if (!TextUtils.isEmpty(recipeString))
            recipe = gson.fromJson(recipeString, Recipe.class);
        return recipe;
    }
}
