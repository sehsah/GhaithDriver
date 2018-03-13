package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/11/2018.
 */

public class GetNewTrip {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("lat_user")
    @Expose
    private String lat_user;

    @SerializedName("lng_user")
    @Expose
    private String lng_user;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("pickup_location")
    @Expose
    private String pickup_location;

    @SerializedName("drop_location")
    @Expose
    private String drop_location;

    @SerializedName("price")
    @Expose
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getlat_user() {
        return lat_user;
    }
    public void setlat_user(String lat_user) {
        this.lat_user = lat_user;
    }

    public String getlng_user() {
        return lng_user;
    }
    public void setlng_user(String lng_user) {
        this.lng_user = lng_user;
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
    public void setlng(String lng) {
        this.lng = lng;
    }


    public String getdistance() {
        return distance;
    }
    public void setdistance(String distance) {
        this.distance = distance;
    }

    public String getpickup_location() {
        return pickup_location;
    }
    public void setpickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getdrop_locationn() {
        return drop_location;
    }
    public void setdrop_location(String drop_location) {
        this.drop_location = drop_location;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }
}
