package com.aina.adnd.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MediaPlayerService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private final String LOG_TAG = MediaPlayerService.class.getSimpleName();
    private final IBinder mBinder = new MediaPlayerServiceBinder();

    private final String IDLE = "Idle";
    private final String PREPARING = "Preparing";
    private final String READY = "Ready";
    private final String PLAYING = "Playing";
    private final String PAUSED = "Paused";


    String mPlayerState = IDLE;
    MediaPlayer mMediaPlayer;
    private String mPreviewUrl;
    private int mDuration;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - MediaPlayer Service Created");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;

            stopSelf();
        }

        Log.d(LOG_TAG, "onDestroy - MediaPlayer Service Destroyed");

        super.onDestroy();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public void play() {

        try {

            mMediaPlayer.setDataSource(mPreviewUrl);

            mMediaPlayer.prepareAsync();

            mPlayerState = PREPARING;

            Log.d(LOG_TAG, "prepareTrack -" + mPlayerState);

        } catch (IOException e) {
            Log.d(LOG_TAG, "prepareTrack Failure");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPlayerState = READY;
        mDuration = mp.getDuration();
        start();
    }

    public void start() {

        if (mPlayerState.equals(READY)
                || mPlayerState.equals(PAUSED)) {
            mMediaPlayer.start();

            mPlayerState = PLAYING;
        }
    }

    public void reset() {
        mMediaPlayer.reset();
        mPlayerState = PREPARING;
    }

    public void pause() {

        if (mPlayerState.equals(PLAYING)) {
            mMediaPlayer.pause();
            mPlayerState = PAUSED;
        }
    }

    public void seekTo(Integer progress) {

        mMediaPlayer.seekTo(progress);
        mPlayerState = READY;
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void setTrackUrl(String url) {
        mPreviewUrl = url;
    }

    public String getPlayerState() {
        return mPlayerState;
    }

    public void setPlayerState(String state) {
        mPlayerState = state;
    }

    public Integer getDuration() {
        return mDuration;
    }

    public Integer getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public class MediaPlayerServiceBinder extends Binder {
        MediaPlayerService getService() {

            return MediaPlayerService.this;
        }
    }

}
