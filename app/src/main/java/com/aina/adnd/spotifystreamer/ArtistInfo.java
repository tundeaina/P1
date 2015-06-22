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
    private String Id;
    private String Name;
    private String ImageUrl;

    public ArtistInfo() {

    }

    public ArtistInfo(String id, String name, String imgurl) {
        this.Id = id;
        this.Name = name;
        this.ImageUrl = imgurl;

    }

    private ArtistInfo(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        ImageUrl = in.readString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imgurl) {
        this.ImageUrl = imgurl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(Id);
        out.writeString(Name);
        out.writeString(ImageUrl);
    }

}
