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

    public static final String NO_IMAGE_URL = "http://www.varsityready.com/images/no_image.png";

    public void testSearchArtists() {



        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results = spotify.searchArtists("Coldplay");

        int artistCount = results.artists.items.size();

        Log.d(LOG_TAG, "------------------------------xxxxxxxxx--------------------------------");

        Log.d(LOG_TAG,String.valueOf(artistCount));
        Log.d(LOG_TAG,"------------------------------xxxxxxxxx--------------------------------");

        String[] artistArray = new String[results.artists.items.size()];
        String[] imageURLArray = new String[results.artists.items.size()];

        int k = 0;

        for(Artist artist:  results.artists.items) {

            //Log.d(LOG_TAG, artist.name);
            artistArray[k] = artist.name;

            //Stuff names Array

            //Log.d(LOG_TAG, artist.id);

            String imageUrl = null;

            for(Image img: artist.images){
                if(img.height<=65 && img.width<=65) {
                    imageUrl = img.url;
                    //Log.d(LOG_TAG, img.url);
                }
            }



            //inspect arr for thumbnail
            //if not add no_image URL
            //Stuff image_url Array

            imageURLArray[k] = (null == imageUrl)?  NO_IMAGE_URL : imageUrl;

            k++;



            assert(artist.name.contains("Coldplay"));
        }

        for(int m = 0; m<artistCount; m++){
            Log.d(LOG_TAG, imageURLArray[m] + ":" + artistArray[m]);
        }

        Log.d(LOG_TAG,"---------------------------------------------------------------------");
    }

}