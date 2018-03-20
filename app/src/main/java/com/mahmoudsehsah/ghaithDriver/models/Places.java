package com.mahmoudsehsah.ghaithDriver.models;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class Places {

    private String name, icon;
    private String lat;
    private String lng;
    private String distance;
    private String id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getlat() {
        return lat;
    }
    public void setlat(String lat) {
        this.lat = lat;
    }

    public String getlng() {
        return lng;
    }
    public void setlng(String lng) {this.lng = lng;}

    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {this.distance = distance;}

    public String getId() {
        return id;
    }
    public void setId(String id) {this.id = id;}

}