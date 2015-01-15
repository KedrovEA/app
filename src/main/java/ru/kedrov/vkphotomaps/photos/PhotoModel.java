package ru.kedrov.vkphotomaps.photos;

public class PhotoModel {

    public final String smallLink;
    public final String largeLink;
    public final double lat;
    public final double lon;

    public PhotoModel(String smallLink, String largeLink, double lat, double lon) {
        this.smallLink = smallLink;
        this.largeLink = largeLink;
        this.lat = lat;
        this.lon = lon;
    }

}
