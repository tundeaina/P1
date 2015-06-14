package com.aina.adnd.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Tunde Aina on 6/13/2015.
 */
public class ArtistListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] artistName;
    private final Integer[] artistThumbnail;

    public ArtistListAdapter(Context context, String[] artistName, Integer[] artistThumbnail) {
        super(context, R.layout.list_item_artist, artistName);
        this.context = context;
        this.artistName = artistName;
        this.artistThumbnail = artistThumbnail;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_artist, null, true);

        TextView artist_name = (TextView) rowView.findViewById(R.id.list_item_name);

        ImageView artist_thumbnail = (ImageView) rowView.findViewById(R.id.list_item_thumbnail);

        artist_name.setText(artistName[position]);

        artist_thumbnail.setImageResource(artistThumbnail[position]);

        return rowView;
    }
}
