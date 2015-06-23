package com.aina.adnd.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tunde Aina on 6/22/2015.
 * http://developer.android.com/reference/android/os/Parcelable.html
 */

public class TrackInfo implements Parcelable {

    public static final Creator<TrackInfo> CREATOR
            = new Creator<TrackInfo>() {
        public TrackInfo createFromParcel(Parcel in) {
            return new TrackInfo(in);
        }

        public TrackInfo[] newArray(int size) {
            return new TrackInfo[size];
        }
    };
    private String TrackName;
    private String AlbumName;
    private String AlbumArtUrl_Small;
    private String AlbumArtUrl_Large;
    private String PreviewUrl;

    public TrackInfo() {

    }

    public TrackInfo(String trackname, String albumname, String imgurl_small,
                     String imgurl_large, String preview_url) {
        this.TrackName = trackname;
        this.AlbumName = albumname;
        this.AlbumArtUrl_Small = imgurl_small;
        this.AlbumArtUrl_Large = imgurl_large;
        this.PreviewUrl = preview_url;

    }

    private TrackInfo(Parcel in) {
        TrackName = in.readString();
        AlbumName = in.readString();
        AlbumArtUrl_Small = in.readString();
        AlbumArtUrl_Large = in.readString();
        PreviewUrl = in.readString();
    }

    public String getTrackName() {
        return TrackName;
    }

    public void setTrackName(String trackName) {
        this.TrackName = trackName;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        this.AlbumName = albumName;
    }

    public String getAlbumArtUrl_Small() {
        return AlbumArtUrl_Small;
    }

    public void setAlbumArtUrl_Small(String albumArtUrlSmall) {
        this.AlbumArtUrl_Small = albumArtUrlSmall;
    }

    public String getAlbumArtUrl_Large() {
        return AlbumArtUrl_Large;
    }

    public void setAlbumArtUrl_Large(String albumArtUrlLarge) {
        this.AlbumArtUrl_Large = albumArtUrlLarge;
    }

    public String getPreviewUrl() {
        return PreviewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.PreviewUrl = previewUrl;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {

        out.writeString(TrackName);
        out.writeString(AlbumName);
        out.writeString(AlbumArtUrl_Small);
        out.writeString(AlbumArtUrl_Large);
        out.writeString(PreviewUrl);
    }

}
