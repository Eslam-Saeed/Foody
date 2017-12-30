package com.udacity.bakingapp.common.base;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.udacity.bakingapp.R;


public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar myToolbar;

    protected void setToolbar(Toolbar toolbar, String title, boolean showUpButton) {
        myToolbar = toolbar;
        myToolbar.setTitle(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toolbar.setElevation(getResources().getDimension(R.dimen.padding_small));

        setSupportActionBar(myToolbar);

        if (showUpButton) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }


    protected void setToolbarNavigationIcon(Toolbar toolbar, int iconId) {
        toolbar.setNavigationIcon(iconId);
    }

    protected void replaceFragment(BaseFragment fragment, int containerId, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack)
            fragmentTransaction.replace(containerId, fragment).addToBackStack("").commit();
        else

            fragmentTransaction.replace(containerId, fragment).commit();
    }

    protected abstract void initializeViews();

    protected abstract void setListeners();

    protected abstract void loadFragments();

}
