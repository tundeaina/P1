package com.aina.adnd.spotifystreamer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Tunde Aina on 7/6/2015.
 */
public class UserPreferences {
    static final String PREF_USER_COUNTRY_CODE = "countrycode";

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUserCountryCode(Context context, String countrycode) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_USER_COUNTRY_CODE, countrycode);
        editor.commit();
    }

    public static String getUserCountryCode(Context context) {
        return getSharedPreferences(context).getString(PREF_USER_COUNTRY_CODE, "");
    }

    public static void flushUserCountryCode(Context context) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(PREF_USER_COUNTRY_CODE);
        editor.commit();
    }
}
