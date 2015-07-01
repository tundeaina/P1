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

    private static final String COUNTRY_DIALOG = "CountryDialog";
    DialogMessanger messanger;
    private CountryListAdapter mAdapter;
    private ArrayList<CountryInfo> mCountryInfo = new ArrayList<CountryInfo>();
    private ListView countryList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        messanger = (DialogMessanger) activity;
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
            countryInfo.setCountryCode(countryInfoToken[0]);
            countryInfo.setCountryName(countryInfoToken[1]);

            int flagResID = getActivity().getResources().getIdentifier(countryInfoToken[0],
                    "drawable", getActivity().getPackageName());

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

                messanger.onDialogMessage(COUNTRY_DIALOG, country);

                dismiss();

            }
        });

        countryList.setAdapter(mAdapter);
    }
}
