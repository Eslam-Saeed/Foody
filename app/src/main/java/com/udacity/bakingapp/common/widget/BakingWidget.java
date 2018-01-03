package com.udacity.bakingapp.common.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.recipes_listing.ActivityRecipes;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        //Log.w("WidgetExample", String.valueOf(context.getPackageName()));
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        views.setRemoteAdapter(R.id.gridView, intent);
        views.setEmptyView(R.id.gridView, R.id.emptyView);

        appWidgetManager.updateAppWidget(appWidgetId, views);
        
        Intent intent1 = new Intent(context, ActivityRecipes.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.gridView, pendingIntent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent != null && intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context.getApplicationContext(), BakingWidget.class));
            //int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            onUpdate(context.getApplicationContext(), AppWidgetManager.getInstance(context), ids);
            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.gridView);
        }
    }
}

