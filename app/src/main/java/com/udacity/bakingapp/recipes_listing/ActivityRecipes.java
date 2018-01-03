package com.udacity.bakingapp.recipes_listing;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.steps.ActivityRecipeDetails;

public class ActivityRecipes extends BaseActivity implements FragmentRecipesListing.RecipeListingInteraction {
    private Toolbar mToolbarRecipesListing;
    private FragmentRecipesListing mFragmentListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_listing);
        initializeViews();
        if (savedInstanceState != null) {
            mFragmentListing = (FragmentRecipesListing) getSupportFragmentManager().getFragment(savedInstanceState, "FragmentListing");
            if (mFragmentListing != null)
                replaceFragment(mFragmentListing, R.id.fragmentContainerRecipeListing, false);
        } else
            loadFragments();
        setToolbar(mToolbarRecipesListing, getString(R.string.app_name), false);

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mFragmentListing != null)
            getSupportFragmentManager().putFragment(outState, "FragmentListing", mFragmentListing);

    }


    @Override
    public void onRecipeClicked(Recipe recipe) {
        ActivityRecipeDetails.startActivity(this, recipe);
    }
}
