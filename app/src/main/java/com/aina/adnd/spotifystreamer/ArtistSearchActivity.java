package com.aina.adnd.spotifystreamer;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class ArtistSearchActivity extends ActionBarActivity
        implements CountryListDialogFragment.CountryListSelectListener,
        ArtistSearchFragment.onArtistSelectListener,
        TopTenTracksFragment.onTrackSelectListener {

    public final static String SAVED_TRACK_INFO = "SavedTrackInfo";
    public final static String TRACK_INDEX = "TrackIndex";
    public final static String ARTIST_NAME = "ArtistName";
    private final static String COUNTRY_CODE = "CountryCode";
    private final static String COUNTRY = "Country";
    private static final String TOPTENTRACKSFRAGMENT_TAG = "TopTenTrackFragment";
    private static final String COUNTRY_DIALOG = "CountryDialog";
    private static final String TRACKPREVIEWFRAGMENT_TAG = "TrackPreviewFragment";
    private static final String DEFAULT_COUNTRY_CODE = "us";
    private static final String DEFAULT_COUNTRY = "United States";
    private static String mCountryCode;
    private static String mCountry;
    private boolean mSw600dp;
    private Bundle mArtistBundle;
    private Bundle mTrackBundle;

    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareItem;

    public static String getCountryCode() {

        if (null == mCountryCode) mCountryCode = DEFAULT_COUNTRY_CODE;

        return mCountryCode;
    }

    public static String getCountry() {

        if (null == mCountry) mCountry = DEFAULT_COUNTRY;

        return mCountry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artist_search);

        mSw600dp = findViewById(R.id.top_ten_tracks_container) != null;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UserPreferences.getUserCountryCode(ArtistSearchActivity.this).length() == 0) {
            mCountryCode = DEFAULT_COUNTRY_CODE;
        } else
            mCountryCode = UserPreferences.getUserCountryCode(ArtistSearchActivity.this);

        CountryFlag countryFlag = new CountryFlag(this, mCountryCode);

        countryFlag.render();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        mShareItem = menu.findItem(R.id.menu_item_share);

        if (!mSw600dp)
            mShareItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_country:
                CountryListDialogFragment countryListDialogFragmentDialog =
                        new CountryListDialogFragment();
                countryListDialogFragmentDialog.show(getFragmentManager(), COUNTRY_DIALOG);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //CallBack From TopTenTracksFragment
    public void onTrackSelected(Bundle bundle) {
        mTrackBundle = bundle;

        Intent shareIntent = createShareIntent(mTrackBundle);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(mShareItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }

        if (mSw600dp) {
            FragmentManager fragmentManager = getFragmentManager();
            TrackPreviewFragment trackPreviewFragment = new TrackPreviewFragment();
            trackPreviewFragment.setArguments(mTrackBundle);
            trackPreviewFragment.show(fragmentManager, TRACKPREVIEWFRAGMENT_TAG);

        } else {
            Intent intent = new Intent(ArtistSearchActivity.this, TrackPreviewActivity.class);
            intent.putExtras(mTrackBundle);
            startActivity(intent);
        }
    }

    //CallBack From ArtistSearchFragment
    public void onArtistSelected(Bundle bundle) {

        mArtistBundle = bundle;

        if (mSw600dp) {

            TopTenTracksFragment topTenTracksFragment = new TopTenTracksFragment();
            topTenTracksFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.top_ten_tracks_container, topTenTracksFragment);
            transaction.addToBackStack(TOPTENTRACKSFRAGMENT_TAG);
            transaction.commit();

        } else {
            Intent intent = new Intent(ArtistSearchActivity.this, TopTenTracksActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //CallBack From CountryListDialogFragment
    public void onCountryListSelect(String sender, Object message) {

        CountryInfo countryInfo = (CountryInfo) message;

        mCountryCode = countryInfo.getCountryCode();

        mCountry = countryInfo.getCountryName();

        UserPreferences.setUserCountryCode(ArtistSearchActivity.this, mCountryCode);

        CountryFlag countryFlag = new CountryFlag(this, mCountryCode);

        countryFlag.render();

        if (mSw600dp && mArtistBundle != null) {

            mArtistBundle.putString(COUNTRY_CODE, mCountryCode);
            mArtistBundle.putString(COUNTRY, mCountry);

            TopTenTracksFragment topTenTracksFragment = new TopTenTracksFragment();
            topTenTracksFragment.setArguments(mArtistBundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.top_ten_tracks_container, topTenTracksFragment);
            transaction.addToBackStack(TOPTENTRACKSFRAGMENT_TAG);
            transaction.commit();

        }

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
