package com.udacity.bakingapp.common.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.helpers.MyApplication;
import com.udacity.bakingapp.common.models.Recipe;


public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Recipe mRecipe;

    public GridRemoteViewsFactory(Context context) {
        this.mContext = context;
        //mRecipe = MyApplication.getmGson().fromJson(AppPreferences.getString(AppPreferences.SELECTED_RECIPE, mContext, ""), Recipe.class);

    }

    @Override
    public void onCreate() {
        mRecipe = MyApplication.getmGson().fromJson(AppPreferences.getString(AppPreferences.SELECTED_RECIPE, mContext, ""), Recipe.class);
    }

    @Override
    public void onDataSetChanged() {
        mRecipe = MyApplication.getmGson().fromJson(AppPreferences.getString(AppPreferences.SELECTED_RECIPE, mContext, ""), Recipe.class);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe == null || mRecipe.getIngredients() == null)
            return 0;
        return mRecipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (mRecipe != null && mRecipe.getIngredients() != null)
            rv.setTextViewText(R.id.txtItemWidget, mRecipe.getIngredients().get(position).getIngredient());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
