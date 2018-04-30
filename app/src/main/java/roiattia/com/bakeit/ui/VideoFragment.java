package roiattia.com.bakeit.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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

import roiattia.com.bakeit.R;

public class VideoFragment extends Fragment
    implements ExoPlayer.EventListener{

    private static final String MULTIMEDIA_URL = "MULTIMEDIA_URL";
    private static final String EXOPLAYER_POSITION = "EXOPLAYER_POSITION";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private ImageView mStepImage;

    private String mMultimediaUrl;
    private boolean mTwoPane;
    private boolean mIsVideo;
    private long mPlayerPosition = 0;

    public VideoFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        mPlayerView = rootView.findViewById(R.id.playerView);
        mStepImage = rootView.findViewById(R.id.iv_step);

        // check if need to restore data in case of rotation
        if(savedInstanceState != null) {
            mMultimediaUrl = savedInstanceState.getString(MULTIMEDIA_URL);
            mPlayerPosition = savedInstanceState.getLong(EXOPLAYER_POSITION);
            mIsVideo = savedInstanceState.getBoolean(RecipeActivity.IS_VIDEO);
        }

        if(mIsVideo){
            loadVideo();
        } else {
            loadImage();
        }

        return rootView;
    }

    private void loadImage() {
        mPlayerView.setVisibility(View.GONE);
        Glide.with(getContext()).load(mMultimediaUrl).into(mStepImage);
    }

    private void loadVideo() {
        mStepImage.setVisibility(View.GONE);

        mTwoPane = getContext().getResources().getBoolean(R.bool.is_tablet);
        // Smartphone mode
        if(!mTwoPane){
            // check if in landscape mode to enable full screen video
            if(getContext().getResources().getBoolean(R.bool.is_landscape)){
                hideSystemUI();
            }
            initializePlayer(Uri.parse(mMultimediaUrl));

            // Tablet mode
        } else {
            if(null != mMultimediaUrl){
                initializePlayer(Uri.parse(mMultimediaUrl));
            }
        }
    }

    public void setMultimediaUrl(String multimediaUrl){
        mMultimediaUrl = multimediaUrl;
        // if on Tablet then initialize the player
        if(mTwoPane && mIsVideo) {
            initializePlayer(Uri.parse(mMultimediaUrl));
        }
    }

    public void setIsVideo(boolean isVideo){
        mIsVideo = isVideo;
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakeIt");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
            // set the player to the last position in case of rotation
            mExoPlayer.seekTo(mPlayerPosition);
            // if in case of rotation then continue play
            if(mPlayerPosition > 0){
                mExoPlayer.setPlayWhenReady(true);
            } else {
                mExoPlayer.setPlayWhenReady(false);
            }
        }
    }

    /**
     * Release ExoPlayer.
     */
    public void releasePlayer() {
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    /**
     * Release the player when the activity is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mExoPlayer != null) {
            outState.putLong(EXOPLAYER_POSITION, mExoPlayer.getCurrentPosition());
        }
        outState.putString(MULTIMEDIA_URL, mMultimediaUrl);
        outState.putBoolean(RecipeActivity.IS_VIDEO, mIsVideo);
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

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }
}
