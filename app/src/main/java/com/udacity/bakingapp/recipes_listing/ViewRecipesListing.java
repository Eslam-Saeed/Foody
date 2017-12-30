package com.udacity.bakingapp.recipes_listing;

import com.udacity.bakingapp.common.base.BaseView;

/**
 * Created by eslam on 12/23/17.
 */

public interface ViewRecipesListing extends BaseView {

    void onListingRecipesFail(String message);

    void onListingRecipesSuccess();
}
