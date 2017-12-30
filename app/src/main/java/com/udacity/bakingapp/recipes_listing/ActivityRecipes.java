package com.udacity.bakingapp.recipes_listing;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.recipe_details.FragmentRecipeDetails;

public class ActivityRecipes extends BaseActivity implements FragmentRecipesListing.RecipeListingInteraction {
    private Toolbar mToolbarRecipesListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_listing);
        initializeViews();
        setToolbar(mToolbarRecipesListing, getString(R.string.app_name), false);
        loadFragments();
    }

    @Override
    protected void initializeViews() {
        mToolbarRecipesListing = findViewById(R.id.toolbarStep);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void loadFragments() {
        replaceFragment(FragmentRecipesListing.newInstance(), R.id.fragmentContainerRecipeListing, false);
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        replaceFragment(FragmentRecipeDetails.newInstance(recipe), R.id.fragmentContainerRecipeListing, true);
    }
}
