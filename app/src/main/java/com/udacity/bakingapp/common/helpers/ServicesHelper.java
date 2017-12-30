package com.udacity.bakingapp.common.helpers;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import com.udacity.bakingapp.common.models.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ServicesHelper {
    private static ServicesHelper mInstance;

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    private RequestQueue mRequestQueue;
    private DefaultRetryPolicy defaultRetry = new DefaultRetryPolicy(
            Constants.TIME_OUT_VALUE,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    public enum Tag {
        GET_ALL_RECIPES
    }

    private ServicesHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static synchronized ServicesHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServicesHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.add(req);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    //================ All Recipes =======================
    public void getListRecipes(final Response.Listener<ArrayList<Recipe>> getListRecipesSuccessListener, Response.ErrorListener getListRecipesErrorListener) {
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String stringResponse) {
                    if (stringResponse != null) {
                        stringResponse = stringResponse.trim().replace("\n", "");
                        Type type = new TypeToken<ArrayList<Recipe>>() {
                        }.getType();
                        if (stringResponse != null)
                            getListRecipesSuccessListener.onResponse((ArrayList<Recipe>) MyApplication.getmGson().fromJson(stringResponse, type));

                    }
                }
            }, getListRecipesErrorListener);
            stringRequest.setTag(Tag.GET_ALL_RECIPES);
            stringRequest.setRetryPolicy(defaultRetry);
            addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
