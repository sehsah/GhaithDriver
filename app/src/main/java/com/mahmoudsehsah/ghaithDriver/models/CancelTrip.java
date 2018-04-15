package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 4/14/2018.
 */

public class CancelTrip {
    @SerializedName("id_user")
    private String id_user;

    @SerializedName("id_trip")
    private String id_trip;

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_trip() {
        return id_trip;
    }

    public void setId_trip(String id_trip) {
        this.id_trip = id_trip;
    }
}
