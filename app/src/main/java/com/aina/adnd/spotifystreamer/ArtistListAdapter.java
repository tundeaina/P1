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
 * Created by Tunde Aina on 6/13/2015.
 */
public class ArtistListAdapter extends ArrayAdapter<ArtistInfo> {

    private final Context context;
    private final ArrayList<ArtistInfo> Artists;

    public ArtistListAdapter(Context context, ArrayList<ArtistInfo> artists) {
        super(context, R.layout.list_item_artist, artists);
        this.context = context;
        this.Artists = artists;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item_artist, null, true);

        TextView artist_name_view = (TextView) rowView.findViewById(R.id.list_item_name);

        ImageView artist_thumbnail_view = (ImageView) rowView.findViewById(R.id.list_item_thumbnail);

        ArtistInfo artist;

        artist = Artists.get(position);

        if (null != artist) {

            artist_name_view.setText(artist.getName());

            String imageURL = artist.getImageUrl();

            if (imageURL.equals(ArtistSearchFragment.FetchArtistsTask.NO_IMAGE_URL))
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .into(artist_thumbnail_view);
            else
                Picasso.with(context)
                        .load(imageURL)
                        .placeholder(R.drawable.no_image)
                        .fit()
                        .into(artist_thumbnail_view);
        }

        return rowView;
    }

    public ArrayList<ArtistInfo> getArtistInfo() {
        return this.Artists;
    }
}
