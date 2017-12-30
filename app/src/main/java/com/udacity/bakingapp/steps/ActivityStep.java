package com.udacity.bakingapp.steps;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.common.base.BaseActivity;
import com.udacity.bakingapp.common.helpers.Constants;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.common.models.Step;

public class ActivityStep extends BaseActivity {
    private Toolbar mToolbarStep;
    private Step mStep;
    private Recipe mRecipe;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private TextView txtShortDescription, txtDescription;
    private ImageView imgStep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        if (getIntent() != null) {
            mStep = getIntent().getParcelableExtra(Constants.SELECTED_STEP);
            mRecipe = getIntent().getParcelableExtra(Constants.SELECTED_RECIPE);
        }
        initializeViews();
        setListeners();
        loadFragments();
        if (mRecipe != null)
            setToolbar(mToolbarStep, mRecipe.getName(), true);
        loadDataToViews();
    }

    @Override
    protected void initializeViews() {
        mToolbarStep = findViewById(R.id.toolbarStep);
        txtShortDescription = findViewById(R.id.txtStepShortDesc);
        txtDescription = findViewById(R.id.txtStepDescription);
        imgStep = findViewById(R.id.imgStep);
        mExoPlayerView = findViewById(R.id.simpleExoPlayer);
    }

    @Override
    protected void setListeners() {

    }

    @Override
    protected void loadFragments() {
    }

    private void loadDataToViews() {
        if (mStep != null) {
            txtShortDescription.setText(mStep.getShortDescription());
            txtDescription.setText(mStep.getDescription());
            if (!TextUtils.isEmpty(mStep.getVideoURL())) {
                imgStep.setVisibility(View.GONE);
                mExoPlayerView.setVisibility(View.VISIBLE);
                mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.arow_green));
                initializeExoPlayer();
            } else if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
                Picasso.with(this).load(mStep.getThumbnailURL()).into(imgStep);
                imgStep.setVisibility(View.VISIBLE);
                mExoPlayerView.setVisibility(View.GONE);
            }
        }
    }

    private void initializeExoPlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            String userAgent = Util.getUserAgent(this, "bakingapp");
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()), new DefaultDataSourceFactory(this, userAgent
            ), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseExoPlayer();
    }

    public static void startActivity(Context context, Recipe recipe, Step step) {
        Intent intent = new Intent(context, ActivityStep.class);
        intent.putExtra(Constants.SELECTED_RECIPE, recipe);
        intent.putExtra(Constants.SELECTED_STEP, step);
        context.startActivity(intent);
    }
}
