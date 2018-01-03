package com.udacity.bakingapp.recipes_listing;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseFragment;
import com.udacity.bakingapp.common.helpers.Utilities;
import com.udacity.bakingapp.common.models.Recipe;

/**
 * Created by eslam on 12/23/17.
 */

public class FragmentRecipesListing extends BaseFragment implements ViewRecipesListing, AdapterRecipesListing.OnRecipeClicked {
    private Context mContext;
    private View view;
    private RecyclerView rvRecipesListing;
    private ProgressBar mProgressBarRecipeListing;
    private PresenterRecipesListing mPresenterRecipesListing;
    private AdapterRecipesListing mAdapterRecipesListing;
    private GridLayoutManager mGridLayoutManager;
    private RecipeListingInteraction mRecipeListingInteraction;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRecipeListingInteraction = (RecipeListingInteraction) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipes_listing, null);
        initializeViews(view);
        setListeners();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        mPresenterRecipesListing = new PresenterRecipesListing(mContext, this);
        mAdapterRecipesListing = new AdapterRecipesListing(mContext, mPresenterRecipesListing.getRecipeList(), this);
        mGridLayoutManager = new GridLayoutManager(mContext, Utilities.calculateNoOfColumns(mContext, getResources().getInteger(R.integer.recipe_image_width)));
        rvRecipesListing.setLayoutManager(mGridLayoutManager);
        rvRecipesListing.setAdapter(mAdapterRecipesListing);
        mPresenterRecipesListing.serviceGetRecipesList();
    }

    @Override
    protected void initializeViews(View v) {
        mProgressBarRecipeListing = v.findViewById(R.id.progressbarRecipesListing);
        rvRecipesListing = v.findViewById(R.id.rvRecipesListing);
    }

    @Override
    protected void setListeners() {

    }

    public static FragmentRecipesListing newInstance() {
        FragmentRecipesListing fragmentRecipesListing = new FragmentRecipesListing();
        return fragmentRecipesListing;
    }

    @Override
    public void showProgress(boolean shouldShow) {
        mProgressBarRecipeListing.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onListingRecipesFail(String message) {
        Utilities.showSnackBar(view, message);
    }

    @Override
    public void onListingRecipesSuccess() {
        if (mAdapterRecipesListing != null)
            mAdapterRecipesListing.notifyDataSetChanged();
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        mRecipeListingInteraction.onRecipeClicked(recipe);
    }

    interface RecipeListingInteraction {
        void onRecipeClicked(Recipe recipe);
    }
}
