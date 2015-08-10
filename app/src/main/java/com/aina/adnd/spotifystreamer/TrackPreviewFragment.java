package com.aina.adnd.spotifystreamer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.aina.adnd.spotifystreamer.MediaPlayerService.MediaPlayerServiceBinder;

/**
 * Fragment containing Media Player
 */
public class TrackPreviewFragment extends DialogFragment {

    public final static String SAVED_TRACK_INFO = "SAVED_TRACK_INFO";
    public final static String TRACK_INDEX = "TRACK_INDEX";
    public final static String ARTIST_NAME = "ARTIST_NAME";
    private final static Integer REFRESH_RATE = 500;
    private static final String CURRENT_POSITION = "CURRENT_POSITION";
    private final String LOG_TAG = TrackPreviewFragment.class.getSimpleName();
    private final String IDLE = "Idle";
    private final String PREPARING = "Preparing";
    private final String READY = "Ready";
    private final String PLAYING = "Playing";
    private final String PAUSED = "Paused";

    TextView artistNameView;
    TextView albumNameView;
    TextView trackNameView;
    ImageView trackAlbumArtView;

    TextView trackStartView;
    TextView trackEndView;

    SeekBar trackSeekBar = null;
    ImageButton mButtonPause;
    MediaPlayerService mService;
    Handler progressHandler = new Handler();

    private Integer mTrackIndex;
    private String mArtistName;
    private String mPreviewUrl;
    private ArrayList<TrackInfo> mTrackInfo = new ArrayList<TrackInfo>();
    private boolean mIsBound;
    private int mCurrentPosition;

    //TODO Get a flag to signify end of a track, them use to toggle play/pause image.

    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {

            Integer currentPosition = 0;

            Integer duration = mService.getDuration();
            trackSeekBar.setMax(duration);
            trackEndView.setText(formatDuration(duration));

            currentPosition = mService.getCurrentPosition();
            trackSeekBar.setProgress(currentPosition/* + REFRESH_RATE*/);
            trackStartView.setText(formatDuration(currentPosition + REFRESH_RATE));

            mCurrentPosition = currentPosition;

//            Log.d(LOG_TAG,
//                    String.valueOf(mService.isPlaying())
//                            + "; " + String.valueOf(mService.getCurrentPosition() + REFRESH_RATE)
//                            + "; " + String.valueOf(mService.getDuration()));


            if (((mService.getCurrentPosition() + REFRESH_RATE) > mService.getDuration()) &&
                    (mService.getDuration() > 0)) {

                mService.setPlayerState(READY);
                progressHandler.removeCallbacks(updateProgress);
                mButtonPause.setImageResource(android.R.drawable.ic_media_play);

            } else
                progressHandler.postDelayed(updateProgress, REFRESH_RATE);
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            MediaPlayerServiceBinder binder = (MediaPlayerServiceBinder) service;

            mService = binder.getService();

            if (mService != null) {
                setTrackInfo();
                playPreview(mPreviewUrl);
                mIsBound = true;
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    public TrackPreviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(CURRENT_POSITION);
        }

        Bundle args = getArguments();

        if (args != null) {
            mTrackInfo = args.getParcelableArrayList(SAVED_TRACK_INFO);
            mArtistName = args.getString(ARTIST_NAME);
            mTrackIndex = args.getInt(TRACK_INDEX, 0);
        }

        View rootView = inflater.inflate(R.layout.fragment_track_preview, container, false);

        mButtonPause = (ImageButton) rootView.findViewById(R.id.trackplayer_pause);
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setTrackInfo();

                if (mService.getPlayerState().equals(PAUSED)) {
                    Log.d(LOG_TAG, "Pressed Pause");
                    mButtonPause.setImageResource(android.R.drawable.ic_media_pause);
                    progressHandler.postDelayed(updateProgress, REFRESH_RATE);
                    mService.start();

                } else if (mService.getPlayerState().equals(PLAYING)) {
                    Log.d(LOG_TAG, "Pressed Play");
                    mService.pause();
                    mButtonPause.setImageResource(android.R.drawable.ic_media_play);
                    progressHandler.removeCallbacks(updateProgress);

                } else if (mService.getPlayerState().equals(READY)) {
                    Log.d(LOG_TAG, "Fresh Play");
                    playPreview(mPreviewUrl);
                }
            }
        });

        ImageButton buttonPrev = (ImageButton) rootView.findViewById(R.id.trackplayer_prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mTrackIndex > 0)
                    mTrackIndex--;
                else
                    mTrackIndex = mTrackInfo.size() - 1;
                setTrackInfo();
                playPreview(mPreviewUrl);
            }
        });

        ImageButton buttonNext = (ImageButton) rootView.findViewById(R.id.trackplayer_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mTrackIndex < mTrackInfo.size() - 1)
                    mTrackIndex++;
                else
                    mTrackIndex = 0;

                setTrackInfo();
                playPreview(mPreviewUrl);

            }
        });

        artistNameView = (TextView) rootView.findViewById(R.id.textview_artist_name);
        albumNameView = (TextView) rootView.findViewById(R.id.textview_album_name);
        trackNameView = (TextView) rootView.findViewById(R.id.textview_track_name);
        trackAlbumArtView = (ImageView) rootView.findViewById(R.id.imageview_album_art);

        trackSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar_track_player);
        trackSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                progress = progressValue;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                mService.seekTo(progress);

                if (!PLAYING.equals(mService.getPlayerState())) {
                    progressHandler.postDelayed(updateProgress, REFRESH_RATE);
                    mService.start();
                }

//                Toast.makeText(getActivity(), "seek bar progress:" + progress,
//                        Toast.LENGTH_SHORT).show();
            }
        });
        trackStartView = (TextView) rootView.findViewById(R.id.textview_track_start);
        trackEndView = (TextView) rootView.findViewById(R.id.textview_track_end);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "onStart - ");

        BindToService();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "onResume - ");

        if (mCurrentPosition > 0) {
            Log.d(LOG_TAG, "onResume mCurrentPosition - " + mCurrentPosition);
            progressHandler.postDelayed(updateProgress, REFRESH_RATE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(LOG_TAG, "onStop - ");

        progressHandler.removeCallbacks(updateProgress);

        //UnBindService();
    }

//    @Override
//    public void onDestroyView() {
//
//        if (getActivity().findViewById(R.id.top_ten_tracks_container) != null) {
//            if (getDialog() != null && getRetainInstance())
//                getDialog().setOnDismissListener(null);
//        }
//        super.onDestroyView();
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(CURRENT_POSITION, mCurrentPosition);
    }


    private void setTrackInfo() {

        mPreviewUrl = mTrackInfo.get(mTrackIndex).getPreviewUrl();

        String mAlbumName = mTrackInfo.get(mTrackIndex).getAlbumName();
        String mTrackName = mTrackInfo.get(mTrackIndex).getTrackName();
        String mAlbumArtUrl = mTrackInfo.get(mTrackIndex).getAlbumArtUrl_Large();

        artistNameView.setText(mArtistName);
        albumNameView.setText(mAlbumName);
        trackNameView.setText(mTrackName);

        Picasso.with(getActivity())
                .load(mAlbumArtUrl)
//                .placeholder(R.drawable.no_image)
//                .resize(300, 300)
//                .centerCrop()
                .into(trackAlbumArtView);
    }

    private void playPreview(String url) {

        mButtonPause.setImageResource(android.R.drawable.ic_media_pause);
        mService.setTrackUrl(url);
        mService.reset();
        mService.play();

        progressHandler.postDelayed(updateProgress, REFRESH_RATE);
    }

    private String formatDuration(Integer duration) {
        return String.format("%01d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }


    void BindToService() {

        Intent intent = new Intent(getActivity()
                .getApplicationContext(), MediaPlayerService.class);

        getActivity()
                .getApplicationContext()
                .bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mIsBound = true;

        //Toast.makeText(getActivity(), "BindToService - Bound", Toast.LENGTH_SHORT).show();

    }

    void UnBindService() {
        if (mIsBound) {
            getActivity().getApplicationContext().unbindService(mConnection);
            mIsBound = false;
        }
    }

}
