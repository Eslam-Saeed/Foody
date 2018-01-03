package com.udacity.bakingapp.common.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.models.Recipe;


public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(getApplicationContext());
    }
}


class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Recipe mRecipe;

    GridRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mRecipe = AppPreferences.getDesiredRecipe(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipe == null || mRecipe.getIngredients() == null)
            return 0;
        else
            return mRecipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        if (mRecipe != null && mRecipe.getIngredients() != null && mRecipe.getIngredients().get(position) != null)
            remoteViews.setTextViewText(R.id.txtIngredient, mRecipe.getIngredients().get(position).getIngredient());

        return remoteViews;
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

