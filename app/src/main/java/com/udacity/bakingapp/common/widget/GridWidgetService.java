package com.udacity.bakingapp.common.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by eslam on 1/1/18.
 */
public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

}
