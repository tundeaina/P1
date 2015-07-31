package com.aina.adnd.spotifystreamer;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MediaPlayerService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private final static long IDLE_TIME = 1 * 30 * 1000;
    private final static int INTERVAL = 1 * 60 * 1000;
    private final static String DESTROY_INTENT = "DestructionIntent";


    private final String LOG_TAG = MediaPlayerService.class.getSimpleName();
    private final IBinder mBinder = new MediaPlayerServiceBinder();
    PlayerState mPlayerState = PlayerState.IDLE;
    MediaPlayer mMediaPlayer;
    ResultReceiver resultReceiver;
    Handler idleCheckHandler = new Handler();
    private String mPreviewUrl;
    private int mDuration;
    private long mLastServiceRequestTime;
    private Runnable idleCheck = new Runnable() {
        @Override
        public void run() {

            long idleCheckTime = new Date().getTime();

            Log.d(LOG_TAG, "idling for "
                    + (idleCheckTime - mLastServiceRequestTime) + " milliseconds");

            if ((idleCheckTime - mLastServiceRequestTime) > IDLE_TIME) {

                Log.d(LOG_TAG, "idleCheck - Send Destruction Intent");

                Bundle bundle = new Bundle();
                bundle.putString(DESTROY_INTENT, DESTROY_INTENT);

                if (resultReceiver == null)
                    Log.d(LOG_TAG, "idleCheck - resultReceiver is NULL");

                resultReceiver.send(100, bundle);

                //destroyService();
            }

            idleCheckHandler.postDelayed(idleCheck, INTERVAL);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {

        mLastServiceRequestTime = new Date().getTime();
        Log.d(LOG_TAG, "mLastServiceRequestTime " + mLastServiceRequestTime);
        idleCheckHandler.postDelayed(idleCheck, 0);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        resultReceiver = intent.getParcelableExtra(DESTROY_INTENT);

        if (resultReceiver == null) Log.d(LOG_TAG, "onStartCommand - resultReceiver is NULL");

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Log.d(LOG_TAG, "onDestroy - Destroying");
        destroyService();
        super.onDestroy();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void destroyService() {

        idleCheckHandler.removeCallbacks(idleCheck);

        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;

            stopSelf();
        }

        Log.d(LOG_TAG, "destroyService - Destroyed");
    }

    public void play() {

        mLastServiceRequestTime = new Date().getTime();
        Log.d(LOG_TAG, "mLastServiceRequestTime " + mLastServiceRequestTime);

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
        mDuration = mp.getDuration();
        start();
    }

    public void start() {

        if (mPlayerState.equals(PlayerState.READY)
                || mPlayerState.equals(PlayerState.PAUSED)) {
            mMediaPlayer.start();

            mLastServiceRequestTime = new Date().getTime();
            Log.d(LOG_TAG, "mLastServiceRequestTime " + mLastServiceRequestTime);

            mPlayerState = PlayerState.PLAYING;
        }
    }

    public void reset() {
        mMediaPlayer.reset();
        mPlayerState = PlayerState.PREPARING;
    }

    public void pause() {

        if (mPlayerState.equals(PlayerState.PLAYING)) {
            mMediaPlayer.pause();

            mLastServiceRequestTime = new Date().getTime();
            Log.d(LOG_TAG, "mLastServiceRequestTime " + mLastServiceRequestTime);

            mPlayerState = PlayerState.PAUSED;
        }
    }

    public void seekTo(Integer progress) {

        mMediaPlayer.seekTo(progress);

        mLastServiceRequestTime = new Date().getTime();
        Log.d(LOG_TAG, "mLastServiceRequestTime " + mLastServiceRequestTime);

        mPlayerState = PlayerState.READY;
    }

    public void setTrackUrl(String url) {
        mPreviewUrl = url;
    }

    public String getPlayerState() {
        return mPlayerState.toString();
    }

    public Integer getDuration() {
        return mDuration;
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
