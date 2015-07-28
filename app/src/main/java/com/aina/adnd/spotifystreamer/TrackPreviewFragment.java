package com.aina.adnd.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.aina.adnd.spotifystreamer.MediaPlayerService.MediaPlayerServiceBinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPreviewFragment extends Fragment {

    private final static Integer MILLI_SECONDS = 1000;
    private final String LOG_TAG = TrackPreviewFragment.class.getSimpleName();
    private final String IDLE = "IDLE";
    private final String PREPARING = "PREPARING";
    private final String READY = "READY";
    private final String PLAYING = "PLAYING";
    private final String PAUSED = "PAUSED";


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
    private Runnable updateProgress = new Runnable() {
        @Override
        public void run() {

            Integer currentPosition = 0;

            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            currentPosition = mService.getCurrentPosition();
            Integer duration = mService.getDuration();

            trackSeekBar.setMax(duration);
            trackSeekBar.setProgress(currentPosition + 1000);
            trackEndView.setText(formatDuration(duration));
            trackStartView.setText(formatDuration(currentPosition + 1000));

            progressHandler.postDelayed(updateProgress, 0);

        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {

            MediaPlayerServiceBinder binder = (MediaPlayerServiceBinder) service;

            Log.d(LOG_TAG, "----- onServiceConnected before getting service-- ");

            mService = binder.getService();

            Log.d(LOG_TAG, "----- onServiceConnected after getting service-- ");

            if (mService != null) {

                mIsBound = true;
            } else Log.d(LOG_TAG, "-----mService is NOTHING -- ");
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    public TrackPreviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        mTrackInfo = intent.getParcelableArrayListExtra(TopTenTracksFragment.SAVED_TRACK_INFO);
        mArtistName = intent.getStringExtra(TopTenTracksFragment.ARTIST_NAME);
        mTrackIndex = intent.getIntExtra(TopTenTracksFragment.TRACK_INDEX, 0);

        View rootView = inflater.inflate(R.layout.fragment_track_preview, container, false);

        mButtonPause = (ImageButton) rootView.findViewById(R.id.trackplayer_pause);
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setTrackInfo();

                if (PAUSED.equals(mService.getPlayerState())) {

                    setPauseButtonImage();
                    progressHandler.postDelayed(updateProgress, 0);
                    mService.start();
                } else if (PLAYING.equals(mService.getPlayerState())) {

                    setPauseButtonImage();

                    mService.pause();
                    progressHandler.removeCallbacks(updateProgress);

                } else {
                    playPreview(mPreviewUrl);
                }
            }
        });

        ImageButton buttonPrev = (ImageButton) rootView.findViewById(R.id.trackplayer_prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mTrackIndex > 0) mTrackIndex--;

                setTrackInfo();

                playPreview(mPreviewUrl);
            }
        });

        ImageButton buttonNext = (ImageButton) rootView.findViewById(R.id.trackplayer_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mTrackIndex < mTrackInfo.size() - 1) mTrackIndex++;

                setTrackInfo();

                playPreview(mPreviewUrl);

            }
        });

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

                Toast.makeText(getActivity(), "seek bar progress:" + progress,
                        Toast.LENGTH_SHORT).show();
            }
        });

        artistNameView = (TextView) rootView.findViewById(R.id.textview_artist_name);
        albumNameView = (TextView) rootView.findViewById(R.id.textview_album_name);
        trackNameView = (TextView) rootView.findViewById(R.id.textview_track_name);
        trackAlbumArtView = (ImageView) rootView.findViewById(R.id.imageview_album_art);
        trackStartView = (TextView) rootView.findViewById(R.id.textview_track_start);
        trackEndView = (TextView) rootView.findViewById(R.id.textview_track_end);

        setTrackInfo();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Toast.makeText(getActivity(), "onStart - Binding",
                Toast.LENGTH_SHORT).show();

        BindToService();
    }

    @Override
    public void onStop() {
        super.onStop();

        Toast.makeText(getActivity(), "onStop - Unbinding",
                Toast.LENGTH_SHORT).show();

        progressHandler.removeCallbacks(updateProgress);

        UnBindService();
    }

    //----------------------------

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
    //----------------------------

    private void playPreview(String url) {

        mService.setTrackUrl(url);

        mService.reset();

        mService.play();

        setPauseButtonImage();

        progressHandler.postDelayed(updateProgress, 0);
    }

    private String formatDuration(Integer duration) {
        return String.format("%01d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

    }

    private void setPauseButtonImage() {

        if (PLAYING.equals(mService.getPlayerState())) {
            mButtonPause.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mButtonPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    void BindToService() {

        Intent intent = new Intent(getActivity()
                .getApplicationContext(), MediaPlayerService.class);

        getActivity()
                .getApplicationContext()
                .bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mIsBound = true;
    }

    void UnBindService() {
        if (mIsBound) {
            getActivity().getApplicationContext().unbindService(mConnection);
            mIsBound = false;
        }
    }

    //TODO Re-init media player OnResume. Take care of Fragment LifeCycle issues
}
