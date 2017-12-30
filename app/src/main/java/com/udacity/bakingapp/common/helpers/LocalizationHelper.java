package com.udacity.bakingapp.common.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Created by Sherif.ElNady on 10/19/2016.
 */
public class LocalizationHelper {

    public static final String LOCALE_ENGLISH = "en";
    public static final String LOCALE_ARABIC = "ar";

    public static final String DEFAULT_LOCALE = LOCALE_ENGLISH;

    public static void changeAppLanguage(String languageToLoad, Context ctx) {

        try {
            if (languageToLoad != null && !"".equals(languageToLoad)) {
                Resources res = ctx.getApplicationContext().getResources();
                android.content.res.Configuration config = res.getConfiguration();

                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config.setLocale(locale);
                ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());

                // save new language to shared preferences
                AppPreferences.setString(AppPreferences.APP_LOCALE_KEY, languageToLoad, ctx);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restartApp(Context context) {
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }


    public static String getLanguage(Context context) {
        // get language from shared preferences
        return AppPreferences.getString(AppPreferences.APP_LOCALE_KEY, context, "");
    }

}
