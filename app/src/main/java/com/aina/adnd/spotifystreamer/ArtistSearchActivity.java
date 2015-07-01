package com.aina.adnd.spotifystreamer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ArtistSearchActivity extends ActionBarActivity implements DialogMessanger {

    private static final String COUNTRY_DIALOG = "CountryDialog";
    private static final String DEFAULT_COUNTRY_CODE = "us";
    private static final String DEFAULT_COUNTRY = "United States";
    private static String mCountryCode = null;
    private static String mCountry = null;

    public static String getCountryCode() {

        if (null == mCountryCode) mCountryCode = DEFAULT_COUNTRY_CODE;

        return mCountryCode;
    }

    public static String getCountry() {

        if (null == mCountry) mCountry = DEFAULT_COUNTRY;

        return mCountry;
    }

    public void onDialogMessage(String sender, Object message) {

        CountryInfo countryInfo = (CountryInfo) message;

        mCountryCode = ((CountryInfo) message).getCountryCode();
        mCountry = ((CountryInfo) message).getCountryName();

        Toast.makeText(ArtistSearchActivity.this, mCountryCode,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_artist_search);

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
        if (id == R.id.action_settings) {

            CountryListDialogFragment countryListDialogFragmentDialog =
                    new CountryListDialogFragment();

            countryListDialogFragmentDialog.show(getFragmentManager(), COUNTRY_DIALOG);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
