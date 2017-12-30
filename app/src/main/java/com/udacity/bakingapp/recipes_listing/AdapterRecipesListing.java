package com.udacity.bakingapp.recipes_listing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.models.Recipe;

import java.util.ArrayList;

/**
 * Created by eslam on 12/23/17.
 */

public class AdapterRecipesListing extends RecyclerView.Adapter<AdapterRecipesListing.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Recipe> mRecipeList;
    private OnRecipeClicked mOnRecipeClicked;

    public AdapterRecipesListing(Context context, ArrayList<Recipe> recipeList, OnRecipeClicked onRecipeClicked) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mRecipeList = recipeList;
        this.mOnRecipeClicked = onRecipeClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_item_recipe, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        if (recipe != null) {
            holder.txtRecipeName.setText(recipe.getName());
            if (!TextUtils.isEmpty(recipe.getImage()))
                Picasso.with(mContext).load(recipe.getImage()).into(holder.imgRecipe);

            holder.setListener(recipe);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgRecipe;
        private TextView txtRecipeName;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
            txtRecipeName = itemView.findViewById(R.id.txtRecipeName);
        }

        void setListener(final Recipe recipe) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnRecipeClicked != null)
                        mOnRecipeClicked.onRecipeClicked(recipe);
                }
            });
        }
    }

    interface OnRecipeClicked {
        void onRecipeClicked(Recipe recipe);
    }
}
