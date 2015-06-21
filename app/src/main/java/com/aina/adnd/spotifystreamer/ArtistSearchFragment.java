package com.aina.adnd.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ArtistListAdapter mAdapter;

    private ArrayList<ArtistInfo> mArtistInfo = new ArrayList<ArtistInfo>();

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        final EditText artistQueryText = (EditText) rootView.findViewById(R.id.edittext_artist);

        artistQueryText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String artistQueryString = artistQueryText.getText().toString();

//                    Toast.makeText(getActivity(), "Search for "
//                            + artistQueryString, Toast.LENGTH_SHORT).show();

                    FetchArtistsTask artistTask = new FetchArtistsTask();
                    artistTask.execute(artistQueryString);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(artistQueryText.getWindowToken(), 0);

                    handled = true;
                }
                return handled;
            }
        });

        mAdapter = new ArtistListAdapter(getActivity(), mArtistInfo);

        ListView artistList = (ListView) rootView.findViewById(R.id.listview_artist);

        artistList.setAdapter(mAdapter);

        artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at "
                        + position, Toast.LENGTH_SHORT).show();

            }
        });
        
        return rootView;
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

            if (results != null) {

                for(Artist artist:  results.artists.items) {

                    ArtistInfo artistInfo = new ArtistInfo();

                    String imageUrl = null;

                    for(Image img: artist.images){
                        if(img.height<=65){
                            imageUrl = img.url;
                            break;
                        }
                        else if(img.height>=200 && img.height<=301) {
                            imageUrl = img.url;
                            break;
                        }
                    }

                    if(null == imageUrl)
                        imageUrl = NO_IMAGE_URL;

                    artistInfo.setName(artist.name);
                    artistInfo.setImageUrl(imageUrl);

                    mAdapter.add(artistInfo);
                }

            }
        }
    }


}
