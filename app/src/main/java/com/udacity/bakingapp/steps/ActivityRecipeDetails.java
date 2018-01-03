package com.udacity.bakingapp.steps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.helpers.Utilities;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.common.models.Step;
import com.udacity.bakingapp.recipe_details.FragmentRecipeDetails;

public class ActivityRecipeDetails extends BaseActivity implements FragmentRecipeDetails.FragmentDetailsInteraction {
    private Toolbar mToolbarStep;
    private Recipe mRecipe;
    private boolean isLandscape = false;
    private FragmentRecipeDetails fragmentDetails;
    private FragmentStep mFragmentStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT && !Utilities.isTablet(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
            setContentView(R.layout.activity_step_without_toolbar);
            isLandscape = true;
        } else
            setContentView(R.layout.activity_recipe_details);
        if (getIntent() != null) {
            mRecipe = getIntent().getParcelableExtra(AppPreferences.SELECTED_RECIPE);
        }
        initializeViews();
        setListeners();
        if (savedInstanceState != null) {
            //fragmentDetails = (FragmentRecipeDetails) getSupportFragmentManager().getFragment(savedInstanceState, "FragmentDetails");
            mFragmentStep = (FragmentStep) getSupportFragmentManager().getFragment(savedInstanceState, "FragmentStep");
        }
        loadFragments();
        if (mRecipe != null && !isLandscape)
            setToolbar(mToolbarStep, mRecipe.getName(), true);

    }

    @Override
    protected void initializeViews() {
        if (!isLandscape)
            mToolbarStep = findViewById(R.id.toolbarStep);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void loadFragments() {
        if (fragmentDetails == null)
            fragmentDetails = FragmentRecipeDetails.newInstance(mRecipe);
        if (mFragmentStep == null)
            replaceFragment(fragmentDetails, R.id.fragmentContainerDetails, false);
        else
            replaceFragment(mFragmentStep, Utilities.isTablet(this) ? R.id.fragmentContainerStep : R.id.fragmentContainerDetails, true);


    }

    public static void startActivity(Context context, Recipe recipe) {
        Intent intent = new Intent(context, ActivityRecipeDetails.class);
        intent.putExtra(AppPreferences.SELECTED_RECIPE, recipe);
        context.startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //getSupportFragmentManager().putFragment(outState, "FragmentDetails", fragmentDetails);
        if (mFragmentStep != null)
            getSupportFragmentManager().putFragment(outState, "FragmentStep", mFragmentStep);
    }


    @Override
    public void showStepDetails(Recipe recipe, Step step) {
        mFragmentStep = FragmentStep.newInstance(recipe, step);
        replaceFragment(mFragmentStep, Utilities.isTablet(this) ? R.id.fragmentContainerStep : R.id.fragmentContainerDetails, true);
    }
}
