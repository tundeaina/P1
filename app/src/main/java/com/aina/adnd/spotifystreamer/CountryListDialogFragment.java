package com.aina.adnd.spotifystreamer;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Tunde Aina on 6/30/2015.
 */
public class CountryListDialogFragment extends DialogFragment {

    private final static String COUNTRY_DIALOG = "CountryDialog";
    private final static String _DR = "dr";
    private final static String _DO = "do";

    CountryListSelectListener mCallback;
    private CountryListAdapter mAdapter;
    private ArrayList<CountryInfo> mCountryInfo = new ArrayList<CountryInfo>();
    private ListView countryList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (CountryListSelectListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CountryListSelectListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = getResources();
        String[] countries = res.getStringArray(R.array.country_code);

        mAdapter = new CountryListAdapter(getActivity(), mCountryInfo);

        for (String country : countries) {

            CountryInfo countryInfo = new CountryInfo();

            String[] countryInfoToken = country.split("-");

            String countryCode = countryInfoToken[0];

            countryInfo.setCountryCode(countryCode);

            countryInfo.setCountryName(countryInfoToken[1]);

            countryCode = (_DO.equals(countryCode)) ? _DR : countryCode;

//            Log.d(COUNTRY_DIALOG, countryCode);

            int flagResID = getActivity().getResources().getIdentifier(
                    countryCode,
                    "drawable",
                    getActivity().getPackageName());

            countryInfo.setCountryFlagId(flagResID);

            mAdapter.add(countryInfo);
        }

        View view = inflater.inflate(R.layout.fragment_country_list, container, false);

        countryList = (ListView) view.findViewById(R.id.listview_countries);

        getDialog().setTitle(R.string.title_dialog_countries);

        setCancelable(true);

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CountryInfo country = mCountryInfo.get(position);

                mCallback.onCountryListSelect(COUNTRY_DIALOG, country);

                dismiss();

            }
        });

        countryList.setAdapter(mAdapter);
    }

    public interface CountryListSelectListener {
        void onCountryListSelect(String sender, Object message);
    }
}
