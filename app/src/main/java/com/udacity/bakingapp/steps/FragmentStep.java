package com.udacity.bakingapp.steps;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.udacity.bakingapp.common.base.BaseFragment;
import com.udacity.bakingapp.common.helpers.AppPreferences;
import com.udacity.bakingapp.common.helpers.Constants;
import com.udacity.bakingapp.common.models.Recipe;
import com.udacity.bakingapp.common.models.Step;

/**
 * Created by eslam on 1/2/18.
 */

public class FragmentStep extends BaseFragment implements ExoPlayer.EventListener {
    private Context mContext;
    private Step mStep;
    private Recipe mRecipe;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mExoPlayerView;
    private TextView txtShortDescription, txtDescription;
    private ImageView imgStep;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mPlaybackBuilder;
    private boolean isLandscape = false;
    private View view;
    private long position = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (savedInstanceState != null)
            position = savedInstanceState.getLong(Constants.VIDEO_CURRENT_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = true;
            view = inflater.inflate(R.layout.fullscreen_video, null);
        } else {
            view = inflater.inflate(R.layout.fragment_recipe_step, null);
            isLandscape = false;
        }

        initializeViews(view);
        getDataFromArguments();
        loadDataToViews();
        setListeners();
        return view;
    }

    private void getDataFromArguments() {
        if (getArguments() != null) {
            mRecipe = getArguments().getParcelable(AppPreferences.SELECTED_RECIPE);
            mStep = getArguments().getParcelable(AppPreferences.SELECTED_STEP);
        }
    }

    @Override
    protected void initializeViews(View v) {
        if (!isLandscape) {
            txtShortDescription = v.findViewById(R.id.txtStepShortDesc);
            txtDescription = v.findViewById(R.id.txtStepDescription);
        }
        imgStep = v.findViewById(R.id.imgStep);
        mExoPlayerView = v.findViewById(R.id.simpleExoPlayer);
    }

    @Override
    protected void setListeners() {
        mExoPlayer.addListener(this);
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
                Picasso.with(mContext).load(mStep.getThumbnailURL()).into(imgStep);
                imgStep.setVisibility(View.VISIBLE);
                mExoPlayerView.setVisibility(View.GONE);
            }
        }
    }

    private void initializeExoPlayer() {
        mMediaSession = new MediaSessionCompat(mContext, "MEDIA_SESSION");
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
            String userAgent = Util.getUserAgent(mContext, "bakingapp");
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mStep.getVideoURL()), new DefaultDataSourceFactory(mContext, userAgent
            ), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            if (position != 0)
                mExoPlayer.seekTo(position);
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
    public void onStop() {
        super.onStop();
        releaseExoPlayer();
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null)
            outState.putLong(Constants.VIDEO_CURRENT_POSITION, mExoPlayer.getContentPosition());

    }

    public static FragmentStep newInstance(Recipe recipe, Step step) {
        FragmentStep fragmentStep = new FragmentStep();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppPreferences.SELECTED_RECIPE, recipe);
        bundle.putParcelable(AppPreferences.SELECTED_STEP, step);
        fragmentStep.setArguments(bundle);
        return fragmentStep;
    }
}
