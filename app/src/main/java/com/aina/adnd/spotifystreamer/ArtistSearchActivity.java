package com.aina.adnd.spotifystreamer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ArtistSearchActivity extends ActionBarActivity implements DialogMessenger {

    private static final String COUNTRY_DIALOG = "CountryDialog";
    private static final String DEFAULT_COUNTRY_CODE = "us";
    private static final String DEFAULT_COUNTRY = "United States";
    private static String mCountryCode;
    private static String mCountry;

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_country) {

            CountryListDialogFragment countryListDialogFragmentDialog =
                    new CountryListDialogFragment();

            countryListDialogFragmentDialog.show(getFragmentManager(), COUNTRY_DIALOG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDialogMessage(String sender, Object message) {

        CountryInfo countryInfo = (CountryInfo) message;

        mCountryCode = ((CountryInfo) message).getCountryCode();
        mCountry = ((CountryInfo) message).getCountryName();

        UserPreferences.setUserCountryCode(ArtistSearchActivity.this, mCountryCode);

        CountryFlag countryFlag = new CountryFlag(this, mCountryCode);
        countryFlag.render();
    }

}
