package com.aina.adnd.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tunde Aina on 6/20/2015.
 * http://developer.android.com/reference/android/os/Parcelable.html
 */

public class ArtistInfo implements Parcelable {

    public static final Parcelable.Creator<ArtistInfo> CREATOR
            = new Parcelable.Creator<ArtistInfo>() {
        public ArtistInfo createFromParcel(Parcel in) {
            return new ArtistInfo(in);
        }

        public ArtistInfo[] newArray(int size) {
            return new ArtistInfo[size];
        }
    };
    private String SpotifyId;
    private String Name;
    private String ThumbnailUrl;

    public ArtistInfo() {

    }

    public ArtistInfo(String id, String name, String imgurl) {
        this.SpotifyId = id;
        this.Name = name;
        this.ThumbnailUrl = imgurl;

    }

    private ArtistInfo(Parcel in) {
        SpotifyId = in.readString();
        Name = in.readString();
        ThumbnailUrl = in.readString();
    }

    public String getId() {
        return SpotifyId;
    }

    public void setId(String id) {
        this.SpotifyId = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String imgurl) {
        this.ThumbnailUrl = imgurl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(SpotifyId);
        out.writeString(Name);
        out.writeString(ThumbnailUrl);
    }

}
