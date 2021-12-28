package com.example.travelbuddy.Places;

public class PlaceModelClass {
    private String id;
    private String place_image;
    private String place_name;
    private String place_description;
    private String lon;
    private String lat;

    public PlaceModelClass(String place_image, String place_name, String place_description) {
        this.place_image = place_image;
        this.place_name = place_name;
        this.place_description = place_description;
    }

    public PlaceModelClass(String id, String place_image, String place_name, String place_description, String lon, String lat) {
        this.id = id;
        this.place_image = place_image;
        this.place_name = place_name;
        this.place_description = place_description;
        this.lon = lon;
        this.lat = lat;
    }

    public String getPlace_image() {
        return place_image;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getPlace_description() {
        return place_description;
    }

    public String getId() {
        return id;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
