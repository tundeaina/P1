package com.aina.adnd.spotifystreamer;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TopTenTracksActivity extends ActionBarActivity {

    //TODO--Pass artistName and artistId to Fragment

    private String artistName;
    private String artistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten_tracks);

        Intent intent = getIntent();

        artistName = intent.getStringExtra(ArtistSearchFragment.ARTIST_NAME);
        artistId = intent.getStringExtra(ArtistSearchFragment.ARTIST_ID);

        ActionBar ab = getSupportActionBar();
        ab.setSubtitle(artistName);
        ab.setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_ten_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getArtistName() {
        return this.artistName;
    }

    public String getArtistId() {
        return this.artistId;
    }
}
