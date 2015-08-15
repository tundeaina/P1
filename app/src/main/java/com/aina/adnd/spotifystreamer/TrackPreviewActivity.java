package com.aina.adnd.spotifystreamer;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class TrackPreviewActivity extends ActionBarActivity {
    public final static String SAVED_TRACK_INFO = "SavedTrackInfo";
    public final static String TRACK_INDEX = "TrackIndex";
    public final static String ARTIST_NAME = "ArtistName";
    private final String LOG_TAG = TrackPreviewActivity.class.getSimpleName();
    private ShareActionProvider mShareActionProvider;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_preview);

        if (savedInstanceState == null) {

            mBundle = getIntent().getExtras();

            FragmentManager fragmentManager = getFragmentManager();
            TrackPreviewFragment trackPreviewFragment = new TrackPreviewFragment();
            trackPreviewFragment.setArguments(mBundle);

            fragmentManager.beginTransaction()
                    .add(R.id.track_preview_container, trackPreviewFragment)
                    .commit();

        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track_preview, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        Intent shareIntent = createShareIntent(mBundle);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent createShareIntent(Bundle bundle) {

        Intent intent = new Intent();
        String artistName = bundle.getString(ARTIST_NAME);
        Integer trackIndex = bundle.getInt(TRACK_INDEX, 0);
        ArrayList<TrackInfo> trackInfo = bundle.getParcelableArrayList(SAVED_TRACK_INFO);

        if (trackInfo != null) {
            String trackUrl = trackInfo.get(trackIndex).getPreviewUrl();
            String trackName = trackInfo.get(trackIndex).getTrackName();

            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "Currently listening to \""
                    + trackName + "\" by " + artistName + " on Spotify. Check it out at\n"
                    + trackUrl);

            intent.setType("text/plain");
        }

        return intent;
    }
}
