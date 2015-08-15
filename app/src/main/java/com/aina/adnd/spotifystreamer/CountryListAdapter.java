package com.aina.adnd.spotifystreamer;

import android.content.Context;
import android.net.Uri;
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
public class CountryListAdapter extends ArrayAdapter<CountryInfo> {

    private final Context context;
    private final ArrayList<CountryInfo> mCountries;

    public CountryListAdapter(Context context, ArrayList<CountryInfo> countries) {
        super(context, R.layout.list_item_country, countries);
        this.context = context;
        this.mCountries = countries;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        CountryInfo country;
        country = mCountries.get(position);

        final ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_country, parent, false);
            viewHolder.mFlagImage = (ImageView) view.findViewById(R.id.list_item_flag);
            viewHolder.mCountryName = (TextView) view.findViewById(R.id.list_item_country);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.mFlagImage.setImageBitmap(null);
        }

        if (null != country) {

            viewHolder.mCountryName.setText(country.getCountryName());

            // Country Flag Image 'url' is safe because the images are stored in \res\Drawable
            Integer imageResID = country.getCountryFlagId();

            Picasso.with(context)
                    .load(imageResID)
                    .into(viewHolder.mFlagImage);
        }

        return view;
    }

    public ArrayList<CountryInfo> getCountryInfo() {
        return this.mCountries;
    }

    class ViewHolder {
        ImageView mFlagImage;
        TextView mCountryName;
    }
}
