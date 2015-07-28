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

    private final static Integer MILLI_SECONDS = 1000;
    private final String LOG_TAG = MediaPlayerService.class.getSimpleName();
    private final IBinder mBinder = new MediaPlayerServiceBinder();
    PlayerState mPlayerState = PlayerState.IDLE;
    MediaPlayer mMediaPlayer;
    private String mPreviewUrl;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
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

            Log.d(LOG_TAG, "onDestroy - Destroying");
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void play() {
        try {

            mMediaPlayer.setDataSource(mPreviewUrl);

            mMediaPlayer.prepareAsync();

            mPlayerState = PlayerState.PREPARING;

            Log.d(LOG_TAG, "prepareTrack -" + mPlayerState.toString());

        } catch (IOException e) {
            Log.d(LOG_TAG, "prepareTrack Failure");
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mPlayerState = PlayerState.READY;

        start();
    }

    public void reset() {
        mMediaPlayer.reset();
        mPlayerState = PlayerState.PREPARING;
    }

    public void pause() {
        if (mPlayerState.equals(PlayerState.PLAYING)) {
            mMediaPlayer.pause();
            mPlayerState = PlayerState.PAUSED;
        }
    }

    public void start() {

        if (mPlayerState.equals(PlayerState.READY)
                || mPlayerState.equals(PlayerState.PAUSED)) {
            mMediaPlayer.start();
            mPlayerState = PlayerState.PLAYING;
        }
    }

    public void seekTo(Integer progress) {
        mMediaPlayer.seekTo(progress * MILLI_SECONDS);
    }

    public void setTrackUrl(String url) {
        mPreviewUrl = url;
    }

    public String getPlayerState() {
        return mPlayerState.toString();
    }

    public Integer getDuration() {
        return mMediaPlayer.getDuration();
    }

    public Integer getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    private enum PlayerState {
        IDLE,
        PREPARING,
        READY,
        PLAYING,
        PAUSED
    }

    public class MediaPlayerServiceBinder extends Binder {
        MediaPlayerService getService() {

            return MediaPlayerService.this;
        }
    }

}
