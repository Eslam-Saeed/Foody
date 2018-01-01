package com.udacity.bakingapp.steps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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

public class ActivityStep extends BaseActivity implements ExoPlayer.EventListener {
    private Toolbar mToolbarStep;
    private Step mStep;
    private Recipe mRecipe;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private TextView txtShortDescription, txtDescription;
    private ImageView imgStep;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mPlaybackBuilder;
    private boolean isLandscape = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar
            setContentView(R.layout.fullscreen_video);
            isLandscape = true;
        } else
            setContentView(R.layout.activity_step);
        if (getIntent() != null) {
            mStep = getIntent().getParcelableExtra(Constants.SELECTED_STEP);
            mRecipe = getIntent().getParcelableExtra(Constants.SELECTED_RECIPE);
        }
        initializeViews();
        loadDataToViews();
        setListeners();
        loadFragments();
        if (mRecipe != null && !isLandscape)
            setToolbar(mToolbarStep, mRecipe.getName(), true);


    }

    @Override
    protected void initializeViews() {
        if (!isLandscape) {
            mToolbarStep = findViewById(R.id.toolbarStep);
            txtShortDescription = findViewById(R.id.txtStepShortDesc);
            txtDescription = findViewById(R.id.txtStepDescription);
        }
        imgStep = findViewById(R.id.imgStep);
        mExoPlayerView = findViewById(R.id.simpleExoPlayer);
    }

    @Override
    protected void setListeners() {
        mExoPlayer.addListener(this);
    }

    @Override
    protected void loadFragments() {
    }

    private void loadDataToViews() {
        initializeExoPlayer();
        if (mStep != null) {
            if (!isLandscape) {
                txtShortDescription.setText(mStep.getShortDescription());
                txtDescription.setText(mStep.getDescription());
            }
            if (!TextUtils.isEmpty(mStep.getVideoURL())) {
                imgStep.setVisibility(View.GONE);
                mExoPlayerView.setVisibility(View.VISIBLE);
                mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_play));
            } else if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
                Picasso.with(this).load(mStep.getThumbnailURL()).into(imgStep);
                imgStep.setVisibility(View.VISIBLE);
                mExoPlayerView.setVisibility(View.GONE);
            }
        }
    }

    private void initializeExoPlayer() {
        mMediaSession = new MediaSessionCompat(this, "MEDIA_SESSION");
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mPlaybackBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mMediaSession.setPlaybackState(mPlaybackBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
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
        mMediaSession.setActive(true);
    }

    void releaseExoPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mMediaSession.setActive(false);
            mExoPlayer.removeListener(this);
            mExoPlayer.setVideoListener(null);
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

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mPlaybackBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getContentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mPlaybackBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getContentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mPlaybackBuilder.build());
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }
    }
}
