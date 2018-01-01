package com.udacity.bakingapp.recipes_listing;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.helpers.Utilities;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.recipe_details.FragmentRecipeDetails;

public class ActivityRecipes extends BaseActivity implements FragmentRecipesListing.RecipeListingInteraction {
    private Toolbar mToolbarRecipesListing;
    private FragmentRecipeDetails fragmentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_listing);
        initializeViews();
        if (savedInstanceState != null) {
            fragmentDetails = (FragmentRecipeDetails) getSupportFragmentManager().getFragment(savedInstanceState, "FragmentDetails");
            if (fragmentDetails != null)
                replaceFragment(fragmentDetails,
                        Utilities.isTablet(this) ? R.id.fragmentContainerDetails :
                                R.id.fragmentContainerRecipeListing, true);
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
    public void onRecipeClicked(Recipe recipe) {
        if (fragmentDetails == null)
            fragmentDetails = FragmentRecipeDetails.newInstance(recipe);
        replaceFragment(fragmentDetails,
                Utilities.isTablet(this) ? R.id.fragmentContainerDetails :
                        R.id.fragmentContainerRecipeListing, true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, "FragmentDetails", fragmentDetails);

    }
}
