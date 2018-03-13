package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/14/2018.
 */

public class updateLocation {
    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("lat")
    @Expose
    private String lat;

    public String getid_user() {
        return id_user;
    }
    public void setid_user(String id_user) {
        this.id_user = id_user;
    }

    public String getlng() {
        return lng;
    }
    public void setlng(String lng) {
        this.lng = lng;
    }

    public String getlat() {
        return lat;
    }
    public void setlat(String lat) {
        this.lat = lat;
    }
}
