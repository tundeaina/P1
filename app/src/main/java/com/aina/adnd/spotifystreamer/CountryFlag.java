package com.aina.adnd.spotifystreamer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.widget.ImageView;

/**
 * Created by Tunde Aina on 7/6/2015.
 */
public class CountryFlag {

    private final static String _DR = "dr";
    private final static String _DO = "do";
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

//        imageView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mActivity, "Refresh Clicked!",
//                        Toast.LENGTH_LONG).show();
//            }
//        });

        imageView.setScaleType(ImageView.ScaleType.CENTER);

        String countryCode = (_DO.equals(mCountryCode)) ? _DR : mCountryCode;

        int flagResID = mActivity.getResources().getIdentifier(
                countryCode,
                "drawable",
                mActivity.getPackageName());

        imageView.setImageResource(flagResID);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.TOP);

        //layoutParams.rightMargin = 20;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
    }
}
