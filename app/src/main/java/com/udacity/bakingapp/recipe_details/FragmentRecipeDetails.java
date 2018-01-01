package com.udacity.bakingapp.recipe_details;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseFragment;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.helpers.Constants;
import com.udacity.bakingapp.common.helpers.MyApplication;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.common.models.Step;
import com.udacity.bakingapp.common.widget.BakingWidget;
import com.udacity.bakingapp.steps.ActivityStep;

/**
 * Created by eslam on 12/23/17.
 */

public class FragmentRecipeDetails extends BaseFragment implements AdapterSteps.ViewStepInteraction {
    private Context mContext;
    private Recipe mRecipe;
    private RecyclerView rvIngredient, rvSteps;
    private AdapterIngredient mAdapterIngredient;
    private AdapterSteps mAdapterSteps;
    private LinearLayoutManager mLayoutManagerIngredients, mLayoutManagerSteps;
    private ImageView imgDesired;
    private boolean isDesired = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (savedInstanceState == null) {
            if (getArguments() != null)
                mRecipe = getArguments().getParcelable(Constants.SELECTED_RECIPE);
        } else
            savedInstanceState.getParcelable(Constants.SELECTED_RECIPE);
        
        Recipe tempRecipe = MyApplication.getmGson().fromJson(AppPreferences.getString(AppPreferences.SELECTED_RECIPE, mContext, ""), Recipe.class);

        if (mRecipe != null && tempRecipe != null) {
            if (mRecipe.getId() == tempRecipe.getId())
                isDesired = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, null);
        initializeViews(view);
        setListeners();
        rvIngredient.setNestedScrollingEnabled(false);
        rvSteps.setNestedScrollingEnabled(false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeVariables();
        rvIngredient.setLayoutManager(mLayoutManagerIngredients);
        rvSteps.setLayoutManager(mLayoutManagerSteps);
        rvIngredient.setAdapter(mAdapterIngredient);
        rvSteps.setAdapter(mAdapterSteps);
    }

    private void initializeVariables() {
        mLayoutManagerIngredients = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mLayoutManagerSteps = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mAdapterIngredient = new AdapterIngredient(mContext, mRecipe.getIngredients());
        mAdapterSteps = new AdapterSteps(mContext, mRecipe.getSteps(), this);
    }

    @Override
    protected void initializeViews(View v) {
        rvIngredient = v.findViewById(R.id.rvIngredients);
        rvSteps = v.findViewById(R.id.rvSteps);
        imgDesired = v.findViewById(R.id.imgDesired);
        if (isDesired)
            imgDesired.setImageResource(R.drawable.ic_desired);
    }

    @Override
    protected void setListeners() {
        imgDesired.setOnClickListener(imgDesiredClickListener);
    }

    private View.OnClickListener imgDesiredClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isDesired) {
                AppPreferences.setString(AppPreferences.SELECTED_RECIPE, "", mContext);
                imgDesired.setBackgroundResource(R.drawable.ic_undesired);
            } else {
                AppPreferences.setString(AppPreferences.SELECTED_RECIPE, MyApplication.getmGson().toJson(mRecipe), mContext);
                imgDesired.setBackgroundResource(R.drawable.ic_desired);
            }
            isDesired = !isDesired;
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, BakingWidget.class));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gridView);
        }
    };

    public static FragmentRecipeDetails newInstance(Recipe recipe) {
        FragmentRecipeDetails fragment = new FragmentRecipeDetails();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.SELECTED_RECIPE, recipe);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStepClicked(Step step) {
        ActivityStep.startActivity(mContext, mRecipe, step);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.SELECTED_RECIPE, mRecipe);
    }
}
