package com.aina.adnd.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tunde Aina on 6/22/2015.
 */
public class TrackListAdapter extends ArrayAdapter<TrackInfo> {

    private final Context context;
    private final ArrayList<TrackInfo> Tracks;

    public TrackListAdapter(Context context, ArrayList<TrackInfo> tracks) {
        super(context, R.layout.list_item_artist, tracks);
        this.context = context;
        this.Tracks = tracks;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_track, null, true);

        TextView album_name_view = (TextView) rowView.findViewById(R.id.list_item_album_name);

        TextView track_name_view = (TextView) rowView.findViewById(R.id.list_item_track_name);

        ImageView album_art_view = (ImageView) rowView.findViewById(R.id.list_item_album_art_small);

        TrackInfo track;

        track = Tracks.get(position);

        if (null != track) {

            album_name_view.setText(track.getAlbumName());

            track_name_view.setText(track.getTrackName());

            String albumArtUrl = track.getAlbumArtUrl_Small();

            if (albumArtUrl.equals(TopTenTracksFragment.FetchTopTracksTask.NO_IMAGE_URL))
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .into(album_art_view);
            else
                Picasso.with(context)
                        .load(albumArtUrl)
                        .placeholder(R.drawable.no_image)
                        .fit()
                        .into(album_art_view);
        }

        return rowView;
    }

    public ArrayList<TrackInfo> getTrackInfo() {
        return this.Tracks;
    }
}
