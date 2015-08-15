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

        ArtistInfo artist;
        artist = Artists.get(position);

        final ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_artist, parent, false);
            viewHolder.mArtistName = (TextView) view.findViewById(R.id.list_item_name);
            viewHolder.mAlbumThumbnail = (ImageView) view.findViewById(R.id.list_item_thumbnail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.mAlbumThumbnail.setImageBitmap(null);
        }

        if (null != artist) {

            viewHolder.mArtistName.setText(artist.getName());
            String imageURL = artist.getThumbnailUrl();

            if (imageURL.equals(ArtistSearchFragment.FetchArtistsTask.NO_IMAGE_URL))
                Picasso.with(context)
                        .load(R.drawable.no_image)
                        .into(viewHolder.mAlbumThumbnail);
            else
                Picasso.with(context)
                        .load(imageURL)
                        .placeholder(R.drawable.no_image)
                        .resize(256, 256)
                        .centerCrop()
                        .into(viewHolder.mAlbumThumbnail);
        }

        return view;
    }

    public ArrayList<ArtistInfo> getArtistInfo() {
        return this.Artists;
    }

    class ViewHolder {
        TextView mArtistName;
        ImageView mAlbumThumbnail;
    }
}
