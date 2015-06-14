package com.aina.adnd.spotifystreamer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * Fragment containing Artists ListView.
 */
public class ArtistSearchFragment extends Fragment {

    public ArtistSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String[] names = {
                "A",
                "B"
        } ;

        Integer[] thumbnail = {
                R.drawable.spotify_thumbnail,
                R.drawable.no_image
        };

        ArtistListAdapter adapter = new ArtistListAdapter(getActivity(), names, thumbnail);

        View rootView = inflater.inflate(R.layout.fragment_artist_search, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.listview_artist);

        list.setAdapter(adapter);

        return rootView;
    }


}
