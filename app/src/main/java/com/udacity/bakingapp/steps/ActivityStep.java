package com.udacity.bakingapp.steps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.helpers.Constants;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.common.models.Step;
import com.udacity.bakingapp.recipe_details.FragmentRecipeDetails;

public class ActivityStep extends BaseActivity {
    private Toolbar mToolbarStep;
    private Step mStep;
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        if (getIntent() != null) {
            mStep = getIntent().getParcelableExtra(Constants.SELECTED_STEP);
            mRecipe = getIntent().getParcelableExtra(Constants.SELECTED_RECIPE);
        }
        initializeViews();
        setListeners();
        loadFragments();
        if (mRecipe != null)
            setToolbar(mToolbarStep, mRecipe.getName(), true);
    }

    @Override
    protected void initializeViews() {
        mToolbarStep = findViewById(R.id.toolbarStep);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void loadFragments() {
        //replaceFragment(FragmentRecipeDetails.newInstance(mRecipe), R.id.fragmentContainerStep, true);
    }

    public static void startActivity(Context context, Recipe recipe, Step step) {
        Intent intent = new Intent(context, ActivityStep.class);
        intent.putExtra(Constants.SELECTED_RECIPE, recipe);
        intent.putExtra(Constants.SELECTED_STEP, step);
        context.startActivity(intent);
    }
}
