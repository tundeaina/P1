package com.aina.adnd.spotifystreamer;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * Fragment containing Tracks ListView.
 */

public class TopTenTracksFragment extends Fragment {

    public final static String SAVED_TRACK_INFO = "SavedTrackInfo";
    public final static String TRACK_INDEX = "TrackIndex";
    public final static String ARTIST_NAME = "ArtistName";
    public final static String ARTIST_ID = "ArtistId";
    public final static String COUNTRY_CODE = "CountryCode";
    private final static String QUERY_PARAMETER = "country";
    private final static String CURR_LIST_POSITION = "CurrentListPosition";
    ListView trackList;
    onTrackSelectListener mCallbackArtistSearchActivity;
    private TrackListAdapter mAdapter;
    private ArrayList<TrackInfo> mTrackInfo = new ArrayList<TrackInfo>();
    private Integer mCurrPosition = 0;
    private String mArtistName;
    private String mArtistId;
    private String mCountryCode;

    public TopTenTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();

        if (args != null) {
            mArtistName = args.getString(ARTIST_NAME);
            mArtistId = args.getString(ARTIST_ID);
            mCountryCode = args.getString(COUNTRY_CODE);
        }

        ActionBarActivity parentActivity = (ActionBarActivity) getActivity();

        CountryFlag countryFlag = new CountryFlag(parentActivity, mCountryCode);

        countryFlag.render();

        parentActivity.getSupportActionBar().setSubtitle(mArtistName);

        if (savedInstanceState != null) {

            mTrackInfo = savedInstanceState.getParcelableArrayList(SAVED_TRACK_INFO);
            mCurrPosition = savedInstanceState.getInt(CURR_LIST_POSITION);
        } else {

            FetchTopTracksTask trackTask = new FetchTopTracksTask();
            trackTask.execute(mArtistId);
        }

        mAdapter = new TrackListAdapter(getActivity(), mTrackInfo);

        View rootView = inflater.inflate(R.layout.fragment_top_ten_tracks, container, false);

        trackList = (ListView) rootView.findViewById(R.id.listview_top10tracks);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                mCurrPosition = position;

                Bundle args = new Bundle();

                args.putParcelableArrayList(SAVED_TRACK_INFO, mAdapter.getTrackInfo());
                args.putString(ARTIST_NAME, mArtistName);
                args.putInt(TRACK_INDEX, position);

                mCallbackArtistSearchActivity.onTrackSelected(args);

                if (getFragmentManager()
                        .findFragmentById(R.id.top_ten_tracks_container) != null) {
                    trackList.setItemChecked(position, true);
                }


            }
        });

        trackList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        trackList.setSelection(mCurrPosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallbackArtistSearchActivity = (onTrackSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onTrackSelectListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        savedState.putParcelableArrayList(SAVED_TRACK_INFO, mTrackInfo);
        savedState.putInt(CURR_LIST_POSITION, mCurrPosition);

        super.onSaveInstanceState(savedState);
    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        trackList.setSelection(mCurrPosition);
//    }

    public interface onTrackSelectListener {
        void onTrackSelected(Bundle bundle);
    }

    public class FetchTopTracksTask extends AsyncTask<String, Void, Tracks> {

        public static final String NO_IMAGE_URL = "NO_IMAGE_URL";
        private final String LOG_TAG = FetchTopTracksTask.class.getSimpleName();

        @Override
        protected Tracks doInBackground(String... params) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(QUERY_PARAMETER, mCountryCode);

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            try {
                return spotify.getArtistTopTrack(params[0], map);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }

        }

        @Override
        protected void onPostExecute(Tracks results) {

            mAdapter.clear();

            if (results.tracks.size() > 0) {

                for (Track track : results.tracks) {

                    TrackInfo trackInfo = new TrackInfo();

                    String imageUrl_Small = null;
                    String imageUrl_Large = null;

                    for (Image img : track.album.images) {
                        if (img.height <= 300) {
                            imageUrl_Small = img.url;
                        }

                        if (img.height >= 301) {
                            imageUrl_Large = img.url;
                        }
                    }

                    imageUrl_Large = (null == imageUrl_Large) ? NO_IMAGE_URL : imageUrl_Large;
                    imageUrl_Small = (null == imageUrl_Small) ? NO_IMAGE_URL : imageUrl_Small;

                    trackInfo.setPreviewUrl(track.preview_url);
                    trackInfo.setAlbumName(track.album.name);
                    trackInfo.setTrackName(track.name);
                    trackInfo.setAlbumArtUrl_Small(imageUrl_Small);
                    trackInfo.setAlbumArtUrl_Large(imageUrl_Large);

                    mAdapter.add(trackInfo);
                }

                mTrackInfo = mAdapter.getTrackInfo();

            } else {
                Toast.makeText(getActivity(), "No tracks found for\n"
                        + mArtistName, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
