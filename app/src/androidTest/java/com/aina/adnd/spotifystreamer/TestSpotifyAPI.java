package com.aina.adnd.spotifystreamer;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class TestSpotifyAPI extends ApplicationTestCase<Application> {

    public TestSpotifyAPI() {
        super(Application.class);
    }

    public static final String LOG_TAG = TestSpotifyAPI.class.getSimpleName();

    public void testSearchArtists() {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists("Coldplay");


        Log.d(LOG_TAG,"------------------------------xxxxxxxxx--------------------------------");
        for(Artist artist:  results.artists.items) {

            Log.d(LOG_TAG, artist.name);

            Log.d(LOG_TAG, artist.id);

            for(Image img: artist.images){
                if(img.height<=65 && img.width<=65)
                    Log.d(LOG_TAG, img.url);
            }

            Log.d(LOG_TAG,"---------------------------------------------------------------------");

            assert(artist.name.contains("Coldplay"));
        }


    }

}