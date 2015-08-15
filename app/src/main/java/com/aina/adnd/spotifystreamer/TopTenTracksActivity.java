package com.aina.adnd.spotifystreamer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TopTenTracksActivity extends ActionBarActivity
        implements CountryListDialogFragment.CountryListSelectListener,
        TopTenTracksFragment.onTrackSelectListener {

    public final static String COUNTRY_CODE = "CountryCode";
    public final static String COUNTRY = "Country";
    private static final String COUNTRY_DIALOG = "CountryDialog";
    private static String mCountryCode;
    private static String mCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_top_ten_tracks);

        if (savedInstanceState == null) {

            TopTenTracksFragment topTenTracksFragment = new TopTenTracksFragment();

            topTenTracksFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_ten_tracks_container, topTenTracksFragment)
                    .commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (UserPreferences.getUserCountryCode(TopTenTracksActivity.this).length() > 0) {

            mCountryCode = UserPreferences.getUserCountryCode(TopTenTracksActivity.this);

            CountryFlag countryFlag = new CountryFlag(this, mCountryCode);

            countryFlag.render();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top_ten_tracks, menu);
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

    //CallBack From CountryListDialogFragment
    public void onCountryListSelect(String sender, Object message) {

        CountryInfo countryInfo = (CountryInfo) message;

        mCountryCode = countryInfo.getCountryCode();

        mCountry = countryInfo.getCountryName();

        UserPreferences.setUserCountryCode(this, mCountryCode);

        finish();

        Intent intent = getIntent();

        intent.putExtra(COUNTRY_CODE, mCountryCode);

        intent.putExtra(COUNTRY, mCountry);

        startActivity(intent);

    }

    //CallBack From TopTenTracksFragment
    public void onTrackSelected(Bundle bundle) {

        Intent intent = new Intent(TopTenTracksActivity.this, TrackPreviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
