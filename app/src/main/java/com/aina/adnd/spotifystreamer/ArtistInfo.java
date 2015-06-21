package com.aina.adnd.spotifystreamer;

/**
 * Created by Tunde Aina on 6/20/2015.
 */
public class ArtistInfo {

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

}
