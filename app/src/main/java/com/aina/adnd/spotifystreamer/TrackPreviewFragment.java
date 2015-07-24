package com.aina.adnd.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPreviewFragment extends Fragment
        implements MediaPlayer.OnPreparedListener {

    private final static Integer MILLI_SECONDS = 1000;
    private final String LOG_TAG = TrackPreviewFragment.class.getSimpleName();
    TextView artistNameView;
    TextView albumNameView;
    TextView trackNameView;
    ImageView trackAlbumArtView;
    SeekBar trackSeekBar = null;
    MediaPlayer mMediaPlayer;
    ImageButton mButtonPause;
    private Integer mDuration;
    private Integer mTrackIndex;
    private String mArtistName;
    private String mPreviewUrl;
    private String mAlbumName;
    private String mTrackName;
    private String mAlbumArtUrl;
    private ArrayList<TrackInfo> mTrackInfo = new ArrayList<TrackInfo>();

    public TrackPreviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();

        mTrackInfo = intent.getParcelableArrayListExtra(TopTenTracksFragment.SAVED_TRACK_INFO);
        mArtistName = intent.getStringExtra(TopTenTracksFragment.ARTIST_NAME);
        mTrackIndex = intent.getIntExtra(TopTenTracksFragment.TRACK_INDEX, 0);

//        Toast.makeText(getActivity(), mPreviewUrl, Toast.LENGTH_SHORT).show();

        View rootView = inflater.inflate(R.layout.fragment_track_preview, container, false);

        mButtonPause = (ImageButton) rootView.findViewById(R.id.trackplayer_pause);
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setTrackInfo();

                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    setPauseButtonImage();
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

                mMediaPlayer.seekTo(progress * MILLI_SECONDS);

                Toast.makeText(getActivity(), "seek bar progress:" + progress,
                        Toast.LENGTH_SHORT).show();
            }
        });


        artistNameView = (TextView) rootView.findViewById(R.id.textview_artist_name);

        albumNameView = (TextView) rootView.findViewById(R.id.textview_album_name);

        trackNameView = (TextView) rootView.findViewById(R.id.textview_track_name);

        trackAlbumArtView = (ImageView) rootView.findViewById(R.id.imageview_album_art);

        setTrackInfo();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaPlayer = new MediaPlayer();
        playPreview(mPreviewUrl);
        //setPauseButtonImage();
    }

    private void setTrackInfo() {

        mPreviewUrl = mTrackInfo.get(mTrackIndex).getPreviewUrl();
        mAlbumName = mTrackInfo.get(mTrackIndex).getAlbumName();
        mTrackName = mTrackInfo.get(mTrackIndex).getTrackName();
        mAlbumArtUrl = mTrackInfo.get(mTrackIndex).getAlbumArtUrl_Large();

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

        mMediaPlayer.reset();
        setPauseButtonImage();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.d(LOG_TAG, "prepareAsync Failure");
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void onPrepared(MediaPlayer player) {
        mDuration = mMediaPlayer.getDuration();
        Log.d(LOG_TAG, mDuration.toString());
        player.start();
        setPauseButtonImage();
    }

    private void setPauseButtonImage() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mButtonPause.setImageResource(android.R.drawable.ic_media_pause);
        } else {
            mButtonPause.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    //TODO Create Runable to progress SeekBar
    //TODO Re-init media player OnResume. Take care of Fragment LifeCycle issues
}
