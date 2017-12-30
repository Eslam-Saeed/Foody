package com.udacity.bakingapp.recipes_listing;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BasePresenter;
import com.udacity.bakingapp.common.helpers.ServicesHelper;
import com.udacity.bakingapp.common.models.Recipe;

import java.util.ArrayList;

/**
 * Created by eslam on 12/23/17.
 */

public class PresenterRecipesListing extends BasePresenter {
    private Context mContext;
    private ViewRecipesListing mViewRecipesListing;
    private ArrayList<Recipe> mRecipeList;

    public PresenterRecipesListing(Context context, ViewRecipesListing viewRecipesListing) {
        mContext = context;
        mViewRecipesListing = viewRecipesListing;
        mRecipeList = new ArrayList<>();
    }

    void serviceGetRecipesList() {
        mViewRecipesListing.showProgress(true);
        ServicesHelper.getInstance(mContext).getListRecipes(getListRecipesSuccessListener, getListRecipesErrorListener);
    }


    private Response.Listener<ArrayList<Recipe>> getListRecipesSuccessListener = new Response.Listener<ArrayList<Recipe>>() {
        @Override
        public void onResponse(ArrayList<Recipe> response) {
            mViewRecipesListing.showProgress(false);
            if (response != null) {
                mRecipeList.addAll(response);
                mViewRecipesListing.onListingRecipesSuccess();
            } else
                mViewRecipesListing.onListingRecipesFail(mContext.getString(R.string.something_went_wrong));

        }
    };

    private Response.ErrorListener getListRecipesErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            mViewRecipesListing.showProgress(false);
            mViewRecipesListing.onListingRecipesFail(mContext.getString(R.string.something_went_wrong));
        }
    };

    public ArrayList<Recipe> getRecipeList() {
        return mRecipeList;
    }
}
