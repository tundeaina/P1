package com.aina.adnd.spotifystreamer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.ImageView;

/**
 * Created by Tunde Aina on 7/6/2015.
 */
public class CountryFlag {

    private final ActionBarActivity mActivity;
    private final String mCountryCode;

    public CountryFlag(ActionBarActivity activity, String countrycode) {
        this.mActivity = activity;
        this.mCountryCode = countrycode;
    }

    public void render() {

        ActionBar actionBar = mActivity.getSupportActionBar();

        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);

        ImageView imageView = new ImageView(actionBar.getThemedContext());

        imageView.setScaleType(ImageView.ScaleType.CENTER);

        int flagResID = mActivity.getResources().getIdentifier(mCountryCode,
                "drawable", mActivity.getPackageName());

        imageView.setImageResource(flagResID);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
    }
}
