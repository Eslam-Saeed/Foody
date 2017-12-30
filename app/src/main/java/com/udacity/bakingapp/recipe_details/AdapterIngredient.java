package com.udacity.bakingapp.recipe_details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.models.Ingredient;

import java.util.ArrayList;

/**
 * Created by eslam on 12/29/17.
 */

public class AdapterIngredient extends RecyclerView.Adapter<AdapterIngredient.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Ingredient> mListIngredient;

    public AdapterIngredient(Context context, ArrayList<Ingredient> listIngredient) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mListIngredient = listIngredient;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_ingredient, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ingredient ingredient = mListIngredient.get(position);
        if (ingredient != null) {
            holder.txtIngredient.setText(ingredient.getIngredient());
            holder.txQuantityMeasure.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());

            if (position == mListIngredient.size() - 1)
                holder.viewSeparator.setVisibility(View.GONE);
            else
                holder.viewSeparator.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return mListIngredient.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtIngredient, txQuantityMeasure;
        private View viewSeparator;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtIngredient = itemView.findViewById(R.id.txtIngredientTitle);
            txQuantityMeasure = itemView.findViewById(R.id.txtQuantityMeasure);
            viewSeparator = itemView.findViewById(R.id.viewSeparator);
        }
    }
}
