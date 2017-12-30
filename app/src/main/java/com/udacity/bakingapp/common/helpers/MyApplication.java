package com.udacity.bakingapp.common.helpers;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Eslam on 11/20/2017.
 */

public class MyApplication extends Application {

    private static Gson mGson;

    @Override
    public void onCreate() {
        super.onCreate();
        mGson = new GsonBuilder().create();
        LocalizationHelper.changeAppLanguage(LocalizationHelper.getLanguage(this), this);

    }

    public static Gson getmGson() {
        if (mGson == null)
            mGson = new GsonBuilder().create();
        return mGson;
    }


    public static MyApplication getInstance(Context context) {
        return ((MyApplication) context.getApplicationContext());
    }

}
