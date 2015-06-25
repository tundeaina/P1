package com.aina.adnd.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Fragment containing Artists ListView.
 */

public class ArtistSearchFragment extends Fragment {

    public final static String ARTIST_ID = "ARTIST_ID";
    public final static String ARTIST_NAME = "ARTIST_NAME";
    private final static String SAVED_ARTIST_INFO = "SAVED_ARTIST_INFO";
    private ArtistListAdapter mAdapter;
    private ArrayList<ArtistInfo> mArtistInfo = new ArrayList<ArtistInfo>();
    private String mArtistQueryString;

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {

            mArtistInfo = savedInstanceState.getParcelableArrayList(SAVED_ARTIST_INFO);
        }

        mAdapter = new ArtistListAdapter(getActivity(), mArtistInfo);

        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        final EditText artistQueryText = (EditText) rootView.findViewById(R.id.edittext_artist);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        artistQueryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    mArtistQueryString = artistQueryText.getText().toString();

                    if (mArtistQueryString.length() > 0) {
                        FetchArtistsTask artistTask = new FetchArtistsTask();
                        artistTask.execute(mArtistQueryString);
                    }

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(artistQueryText.getWindowToken(), 0);

                    handled = true;
                }
                return handled;
            }
        });

        ListView artistList = (ListView) rootView.findViewById(R.id.listview_artist);

        artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                ArtistInfo artist = mArtistInfo.get(position);

//                Toast.makeText(getActivity(), "You Clicked on "
//                        + artist.getId(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), TopTenTracksActivity.class);
                intent.putExtra(ARTIST_ID, artist.getId());
                intent.putExtra(ARTIST_NAME, artist.getName());
                startActivity(intent);


            }
        });

        artistList.setAdapter(mAdapter);
        
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        ArrayList<ArtistInfo> artistinfo = mAdapter.getArtistInfo();

        savedState.putParcelableArrayList(SAVED_ARTIST_INFO, artistinfo);

        super.onSaveInstanceState(savedState);
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, ArtistsPager> {

        public static final String NO_IMAGE_URL = "NO_IMAGE_URL";
        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected ArtistsPager doInBackground(String... params) {

            try{

                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();
                return spotify.searchArtists(params[0]);

            }
            catch (Exception e){
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArtistsPager results) {

            mAdapter.clear();

            if (results.artists.items.size() > 0) {

                for(Artist artist:  results.artists.items) {

                    ArtistInfo artistInfo = new ArtistInfo();

                    String imageUrl = null;

                    for(Image img: artist.images){
                        if (img.width <= 600) {
                            imageUrl = img.url;
                            break;
                        }
                    }

                    if(null == imageUrl)
                        imageUrl = NO_IMAGE_URL;

                    artistInfo.setId(artist.id);
                    artistInfo.setName(artist.name);
                    artistInfo.setThumbnailUrl(imageUrl);

                    mAdapter.add(artistInfo);
                }

            } else {
                Toast.makeText(getActivity(), "Spotify can't find\n"
                        + mArtistQueryString, Toast.LENGTH_LONG).show();
            }
        }
    }


}
