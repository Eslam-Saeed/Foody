package com.udacity.bakingapp.common.base;

import android.support.v4.app.Fragment;
import android.view.View;

public abstract class BaseFragment extends Fragment {
    protected abstract void initializeViews(View v);

    protected abstract void setListeners();


}
