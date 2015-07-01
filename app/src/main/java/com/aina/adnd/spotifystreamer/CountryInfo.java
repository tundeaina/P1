package com.aina.adnd.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tunde Aina on 6/22/2015.
 * http://developer.android.com/reference/android/os/Parcelable.html
 */

public class CountryInfo implements Parcelable {

    public static final Creator<CountryInfo> CREATOR
            = new Creator<CountryInfo>() {
        public CountryInfo createFromParcel(Parcel in) {
            return new CountryInfo(in);
        }

        public CountryInfo[] newArray(int size) {
            return new CountryInfo[size];
        }
    };

    private String CountryCode;
    private String CountryName;
    private Integer CountryFlagId;

    public CountryInfo() {

    }

    public CountryInfo(String code, String name, Integer flagId) {
        this.CountryCode = code;
        this.CountryName = name;
        this.CountryFlagId = flagId;
    }

    private CountryInfo(Parcel in) {
        CountryCode = in.readString();
        CountryName = in.readString();
        CountryFlagId = in.readInt();
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        this.CountryCode = countryCode;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        this.CountryName = countryName;
    }

    public Integer getCountryFlagId() {
        return CountryFlagId;
    }

    public void setCountryFlagId(Integer countryFlagId) {
        this.CountryFlagId = countryFlagId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(CountryCode);
        out.writeString(CountryName);
        out.writeInt(CountryFlagId);
    }

}
