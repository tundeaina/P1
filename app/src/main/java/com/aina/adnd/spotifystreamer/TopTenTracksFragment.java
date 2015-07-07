package com.aina.adnd.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

    private final static String SAVED_TRACK_INFO = "SAVED_TRACK_INFO";
    private final static String TRACK_PREVIEW_URL = "TRACK_PREVIEW_URL";
    private final static String ARTIST_NAME = "ARTIST_NAME";
    private final static String ALBUM_NAME = "ALBUM_NAME";
    private final static String TRACK_NAME = "TRACK_NAME";
    private final static String ALBUM_ART = "ALBUM_ART";
    private final static String QUERY_PARAMETER = "country";

    private TrackListAdapter mAdapter;
    private ArrayList<TrackInfo> mTrackInfo = new ArrayList<TrackInfo>();

    private String mArtistName;
    private String mArtistId;
    private String mCountryCode;
    private String mCountry;

    public TopTenTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        mArtistName = intent.getStringExtra(ArtistSearchFragment.ARTIST_NAME);
        mArtistId = intent.getStringExtra(ArtistSearchFragment.ARTIST_ID);
        mCountryCode = intent.getStringExtra(ArtistSearchFragment.COUNTRY_CODE);
        mCountry = intent.getStringExtra(ArtistSearchFragment.COUNTRY);

        CountryFlag countryFlag = new CountryFlag(((TopTenTracksActivity) getActivity())
                , mCountryCode);

        countryFlag.render();

//        ((TopTenTracksActivity) getActivity()).getSupportActionBar()
//                .setTitle(getActivity().getTitle() + " - [" + mCountry + "]");

        ((TopTenTracksActivity) getActivity()).getSupportActionBar()
                .setSubtitle(mArtistName);

        if (savedInstanceState != null) {

            mTrackInfo = savedInstanceState.getParcelableArrayList(SAVED_TRACK_INFO);
        } else {

            FetchTopTracksTask trackTask = new FetchTopTracksTask();
            trackTask.execute(mArtistId);
        }



        mAdapter = new TrackListAdapter(getActivity(), mTrackInfo);

        View rootView = inflater.inflate(R.layout.fragment_top_ten_tracks, container, false);

        ListView trackList = (ListView) rootView.findViewById(R.id.listview_top10tracks);

        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TrackInfo track = mTrackInfo.get(position);

                Toast.makeText(getActivity(), track.getPreviewUrl(), Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getActivity(), TrackPreviewActivity.class);
//                intent.putExtra(ARTIST_NAME, artistName);
//                intent.putExtra(TRACK_PREVIEW_URL, track.getPreviewUrl());
//                intent.putExtra(ALBUM_NAME, track.getAlbumName());
//                intent.putExtra(TRACK_NAME, track.getTrackName());
//                intent.putExtra(ALBUM_ART, track.getAlbumArtUrl_Large());
//                startActivity(intent);

            }
        });

        trackList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {

        ArrayList<TrackInfo> trackInfo = mAdapter.getTrackInfo();

        savedState.putParcelableArrayList(SAVED_TRACK_INFO, trackInfo);

        super.onSaveInstanceState(savedState);

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

            } else {
                Toast.makeText(getActivity(), "No tracks found for\n"
                        + mArtistName, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
