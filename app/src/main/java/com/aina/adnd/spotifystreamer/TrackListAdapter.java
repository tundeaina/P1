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

        TrackInfo track;
        track = Tracks.get(position);

        final ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_track, parent, false);
            viewHolder.mAlbumName = (TextView) view.findViewById(R.id.list_item_album_name);
            viewHolder.mTrackName = (TextView) view.findViewById(R.id.list_item_track_name);
            viewHolder.mAlbumArt = (ImageView) view.findViewById(R.id.list_item_album_art_small);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.mAlbumArt.setImageBitmap(null);
        }

        if (null != track) {

            viewHolder.mAlbumName.setText(track.getAlbumName());
            viewHolder.mTrackName.setText(track.getTrackName());

            String albumArtUrl = track.getAlbumArtUrl_Small();

            if (albumArtUrl.equals(TopTenTracksFragment.FetchTopTracksTask.NO_IMAGE_URL))
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .into(viewHolder.mAlbumArt);
            else
                Picasso.with(context)
                        .load(albumArtUrl)
                        .placeholder(R.drawable.no_image)
                        .resize(256, 256)
                        .centerCrop()
                        .into(viewHolder.mAlbumArt);
        }

        return view;
    }

    public ArrayList<TrackInfo> getTrackInfo() {
        return this.Tracks;
    }

    class ViewHolder {
        TextView mAlbumName;
        TextView mTrackName;
        ImageView mAlbumArt;
    }
}
